package main.java.org.networking;

import main.java.org.game.Isten;
import main.java.org.game.UI.TimeCounter;

public class TimeHandler extends ServerSideHandler {

    @Override
    public void create(GameServer server) {
        this.server = server;
        this.isten = server.isten;
    }

    @Override
    public void sendDataToClient(PlayerMP client) {
        Packet07Timer packet = new Packet07Timer(TimeCounter.getTimeRemaining());
        server.sendData(packet.getData(), client.ipAddress, client.port);
    }

    @Override
    public void sendDataToAllClients(Packet packet) {
        //send to every client
        packet.writeData(server);
    }

    @Override
    public void update(Isten isten, double deltaTime) {
        TimeCounter.tick(deltaTime, isten.getPlayer());
        Packet07Timer packet = new Packet07Timer(TimeCounter.getTimeRemaining());
        sendDataToAllClients(packet);
    }
}
