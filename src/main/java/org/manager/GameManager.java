package main.java.org.manager;

import main.java.org.game.Audio.AudioManager;
import main.java.org.game.Graphics.GameRenderer;
import main.java.org.game.Isten;
import main.java.org.game.Isten2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class GameManager {

    private JFrame frame;
    private JPanel currentPanel;

    private static GameStage stage = GameStage.MAIN_MENU;

    public GameManager() {
        //Create frame
        frame = new JFrame("iLaby");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("assets/ui/logo.png"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(new Dimension(800, 800));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setBackground(new Color(50, 50, 50));
    }

    public void changePanel(JPanel panel) {
        boolean isPanelAdded = false;
        for (int i = 0; i < frame.getComponentCount(); i++) {
            if (frame.getComponent(i) == panel) {
                isPanelAdded = true;
                break;
            }
        }

        if (!isPanelAdded)
            frame.add(panel);

        frame.setContentPane(panel);
        panel.requestFocusInWindow();
        frame.revalidate();
        frame.pack();
        currentPanel = panel;
        //currentPanel.setFocusable(false);
        //currentPanel.setFocusable(true);
    }

    public void gameLoop() {
        while (true) {
            switch (stage) {
                case MAIN_MENU:
                    Isten2 isten2 = new Isten2();
                    changePanel(isten2.getRenderer());
                    isten2.init();

                    long lastFrame2 = System.nanoTime();
                    while (stage == GameStage.MAIN_MENU) {
                        long currentTime = System.nanoTime();
                        double deltaTime = (currentTime - lastFrame2) * 0.000000001;
                        lastFrame2 = currentTime;

                        isten2.update(deltaTime);
                        try {
                            Thread.sleep(1);
                        } catch (Exception amogus) { //do not remove plz
                        }
                    }
                    break;
                case INGAME:
                    Isten isten = new Isten();
                    changePanel(isten.getRenderer());//ez az isten.init elott fusson
                    isten.init();

                    long lastFrame = System.nanoTime();
                    while (stage == GameStage.INGAME) {
                        long currentTime = System.nanoTime();
                        double deltaTime = (currentTime - lastFrame) * 0.000000001;
                        lastFrame = currentTime;

                        isten.update(deltaTime);
                        try {
                            Thread.sleep(1);
                        } catch (Exception amogus) { //do not remove plz
                        }
                    }
                    break;
                case EXIT:
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    break;
            }
        }
    }

    public static void setStage(GameStage newStage) {
        stage = newStage;
    }

    public enum GameStage {
        MAIN_MENU, INGAME, EXIT
    }
}
