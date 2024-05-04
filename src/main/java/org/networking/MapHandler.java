package main.java.org.networking;

import main.java.org.game.Isten;
import main.java.org.game.Map.*;
import main.java.org.linalg.Vec2;

import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;

public class MapHandler extends ServerSideHandler {

    int sec = 0;
    boolean stop = true;
    double delta = 0;


    @Override
    public void create(GameServer server) {
        this.server = server;
        this.isten = server.isten;
        isten.getMap().init(isten);
        isInitialized = true;
        sendDataToWaitingClients();
    }

    @Override
    public void sendDataToClient(PlayerMP client) {

        if(!isInitialized) {
            if(!waitingClients.contains(client)) waitingClients.add(client);
            return;
        }

        Map map = isten.getMap();
        for(int i = 0; i < map.getMapRowSize(); i++) {
            for(int j = 0; j < map.getMapColumnSize(); j++) {
                Vec2 pos = map.getUnitRooms()[i][j].getPosition();
                int type = map.getUnitRooms()[i][j].getOwnerRoom().getRoomType().ordinal();
                Packet04UnitRoom packet = new Packet04UnitRoom(pos.x, pos.y, type);
                server.sendData(packet.getData(), client.ipAddress, client.port);
            }
        }


        //
        for(int i = 0; i < map.getEdgeManager().getRoomEdges().size(); i++) {
            EdgeBetweenRooms re = map.getEdgeManager().getRoomEdges().get(i);
            for (int j = 0; j < re.getWalls().size(); j++) {
                EdgePiece edgePiece = re.getWalls().get(j);
                Vec2 pos = edgePiece.getImage().getPosition();
                Vec2 scale = edgePiece.getImage().getScale();
                Packet20Wall packet = new Packet20Wall(pos.x, pos.y, scale.x, scale.y, edgePiece.isDoor());
                server.sendData(packet.getData(), client.ipAddress, client.port);
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }



    }

    @Override
    public void sendDataToAllClients(Packet packet) {
        packet.writeData(server);
    }

    @Override
    public void update(Isten isten, double deltaTime) {
        //for testing
        if(isten.getInputHandler().isKeyReleased(KeyEvent.VK_SPACE)){
            stop = !stop;
            //if(stop) printMap();
        }

        if(!stop) {
            delta += deltaTime;
            if (delta > 1) {
                //TESTCASE 1:::

                if(sec %3==0){
                    Vec2 pos = isten.getMap().addDoorToEdgeWithoutDoor(isten);
                    handleAddOrDeleteDoor(pos, true);
                    //System.out.println("ajtoaddolas tortent");
                }
                else{
                    Vec2 pos = isten.getMap().TakeOutDoor(isten,true);
                    if(pos.x != -1 && pos.y != -1) {
                        handleAddOrDeleteDoor(pos, false);
                        //stop = true;
                        //System.out.println("edgeNum: "+isten.getMap().getEdgeManager().getRoomEdges().size());
                        //System.out.println("doorNum: "+isten.getMap().getEdgeManager().getDoorNum());
                        //System.out.println("ajtokivetel tortent");
                    }


                }
                //TESTCASE 2:
                if (sec % 4 == 0) {
                    Collections.shuffle(isten.getMap().getRooms());
                    Room r1 = isten.getMap().getRooms().get(0);
                    Room r2 = isten.getMap().getRooms().get(0).getPhysicallyAdjacentRooms().get(0);
                    handleUnitRoomChange(r2.getUnitRooms(), r1.getRoomType().ordinal());
                    handleWallDeletion(isten.getMap().getEdgeManager().getEdgeBetweenRooms(r1, r2));
                    isten.getMap().mergeRooms(r1, r2, isten);
                    handleRoomEdges(r1);

                    //System.out.println("r1 adjacentrooms Number: " + rooms.get(0).getPhysicallyAdjacentRooms().size());

                }
                //TESTCASE 3:

                if((sec+2)%4==0) {
                    for (Room splittable : isten.getMap().getRooms()) {
                        int newID;
                        if ((newID = isten.getMap().splitRooms(splittable, isten)) != -1) {

                            for(Room room: isten.getMap().getRooms()) {
                                if(room.getID() == newID) {
                                    handleUnitRoomChange(room.getUnitRooms(), room.getRoomType().ordinal());
                                    handleWallAddition(isten.getMap().getEdgeManager().getEdgeBetweenRooms(splittable, room));
                                    handleRoomEdges(room);
                                    handleRoomEdges(splittable);
                                    break;
                                }
                            }
                            //System.out.println("sikerult a split");
                            //System.out.println(splittable.getID() + " adjacentrooms: " + splittable.getPhysicallyAdjacentRooms().size());
                            //System.out.println(splittable.getID() + " Dooradjacentrooms: " + splittable.getDoorAdjacentRooms().size());
                            //stop = true;
                            break;
                        }
                    }
                }


                sec++;
                delta = 0;
            }
        }
    }


    private void handleAddOrDeleteDoor(Vec2 pos, boolean isDoor) {
        Packet22EdgePieceChanged packet = new Packet22EdgePieceChanged(pos.x,
                pos.y,
                isDoor);
        sendDataToAllClients(packet);
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleRoomEdges(Room room) {
        for(EdgeBetweenRooms edgeBetweenRooms: isten.getMap().getEdgeManager().getAllEdgeForARoom(room)) {
            for(EdgePiece edgePiece: edgeBetweenRooms.getWalls()) {
                Packet22EdgePieceChanged packet = new Packet22EdgePieceChanged(edgePiece.getPosition().x,
                        edgePiece.getPosition().y,
                        edgePiece.isDoor());
                sendDataToAllClients(packet);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void handleWallAddition(EdgeBetweenRooms edge) {
        for(EdgePiece edgePiece: edge.getWalls()) {
            Packet20Wall packet = new Packet20Wall(edgePiece.getPosition().x, edgePiece.getPosition().y,
                    edgePiece.getCollider().getScale().x, edgePiece.getCollider().getScale().y,
                    edgePiece.isDoor());
            sendDataToAllClients(packet);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void handleWallDeletion(EdgeBetweenRooms edge) {
        for(EdgePiece edgePiece: edge.getWalls()) {
            Packet23WallDelete packet = new Packet23WallDelete(edgePiece.getPosition().x,
                    edgePiece.getPosition().y);
            sendDataToAllClients(packet);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void handleUnitRoomChange(List<UnitRoom> unitRooms, int type) {

        for(UnitRoom unitRoom: unitRooms) {
            Packet04UnitRoom packet = new Packet04UnitRoom(unitRoom.getPosition().x,
                    unitRoom.getPosition().y, type);
            sendDataToAllClients(packet);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
