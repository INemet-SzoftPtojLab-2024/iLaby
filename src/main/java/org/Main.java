package main.java.org;

import main.java.org.linalg.Vec2;
import main.java.org.manager.GameManager;
import main.java.org.game.Graphics.*;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        GameManager gameManager = new GameManager();

        gameManager.gameLoop();

    }
}