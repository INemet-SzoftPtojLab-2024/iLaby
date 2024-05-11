package main.java.org.game.Graphics;

import main.java.org.linalg.Vec2;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class ImageUITest {
    @Test
    public void testIsUIElement() {
        ImageUI imageUI = new ImageUI();
        assertTrue(imageUI.isUIElement());
    }
     @Test
    public void testDefaultConstructor() {
        ImageUI imageUI = new ImageUI();
        assertNull( imageUI.getImage());
         assertEquals(0, imageUI.getPosition().x);
         assertEquals(0, imageUI.getPosition().y);
        assertEquals(1, imageUI.getScale().x);
        assertEquals(1, imageUI.getScale().y);

    }

    @Test
    public void testParameterizedConstructor() {
        Vec2 position = new Vec2(5, 10);
        Vec2 scale = new Vec2(2, 2);
        String imagePath = "assets/character/character0_left1.png";

        ImageUI imageUI = new ImageUI(position, scale, imagePath);

        assertEquals(position, imageUI.getPosition());
        assertEquals( scale, imageUI.getScale());

        BufferedImage loadedImage = Image.loadImage(imagePath);
        assertEquals( loadedImage, imageUI.getImage());
    }
}