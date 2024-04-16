package main.java.org.game;

import main.java.org.game.UI.MainMenu;
import main.java.org.game.UI.MultiMenu;

public class Isten2 extends Isten{
    @Override
    protected void addUpdatables() {
        addUpdatable(new MainMenu());
        addUpdatable(new MultiMenu());
    }

    @Override
    public void init() {
        addUpdatables();
        addRenderables();
    }
}
