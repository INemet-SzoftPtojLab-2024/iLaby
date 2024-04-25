package main.java.org.networking;

import main.java.org.game.Isten;
import main.java.org.game.Map.Map;
import main.java.org.game.Map.UnitRoom;
import main.java.org.linalg.Vec2;
import main.java.org.networking.GameServer;
import main.java.org.networking.Packet;
import main.java.org.networking.PlayerMP;
import main.java.org.networking.ServerSideHandler;

import java.util.ArrayList;
import java.util.Arrays;

public class MapHandler extends ServerSideHandler {

    @Override
    public void create(GameServer server) {
        this.server = server;
        this.isten = server.isten;
        isten.getMap().init(isten);
    }

    @Override
    public void sendDataToClient(PlayerMP client) {
        Map map = isten.getMap();
        for(int i = 0; i < map.getMapRowSize(); i++) {
            for(int j = 0; j < map.getMapColumnSize(); j++) {
                Vec2 pos = map.getUnitRooms()[i][j].getPosition();
                int type = map.getUnitRooms()[i][j].getOwnerRoom().getRoomType().ordinal();
                Packet04UnitRoom packet04UnitRoom = new Packet04UnitRoom(pos.x, pos.y, type);
                server.sendData(packet04UnitRoom.getData(), client.ipAddress, client.port);
            }
        }
    }

    @Override
    public void sendDataToAllClients(Packet packet) {


    }

    @Override
    public void update(Isten isten, double deltaTime) {

    }
}
