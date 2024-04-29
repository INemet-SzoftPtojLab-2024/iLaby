package main.java.org.networking;

import main.java.org.entities.player.Player;
import main.java.org.game.Isten;

import java.util.ArrayList;

public class DeathHandler extends ServerSideHandler {

    private double currTime = 0;
    private void checkIfPlayerInRoomWithVillain(PlayerMP player) {
        boolean isInRoomWithVillain = player.checkIfPlayerInVillainRoom(isten);
        if(isInRoomWithVillain) {
            Packet21Death packet21Death = new Packet21Death(player.getUsername());
            sendDataToAllClients(packet21Death);
        }
    }

    @Override
    public void create(GameServer server) {
        this.server = server;
        this.isten = server.isten;
        isInitialized = true;
    }

    @Override
    public void sendDataToClient(PlayerMP client) {

    }

    @Override
    public void sendDataToAllClients(Packet packet) {
        packet.writeData(server);
    }

    @Override
    public void update(Isten isten, double deltaTime) {
        if(currTime < 10000) {
            currTime += deltaTime;
        }
        else currTime = 0;

        if((int)(currTime * 100)% 2 == 0) {
            for(int i = 0; i < isten.getUpdatables().size(); i++) {
                if(isten.getUpdatable(i).getClass() == PlayerMP.class) {
                    PlayerMP player = (PlayerMP)isten.getUpdatable(i);
                    checkIfPlayerInRoomWithVillain(player);
                }
            }
        }
    }
}
