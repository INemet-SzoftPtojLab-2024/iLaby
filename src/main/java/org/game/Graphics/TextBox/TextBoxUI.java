package main.java.org.game.Graphics.TextBox;

import main.java.org.game.Graphics.Renderable;
import main.java.org.linalg.Vec2;

import java.awt.*;

public class TextBoxUI extends Renderable {

    private Font font=defaultFont;
    private int fontSize=32;
    private static Font defaultFont=new Font("Dialog", Font.PLAIN, 32);

    private final StringBuilder text=new StringBuilder();

    private boolean focused=false;

    private TextBoxValueChangedListener onValueChange=null;
    private TextBoxInputStartListener onInputStart=null;
    private TextBoxInputEndListener onInputEnd=null;

    public TextBoxUI(Vec2 pos,Vec2 scale)
    {
        this.position=pos;
        this.scale=scale;
    }

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public boolean isUIElement() {
        return true;
    }

    @Override
    public void processInput(Vec2 mousePos, boolean mousePressed, boolean mouseHeld, boolean mouseReleased, boolean mouseClicked)
    {
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
     * Listener, whose onValueChanged function will be called every time the value of the textbox is changed via keyboard
     * @param tbvcl the listener (<b>null</b> if no callback is needed)
     */
    public void onValueChange(TextBoxValueChangedListener tbvcl)
    {
        onValueChange=tbvcl;
    }

    /**
     * Listener, whose onInputStart function will be called when the user started interacting with the textbox (clicked on it)
     * @param tbisl the listener (<b>null</b> if no callback is needed)
     */
    public void onInputStart(TextBoxInputStartListener tbisl)
    {
        onInputStart=tbisl;
    }

    /**
     * Listener, whose onInputEnd function will be called when the user stopped interacting with the textbox (pressed enter or clicked out of the input field) <br>
     * Don't use lambda functions, as
     * @param tbiel the listener (<b>null</b> if no callback is needed)
     */
    public void onInputEnd(TextBoxInputEndListener tbiel)
    {
        onInputEnd=tbiel;
    }
}
