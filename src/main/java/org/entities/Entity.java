package main.java.org.entities;

import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;

public abstract class Entity extends Updatable {
    @Override
    public abstract void onStart(Isten isten);

    @Override
    public abstract void onUpdate(Isten isten, double deltaTime);

    @Override
    public abstract void onDestroy();
}
