package main.java.org.networking;

import main.java.org.entities.player.Player;
import main.java.org.game.Graphics.Text;
import main.java.org.game.Isten;
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

    public PlayerMP(String name, InetAddress ipAddress, int port, Vec2 spawnPosition) {
        super(name, spawnPosition);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        super.onUpdate(isten, deltaTime);
        sendDataToServer(isten);
    }
    public void sendDataToServer(Isten isten) {
        //movePacket
        if(getPlayerCollider() == null) return;
        if(getPlayerCollider().getVelocity().x == 0 && getPlayerCollider().getVelocity().y == 0) return;
        Packet02Move movePacket = new Packet02Move(getUsername(), getPlayerCollider().getPosition().x, getPlayerCollider().getPosition().y);
        movePacket.writeData(isten.getSocketClient());
    }

    public String getUsername() {
        return getPlayerName().getText();
    }
}
