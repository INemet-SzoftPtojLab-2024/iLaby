package main.java.org.game.UI;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.awt.image.*;

public class Minimap extends Updatable {

    private final int width;
    private final int height;

    private ImageUI displayedImage=null;
    private BufferedImage imageData;
    private WritableRaster packedData;
    private int[] rawData;

    private final Object syncObject=new Object();
    private boolean canRerender=true;

    public Minimap(int res)
    {
        this.width=res;
        this.height=res;
        rawData=new int[4*res*res];
        packedData=WritableRaster.createPackedRaster(DataBuffer.TYPE_INT, this.width, this.height,4,8,null);
        imageData=new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void onStart(Isten isten) {
        displayedImage=new ImageUI();
        displayedImage.setPosition(new Vec2(20,20));
        displayedImage.setScale(new Vec2(100,100));
        displayedImage.setSortingLayer(-69420);
        displayedImage.setAlignment(Renderable.RIGHT, Renderable.TOP);
        displayedImage.setOrigin(Renderable.RIGHT, Renderable.TOP);
        displayedImage.setImage(imageData);

        isten.getRenderer().addRenderable(displayedImage);
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        synchronized (syncObject)
        {
            if(canRerender==true)
            {
                canRerender=false;
                Thread thread = new Thread(this::draw);
                thread.start();
            }
        }
    }

    @Override
    public void onDestroy() {

    }

    private void draw()
    {
        int currentIndex=0;
        for(int row=0;row<height;row++)
        {
            for(int col=0;col<width;col++)
            {
                if(Math.pow(row-height/2,2)+Math.pow(col-width/2,2)>2500)
                {
                    rawData[currentIndex]=0;
                    rawData[currentIndex+1]=0;
                    rawData[currentIndex+2]=0;
                    rawData[currentIndex+3]=0;
                }
                else
                {
                    rawData[currentIndex]=255;
                    rawData[currentIndex+1]=255;
                    rawData[currentIndex+2]=255;
                    rawData[currentIndex+3]=255;
                }

                currentIndex+=4;
            }
        }

        packedData.setPixels(0,0,this.width,this.height,rawData);
        imageData.setData(packedData);

        synchronized (syncObject)
        {
            canRerender=true;
        }
    }
}
