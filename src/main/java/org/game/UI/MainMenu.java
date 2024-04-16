package main.java.org.game.UI;

import main.java.org.game.Audio.AudioManager;
import main.java.org.game.Audio.Sound;
import main.java.org.game.Graphics.ButtonUI;
import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;
import main.java.org.manager.GameManager;

import java.util.ArrayList;

public class MainMenu extends Updatable {
    private ArrayList<ImageUI> images = new ArrayList<>();
    private ArrayList<ButtonUI> buttons = new ArrayList<>();
    private boolean diff;
    private boolean multi;
    private boolean character;

    @Override
    public void onStart(Isten isten) {
        AudioManager.preloadSound("./assets/audio/click.ogg");

        diff = false;
        multi = false;
        character = false;

        images.add(new ImageUI(new Vec2(0, -300), new Vec2(600, 150), "./assets/ui/logo.png"));
        images.add(new ImageUI(new Vec2(0, 375), new Vec2(300, 50), "./assets/ui/developer_logo.png"));
        images.add(new ImageUI(new Vec2(0, 0), new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()), "./assets/ui/menu_background.jpg"));

        buttons.add(new ButtonUI(new Vec2(0, -125), new Vec2(375, 100), "./assets/ui/button_background.jpg", "Solo", "./assets/Monocraft.ttf", 50));
        buttons.add(new ButtonUI(new Vec2(0, 0), new Vec2(375, 100), "./assets/ui/button_background.jpg", "Multi", "./assets/Monocraft.ttf", 50));
        buttons.add(new ButtonUI(new Vec2(0, 125), new Vec2(375, 100), "./assets/ui/button_background.jpg", "Character", "./assets/Monocraft.ttf", 50));
        buttons.add(new ButtonUI(new Vec2(0, 250), new Vec2(375, 100), "./assets/ui/button_background.jpg", "Exit", "./assets/Monocraft.ttf", 50));
        buttons.add(new ButtonUI(new Vec2(0, 250), new Vec2(375, 100), "./assets/ui/button_background.jpg", "Back", "./assets/Monocraft.ttf", 50));
        buttons.add(new ButtonUI(new Vec2(0, -125), new Vec2(375, 100), "./assets/ui/button_background.jpg", "Easy-peasy", "./assets/Monocraft.ttf", 50));
        buttons.add(new ButtonUI(new Vec2(0, 0), new Vec2(375, 100), "./assets/ui/button_background.jpg", "Casual", "./assets/Monocraft.ttf", 50));
        buttons.add(new ButtonUI(new Vec2(0, 125), new Vec2(375, 100), "./assets/ui/button_background.jpg", "Never", "./assets/Monocraft.ttf", 50));

        for (Renderable r : images) {
            r.setAlignment(Renderable.CENTER, Renderable.CENTER);
            isten.getRenderer().addRenderable(r);
            r.setSortingLayer(-69);
        }
        images.get(images.size() - 1).setSortingLayer(-13);
        for (ButtonUI r : buttons) {
            r.setAlignment(Renderable.CENTER, Renderable.CENTER);
            isten.getRenderer().addRenderable(r);
            r.setSortingLayer(-69);
        }
        for (int i = 0; i < buttons.size(); i++) {
            if (i > 3) buttons.get(i).setVisibility(false);
        }
        buttons.get(0).addClickListener(() -> {
            AudioManager.playSound("./assets/audio/click.ogg");
            diff = true;
        });
        buttons.get(1).addClickListener(() -> {
            AudioManager.playSound("./assets/audio/click.ogg");
            multi = true;
        });
        buttons.get(2).addClickListener(() -> {
            AudioManager.playSound("./assets/audio/click.ogg");
            character = true;
        });
        buttons.get(3).addClickListener(() -> {
            AudioManager.playSound("./assets/audio/click.ogg");
            GameManager.setStage(GameManager.GameStage.EXIT);
        });
        buttons.get(4).addClickListener(() -> {
            AudioManager.playSound("./assets/audio/click.ogg");
            diff = false;
            multi = false;
            character = false;
            for (int i = 0; i < buttons.size(); i++) {
                buttons.get(i).setVisibility(i <= 3);
            }
        });
        buttons.get(5).addClickListener(() -> {
            AudioManager.playSound("./assets/audio/click.ogg");
            TimeCounter.setTime(901);
            GameManager.setStage(GameManager.GameStage.INGAME);
        });
        buttons.get(6).addClickListener(() -> {
            AudioManager.playSound("./assets/audio/click.ogg");
            TimeCounter.setTime(601);
            GameManager.setStage(GameManager.GameStage.INGAME);
        });
        buttons.get(7).addClickListener(() -> {
            AudioManager.playSound("./assets/audio/click.ogg");
            TimeCounter.setTime(11);
            GameManager.setStage(GameManager.GameStage.INGAME);
        });
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        images.get(images.size() - 1).setScale(new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()));
        if (diff && !buttons.get(5).getVisibility()) {
            for (int i = 0; i < buttons.size(); i++) {
                if (i < 4) buttons.get(i).setVisibility(false);
                else buttons.get(i).setVisibility(true);
            }
        }
        if (multi && !buttons.get(4).getVisibility()) {
            for (int i = 0; i < buttons.size(); i++) {
                if (i == 4) buttons.get(i).setVisibility(true);
                else buttons.get(i).setVisibility(false);
            }
        }
        if (character && !buttons.get(4).getVisibility()) {
            for (int i = 0; i < buttons.size(); i++) {
                if (i == 4) buttons.get(i).setVisibility(true);
                else buttons.get(i).setVisibility(false);
            }
        }
    }

    @Override
    public void onDestroy() {

    }
}
