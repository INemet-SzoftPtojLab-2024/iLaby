package main.java.org.game.Graphics;

import main.java.org.game.Input.Input;
import main.java.org.linalg.Vec2;

import java.awt.*;
import java.util.Comparator;

/**
 * Abstract class for objects that can be rendered.
 */
public abstract class Renderable implements Cloneable{
    protected Vec2 position=new Vec2();
    protected Vec2 scale=new Vec2();
    protected Vec2 renderedPosition=new Vec2();//the pixel position of the position
    protected Vec2 renderedScale=new Vec2();//the pixel scale of the scale

    protected int sortingLayer=0;//sorting layer


    protected char hOrigin=CENTER;
    protected char vOrigin=CENTER;

    private boolean visible=true;

    //ui things
    /** possible values: Renderable.LEFT, Renderable.CENTER, Renderable.RIGHT <br>
     * for example a value of Renderable.LEFT means that the position of the x=0 abscissa is the left side of the screen <br>
     * in the case of LEFT and CENTER, the positive direction is from left to right, in the case of RIGHT, the positive direction is from right to left <br>
     * the default value is Renderable.LEFT <br>
     * ignored for non-ui renderables */
    private char hAlignment=LEFT;
    /** possible values: Renderable.TOP, Renderable.CENTER, Renderable.BOTTOM <br>
     * for example a value of Renderable.BOTTOM means that the position of the y=0 ordinate is the bottom side of the screen <br>
     * in the case of TOP and CENTER, the positive direction is from top to bottom, in the cas of BOTTOM, the positive direction is from bottom to top <br>
     * the default value is Renderable.TOP <br>
     * ignored for non-ui renderables */
    private char vAlignment=TOP;

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

    public final int getSortingLayer(){return sortingLayer;}
    public final void setSortingLayer(int sortingLayer){this.sortingLayer=sortingLayer;}

    /**
     * sets the origin of the renderable <br>
     * for example hOrigin=LEFT, vOrigin=BOTTOM means that the position value of the renderable gives us the position of the bottom left corner<br>
     * the default values for both horizontal and vertical are Renderable.CENTER
     * @param hOrigin the horizontal position of the origin, possible values are Renderable.LEFT, Renderable.CENTER, Renderable.RIGHT
     * @param vOrigin the vertical position of the origin, possible values are Renderable.BOTTOM, Renderable.CENTER, Renderable.TOP
     */
    public final void setOrigin(int hOrigin, int vOrigin)
    {
        if(hOrigin==LEFT||hOrigin==CENTER||hOrigin==RIGHT)
            this.hOrigin=(char)hOrigin;
        if(vOrigin==BOTTOM||vOrigin==CENTER|| vOrigin==TOP)
            this.vOrigin=(char)vOrigin;
    }


    /**
     * sets the alignment of the renderable <br>
     * for example hAlignment=LEFT, vAlignment=BOTTOM means that the position value of the renderable means the distance from the bottom left side of the screen
     * @param hAlignment the horizontal alignment of the renderable, possible values are Renderable.LEFT, Renderable.CENTER, Renderable.RIGHT
     * @param vAlignment the vertical alignment of the renderable, possible values are Renderable.BOTTOM, Renderable.CENTER, Renderable.TOP
     */
    public final void setAlignment(int hAlignment, int vAlignment)
    {
        if(hAlignment==LEFT||hAlignment==CENTER||hAlignment==RIGHT)
            this.hAlignment=(char)hAlignment;
        if(vAlignment==BOTTOM||vAlignment==CENTER|| vAlignment==TOP)
            this.vAlignment=(char)vAlignment;
    }

    /** returns the horizontal alignment of the renderable */
    public final int getHorizontalAlignment()
    {
        return (int)this.hAlignment;
    }

    /** returns the vertical alignment of the renderable */
    public final int getVerticalAlignment()
    {
        return (int)this.vAlignment;
    }



    public final Vec2 getRenderedPosition()
    {
        return this.renderedPosition;
    }
    public final void setRenderedPosition(Vec2 renderedPosition)
    {
        this.renderedPosition=renderedPosition;
    }
    public final Vec2 getRenderedScale()
    {
        return this.renderedScale;
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


    /**a function that is called by the Isten <br>
     * Renderables have the opportunity to override it if necessary
     * @param inputHandler the input handler
     */
    public void processInput(Input inputHandler){}


    @Override
    public Renderable clone() {
        try {
            Renderable clone = (Renderable) super.clone();
            clone.position=position.clone();
            clone.scale=scale.clone();
            clone.hAlignment=hAlignment;
            clone.vAlignment=vAlignment;
            clone.hOrigin=hOrigin;
            clone.vOrigin=vOrigin;
            clone.visible=visible;
            clone.sortingLayer=sortingLayer;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * helper function for the ui position calculation <br>
     * it converts the position value of the renderable of whatever alignment to a position value with top-left alignment <br>
     * do not call it for non-ui renderables, it will do nothing <br>
     * @return the position value converted to the top-left alignment
     */
    public static Vec2 convertUIPositionToTopLeft(final Renderable morbius, final Vec2 screenSize)
    {
        if(!morbius.isUIElement())
            return morbius.position.clone();

        Vec2 converted=new Vec2();

        switch(morbius.hAlignment)
        {
            case LEFT:
                converted.x=morbius.position.x;
                break;

            case CENTER:
                converted.x=morbius.position.x+screenSize.x/2;
                break;

            case RIGHT:
                converted.x=screenSize.x-morbius.position.x;
                break;

            default:
                return morbius.position.clone();
        }

        switch(morbius.vAlignment)
        {
            case TOP:
                converted.y=morbius.position.y;
                break;

            case CENTER:
                converted.y=morbius.position.y+screenSize.y/2;
                break;

            case BOTTOM:
                converted.y=screenSize.y-morbius.position.y;
                break;

            default:
                return morbius.position.clone();
        }

        return converted;
    }

    public static class SortingLayerComparator implements Comparator<Renderable>{
        public int compare(Renderable a, Renderable b)
        {
            return b.sortingLayer - a.sortingLayer;
        }
    }
}