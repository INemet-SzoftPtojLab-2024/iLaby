package main.java.org.game.Graphics;

import main.java.org.manager.KeyHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GameRenderer extends JPanel implements ActionListener {

    private ArrayList<Renderable> renderables;

    public GameRenderer() {
        this.setPreferredSize(new Dimension(500, 500));
        addKeyListener(new TAdapter());
        setFocusable(true);
        setDoubleBuffered(true);
        renderables = new ArrayList<>();
    }

    public void addRenderable(Renderable r) {
        renderables.add(r);
    }

    public void paint(Graphics graphics) {

        setBackground(new Color(50,50,50));

        if(renderables.isEmpty()) return;
        for(int i = 0; i < renderables.size(); i++) {
            renderables.get(i).render(graphics);
        }

        graphics.dispose();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }


    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            KeyHandler.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {
            KeyHandler.keyPressed(e);
        }
    }
}
