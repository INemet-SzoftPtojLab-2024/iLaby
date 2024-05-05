package main.java.org.game.UI;

import main.java.org.game.Audio.AudioManager;
import main.java.org.game.Graphics.ButtonUI;
import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Graphics.TextUI;
import main.java.org.game.Isten;
import main.java.org.game.PlayerPrefs.PlayerPrefs;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;
import main.java.org.manager.GameManager;

import javax.swing.*;
import java.util.ArrayList;

/**
 * This class represents the main menu.
 */
public class MainMenu extends Updatable {
    private ArrayList<ImageUI> images = new ArrayList<>();
    private ArrayList<ImageUI> charImages = new ArrayList<>();
    private ArrayList<ButtonUI> buttons = new ArrayList<>();
    private ArrayList<TextUI> texts = new ArrayList<>();
    private boolean diff;
    private boolean multi;
    private boolean character;
    private int activeCharImage;

    @Override
    public void onStart(Isten isten) {
        AudioManager.preloadSound("./assets/audio/click.ogg");

        diff = false;
        multi = false;
        character = false;

        images.add(new ImageUI(new Vec2(0, -275), new Vec2(600, 200), "./assets/ui/logo.png"));
        images.add(new ImageUI(new Vec2(0, 375), new Vec2(300, 50), "./assets/ui/developer_logo.png"));
        images.add(new ImageUI(new Vec2(0, 0), new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()), "./assets/ui/menu_background.jpg"));

        for (int i = 0; i < 3; i++) {
            charImages.add(new ImageUI(new Vec2(0, 75), new Vec2(200, 200), "./assets/character/character" + i + "_right1.png"));
        }

        buttons.add(new ButtonUI(new Vec2(0, -125), new Vec2(375, 100), "./assets/ui/button_background.png", "Solo", "./assets/Bavarian.otf", 45));
        buttons.add(new ButtonUI(new Vec2(0, 0), new Vec2(375, 100), "./assets/ui/button_background.png", "Multi", "./assets/Bavarian.otf", 45));
        buttons.add(new ButtonUI(new Vec2(0, 125), new Vec2(375, 100), "./assets/ui/button_background.png", "Character", "./assets/Bavarian.otf", 45));
        buttons.add(new ButtonUI(new Vec2(0, 250), new Vec2(375, 100), "./assets/ui/button_background.png", "Exit", "./assets/Bavarian.otf", 45));
        buttons.add(new ButtonUI(new Vec2(0, 275), new Vec2(275, 100), "./assets/ui/button_background.png", "Back", "./assets/Bavarian.otf", 45));
        buttons.add(new ButtonUI(new Vec2(0, -125), new Vec2(375, 100), "./assets/ui/button_background.png", "Easy-peasy", "./assets/Bavarian.otf", 45));
        buttons.add(new ButtonUI(new Vec2(0, 0), new Vec2(375, 100), "./assets/ui/button_background.png", "Casual", "./assets/Bavarian.otf", 45));
        buttons.add(new ButtonUI(new Vec2(0, 125), new Vec2(375, 100), "./assets/ui/button_background.png", "Never", "./assets/Bavarian.otf", 45));
        buttons.add(new ButtonUI(new Vec2(175, -125), new Vec2(275, 90), "./assets/ui/button_background.png", "Change", "./assets/Bavarian.otf", 45));
        buttons.add(new ButtonUI(new Vec2(-250, 75), new Vec2(100, 100), "./assets/ui/arrow_left.png", "", "./assets/Bavarian.otf", 50));
        buttons.add(new ButtonUI(new Vec2(250, 75), new Vec2(100, 100), "./assets/ui/arrow_right.png", "", "./assets/Bavarian.otf", 50));

        if(PlayerPrefs.hasKey("name")==false)
            PlayerPrefs.setString("name", "name");
        texts.add(new TextUI(PlayerPrefs.getString("name"), new Vec2(-175, -125), 50, 0, 0, 255));

        for (int i = 0; i < images.size(); i++) {
            images.get(i).setAlignment(Renderable.CENTER, Renderable.CENTER);
            isten.getRenderer().addRenderable(images.get(i));
            images.get(i).setSortingLayer(-69);
        }
        images.get(2).setSortingLayer(-13);

        for (ImageUI i : charImages) {
            i.setAlignment(Renderable.CENTER, Renderable.CENTER);
            i.setSortingLayer(-69);
            isten.getRenderer().addRenderable(i);
            i.setVisibility(false);
            if(PlayerPrefs.hasKey("skin"))
                activeCharImage = PlayerPrefs.getInt("skin");
            else {
                PlayerPrefs.setInt("skin",0);
                activeCharImage=0;
            }
        }

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
            TimeCounter.setTime(901);
            GameManager.setStage(GameManager.GameStage.MULTI);
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
            for (TextUI t : texts) t.setVisibility(false);

            for (ImageUI i : charImages) i.setVisibility(false);
        });
        buttons.get(5).addClickListener(() -> {
            AudioManager.playSound("./assets/audio/click.ogg");
            TimeCounter.setTime(901);
            GameManager.setStage(GameManager.GameStage.SOLO);
        });
        buttons.get(6).addClickListener(() -> {
            AudioManager.playSound("./assets/audio/click.ogg");
            TimeCounter.setTime(601);
            GameManager.setStage(GameManager.GameStage.SOLO);
        });
        buttons.get(7).addClickListener(() -> {
            AudioManager.playSound("./assets/audio/click.ogg");
            TimeCounter.setTime(11);
            GameManager.setStage(GameManager.GameStage.SOLO);
        });
        buttons.get(8).addClickListener(() -> {
            AudioManager.playSound("./assets/audio/click.ogg");
            String name = JOptionPane.showInputDialog("Username");
            if (name != null) {
                if (!name.isEmpty()) {
                    texts.get(0).setText(name);
                    PlayerPrefs.setString("name", name);
                }
            }
        });
        buttons.get(9).addClickListener(() -> {
            AudioManager.playSound("./assets/audio/click.ogg");
            if (activeCharImage > 0) activeCharImage--;
            else activeCharImage = charImages.size() - 1;
            PlayerPrefs.setInt("skin", activeCharImage);
            for (int i = 0; i < charImages.size(); i++) charImages.get(i).setVisibility(i == activeCharImage);
        });
        buttons.get(10).addClickListener(() -> {
            AudioManager.playSound("./assets/audio/click.ogg");
            if (activeCharImage < charImages.size() - 1) activeCharImage++;
            else activeCharImage = 0;
            PlayerPrefs.setInt("skin", activeCharImage);
            for (int i = 0; i < charImages.size(); i++) charImages.get(i).setVisibility(i == activeCharImage);
        });

        for (TextUI t : texts) {
            t.setAlignment(Renderable.CENTER, Renderable.CENTER);
            t.setSortingLayer(-69);
            isten.getRenderer().addRenderable(t);
            t.setVisibility(false);
        }
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        images.get(images.size() - 1).setScale(new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()));
        if (diff && !buttons.get(5).getVisibility()) {
            for (int i = 0; i < buttons.size(); i++) {
                if (i < 4 || i > 7) buttons.get(i).setVisibility(false);
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
                if (i == 4 || i > 7) buttons.get(i).setVisibility(true);
                else buttons.get(i).setVisibility(false);
            }
            charImages.get(activeCharImage).setVisibility(true);
            texts.get(0).setVisibility(true);
        }
    }

    @Override
    public void onDestroy() {

    }

    public boolean isMulti() {
        return multi;
    }
}
