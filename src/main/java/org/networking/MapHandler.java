package main.java.org.networking;

import main.java.org.game.Isten;
import main.java.org.game.Map.EdgeBetweenRooms;
import main.java.org.game.Map.EdgePiece;
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
                Packet04UnitRoom packet = new Packet04UnitRoom(pos.x, pos.y, type);
                server.sendData(packet.getData(), client.ipAddress, client.port);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        /*
        for(int i = 0; i < map.getEdgeManager().getRoomEdges().size(); i++) {
            EdgeBetweenRooms re = map.getEdgeManager().getRoomEdges().get(i);
            for(int j = 0; j < re.getWalls().size(); j++) {
                EdgePiece edgePiece = re.getWalls().get(j);
                Vec2 pos = edgePiece.getImage().getPosition();
                Vec2 scale = edgePiece.getImage().getScale();
                String path;
                if(edgePiece.isDoor()) {
                    path = "/assets/doors/doors_leaf_closed.png";
                }
                else {
                    path = "/assets/walls/wall_mid.png";
                }
                Packet0010Wall packet = new Packet0010Wall(pos.x, pos.y, scale.x, scale.y, path);
                server.sendData(packet.getData(), client.ipAddress, client.port);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

         */



    }

    @Override
    public void sendDataToAllClients(Packet packet) {


    }

    @Override
    public void update(Isten isten, double deltaTime) {

    }
}
