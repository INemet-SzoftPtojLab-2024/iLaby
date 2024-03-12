package main.java.org.game.Graphics;

import main.java.org.game.Input.Input;

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

    /**
     * Constructor for GameRenderer.
     */
    public GameRenderer(Input input) {
        this.setPreferredSize(new Dimension(500, 500));
        setFocusable(true);

        //input
        input.setTargetComponent(this);
        this.addKeyListener(input);
        this.addMouseListener(input);

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
            if(renderables.get(i).isVisible())
            renderables.get(i).render(graphics);
        }
    }

    public KeyHandler getKeyHandler(){return keyHandler;}

    @Override
    public void actionPerformed(ActionEvent e) {
        //Needed for implementation
    }
}