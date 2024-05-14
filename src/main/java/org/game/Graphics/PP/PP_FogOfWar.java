package main.java.org.game.Graphics.PP;

import main.java.org.game.Graphics.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class PP_FogOfWar implements PostProcessEffectBased{

    private final Object syncObject=new Object();
    private BufferedImage source;
    private ArrayList<FogUnitPP> unitsToDraw=new ArrayList<>();

    private final BufferedImage[] fogImages=new BufferedImage[22];

    public PP_FogOfWar(BufferedImage source)
    {
        this.source=source;

        for(int i=0;i<22;i++)
        {
            String path="./assets/fog/fog_mask_"+i+".png";
            fogImages[i]= Image.loadImage(path);
        }
    }

    @Override
    public void doPass(Graphics frameBuffer, int width, int height) {
        if(source==null)
            return;

        synchronized (syncObject)
        {
            Graphics2D g2d=(Graphics2D) source.getGraphics();
            g2d.setBackground(new Color(0,0,0,0));
            g2d.clearRect(0,0, source.getWidth(), source.getHeight());


            for(FogUnitPP unit : unitsToDraw)
                g2d.drawImage(fogImages[unit.index], unit.drawX, unit.drawY, unit.width, unit.height, null);

            Graphics2D frameBuffer2D=(Graphics2D)frameBuffer;

            frameBuffer2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            frameBuffer2D.setComposite(AlphaComposite.DstIn);

            frameBuffer2D.drawImage(source, 0,0,width, height,null);

            frameBuffer2D.setComposite(AlphaComposite.SrcOver);
            frameBuffer2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        }
    }

    public void setImage(BufferedImage source)
    {
        synchronized (syncObject)
        {
            this.source=source;
        }
    }

    public void setUnitsToDraw(ArrayList<FogUnitPP> unitsToDraw)
    {
        synchronized (syncObject)
        {
            this.unitsToDraw=unitsToDraw;
        }
    }

    public static class FogUnitPP
    {
        public final int drawX, drawY;
        public final int width, height;
        public final int index;

        public FogUnitPP(int drawX, int drawY, int width, int height, int index)
        {
            this.drawX=drawX;
            this.drawY=drawY;
            this.width=width;
            this.height=height;
            this.index=index;
        }
    }
}
