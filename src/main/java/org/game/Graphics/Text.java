package main.java.org.game.Graphics;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import main.java.org.linalg.*;

/**
 * Class for rendering text.
 */
public class Text extends Renderable {
    private final String text;
    private Font font;
    private final Color color;

    /**
     * Default constructor for Text.
     * Initializes font to "Dialog", size to 14, color to white, and text to "Basic Text".
     */
    public Text() {
        font = new Font("Dialog", Font.PLAIN, 14);
        color = Color.WHITE;
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
        try {
           Font f = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
           font = f.deriveFont(Font.BOLD, fontSize);
        }
        catch(IOException | FontFormatException e) {
            System.err.println(e.getMessage());
        }
        color = new Color(r,g,b);
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
        color = new Color(r,g,b);
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

        //Shadow
        graphics.setColor(Color.black);
        graphics.drawString(text, (int)position.x+5, (int)position.y+5);

        //Text with its color
        graphics.setColor(color);
        graphics.drawString(text, (int)position.x, (int)position.y);
    }

    /**
     * Method to get the text.
     *
     * @return The text
     */
    public String getText() {
        return text;
    }
}