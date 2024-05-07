package main.java.org.networking;

import main.java.org.game.Isten;
import main.java.org.game.Map.Door;
import main.java.org.game.Map.EdgeBetweenRooms;
import main.java.org.game.Map.EdgePiece;
import main.java.org.game.updatable.Updatable;

import java.awt.event.KeyEvent;

public class ClientMap extends Updatable {

    private EdgeBetweenRooms edgeBetweenRooms;
    private Isten isten;
    public static int mapWidth;
    public static int mapHeight;

    public void addEdgePiece(EdgePiece edgePiece) {
        edgeBetweenRooms.getWalls().add(edgePiece);
    }
    @Override
    public void onStart(Isten isten) {
        this.isten = isten;
        ClientMap.mapWidth = isten.getMap().getMapColumnSize();
        ClientMap.mapHeight = isten.getMap().getMapRowSize();
        edgeBetweenRooms = new EdgeBetweenRooms();
        isten.getPhysicsEngine().addColliderGroup(edgeBetweenRooms.getColliderGroup());
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        //check if door is opened by player
        if(isten.getInputHandler().isKeyReleased(KeyEvent.VK_O)) {
            Packet25PlayerPosForDoorOpen packet = new Packet25PlayerPosForDoorOpen(isten.getPlayer().getPlayerCollider().getPosition().x,
                    isten.getPlayer().getPlayerCollider().getPosition().y);
            packet.writeData(isten.getSocketClient());
        }
    }

    @Override
    public void onDestroy() {

    }

    public EdgeBetweenRooms getEdgeBetweenRooms() {
        return edgeBetweenRooms;
    }

    public void removeEdgePiece(EdgePiece piece) {
        isten.getRenderer().deleteRenderable(piece.getImage());
        edgeBetweenRooms.getColliderGroup().removeCollider(piece.getCollider());
        edgeBetweenRooms.getWalls().remove(piece);
    }
}
