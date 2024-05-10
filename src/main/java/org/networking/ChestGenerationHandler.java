package main.java.org.networking;

import main.java.org.game.Isten;
import main.java.org.items.Chest;

import java.util.Vector;

public class ChestGenerationHandler extends ServerSideHandler {
    Vector<Chest> chests;
    @Override
    public void create(GameServer server) {
        this.server = server;
        this.isten = server.isten;
        isInitialized = true;

        isten.getChestManager().init(isten);

        chests = isten.getChestManager().getChests();
    }

    @Override
    public void sendDataToClient(PlayerMP client) {
        for (int i = 0; i<chests.size();i++)
        {
            Packet10ChestGeneration packet = new Packet10ChestGeneration(
                    chests.get(i).getWallLocation().ordinal(),
                    chests.get(i).getUnitRoomPosition(),
                    chests.get(i).getChestType(), i);
            server.sendData(packet.getData(), client.ipAddress, client.port);
        }
    }

    @Override
    public void sendDataToAllClients(Packet packet) {

    }

    @Override
    public void update(Isten isten, double deltaTime) {

    }
}
