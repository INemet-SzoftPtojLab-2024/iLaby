package main.java.org.game.Graphics;

import main.java.org.linalg.Vec2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class for storing images.
 * Can be rendered, drawn in GameRenderer.
 */
public class Image extends Renderable {
    private BufferedImage image;

    /**
     * Default constructor for Image.
     * Initializes position to origin, scale to (1,1), width to 10, and height to 10.
     */
    public Image() {
        position = new Vec2();
        scale = new Vec2(1,1);
    }
    /**
     * Constructor for Image.
     *
     * @param pos The position of the image
     * @param scale The scale of the image
     * @param imagePath The path to the image file
     */
    public Image(Vec2 pos, Vec2 scale, String imagePath) {

        this.position = pos;
        this.scale = scale;
        this.visible = visible;

        try {
            image = ImageIO.read(new File(imagePath));
        }
        catch(IOException e) {
            System.err.println(e.getMessage());
        }

    }
    /**
     * Method to render the image.
     *
     * @param graphics The Graphics object on which to render the image
     */
    @Override
    public void render(Graphics graphics) {
        if(image == null) {
            System.err.println("Image is null!");
            return;
        }

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

        graphics.drawImage(image, (int)tempPos.x, (int)tempPos.y, (int)(renderedScale.x), (int)(renderedScale.y), null, null);
    }


    public BufferedImage getImage() {
        return image;
    }

    @Override
    public boolean isUIElement()
    {
        return false;
    }
}