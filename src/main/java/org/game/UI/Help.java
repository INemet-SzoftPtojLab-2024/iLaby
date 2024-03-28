package main.java.org.game.UI;

import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.awt.event.KeyEvent;

/**
 * The help image, that's visible in game, when F1 pressed.
 * Nothing more.
 */
public class Help extends Updatable {

    private ImageUI help;

    @Override
    public void onStart(Isten isten) {
        help = new ImageUI(new Vec2(0, 0), new Vec2(600, 600), "./assets/ui/help.png");
        help.setVisibility(false);
        help.setSortingLayer(-70);
        help.setAlignment(Renderable.CENTER,Renderable.CENTER);
        isten.getRenderer().addRenderable(help);
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        if(isten.getInputHandler().isKeyPressed(KeyEvent.VK_F1))
            help.setVisibility(!help.getVisibility());
    }

    @Override
    public void onDestroy() {
        //not implemented yet
    }
}
