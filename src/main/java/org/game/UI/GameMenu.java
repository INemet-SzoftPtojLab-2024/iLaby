package main.java.org.game.UI;

import main.java.org.game.Audio.AudioManager;
import main.java.org.game.Graphics.ButtonUI;
import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Graphics.TextUI;
import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;
import main.java.org.manager.GameManager;

import java.awt.event.KeyEvent;

public class GameMenu extends Updatable {

    private TextUI text_title;
    private ButtonUI button_continue;
    private ButtonUI button_escape;
    private ImageUI image_background;

    @Override
    public void onStart(Isten isten) {
        AudioManager.preloadSound("./assets/audio/click.ogg");

        //text
        text_title=new TextUI("Paused",new Vec2(0,-100),"./assets/Bavarian.otf",50,255,255,255);
        text_title.setAlignment(Renderable.CENTER,Renderable.CENTER);
        text_title.setSortingLayer(-80);
        isten.getRenderer().addRenderable(text_title);

        //buttons
        button_continue = new ButtonUI(new Vec2(0, 20), new Vec2(250, 65), "./assets/ui/button_background.png", "Continue", "./assets/Bavarian.otf", 30);
        button_continue.setAlignment(Renderable.CENTER,Renderable.CENTER);
        button_continue.setSortingLayer(-80);
        isten.getRenderer().addRenderable(button_continue);
        button_continue.addClickListener(() -> {
            AudioManager.playSound("./assets/audio/click.ogg");
            show(false);
        });


        button_escape = new ButtonUI(new Vec2(0, 100), new Vec2(250, 65), "./assets/ui/button_background.png", "Escape", "./assets/Bavarian.otf", 30);
        button_escape.setAlignment(Renderable.CENTER,Renderable.CENTER);
        button_escape.setSortingLayer(-80);
        isten.getRenderer().addRenderable(button_escape);
        button_escape.addClickListener(() -> {
            //AudioManager.playSound("./assets/audio/click.ogg");
            GameManager.setStage(GameManager.GameStage.MAIN_MENU);
        });

        //images
        image_background=new ImageUI(new Vec2(0,0),new Vec2(600,600),"./assets/ui/pause_menu_bg.png");
        image_background.setAlignment(Renderable.CENTER,Renderable.CENTER);
        image_background.setSortingLayer(-79);
        isten.getRenderer().addRenderable(image_background);


        show(false);
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        if(isten.getInputHandler().isKeyPressed(KeyEvent.VK_ESCAPE)) show(!button_escape.getVisibility());
    }

    @Override
    public void onDestroy() {

    }

    private void show(boolean visible)
    {
        text_title.setVisibility(visible);
        button_escape.setVisibility(visible);
        button_continue.setVisibility(visible);
        image_background.setVisibility(visible);
    }
}
