package main.java.org.game;

import main.java.org.game.UI.GameMode;
import main.java.org.game.UI.MainMenu;
import main.java.org.game.updatable.Updatable;
import main.java.org.manager.GameManager;

public class Isten2 extends Isten{

    @Override
    protected void addRenderables() {

    }

    @Override
    protected void addUpdatables() {
        addUpdatable(new MainMenu());
        addUpdatable(new GameMode());
    }
}
