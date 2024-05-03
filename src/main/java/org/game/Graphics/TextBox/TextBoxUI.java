package main.java.org.game.Graphics.TextBox;

import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Graphics.Text;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Input.Input;
import main.java.org.linalg.Vec2;


import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class TextBoxUI extends Renderable {

    private Font font=defaultFont;
    private int fontSize=32;
    private static Font defaultFont=new Font("Dialog", Font.PLAIN, 32);
    private final int padding=3;

    private BufferedImage background=null;


    private BufferedImage graphics;

    private Color textColour=new Color(255,255,255);
    private Color cursorColour=new Color(255,209,0);
    private Color lineColourActive=new Color(0,255,255);
    private Color lineColourInactive=new Color(200,200,200);

    private String text="";

    private boolean focused=false;

    private TextBoxValueChangedListener onValueChange=null;
    private TextBoxInputStartListener onInputStart=null;
    private TextBoxInputEndListener onInputEnd=null;

    public TextBoxUI(Vec2 pos,Vec2 scale)
    {
        this.position=pos;
        this.scale=scale;

        graphics=new BufferedImage((int)scale.x,(int)scale.y,BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void render(Graphics graphics) {
        if(this.graphics.getWidth()!=(int)this.scale.x||this.graphics.getHeight()!=(int)this.scale.y)
            this.graphics=new BufferedImage((int)scale.x,(int)scale.y,BufferedImage.TYPE_INT_ARGB);

        //clear colour attachment
        Graphics2D g=(Graphics2D) this.graphics.getGraphics();
        g.setBackground(new Color(0,0,0,0));
        g.clearRect(0,0,this.graphics.getWidth(), this.graphics.getHeight());

        //draw line
        if(focused)
            g.setColor(lineColourActive);
        else
            g.setColor(lineColourInactive);
        g.fillRect(0,this.graphics.getHeight()-3-padding,this.graphics.getWidth(),3);

        //draw text
        g.setColor(textColour);
        g.setFont(font);
        FontMetrics fm=g.getFontMetrics(font);

        int textWidth= fm.stringWidth(text);
        int textHeight= fm.getHeight();
        g.drawString(text,Math.min(padding,this.graphics.getWidth()-textWidth-4-padding),this.graphics.getHeight()/2+fm.getMaxDescent());

        //draw cursor
        if(focused&&(System.nanoTime()/500000000)%2==0)
        {
            g.setColor(cursorColour);
            int cursorX=textWidth>this.graphics.getWidth()-4-padding?this.graphics.getWidth()-2-padding:textWidth+1+padding;
            int cursorY=this.graphics.getHeight()/2-(int)(0.85f*textHeight)+fm.getMaxDescent();
            g.fillRect(cursorX, cursorY,2,textHeight);
        }

        //padding
        g.clearRect(0,0,this.graphics.getWidth(),padding);
        g.clearRect(0,this.graphics.getHeight()-padding,this.graphics.getWidth(),padding);
        g.clearRect(0,0,padding, this.graphics.getHeight());
        g.clearRect(this.graphics.getWidth()-padding,0,padding,this.graphics.getHeight());

        //draw to screen
        Vec2 tempPos=new Vec2();

        switch(hOrigin)
        {
            case Renderable.LEFT:
                tempPos.x=renderedPosition.x;
                break;

            case Renderable.CENTER:
                tempPos.x=renderedPosition.x-0.5f*renderedScale.x;
                break;

            case Renderable.RIGHT:
                tempPos.x=renderedPosition.x-renderedScale.x;
                break;
        }

        switch(vOrigin)
        {
            case Renderable.TOP:
                tempPos.y=renderedPosition.y;
                break;

            case Renderable.CENTER:
                tempPos.y=renderedPosition.y-0.5f*renderedScale.y;
                break;

            case Renderable.BOTTOM:
                tempPos.y=renderedPosition.y-renderedScale.y;
                break;
        }

        if(background!=null)//draw background
            graphics.drawImage(background, Math.round(tempPos.x), Math.round(tempPos.y), (int)(renderedScale.x+0.001), (int)(renderedScale.y+0.001), null);
        //draw the real stuff
        graphics.drawImage(this.graphics, Math.round(tempPos.x), Math.round(tempPos.y), (int)(renderedScale.x+0.001), (int)(renderedScale.y+0.001), null);
    }

    @Override
    public boolean isUIElement() {
        return true;
    }


    private boolean hasBeenRegistered=false;

    @Override
    public void processInput(Input inputHandler)
    {
        if(!hasBeenRegistered)
        {
            inputHandler.addTypeListener(this::onType);
            hasBeenRegistered=true;
        }

        boolean mousePressed=inputHandler.isMouseButtonPressed(Input.MOUSE_LEFT);
        Vec2 mousePos=inputHandler.getMousePosition();

        if(!mousePressed)
            return;

        Vec2 tempPos=new Vec2();

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

        boolean isClickInside=true;

        if(tempPos.x>mousePos.x)
            isClickInside=false;
        else if(tempPos.y>mousePos.y)
            isClickInside=false;
        else if(tempPos.x+renderedScale.x<mousePos.x)
            isClickInside=false;
        else if(tempPos.y+renderedScale.y<mousePos.y)
            isClickInside=false;

        if(isClickInside&&!focused)//input start
        {
            focused=true;
            if(onInputStart!=null)
                onInputStart.onInputStart(this);
        }
        else if(!isClickInside&&focused)//input end
        {
            focused=false;
            if(onInputEnd!=null)
                onInputEnd.onInputEnd(this);
        }
    }

    /**
     * returns the current content of the textbox
     * @return a string
     */
    public String getText()
    {
        return text.toString();
    }

    /**
     * sets the content of the textbox
     * @param text the new content
     */
    public void setText(String text)
    {
        this.text=text;
    }

    /**
     * Listener, whose onValueChanged function will be called every time the value of the textbox is changed via keyboard <br>
     * <br><b>Example usage: </b><br>
     * new TextBoxUI(...).onValueChange(textBox->System.out.println(textBox.getText()));
     * @param tbvcl the listener (<b>null</b> if no callback is needed)
     */
    public void onValueChange(TextBoxValueChangedListener tbvcl)
    {
        onValueChange=tbvcl;
    }

    /**
     * Listener, whose onInputStart function will be called when the user started interacting with the textbox (clicked on it)<br>
     * <br><b>Example usage: </b><br>
     * new TextBoxUI(...).onInputStart(textBox->System.out.println(textBox.getText()));
     * @param tbisl the listener (<b>null</b> if no callback is needed)
     */
    public void onInputStart(TextBoxInputStartListener tbisl)
    {
        onInputStart=tbisl;
    }

    /**
     * Listener, whose onInputEnd function will be called when the user stopped interacting with the textbox (pressed enter or clicked out of the input field) <br>
     * <br><b>Example usage: </b><br>
     * new TextBoxUI(...).onInputEnd(textBox->System.out.println(textBox.getText()));
     * @param tbiel the listener (<b>null</b> if no callback is needed)
     */
    public void onInputEnd(TextBoxInputEndListener tbiel)
    {
        onInputEnd=tbiel;
    }

    public void setFont(String fontPath, int fontSize)
    {
        this.font= Text.loadFont(fontPath, fontSize);
        if(font==null)
            font=defaultFont;
    }

    public void setBackground(String imagePath)
    {
        background=Image.loadImage(imagePath);
    }

    public void setBackground(BufferedImage image)
    {
        background=image;
    }

    public void setTextColour(int r, int g, int b, int a)
    {
        textColour=new Color(r,g,b,a);
    }

    public void setCursorColour(int r, int g, int b, int a)
    {
        cursorColour=new Color(r,g,b,a);
    }

    /**
     * sets the colour of the line under the text when the textbox is focused
     */
    public void setLineColourActive(int r, int g, int b, int a)
    {
        lineColourActive=new Color(r,g,b,a);
    }

    /**
     * sets the colour of the line under the text when the textbox is not focused
     */
    public void setLineColourInactive(int r, int g, int b, int a)
    {
        lineColourInactive=new Color(r,g,b,a);
    }

    private void onType(char c)
    {
        if(!focused)
            return;

        switch(c)
        {
            case '\b'://backspace
                if(!text.isEmpty())
                {
                    text=text.substring(0,text.length()-1);
                    if(onValueChange!=null)
                        onValueChange.onValueChanged(this);
                }
                break;

            case '\n'://enter
                if(onInputEnd!=null)
                    onInputEnd.onInputEnd(this);
                focused=false;
                break;

            default://everything else
                if(font.canDisplay(c))
                {
                    text+=c;
                    if(onValueChange!=null)
                        onValueChange.onValueChanged(this);
                }
                break;
        }
    }
}
