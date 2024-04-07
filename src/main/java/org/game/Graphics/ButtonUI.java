package main.java.org.game.Graphics;

import main.java.org.linalg.Vec2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ButtonUI extends Renderable{

    private ImageUI image=null;
    private String imagePath=null;

    private TextUI text=null;
    private String textText="";
    private String textFontPath=null;
    private int textFontSize=24;
    private int textColorR=255, textColorG=255, textColorB=255;

    private ClickListener onClick=null;
    private boolean isHovered=false, isHeld=false;

    public ButtonUI(Vec2 pos, Vec2 scale)
    {
        this.setPosition(pos);
        this.setScale(scale);

        refreshImageInternal();
        refreshTextInternal();
    }

    public ButtonUI(Vec2 pos, Vec2 scale, String imagePath, String text, String fontPath, int fontSize)
    {
        this.setPosition(pos);
        this.setScale(scale);

        this.imagePath=imagePath;
        refreshImageInternal();

        this.textText=text;
        this.textFontPath=fontPath;
        this.textFontSize=fontSize;
        refreshTextInternal();
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
            image.setAlignment(this.getHorizontalAlignment(),this.getVerticalAlignment());
            image.render(graphics);
        }
        if(text!=null)
        {
            text.setRenderedPosition(this.renderedPosition);
            text.setAlignment(this.getHorizontalAlignment(),this.getVerticalAlignment());
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

    /** sets the text in the button  */
    public void setText(String text)
    {
        this.textText=text;
        refreshTextInternal();
    }

    /** sets the font size of the text in the button */
    public void setFontSize(int fontSize)
    {
        this.textFontSize=fontSize;
        refreshTextInternal();
    }

    /** sets the font of the text in the button */
    public void setFont(String fontPath)
    {
        this.textFontPath=fontPath;
        refreshTextInternal();
    }

    /** sets the colour of the text in the button */
    public void setTextColor(int r, int g, int b)
    {
        this.textColorR=r;
        this.textColorG=g;
        this.textColorB=b;
        if(this.text!=null)
            this.text.setColor(new Color(this.textColorR, this.textColorG, this.textColorB));
    }


    /** sets the background image of the button */
    public void setImage(String imagePath)
    {
        this.imagePath=imagePath;
        refreshImageInternal();
    }


    private void refreshTextInternal()
    {
        if(textText==null||textText.isBlank())
        {
            this.text=null;
            return;
        }

        if(this.textFontPath==null)
            this.text=new TextUI(this.textText,new Vec2(), this.textFontSize,this.textColorR,this.textColorG,this.textColorB);
        else
            this.text=new TextUI(this.textText,new Vec2(), this.textFontPath, this.textFontSize,this.textColorR,this.textColorG,this.textColorB);
        this.text.setShadowOn(false);
    }

    private void refreshImageInternal()
    {
        if(this.imagePath==null||this.imagePath.isBlank())
        {
            this.image=null;
            return;
        }

        this.image=new ImageUI(new Vec2(), new Vec2(),this.imagePath);
    }
}
