package main.java.org.game.UI;

import main.java.org.game.Graphics.ButtonUI;
import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

public class GameMode extends Updatable {
    private ButtonUI easy;
    private ButtonUI normal;
    private ButtonUI hard;

    @Override
    public void onStart(Isten isten) {
        easy = new ButtonUI(new Vec2(25, 100), new Vec2(25, 100));
        normal = new ButtonUI(new Vec2(25, 100), new Vec2(25, 100));
        hard = new ButtonUI(new Vec2(25, 100), new Vec2(25, 100));
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {

    }

    @Override
    public void onDestroy() {

    }
}
