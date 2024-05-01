package main.java.org.game;

import main.java.org.game.UI.MainMenu;

public class Isten2 extends Isten{
    @Override
    protected void addUpdatables() {
        addUpdatable(new MainMenu());
    }
}
