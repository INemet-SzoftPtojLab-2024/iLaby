package main.java.org.networking;

import main.java.org.game.Isten;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.RoomType;
import main.java.org.game.Map.UnitRoom;

public class PlayerStatusHandler extends ServerSideHandler {

    private double currTime = 0;
    private void checkIfPlayerInRoomWithVillain(PlayerMP player,double deltaTime) {
        if(player.isAlive()){
            boolean isInRoomWithVillain = player.checkIfPlayerInVillainRoom(isten,deltaTime);
            if(isInRoomWithVillain) {
                Packet21Death packet21Death = new Packet21Death(player.getUsername());
                sendDataToAllClients(packet21Death);
            }
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

    @Override
    public void update(Isten isten, double deltaTime) {
        if(currTime < 10000) {
            currTime += deltaTime;
        }
        else currTime = 0;

        if((int)(currTime * 100) % 100 == 0) {
            for(int i = 0; i < isten.getUpdatables().size(); i++) {
                if(isten.getUpdatable(i).getClass() == PlayerMP.class) {
                    PlayerMP player = (PlayerMP)isten.getUpdatable(i);

                    checkIfPlayerInRoomWithVillain(player,deltaTime);
                    checkIfPlayerInGasRoom(player);
                }
            }
        }
    }
}
