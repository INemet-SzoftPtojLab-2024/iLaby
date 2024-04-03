package main.java.org.game.Graphics;

import main.java.org.linalg.Vec2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ButtonUI extends Renderable{

    private ImageUI image=null;
    private TextUI text=null;
    private ClickListener onClick=null;
    private boolean isHovered=false, isHeld=false;

    public ButtonUI(Vec2 pos, Vec2 scale)
    {
        this.setPosition(pos);
        this.setScale(scale);
    }

    public ButtonUI(Vec2 pos, Vec2 scale, String imagePath, String text, String fontPath, int fontSize)
    {
        this.setPosition(pos);
        this.setScale(scale);

        this.image=new ImageUI(pos, scale,imagePath);
        this.image.setAlignment(this.getHorizontalAlignment(),this.getVerticalAlignment());

        this.text=new TextUI(text,pos, fontPath, fontSize,255,255,255);
        this.text.setAlignment(this.getHorizontalAlignment(),this.getVerticalAlignment());
    }

    @Override
    public void render(Graphics graphics) {

        if(isHovered)
            ((Graphics2D)graphics).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.8f));
        else if(isHeld)
            ((Graphics2D)graphics).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.6f));

        if(image!=null)
        {
            image.setRenderedPosition(this.renderedPosition);
            image.setRenderedScale(this.renderedScale);
            image.render(graphics);
        }
        if(text!=null)
        {
            text.setRenderedPosition(this.renderedPosition);
            text.setRenderedScale(this.renderedScale);
            text.render(graphics);
        }

        if(isHovered||isHeld)
            ((Graphics2D)graphics).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
    }

    @Override
    public void processInput(Vec2 mousePos, boolean mousePressed, boolean mouseHeld, boolean mouseReleased, boolean mouseClicked)
    {
        Vec2 tempPos=new Vec2();

        isHovered=false;
        isHeld=false;

        switch (hOrigin) {
            case Renderable.LEFT -> tempPos.x = renderedPosition.x;
            case Renderable.CENTER -> tempPos.x = renderedPosition.x - 0.5f * renderedScale.x;
            case Renderable.RIGHT -> tempPos.x = renderedPosition.x - renderedScale.x;
        }

        switch (vOrigin) {
            case Renderable.TOP -> tempPos.y = renderedPosition.y;
            case Renderable.CENTER -> tempPos.y = renderedPosition.y - 0.5f * renderedScale.y;
            case Renderable.BOTTOM -> tempPos.y = renderedPosition.y - renderedScale.y;
        }

        if(tempPos.x>mousePos.x)
            return;
        if(tempPos.y>mousePos.y)
            return;
        if(tempPos.x+renderedScale.x<mousePos.x)
            return;
        if(tempPos.y+renderedScale.y<mousePos.y)
            return;

        if(!mouseHeld)
            isHovered=true;
        else
            isHeld=true;

        if(mouseClicked&&onClick!=null)
            onClick.onClick();
    }

    @Override
    public boolean isUIElement() {
        return true;
    }

    /** adds a click listener the the button <br>
     * the onClick function of the ClickListener will be called in case of a click
     * @param clk_amg the ClickListener (it should be null, if you want the button to do nothing)
     */
    public void addClickListener(ClickListener clk_amg)
    {
        onClick=clk_amg;
    }
}
