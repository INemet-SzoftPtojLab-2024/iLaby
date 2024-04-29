package main.java.org.game.UI;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Isten;
import main.java.org.game.Map.*;
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
    private final int wallWidthInPixels;

    private ImageUI displayedImage=null;
    private BufferedImage displayedImageData;
    private int[] rawData;

    private final Object syncObject=new Object();
    private boolean canRerender=true;

    public Minimap(int displayedScale, int res, int pixelsPerUnit, int wallWidthInPixels)
    {
        this.displayedScale=displayedScale;

        this.width=res;
        this.height=res;
        this.pixelsPerUnit=pixelsPerUnit;
        this.wallWidthInPixels=wallWidthInPixels;

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
        Arrays.fill(rawData,0);//reset the content of the image

        final Vec2 playerPos=isten.getPlayer().getPlayerCollider().getPosition().clone();

        final Vec2 lowerBound=Vec2.subtract(playerPos, new Vec2(width*0.5f/pixelsPerUnit, height*0.5f/pixelsPerUnit));
        final Vec2 upperBound=Vec2.sum(playerPos, new Vec2(width*0.5f/pixelsPerUnit, height*0.5f/pixelsPerUnit));
        final Vec2 covered=Vec2.subtract(upperBound,lowerBound);
        final Vec2 onePerCovered=new Vec2(1/covered.x, 1/covered.y);

        ArrayList<Room> rooms = isten.getMap().getRooms();
        if(rooms==null)
            rooms=new ArrayList<>();

        for(int i=0;i<rooms.size();i++)
        {
            //draw unit rooms
            ArrayList<UnitRoom> unitRooms=rooms.get(i).getUnitRooms();
            for(int j=0;j<unitRooms.size();j++)
            {
                Vec2 startPos=unitRooms.get(j).getPosition().clone();
                startPos.x-=0.5f+lowerBound.x;
                startPos.y-=0.5f+lowerBound.y;

                if(startPos.x+1<0||startPos.y+1<0||startPos.x>covered.x||startPos.y>covered.y)
                    continue;

                startPos.y=covered.y-startPos.y-1;

                int drawX, drawY;
                drawX=Math.round(width*startPos.x* onePerCovered.x);
                drawY=Math.round(height* startPos.y* onePerCovered.y);

                for(int k=drawY>-1?drawY:0;k<height&&k<drawY+pixelsPerUnit;k++)
                {
                    for(int l=drawX>-1?drawX:0;l<width&&l<drawX+pixelsPerUnit;l++)
                    {
                        int index=4*(width*k+l);
                        rawData[index++]=0;
                        rawData[index++]=0;
                        rawData[index++]=0;
                        rawData[index]=255;
                    }
                }
            }
        }

        //edging intensifies (drawing edges)
        ArrayList<EdgeBetweenRooms> edges =isten.getMap().getEdgeManager().getRoomEdges();
        for(int i=0;i<edges.size();i++)
        {
            ArrayList<EdgePiece> edgePieces=edges.get(i).getWalls();

            for(int j=0;j<edgePieces.size();j++)
            {
                Vec2 startPos=edgePieces.get(j).getCollider().getPosition().clone();
                Vec2 scale=edgePieces.get(j).getCollider().getScale();
                startPos.x-=0.5f*scale.x+lowerBound.x;
                startPos.y-=0.5f*scale.y+lowerBound.y;

                if(startPos.x+1<0||startPos.y+1<0||startPos.x>covered.x||startPos.y>covered.y)
                    continue;

                startPos.y=covered.y-startPos.y-scale.y;

                int drawX, drawY;
                drawX=Math.round(width*startPos.x* onePerCovered.x);
                drawY=Math.round(height* startPos.y* onePerCovered.y)+1;

                int drawEndX=Math.round(scale.x*pixelsPerUnit);
                int drawEndY=Math.round(scale.y*pixelsPerUnit);

                if(drawEndX>drawEndY)
                {
                    drawX--;
                    drawEndX+=drawX+wallWidthInPixels/2;
                    drawEndY=drawY+wallWidthInPixels;
                }
                else
                {
                    drawY--;
                    drawEndY+=drawY;
                    drawEndX=drawX+wallWidthInPixels;
                }

                if(drawX<0)
                    drawX=0;
                if(drawY<0)
                    drawY=0;
                if(drawEndX>width)
                    drawEndX=width;
                if(drawEndY>height)
                    drawEndY=height;

                int r=255, g=255,b=255;
                if(edgePieces.get(j) instanceof Door)
                {
                    //r=255;
                    g=205;
                    b=0;
                }

                for(int k=drawY;k<drawEndY;k++)
                {
                    for(int l=drawX;l<drawEndX;l++)
                    {
                        int index=4*(width*k+l);
                        rawData[index++]=255;
                        rawData[index++]=255;
                        rawData[index++]=255;
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
