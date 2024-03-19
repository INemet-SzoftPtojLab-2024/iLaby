package main.java.org.game.Graphics;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import main.java.org.linalg.*;

/**
 * Class for rendering text.
 */
public class TextUI extends Text {

    /**
     * Default constructor for TextUI.
     * Initializes font to "Dialog", size to 14, color to white, and text to "Basic Text".
     */
    public TextUI() {
        super();
    }

    /**
     * Constructor for TextUI with custom font loaded from a file.
     *
     * @param text The text to render
     * @param pos The position of the text
     * @param fontPath The path to the font file
     * @param fontSize The size of the font
     * @param r The red component of the text color
     * @param g The green component of the text color
     * @param b The blue component of the text color
     */
    public TextUI(String text, Vec2 pos, String fontPath, int fontSize, int r, int g, int b)
    {
        super(text, pos, fontPath, fontSize, r, g, b);
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
    public TextUI(String text, Vec2 pos, int fontSize, int r, int g, int b) {

        super(text, pos, fontSize, r, g, b);
    }

    @Override
    public boolean isUIElement()
    {
        return true;
    }
}