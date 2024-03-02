package main.java.org.game.Graphics;

import main.java.org.linalg.Vec2;

import java.awt.*;

abstract class Renderable {
    protected Vec2 position;
    abstract public void render(Graphics graphics);

    public Vec2 getPosition() {
        return position;
    }
}