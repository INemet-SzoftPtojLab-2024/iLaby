package main.java.org.game.UI;

import main.java.org.game.Graphics.ButtonUI;
import main.java.org.game.Graphics.ClickListener;
import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;
import main.java.org.manager.GameManager;

import java.awt.event.KeyEvent;

public class GameMenu extends Updatable {

    private ButtonUI escape;
    //private ImageUI ded;


    @Override
    public void onStart(Isten isten) {
        escape = new ButtonUI(new Vec2(0, 100), new Vec2(375, 100), "./assets/ui/button_background.jpg", "Escape", "./assets/Monocraft.ttf", 50);
        //ded = new ImageUI(new Vec2(0, 0), new Vec2(375, 100), "./assets/ui/character/ded.png");

        escape.setAlignment(Renderable.CENTER,Renderable.CENTER);
        escape.setVisibility(false);
        escape.setSortingLayer(-80);
        isten.getRenderer().addRenderable(escape);

        escape.addClickListener(() -> {
            GameManager.setStage(GameManager.GameStage.MAIN_MENU);
        });
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        if(isten.getInputHandler().isKeyPressed(KeyEvent.VK_ESCAPE)) escape.setVisibility(!escape.getVisibility());
    }

    @Override
    public void onDestroy() {

    }
}
