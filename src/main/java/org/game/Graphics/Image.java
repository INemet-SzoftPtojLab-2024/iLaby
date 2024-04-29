package main.java.org.game.Graphics;

import main.java.org.linalg.Vec2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

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

        image = loadImage_internal(imagePath);
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

        graphics.drawImage(image, Math.round(tempPos.x)-1, Math.round(tempPos.y)-1, (int)(renderedScale.x+0.001)+2, (int)(renderedScale.y+0.001)+2, null, null);
    }


    public BufferedImage getImage() {
        return image;
    }
    public void setImage(BufferedImage image){this.image=image;}

    @Override
    public boolean isUIElement()
    {
        return false;
    }


    //resource management
    /** a hashmap for the already loaded images */
    private static HashMap<String, BufferedImage> loadedImages=new HashMap<>();

    /**
     * load an image with a specified path. if it has already been loaded, no import is done
     * @param imagePath the path of the image
     * @return the loaded image or null, if the loading was unsuccessful
     */
    private static BufferedImage loadImage_internal(String imagePath)
    {
        if(imagePath==null)
            return null;

        File file=new File(imagePath);

        if(!file.exists()||!file.canRead())//if the file cannot be opened, there is nothing we can do
        {
            System.out.println(file.getAbsolutePath()+" could not be loaded");
            return null;
        }

        if(loadedImages.containsKey(file.getAbsolutePath()))//if the image is already loaded, return it
        {
            return loadedImages.get(file.getAbsolutePath());
        }

        //if the image is not yet loaded, try to do so and then return the value
        BufferedImage image=null;

        try{
            image=ImageIO.read(file);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        if(image==null)
        {
            System.out.println(file.getAbsolutePath()+" could not be loaded");
            return null;
        }

        loadedImages.put(file.getAbsolutePath(), image);
        return image;
    }
}