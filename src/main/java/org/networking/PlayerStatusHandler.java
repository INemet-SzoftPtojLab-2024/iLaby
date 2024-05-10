package main.java.org.networking;

import main.java.org.game.Isten;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.RoomType;
import main.java.org.game.Map.UnitRoom;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatusHandler extends ServerSideHandler {

    List<PlayerMP> clients = new ArrayList<>();
    private double currTime = 0;
    private void checkIfPlayerInRoomWithVillain(PlayerMP player,double deltaTime) {
            boolean isInRoomWithVillain = player.checkIfPlayerInVillainRoom(isten,deltaTime);
            player.setPlayerInVillainRoom(isInRoomWithVillain);

        Packet41IsPlayerInVillainRoom packet41IsPlayerInVillainRoom = new Packet41IsPlayerInVillainRoom(player.getUsername(), isInRoomWithVillain);
        sendDataToAllClients(packet41IsPlayerInVillainRoom);

            if(!player.isAlive()) {
                Packet21Death packet21Death = new Packet21Death(player.getUsername());
                sendDataToAllClients(packet21Death);
            }
    }

    private void checkIfPlayerInGasRoom(PlayerMP player){
        if(player.getCurrentRoom() != null && player.getCurrentRoom().getRoomType() == RoomType.GAS) {
            Packet26InGasRoom packet = new Packet26InGasRoom(player.getUsername(), true);
            sendDataToAllClients(packet);
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

    private PlayerMP findClient(String username) {
        for(PlayerMP client: clients) {
            if(client.getUsername().equalsIgnoreCase(username)) {
                return client;
            }
        }
        return null;
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

                    Room currentRoom = player.getPlayerRoom(isten, player.getPlayerCollider().getPosition());
                    if(player.getCurrentRoom() != currentRoom) {
                        if(player.getCurrentRoom() != null){
                            Packet28PlayerChangedRoom packet = new Packet28PlayerChangedRoom(player.getUsername());
                            PlayerMP client = findClient(player.getUsername());
                            if(client.port != -1) {
                                server.sendData(packet.getData(), client.ipAddress, client.port);
                            }
                            player.getCurrentRoom().decreasePlayerCount();
                            currentRoom.increasePlayerCount();


                        }
                    }
                    player.setCurrentRoom(currentRoom);

                    checkIfPlayerInRoomWithVillain(player,deltaTime);
                    checkIfPlayerInGasRoom(player);
                }
            }
        }
    }
    public void addClient(PlayerMP client) {
        clients.add(client);
    }
}
