package main.java.org.game.Graphics;

import main.java.org.linalg.Vec2;

import java.awt.*;

/**
 * Abstract class for objects that can be rendered.
 */
public abstract class Renderable {
    protected Vec2 position=new Vec2();
    protected Vec2 scale=new Vec2();
    protected Vec2 renderedPosition=new Vec2();//the pixel position of the position
    protected Vec2 renderedScale=new Vec2();//the pixel scale of the scale

    protected int hOrigin=CENTER;
    protected int vOrigin=CENTER;

    private boolean visible=true;

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
    public final Vec2 getPosition() {
        return position;
    }
    public final void setPosition(Vec2 position) { this.position=position;}

    public final Vec2 getScale() {
        return scale;
    }
    public final void setScale(Vec2 scale) {
        this.scale = scale;
    }

    public final boolean getVisibility(){
        return this.visible;
    }
    public final void setVisibility(boolean visible)
    {
        this.visible=visible;
    }

    /**
     * sets the origin of the renderable <br>
     * for example hOrigin=LEFT, vOrigin=BOTTOM means that the position value of the renderable gives us the position of the bottom left corner
     * @param hOrigin the horizontal position of the origin, possible values are Renderable.LEFT, Renderable.CENTER, Renderable.RIGHT
     * @param vOrigin the horizontal position of the origin, possible values are Renderable.BOTTOM, Renderable.CENTER, Renderable.TOP
     */
    public final void setOrigin(int hOrigin, int vOrigin)
    {
        if(hOrigin==LEFT||hOrigin==CENTER||hOrigin==RIGHT)
            this.hOrigin=hOrigin;
        if(vOrigin==BOTTOM||vOrigin==CENTER|| vOrigin==TOP)
            this.vOrigin=vOrigin;
    }

    public final void setRenderedPosition(Vec2 renderedPosition)
    {
        this.renderedPosition=renderedPosition;
    }

    public final void setRenderedScale(Vec2 renderedScale)
    {
        this.renderedScale=renderedScale;
    }

    public abstract boolean isUIElement();

    public static final int CENTER=2;
    public static final int BOTTOM=1;
    public static final int TOP=3;
    public static final int LEFT=1;
    public static final int RIGHT=3;
}