package main.java.org.manager;

import main.java.org.game.Graphics.GameRenderer;
import main.java.org.game.Isten;

import javax.swing.*;
import java.awt.*;

public class GameManager {

    private JFrame frame;
    private JPanel currentPanel;

    private GameStage stage=GameStage.INGAME;

    public GameManager() {
        //Create frame
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setBackground(new Color(50,50,50));

    }

    public void changePanel(JPanel panel) {
        boolean isPanelAdded=false;
        for(int i=0;i<frame.getComponentCount();i++)
        {
            if(frame.getComponent(i)==panel)
            {
                isPanelAdded=true;
                break;
            }
        }

        if(!isPanelAdded)
            frame.add(panel);

        frame.setContentPane(panel);
        panel.requestFocusInWindow();
        frame.revalidate();
        frame.pack();
        currentPanel = panel;
        //currentPanel.setFocusable(false);
        //currentPanel.setFocusable(true);
    }

    public void gameLoop()
    {
        while(true)
        {
            switch (stage)
            {
                case INGAME:
                    Isten isten=new Isten();
                    changePanel(isten.getRenderer());//ez az isten.init elott fusson
                    isten.init();

                    long lastFrame=System.nanoTime();
                    while(stage==GameStage.INGAME)
                    {
                        long currentTime=System.nanoTime();
                        float deltaTime=(float)((currentTime-lastFrame)*0.000000001);
                        lastFrame=currentTime;

                        isten.update(deltaTime);
                    }
                    break;
            }
        }
    }


    public enum GameStage{
        MAIN_MENU, INGAME
    }
}
