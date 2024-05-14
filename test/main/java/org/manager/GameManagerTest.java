package main.java.org.manager;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.swing.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GameManagerTest {
    @Mock
    GameManager gameManager;
    @BeforeEach
    public void setUp()
    {
        gameManager=mock(GameManager.class);
    }
    @Test
    public void testChangePanel() {
        gameManager = new GameManager();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();

        gameManager.changePanel(panel1);
        assertEquals(panel1, gameManager.getCurrentPanel());

        gameManager.changePanel(panel2);
        assertEquals(panel2, gameManager.getCurrentPanel());
    }
    @Test
    public void testSetStage() {
        GameManager.setStage(GameManager.GameStage.MAIN_MENU);
        assertEquals(GameManager.GameStage.MAIN_MENU, GameManager.getStage());

        GameManager.setStage(GameManager.GameStage.SOLO);
        assertEquals(GameManager.GameStage.SOLO, GameManager.getStage());

        GameManager.setStage(GameManager.GameStage.MULTI);
        assertEquals(GameManager.GameStage.MULTI, GameManager.getStage());

        GameManager.setStage(GameManager.GameStage.EXIT);
        assertEquals(GameManager.GameStage.EXIT, GameManager.getStage());
    }
    @Test
    void testGameLoop() {

        GameManager.setStage(GameManager.GameStage.MAIN_MENU);
        gameManager.gameLoop();
        assertEquals(GameManager.getStage(), GameManager.GameStage.MAIN_MENU);

        GameManager.setStage(GameManager.GameStage.SOLO);
        gameManager.gameLoop();
        assertEquals(GameManager.getStage(), GameManager.GameStage.SOLO);

        GameManager.setStage(GameManager.GameStage.MULTI);
        gameManager.gameLoop();
        assertEquals(GameManager.getStage(), GameManager.GameStage.MULTI);

        GameManager.setStage(GameManager.GameStage.EXIT);
        gameManager.gameLoop();
        assertEquals(GameManager.getStage(), GameManager.GameStage.EXIT);

    }
}