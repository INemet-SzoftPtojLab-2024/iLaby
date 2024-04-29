package main.java.org.game.UI;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Isten;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.awt.*;
import java.awt.image.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Minimap extends Updatable {

    private int displayedScale;

    private final int width;
    private final int height;

    private final int pixelsPerUnit;

    private ImageUI displayedImage=null;
    private BufferedImage displayedImageData;
    private int[] rawData;

    private final Object syncObject=new Object();
    private boolean canRerender=true;

    public Minimap(int displayedScale, int res, int pixelsPerUnit)
    {
        this.displayedScale=displayedScale;

        this.width=res;
        this.height=res;
        this.pixelsPerUnit=pixelsPerUnit;

        rawData=new int[4*res*res];
        displayedImageData=new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void onStart(Isten isten) {
        displayedImage=new ImageUI();
        displayedImage.setPosition(new Vec2(20,20));
        displayedImage.setScale(new Vec2(displayedScale,displayedScale));
        displayedImage.setSortingLayer(-69420);
        displayedImage.setAlignment(Renderable.RIGHT, Renderable.TOP);
        displayedImage.setOrigin(Renderable.RIGHT, Renderable.TOP);
        displayedImage.setImage(displayedImageData);

        isten.getRenderer().addRenderable(displayedImage);
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        synchronized (syncObject)
        {
            if(canRerender==true)
            {
                canRerender=false;
                Thread thread = new Thread(()->{this.draw(isten);});
                thread.start();
            }
        }
    }

    @Override
    public void onDestroy() {

    }

    private void draw(Isten isten)
    {
        Arrays.fill(rawData,0);

        ArrayList<Room> rooms = isten.getMap().getRooms();
        if(rooms==null)
            rooms=new ArrayList<>();

        final Vec2 playerPos=isten.getPlayer().getPlayerCollider().getPosition().clone();

        final Vec2 lowerBound=Vec2.subtract(playerPos, new Vec2(width*0.5f/pixelsPerUnit, height*0.5f/pixelsPerUnit));
        final Vec2 upperBound=Vec2.sum(playerPos, new Vec2(width*0.5f/pixelsPerUnit, height*0.5f/pixelsPerUnit));
        final Vec2 covered=Vec2.subtract(upperBound,lowerBound);
        final Vec2 onePerCovered=new Vec2(1/covered.x, 1/covered.y);

        for(int i=0;i<rooms.size();i++)
        {
            int roomColour= (421537*i)%256;

            ArrayList<UnitRoom> unitRooms=rooms.get(i).getUnitRooms();
            for(int j=0;j<unitRooms.size();j++)
            {
                Vec2 startPos=unitRooms.get(j).getPosition().clone();
                startPos.x-=0.5f+playerPos.x;
                startPos.y-=0.5f+playerPos.y;

                if(startPos.x+1<lowerBound.x||startPos.y+1<lowerBound.y||startPos.x>upperBound.x||startPos.y>upperBound.y)
                    continue;

                startPos.x-=lowerBound.x;
                startPos.y-=lowerBound.y;
                startPos.y=covered.y-startPos.y-1;

                int drawX, drawY;
                drawX=Math.round(width*startPos.x* onePerCovered.x)-1;
                drawY=Math.round(height* startPos.y* onePerCovered.y)-1;

                for(int k=drawY>-1?drawY:0;k<height&&k<drawY+pixelsPerUnit+1;k++)
                {
                    for(int l=drawX>-1?drawX:0;l<width&&l<drawX+pixelsPerUnit+1;l++)
                    {
                        int index=4*(width*k+l);
                        rawData[index++]=roomColour;
                        rawData[index++]=roomColour;
                        rawData[index++]=roomColour;
                        rawData[index]=255;
                    }
                }
            }
        }

        //apply transparency mask
        int currentIndex=0;
        int radius=(width/2)*(height/2);
        for(int row=0;row<height;row++)
        {
            for(int col=0;col<width;col++)
            {
                if(Math.pow(row-height/2,2)+Math.pow(col-width/2,2)>radius)
                {
                    rawData[currentIndex+3]=0;
                }

                currentIndex+=4;
            }
        }

        displayedImageData.getRaster().setPixels(0,0, this.width, this.height, rawData);

        synchronized (syncObject)
        {
            canRerender=true;
        }
    }
}
