package main.java.org.game.UI;

import main.java.org.game.Graphics.ButtonUI;
import main.java.org.game.Graphics.ClickListener;
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

    @Override
    public void onStart(Isten isten) {
        images.add(new ImageUI(new Vec2(0, -300), new Vec2(600, 150), "./assets/ui/logo.png"));
        images.add(new ImageUI(new Vec2(0, 375), new Vec2(300, 50), "./assets/ui/developer_logo.png"));
        images.add(new ImageUI(new Vec2(0, 0), new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()), "./assets/ui/menu_background.jpg"));
        buttons.add(new ButtonUI(new Vec2(0, -100), new Vec2(300, 100),"./assets/ui/button_background.jpg","Solo","./assets/Monocraft.ttf",50));
        buttons.add(new ButtonUI(new Vec2(0, 50), new Vec2(300, 100),"./assets/ui/button_background.jpg","Multi","./assets/Monocraft.ttf",50));
        buttons.add(new ButtonUI(new Vec2(0, 200), new Vec2(300, 100),"./assets/ui/button_background.jpg","Exit","./assets/Monocraft.ttf",50));

        for (Renderable r : images) {
            r.setAlignment(Renderable.CENTER, Renderable.CENTER);
            isten.getRenderer().addRenderable(r);
            r.setSortingLayer(-69);
        }
        images.get(images.size()-1).setSortingLayer(-13);
        for (ButtonUI r : buttons) {
            r.setAlignment(Renderable.CENTER, Renderable.CENTER);
            isten.getRenderer().addRenderable(r);
            r.setSortingLayer(-69);
        }
        buttons.get(0).addClickListener(() -> {
            GameManager.setStage(GameManager.GameStage.INGAME);
        });
        buttons.get(2).addClickListener(() -> {
            GameManager.setStage(GameManager.GameStage.EXIT);
        });
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        images.get(images.size()-1).setScale(new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()));

    }

    @Override
    public void onDestroy() {

    }
}
