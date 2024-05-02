package main.java.org.game.UI;

import main.java.org.entities.villain.Villain;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Isten;
import main.java.org.game.Map.*;
import main.java.org.game.updatable.Updatable;
import main.java.org.items.Chest;
import main.java.org.linalg.Vec2;

import java.awt.*;
import java.awt.image.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class Minimap extends Updatable {

    private int displayedScale;
    private ArrayList<EdgePiece> edgePieces = new ArrayList<>();
    private final int width;
    private final int height;

    private final int pixelsPerUnit;
    private final int wallWidthInPixels;

    private ImageUI displayedImage=null;
    private BufferedImage displayedImageData;
    private int[] rawData;

    private final Object syncObject=new Object();
    private boolean canRerender=true;
    private Thread currentThread=null;

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
        displayedImage.setAlignment(Renderable.RIGHT, Renderable.BOTTOM);
        displayedImage.setOrigin(Renderable.RIGHT, Renderable.BOTTOM);
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
                currentThread = new Thread(()->{this.draw(isten);});
                currentThread.start();
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

        /*ArrayList<Room> rooms = isten.getMap().getRooms();
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
        }*/

        //edging intensifies (drawing edges)

        if(isten.getMap().getEdgeManager() == null) return;

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
                    rawData[index++]=r;
                    rawData[index++]=g;
                    rawData[index++]=b;
                    rawData[index]=255;
                }
            }
        }

        //draw the great edges
        do{
            float mapSizeX, mapSizeY;
            mapSizeX=isten.getMap().getMapColumnSize();
            mapSizeY=isten.getMap().getMapRowSize();

            float wallWidth=0.1f;

            try{
                Vec2 temp=isten.getMap().getEdgeManager().getRoomEdges().get(0).getWalls().get(0).getCollider().getScale();
                wallWidth=temp.x>temp.y?temp.y:temp.x;
            }
            catch (Exception ex){}

            Vec2[] positions=new Vec2[]{
                    new Vec2(-0.5f, 0.5f*mapSizeY-0.5f),
                    new Vec2(0.5f*mapSizeX-0.5f, mapSizeY-0.5f),
                    new Vec2(mapSizeX-0.5f, 0.5f*mapSizeY-0.5f),
                    new Vec2(0.5f*mapSizeX-0.5f,-0.5f)
            };
            Vec2[] scales=new Vec2[]{
                    new Vec2(wallWidth, mapSizeY),
                    new Vec2(mapSizeX, wallWidth),
                    new Vec2(wallWidth, mapSizeY),
                    new Vec2(mapSizeX, wallWidth)
            };

            for(int j=0;j<4;j++)
            {
                Vec2 startPos=positions[j].clone();
                Vec2 scale=scales[j];
                startPos.x-=0.5f*scale.x+lowerBound.x;
                startPos.y-=0.5f*scale.y+lowerBound.y;
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
        } while(69==420);

        //draw player
        do{
            for(int i=height/2-wallWidthInPixels;i<height/2+wallWidthInPixels+1;i++)
            {
                for (int j=width/2-wallWidthInPixels;j<width/2+wallWidthInPixels+1;j++)
                {
                    int currentIndex=4*(i*width+j);
                    rawData[currentIndex++]=0;
                    rawData[currentIndex++]=255;
                    rawData[currentIndex++]=0;
                    rawData[currentIndex]=255;
                }
            }
        }while(69==420);

        do{//draw enemies and chests
            class Colon{int r; int g; int b; public Colon(int _r, int _g, int _b){r=_r;g=_g;b=_b;}}

            ArrayList<Vec2> positions=new ArrayList<>();
            ArrayList<Colon> colours=new ArrayList<>();

            //enemies
            ArrayList<Villain> villains=isten.getUpdatablesByType(Villain.class);

            for(int i=0;i<villains.size();i++)
            {
                positions.add(villains.get(i).getVillainCollider().getPosition());
                colours.add(new Colon(255,0,0));
            }

            //chests
            Vector<Chest> chests = isten.getChestManager().getChests();
            for(int i=0;i<chests.size();i++)
            {
                positions.add(chests.get(i).getPosition());
                colours.add(new Colon(0,255,255));
            }

            for(int j=0;j<positions.size();j++)
            {
                Vec2 startPos=positions.get(j).clone();
                startPos.x-=lowerBound.x;
                startPos.y-=lowerBound.y;

                if(startPos.x<0||startPos.y<0||startPos.x>covered.x||startPos.y>covered.y)
                    continue;

                startPos.y=covered.y-startPos.y;

                int drawX, drawY;
                drawX=Math.round(width*startPos.x* onePerCovered.x)-wallWidthInPixels;
                drawY=Math.round(height* startPos.y* onePerCovered.y)-wallWidthInPixels;
                int drawEndX, drawEndY;
                drawEndX=drawX+2*wallWidthInPixels;
                drawEndY=drawY+2*wallWidthInPixels;
                //System.out.println(drawX+" "+drawY+" "+drawEndX+" "+drawEndY);

                for(int k=drawY>-1?drawY:0;k<height&&k<drawEndY;k++)
                {
                    for(int l=drawX>-1?drawX:0;l<width&&l<drawEndX;l++)
                    {
                        Colon col=colours.get(j);

                        int index=4*(width*k+l);
                        rawData[index++]=col.r;
                        rawData[index++]=col.g;
                        rawData[index++]=col.b;
                        rawData[index]=255;
                    }
                }
            }
        }while(69==420);

        //apply transparency mask
        int currentIndex=3;//offset to alpha value
        float outerRadius=(float)Math.sqrt((width*0.5f)*(height*0.5f));
        float innerRadius=outerRadius*0.9f;
        float onePerInterpolationDistance=1/(outerRadius-innerRadius);
        for(int row=0;row<height;row++)
        {
            for(int col=0;col<width;col++, currentIndex+=4)
            {
                float length=(float)Math.sqrt(Math.pow(row-height*0.5f,2)+Math.pow(col-width*0.5f,2));

                if(rawData[currentIndex]==0)
                    rawData[currentIndex]=128;

                if(length<innerRadius)
                {
                    continue;
                }
                if(length>outerRadius)
                {
                    rawData[currentIndex]=0;
                    continue;
                }

                rawData[currentIndex]=(int)((rawData[currentIndex]/255.0f)*(255-255*(length-innerRadius)*onePerInterpolationDistance));
            }
        }

        displayedImageData.getRaster().setPixels(0,0, this.width, this.height, rawData);

        synchronized (syncObject)
        {
            canRerender=true;
            currentThread=null;
        }
    }

    public void addEdgePiece(EdgePiece piece) {
        edgePieces.add(piece);
    }
}
