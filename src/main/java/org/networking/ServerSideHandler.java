package main.java.org.networking;

import main.java.org.game.Isten;

public abstract class ServerSideHandler {

    boolean isInitialized = false;
    Isten isten;
    GameServer server;
    public abstract void create(GameServer server);

    public abstract void sendDataToClient(PlayerMP client);
    public abstract void sendDataToAllClients(Packet packet);
    public abstract void update(Isten isten, double deltaTime);

    public void setInitialized(boolean b) {
        isInitialized = b;
    }
}
