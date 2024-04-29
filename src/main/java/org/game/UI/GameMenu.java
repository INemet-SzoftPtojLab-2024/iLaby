package main.java.org.game.UI;

import main.java.org.game.Audio.AudioManager;
import main.java.org.game.Graphics.ButtonUI;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;
import main.java.org.manager.GameManager;

import java.awt.event.KeyEvent;

public class GameMenu extends Updatable {

    private ButtonUI escape;

    @Override
    public void onStart(Isten isten) {
        AudioManager.preloadSound("./assets/audio/click.ogg");

        escape = new ButtonUI(new Vec2(0, 100), new Vec2(375, 100), "./assets/ui/button_background.jpg", "Escape", "./assets/Bavarian.otf", 50);

        escape.setAlignment(Renderable.CENTER,Renderable.CENTER);
        escape.setVisibility(false);
        escape.setSortingLayer(-80);
        isten.getRenderer().addRenderable(escape);

        escape.addClickListener(() -> {
            AudioManager.playSound("./assets/audio/click.ogg");
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
