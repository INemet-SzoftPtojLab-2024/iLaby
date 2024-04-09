package main.java.org.networking;

import main.java.org.entities.player.Player;
import main.java.org.game.Graphics.Text;
import main.java.org.linalg.Vec2;

import java.net.InetAddress;

public class PlayerMP extends Player {
    public InetAddress ipAddress;
    public int port;

    public PlayerMP(String name, InetAddress ipAddress, int port) {
        super(name);
        this.ipAddress = ipAddress;
        this.port = port;

    }

    public String getUsername() {
        return getPlayerName().getText();
    }
}
