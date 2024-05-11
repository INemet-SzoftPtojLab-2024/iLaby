package main.java.org.game.Input;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import static org.junit.jupiter.api.Assertions.*;


class InputTest {
    private Input input;

    @BeforeEach
    void setUp() {
        input = new Input();
    }

    @Test
    void testKeyPressAndRelease() {
        KeyEvent press = new KeyEvent(mock(Component.class), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'a');
        KeyEvent release = new KeyEvent(mock(Component.class), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_A, 'a');

        input.keyPressed(press);
        input.update();
        assertTrue(input.isKeyDown(KeyEvent.VK_A));

        input.keyReleased(release);
        input.update();
        assertFalse(input.isKeyDown(KeyEvent.VK_A));
    }
}
