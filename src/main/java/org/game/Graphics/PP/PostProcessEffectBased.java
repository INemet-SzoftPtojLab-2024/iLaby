package main.java.org.game.Graphics.PP;

import java.awt.*;

public interface PostProcessEffectBased {
    //jobb lenne egy külön framebuffert csinálni a gamerendererben, mert az nagyobb szabadságot adna, de sajnos a swing baszott lassú
    void doPass(Graphics frameBuffer, int width, int height);
}
