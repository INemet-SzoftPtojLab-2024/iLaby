package main.java.org.game.Graphics;

import main.java.org.linalg.Vec2;

import java.awt.*;

/**
 * Abstract class for objects that can be rendered.
 */
abstract class Renderable {
    protected Vec2 position;
    /**
     * Abstract method to render the object.
     *
     * @param graphics The Graphics object on which to render the object
     */
    abstract public void render(Graphics graphics);
    /**
     * Method to get the position of the renderable object.
     *
     * @return The position of the renderable object
     */
    public Vec2 getPosition() {
        return position;
    }
}