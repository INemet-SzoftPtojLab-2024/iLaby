package main.java.org.game.Graphics.PP;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PP_FogOfWar implements PostProcessEffectBased{

    private BufferedImage source;

    public PP_FogOfWar(BufferedImage source)
    {
        this.source=source;
    }

    @Override
    public void doPass(Graphics frameBuffer, int width, int height) {
        if(source==null)
            return;

        ((Graphics2D)frameBuffer).setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        frameBuffer.drawImage(source, 0,0,width, height,null);
        ((Graphics2D)frameBuffer).setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
    }

    public void setImage(BufferedImage source)
    {
        this.source=source;
    }
}
