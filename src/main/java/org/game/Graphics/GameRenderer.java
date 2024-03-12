package main.java.org.game.Graphics;

import main.java.org.manager.KeyHandler;
import main.java.org.manager.MouseHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * GameRenderer class handles rendering of game elements.
 */
public class GameRenderer extends JPanel implements ActionListener {

    private ArrayList<Renderable> renderables;
    private static KeyHandler keyHandler;
    private static MouseHandler mouseHandler = new MouseHandler();

    /**
     * Constructor for GameRenderer.
     */
    public GameRenderer() {
        this.setPreferredSize(new Dimension(500, 500));
        setFocusable(true);

        //Keyboard
        keyHandler = new KeyHandler();
        addKeyListener(new TAdapter());

        //Mouse
        addMouseListener(mouseHandler);

        setDoubleBuffered(true);
        renderables = new ArrayList<>();
    }

    /**
     * Method to add a renderable object to the list.
     *
     * @param r The renderable object to add
     */
    public void addRenderable(Renderable r) {
        renderables.add(r);
    }

    public void deleteRenderable(Renderable r){renderables.remove(r);}

    /**
     * Method to paint/render the graphics.
     *
     * @param graphics The Graphics object to render on
     */
    public void paint(Graphics graphics) {

        setBackground(new Color(50,50,50));

        //graphics.dispose();

        graphics.fillRect(0,0,this.getWidth(), this.getHeight());

        if(renderables.isEmpty()) return;
        for(int i = 0; i < renderables.size(); i++) {
            renderables.get(i).render(graphics);
        }
    }

    public KeyHandler getKeyHandler(){return keyHandler;}

    @Override
    public void actionPerformed(ActionEvent e) {
        //Needed for implementation
    }

    /**
     * Internal class for handling keyboard events.
     */
    private class TAdapter extends KeyAdapter {

        /**
         * Method called when a key is released.
         *
         * @param e The KeyEvent object representing the key release event
         */
        public void keyReleased(KeyEvent e) {
            keyHandler.keyReleased(e); // Call key released method in key handler
        }

        /**
         * Method called when a key is pressed.
         *
         * @param e The KeyEvent object representing the key press event
         */
        public void keyPressed(KeyEvent e) {
            keyHandler.keyPressed(e); // Call key pressed method in key handler
        }
    }
}