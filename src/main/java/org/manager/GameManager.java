package main.java.org.manager;

import main.java.org.entities.player.Player;
import main.java.org.game.Audio.AudioManager;
import main.java.org.game.Isten;
import main.java.org.game.Isten2;
import main.java.org.game.PlayerPrefs.PlayerPrefs;
import main.java.org.game.UI.MainMenu;
import main.java.org.networking.SharedObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class GameManager {

    private boolean multi;
    private JFrame frame;
    private JPanel currentPanel;
    private Isten isten;
    private static GameStage stage = GameStage.MAIN_MENU;

    public GameManager() {
        //Create frame
        frame = new JFrame("eLaby");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("assets/ui/logo.png"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(new Dimension(800, 800));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setBackground(new Color(50, 50, 50));
    }
    public JPanel getCurrentPanel()
    {
        return currentPanel;
    }
    public static GameStage getStage()
    {
        return stage;
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

        if (currentPanel != null) {
            panel.setPreferredSize(currentPanel.getSize());
        }

        panel.requestFocusInWindow();
        frame.revalidate();
        frame.pack();
        currentPanel = panel;
        //currentPanel.setFocusable(false);
        //currentPanel.setFocusable(true);
    }

    public void gameLoop() {
        //load saves
        PlayerPrefs.load();

        //game stuff
        while (true) {
            switch (stage) {
                case MAIN_MENU:
                    isten = new Isten2();
                    changePanel(isten.getRenderer());
                    isten.init();

                    long lastFrame2 = System.nanoTime();
                    while (stage == GameStage.MAIN_MENU) {
                        long currentTime = System.nanoTime();
                        double deltaTime = (currentTime - lastFrame2) * 0.000000001;
                        lastFrame2 = currentTime;

                        isten.update(deltaTime);
                        try {
                            Thread.sleep(1);
                        } catch (Exception amogus) { //do not remove plz
                        }
                    }
                    AudioManager.closeAllSounds();
                    AudioManager.unloadPreloadedSounds();

                    PlayerPrefs.save();
                    break;
                case SOLO:
                    isten = new Isten();
                    changePanel(isten.getRenderer());//ez az isten.init elott fusson
                    isten.init();

                    //Wait for the server to initialize (only on server side)
                    if(isten.getSocketServer() != null) {
                        SharedObject serverInitLock = isten.getSocketServer().getInitializationLock();
                        try {
                            serverInitLock.waitForNotification();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    long lastFrame = System.nanoTime();
                    while (stage == GameStage.SOLO) {
                        long currentTime = System.nanoTime();
                        double deltaTime = (currentTime - lastFrame) * 0.000000001;
                        lastFrame = currentTime;

                        isten.update(deltaTime);
                        try {
                            Thread.sleep(1);
                        } catch (Exception amogus) { //do not remove plz
                        }
                    }
                    AudioManager.closeAllSounds();
                    AudioManager.unloadPreloadedSounds();

                    PlayerPrefs.save();
                    break;
                case MULTI:
                    isten = new Isten();
                    changePanel(isten.getRenderer());//ez az isten.init elott fusson
                    isten.initMP();

                    //Wait for the server to initialize (only on server side)
                    if(isten.getSocketServer() != null) {
                        SharedObject serverInitLock = isten.getSocketServer().getInitializationLock();
                        try {
                            serverInitLock.waitForNotification();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }


                    long lastFrame3 = System.nanoTime();
                    while (stage == GameStage.MULTI) {
                        long currentTime = System.nanoTime();
                        double deltaTime = (currentTime - lastFrame3) * 0.000000001;
                        lastFrame3 = currentTime;

                        isten.update(deltaTime);
                        try {
                            Thread.sleep(1);
                        } catch (Exception amogus) { //do not remove plz
                        }
                    }
                    AudioManager.closeAllSounds();
                    AudioManager.unloadPreloadedSounds();

                    PlayerPrefs.save();
                    break;
                case EXIT:
                    PlayerPrefs.save();
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    break;
            }
        }
    }

    public static void setStage(GameStage newStage) {
        stage = newStage;
    }

    public enum GameStage {
        MAIN_MENU, SOLO, MULTI, EXIT
    }
}
