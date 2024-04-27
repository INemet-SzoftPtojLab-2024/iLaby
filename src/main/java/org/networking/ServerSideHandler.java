package main.java.org.networking;

import main.java.org.game.Isten;

import java.util.ArrayList;

public abstract class ServerSideHandler {

    boolean isInitialized = false;
    protected Isten isten;
    protected GameServer server;
    protected ArrayList<PlayerMP> waitingClients = new ArrayList<>();
    public abstract void create(GameServer server);

    public abstract void sendDataToClient(PlayerMP client);
    public abstract void sendDataToAllClients(Packet packet);
    public abstract void update(Isten isten, double deltaTime);
    protected void sendDataToWaitingClients() {
        for(int i = 0; i < waitingClients.size(); i++) {
            PlayerMP client = waitingClients.get(i);
            sendDataToClient(client);
            waitingClients.remove(client);
        }
    }
    public void setInitialized(boolean b) {
        isInitialized = b;
    }
}
