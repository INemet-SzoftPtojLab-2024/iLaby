package main.java.org.game.Graphics;

import main.java.org.linalg.Vec2;
import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import static org.junit.jupiter.api.Assertions.*;

class ImageTest {
    @Test
    void testImageDefaultConstructor() {
        Image image = new Image();
        assertNotNull(image);
        assertEquals(0, image.getPosition().x);
        assertEquals(0, image.getPosition().y);
        assertEquals(1, image.getScale().x);
        assertEquals(1, image.getScale().y);
    }
    @Test
    void testImageConstructorWithImagePath() {
        Vec2 pos = new Vec2(100, 200);
        Vec2 scale = new Vec2(2, 2);
        String imagePath = "assets/character/character0_ded.png";

        Image image = new Image(pos, scale, imagePath);

        assertNotNull(image);
        assertEquals(pos, image.getPosition());
        assertEquals(scale, image.getScale());
        assertNotNull(image.getImage());
    }
    @Test
    void testSetImage() {
        Vec2 pos = new Vec2(100, 200);
        Vec2 scale = new Vec2(2, 2);
        String imagePath = "assets/character/character1_ded.png";

        Image image = new Image(pos, scale, imagePath);

        BufferedImage newImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        image.setImage(newImage);

        assertEquals(newImage, image.getImage());
    }

    @Test
    void testIsUIElement() {
        Vec2 pos = new Vec2(100, 200);
        Vec2 scale = new Vec2(2, 2);
        String imagePath = "assets/character/character0_left1.png";

        Image image = new Image(pos, scale, imagePath);

        assertFalse(image.isUIElement());
    }
    @Test
    public void testLoadImage_internal() {
        String existingImagePath = "assets/character/character0_right1.png";
        String nonExistingImagePath = "path/to/nonexisting/image.jpg";

        BufferedImage existingImage = Image.loadImage_internal(existingImagePath);
        assertNotNull(existingImage);

        BufferedImage nonExistingImage = Image.loadImage_internal(nonExistingImagePath);
        assertNull(nonExistingImage);
    }
}