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

        frameBuffer.drawImage(source, 0,0,width, height,null);
    }
}
