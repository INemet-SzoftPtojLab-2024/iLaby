package main.java.org.game.Graphics;

import main.java.org.linalg.Vec2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUI extends Image{
    /**
     * Default constructor for Image.
     * Initializes position to origin, scale to (1,1), width to 10, and height to 10.
     */
    public ImageUI() {
        super();
    }
    /**
     * Constructor for ImageUI.
     *
     * @param pos The position of the image
     * @param scale The scale of the image
     * @param imagePath The path to the image file
     */
    public ImageUI(Vec2 pos, Vec2 scale, String imagePath) {

        super(pos, scale, imagePath);
    }

    @Override
    public boolean isUIElement()
    {
        return true;
    }
}
