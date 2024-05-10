package main.java.org.game.Camera;

import main.java.org.linalg.Vec2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CameraTest {
    @Test
    public void testGetPosition() {
        Camera camera = new Camera();

        Vec2 position = new Vec2(10, 20);
        camera.setPosition(position);

        Vec2 retrievedPosition = camera.getPosition();
        assertNotSame(position, retrievedPosition);

        assertEquals(position.x, retrievedPosition.x);
        assertEquals(position.y, retrievedPosition.y);
    }

    @Test
    public void testGetPixelsPerUnit() {
        Camera camera = new Camera();

        float pixelsPerUnit = camera.getPixelsPerUnit();
        assertEquals(50, pixelsPerUnit);
    }

    @Test
    public void testSetPixelsPerUnit() {
        Camera camera = new Camera();

        float pixelsPerUnit = 100;
        camera.setPixelsPerUnit(pixelsPerUnit);

        assertEquals(pixelsPerUnit, camera.getPixelsPerUnit());
    }
}