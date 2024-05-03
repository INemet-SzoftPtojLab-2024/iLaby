package main.java.org.game.Graphics.TextBox;

import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Input.Input;
import main.java.org.linalg.Vec2;

import java.awt.*;
import java.awt.event.KeyEvent;

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

    private long lastBackspaceToggle=-1;//helper for processInput

    @Override
    public void processInput(Input inputHandler)
    {
        boolean mousePressed=inputHandler.isMouseButtonPressed(Input.MOUSE_LEFT);
        Vec2 mousePos=inputHandler.getMousePosition();

        if(mousePressed)
        {
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
        else
        {
            if(!focused)
                return;

            if(inputHandler.isKeyPressed(KeyEvent.VK_ENTER))
            {
                if(onInputEnd!=null)
                    onInputEnd.onInputEnd(this);
                return;
            }

            if(inputHandler.isKeyDown(KeyEvent.VK_BACK_SPACE))
            {
                if(!text.isEmpty()&&System.nanoTime()-lastBackspaceToggle>400000000)
                {
                    text.deleteCharAt(text.length()-1);
                    if(onValueChange!=null)
                        onValueChange.onValueChanged(this);

                    if(lastBackspaceToggle==-1)//hogy csak az elso lenyomas utan kelljen kivarni a teljes cooldownt
                        lastBackspaceToggle=System.nanoTime();
                    else
                        lastBackspaceToggle=System.nanoTime()-350000000;
                    return;
                }
            }
            else
                lastBackspaceToggle=-1;

            char key=(char)-1;
            boolean lowerCase= inputHandler.isKeyDown(KeyEvent.VK_SHIFT) == inputHandler.isCapsLockOn();
            for(int i=KeyEvent.VK_A;i<=KeyEvent.VK_Z;i++)//betuk
            {
                if(inputHandler.isKeyPressed(i))
                {
                    key=(char)i;
                    if(lowerCase)
                        key+='a'-'A';
                    break;
                }
            }
            for(int i=KeyEvent.VK_0;i<=KeyEvent.VK_9;i++)//szamok
            {
                if(inputHandler.isKeyPressed(i))
                {
                    key=(char)i;
                    break;
                }
            }
            if(inputHandler.isKeyPressed(KeyEvent.VK_SPACE))
                key=(char)KeyEvent.VK_SPACE;

            if(key!=(char)-1) {
                text.append(key);
                if(onValueChange!=null)
                    onValueChange.onValueChanged(this);
                return;
            }
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
        this.text.delete(0,this.text.length());
        this.text.append(text);
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
}
