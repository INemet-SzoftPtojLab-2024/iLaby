package main.java.org.manager;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Class to handle mouse events
 */
    public class MouseHandler implements MouseListener {
    /**
     * Method called when the mouse is clicked.
     *
     * @param e The MouseEvent object representing the mouse click event
     */
        @Override
        public void mouseClicked(MouseEvent e) {
            int mX = e.getX();
            int mY = e.getY();
            //System.out.println("Mouse " + mX + ", " + mY);
        }
    /**
     * Method called when a mouse button is pressed.
     *
     * @param e The MouseEvent object representing the mouse press event
     */
        @Override
        public void mousePressed(MouseEvent e) {
            int mX = e.getX();
            int mY = e.getY();

        }
    /**
     * Method called when a mouse button is released.
     *
     * @param e The MouseEvent object representing the mouse release event
     */
        @Override
        public void mouseReleased(MouseEvent e) {
            int mX = e.getX();
            int mY = e.getY();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            int mX = e.getX();
            int mY = e.getY();
        }
        @Override
        public void mouseExited(MouseEvent e) {
            int mX = e.getX();
            int mY = e.getY();
        }
    }