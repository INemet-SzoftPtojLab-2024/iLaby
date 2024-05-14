package main.java.org.networking;

import main.java.org.entities.player.Player;
import main.java.org.game.Isten;
import main.java.org.linalg.Vec2;

import java.net.InetAddress;

public class PlayerMP extends Player {
    public InetAddress ipAddress;
    public int port;
    public PlayerMP(Isten isten, String name, InetAddress ipAddress, int port) {
        super(isten, name);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public PlayerMP(Isten isten, String name, InetAddress ipAddress, int port, Vec2 spawnPosition) {
        super(isten, name, spawnPosition);
        this.ipAddress = ipAddress;
        this.port = port;
        this.spawnPosition = spawnPosition;
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        super.onUpdate(isten, deltaTime);
        sendDataToServer(isten, deltaTime);
    }
    public void sendDataToServer(Isten isten, double deltaTime) {
        //movePacket
        sendMoveData(isten);
        //fog sync
        sendFogData(isten);
    }

    @Override
    protected void sendAnimationData(Isten isten) {
        Packet03Animation animationPacket = new Packet03Animation(getPlayerName().getText(), getActiveImage());
        animationPacket.writeData(isten.getSocketClient());
    }
    void sendMoveData(Isten isten) {
        if(getPlayerCollider() == null) return;
        if(getPlayerCollider().getVelocity().x == 0 && getPlayerCollider().getVelocity().y == 0) return;
        Packet02Move movePacket = new Packet02Move(getUsername(), getPlayerCollider().getPosition().x, getPlayerCollider().getPosition().y);
        movePacket.writeData(isten.getSocketClient());
    }

    private void sendFogData(Isten isten)
    {
        if(fogSyncOutgoing==null)
            return;

        int[] data=null;

        synchronized (fogSyncOutgoing)
        {
            if(fogSyncOutgoing.size()==0)
                return;

            data=new int[fogSyncOutgoing.size()];
            for(int i=0;i<data.length;i++)
                data[i]=(int)fogSyncOutgoing.get(i);

            fogSyncOutgoing.clear();
        }

        Packet50Fog packet=new Packet50Fog(data);
        packet.writeData(isten.getSocketClient());
    }

    public String getUsername() {
        return getPlayerName().getText();
    }
}
