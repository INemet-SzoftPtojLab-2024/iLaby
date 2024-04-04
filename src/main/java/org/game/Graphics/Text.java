package main.java.org.game.Graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import main.java.org.linalg.*;

import javax.imageio.ImageIO;

/**
 * Class for rendering text.
 */
public class Text extends Renderable {
    private String text;
    private Font font;
    private boolean shadowOn=true;//is the shadow of the text rendered
    private Color color=Color.white;

    /**
     * Default constructor for Text.
     * Initializes font to "Dialog", size to 14, color to white, and text to "Basic Text".
     */
    public Text() {
        font = new Font("Dialog", Font.PLAIN, 14);
        setColor(Color.WHITE);
        text = "Basic Text";
    }

    /**
     * Constructor for Text with custom font loaded from a file.
     *
     * @param text The text to render
     * @param pos The position of the text
     * @param fontPath The path to the font file
     * @param fontSize The size of the font
     * @param r The red component of the text color
     * @param g The green component of the text color
     * @param b The blue component of the text color
     */
    public Text(String text, Vec2 pos, String fontPath, int fontSize, int r, int g, int b) {

        this.position = pos;
        this.font=loadFont_internal(fontPath, Font.BOLD, fontSize);
        setColor(new Color(r,g,b));
        this.text = text;
    }

    /**
     * Constructor for Text with default font.
     *
     * @param text The text to render
     * @param pos The position of the text
     * @param fontSize The size of the font
     * @param r The red component of the text color
     * @param g The green component of the text color
     * @param b The blue component of the text color
     */
    public Text(String text, Vec2 pos, int fontSize, int r, int g, int b) {

        this.position = pos;
        font = new Font("Dialog", Font.BOLD, fontSize);
        setColor(new Color(r,g,b));
        this.text = text;
    }

    /**
     * Method to render the text.
     *
     * @param graphics The Graphics object on which to render the text
     */
    @Override
    public void render(Graphics graphics) {

        //Set font
        graphics.setFont(font);
        FontMetrics fm=graphics.getFontMetrics(font);

        //calculate position
        Vec2 tempPos=new Vec2();
        Vec2 tempScale;
        if(text!=null)
            tempScale=new Vec2(fm.stringWidth(text), fm.getHeight());
        else
            tempScale=new Vec2();

        switch(hOrigin)
        {
            case Renderable.LEFT:
                tempPos.x=renderedPosition.x;
                break;

            case Renderable.CENTER:
                tempPos.x=renderedPosition.x-0.5f*tempScale.x;
                break;

            case Renderable.RIGHT:
                tempPos.x=renderedPosition.x-tempScale.x;
                break;
        }

        switch(vOrigin)
        {
            case Renderable.BOTTOM:
                tempPos.y=renderedPosition.y;
                break;

            case Renderable.CENTER:
                tempPos.y=renderedPosition.y+0.5f*tempScale.y;
                break;

            case Renderable.TOP:
                tempPos.y=renderedPosition.y+tempScale.y;
                break;
        }

        //Shadow
        if(shadowOn)
        {
            graphics.setColor(Color.black);
            graphics.drawString(text, (int)(tempPos.x+2), (int)(tempPos.y+2.5));
        }

        //Text with its color
        graphics.setColor(this.getColor());
        graphics.drawString(text, (int)tempPos.x, (int)tempPos.y);
    }

    /**
     * Method to get the text.
     * @return The text
     */
    public String getText() {
        return text;
    }

    public void setText(String text){
        this.text=text;
     }

    public final Color getColor(){
        return color;
    }
    public final void setColor(Color color){
        this.color=color;
    }

    @Override
    public boolean isUIElement()
    {
        return false;
    }

    /** is the shadow of the text rendered <br>
     * the default value is true */
    public final boolean isShadowOn(){return shadowOn;}
    /** should the shadow of the text be rendered or not */
    public final void setShadowOn(boolean value){shadowOn=value;}


    //resource management
    /** hashmap for the already loaded fonts */
    private static HashMap<String, Font> loadedFonts=new HashMap<>();

    /**
     * returns a Font object with the given parameters <br>
     * it imports a new Font object only if necessary
     * @param fontPath the path to the font to be loaded
     * @param style the style of the text. the possible values are Font.PLAIN, Font.BOLD and Font.ITALIC
     * @param fontSize the size of the loaded font
     * @return the loaded font, or null, if it was unsuccessful
     */
    private static Font loadFont_internal(String fontPath, int style, int fontSize)
    {
        if(fontPath==null)
            return null;
        if(style!=Font.PLAIN&&style!=Font.BOLD&&style!=Font.ITALIC)
        {
            System.out.println("Invalid style parameter in loadFont_internal for "+fontPath);
            return null;
        }


        File file=new File(fontPath);
        if(!file.exists()||!file.canRead())//if the file cannot be opened, there is nothing we can do
        {
            System.out.println(file.getAbsolutePath()+" could not be loaded");
            return null;
        }

        if(loadedFonts.containsKey(file.getAbsolutePath()))//if the font is already loaded, return it
        {
            return loadedFonts.get(file.getAbsolutePath()).deriveFont(style, fontSize);
        }

        //if the font is not yet loaded, try to do so and then return the value
        Font font=null;

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
        }
        catch(IOException | FontFormatException e) {
            System.err.println(e.getMessage());
        }
        if(font==null)
        {
            System.out.println(file.getAbsolutePath()+" could not be loaded");
            return null;
        }

        loadedFonts.put(file.getAbsolutePath(), font);
        return font.deriveFont(style, fontSize);
    }
}