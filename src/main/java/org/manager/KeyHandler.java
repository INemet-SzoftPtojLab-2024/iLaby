package main.java.org.manager;
import java.awt.event.KeyEvent;


public class KeyHandler {

    /**Key pressed in ASCI characters
     *
     */
    private int[] keyPressed = new int[256];

    /**Constructor for KeyHandler
     */
    public KeyHandler() {
        for(int i = 0; i < 256; i++) {
            keyPressed[i] = 0;
        }
    }
    /**
     * Method called when a key is pressed.
     *
     * @param e The KeyEvent object representing the key press event
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        keyPressed[key] = 1;
        //System.out.println(key);
    }

    /**
     * Method called when a key is released.
     *
     * @param e The KeyEvent object representing the key release event
     */
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        keyPressed[key] = 0;
    }

    /**
     * Method to get the array representing pressed keys.
     *
     * @return An array representing pressed keys, where 1 indicates pressed and 0 indicates released
     */
    int[] getKeyPressed() {
        return keyPressed;
    }
}
