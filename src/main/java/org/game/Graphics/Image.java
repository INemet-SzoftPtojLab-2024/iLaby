package main.java.org.game.Graphics;

import main.java.org.linalg.Vec2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image extends Renderable {
    private Vec2 scale;
    private int width;
    private int height;
    private BufferedImage image;

    public Image() {
        position = new Vec2();
        scale = new Vec2(1,1);
        width = 10;
        height = 10;
    }
    public Image(Vec2 pos, int w, int h, Vec2 scale, String imagePath) {

        this.position = pos;
        this.width = w;
        this.height = h;
        this.scale = scale;

        try {
            image = ImageIO.read(new File(imagePath));
        }
        catch(IOException e) {
            System.err.println(e.getMessage());
        }

    }

    @Override
    public void render(Graphics graphics) {
        if(image == null) {
            System.err.println("Image is null!");
            return;
        }
        graphics.drawImage(image, (int)position.x, (int)position.y, (int)(width * scale.x), (int)(height * scale.y), null, null);
    }

    public Vec2 getScale() {
        return scale;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setScale(Vec2 scale) {
        this.scale = scale;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}