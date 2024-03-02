package main.java.org.manager;
import java.awt.event.KeyEvent;


public class KeyHandler {

    public static void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A) {
            System.out.println("AAA");
        }

        if (key == KeyEvent.VK_D) {

        }

        if (key == KeyEvent.VK_SHIFT) {

        }

        if (key == KeyEvent.VK_SPACE) {
        }

        if (key == KeyEvent.VK_ESCAPE) {

        }
    }
    public static void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A) {

        }

        if (key == KeyEvent.VK_D) {

        }

        if (key == KeyEvent.VK_SHIFT) {

        }
    }
}
