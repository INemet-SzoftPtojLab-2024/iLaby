package main.java.org.game.Map;

import lombok.Getter;
import lombok.Setter;
import main.java.org.game.Isten;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;
import java.util.Random;

@Getter
@Setter
/**
 * This class is responsible for all the Walls(edges), between the rooms. It contains the doors too.
 */
//otlet: minden roomba a hozza tartozo edget eltarolni(lehet folosleges), vagy ez mar atvenne az edge manager szerepet?!
public class EdgeManager {
    private ArrayList<EdgeBetweenRooms> roomEdges;
    public EdgeManager(){
        roomEdges = new ArrayList<>();
    }


    public EdgeBetweenRooms getEdgeBetweenRooms(Room r1, Room r2) {
        for(EdgeBetweenRooms edgeBetweenRooms: roomEdges){
            if(edgeBetweenRooms.getNodeRooms().contains(r1) && edgeBetweenRooms.getNodeRooms().contains(r2))
            {
                return edgeBetweenRooms;
            }
        }
        return null;
    }


    public void deleteEdge(Room r1, Room r2, Isten isten){
        EdgeBetweenRooms edgeToDelete = getEdgeBetweenRooms(r1, r2);
        for(int i = 0; i < roomEdges.size(); i++){
            if(edgeToDelete.equals(roomEdges.get(i))){
                //remove all the wallpiece from the edge.
                for(Wall wallsToDelete : edgeToDelete.getWalls()){
                    wallsToDelete.removeWall(isten, edgeToDelete.getColliderGroup());
                }
                //edgeToDelete.getNodeRooms().clear(); //it's not required
                //edgeToDelete.getWalls().clear(); //it's not required

                // removeing the collidergroup from the physics engine
                //edgeToDelete.getColliderGroup().getColliders().clear(); //it's not required
                isten.getPhysicsEngine().removeColliderGroup(edgeToDelete.getColliderGroup().id);
                roomEdges.remove(i);
                return;
            }
        }
    }

    //sets new neighbor to r1
    //ahol r2 van oda r1-et állítunk (merge miatt)
    public void updateEdgesAfterMerge(Room remaining, Room removed){ //mergenel hasznaljuk csak
        //r1 szoba marad
        //r2 torlodik (ahol szerepl, ki kell cserelni r1-re)
        for(EdgeBetweenRooms edgeBetweenRoom : roomEdges){
            if(edgeBetweenRoom.getNodeRooms().contains(removed)){
                edgeBetweenRoom.getNodeRooms().remove(removed);
                edgeBetweenRoom.getNodeRooms().add(remaining);
            }
        }
    }
    public void updateEdgesAfterSplit(Room oldRoom, Room newRoom, Isten isten){
        //and colliders(walls and edges)
        EdgeBetweenRooms newEdge = new EdgeBetweenRooms(oldRoom,newRoom);
        addEdge(newEdge, isten);
        //az uj szobabol meg nem minden szobaba lesz ajto
        addDoor(oldRoom, newRoom, isten);

        int oldRoomID = oldRoom.getID();
        int newRoomID = newRoom.getID();
        //vegigmegyunk az osszes olyan edgen ami tartelmazza az regi szobát,
        // mivel meg semmi sincs atallitva ezert az split elotti edgeklesznek ervenyben
        ArrayList<EdgeBetweenRooms> edgesToAdd = new ArrayList<>();
        for(EdgeBetweenRooms checkEdge : roomEdges){
            boolean wasInOldRoom = false;
            boolean wasInNewRoom = false;
            if(checkEdge.getNodeRooms().contains(oldRoom)) {
                //ha az edge a ket vizsgalt szoba kozott van, akkor nem kell tovabb vizsgalodni, mert ezt mar beallitottuk az addolasnal
                if (!getEdgeBetweenRooms(newRoom, oldRoom).equals(checkEdge)) {
                    //az edge falainak a unitroomjaitnak az ownerroomjait nezzuk
                    for (Wall checkEdgeWall : checkEdge.getWalls()) {
                        if (checkEdgeWall.getUnitRoomsBetween().get(0).getOwnerRoom().getID() == oldRoomID
                                || checkEdgeWall.getUnitRoomsBetween().get(1).getOwnerRoom().getID() == oldRoomID)
                            wasInOldRoom = true;
                        if (checkEdgeWall.getUnitRoomsBetween().get(0).getOwnerRoom().getID() == newRoomID
                                || checkEdgeWall.getUnitRoomsBetween().get(1).getOwnerRoom().getID() == newRoomID)
                            wasInNewRoom = true;

                    }
                    //melyik szobaban jartunk
                    if (wasInOldRoom && wasInNewRoom) { // ha mindkettoben jartunk akkor --> split the edge
                        //delete from the edge the walls, that are between newroom and the other room
                        ArrayList<Wall> wallsToRemoveFromCheckEdge = new ArrayList<>();
                        boolean doorRemoved = false;
                        for (Wall checkEdgeWall : checkEdge.getWalls()) {
                            //the wall is between the newRoom and the other one --> delete wall from checkEdge
                            //a maradek lesz az oldroom edge-e
                            if (checkEdgeWall.getUnitRoomsBetween().get(0).getOwnerRoom().getID() == newRoomID
                                    || checkEdgeWall.getUnitRoomsBetween().get(1).getOwnerRoom().getID() == newRoomID) {

                                if (checkEdgeWall.isDoor())//ha ajtot torlunk
                                    doorRemoved = true;
                                wallsToRemoveFromCheckEdge.add(checkEdgeWall);
                                checkEdgeWall.removeWall(isten, checkEdge.getColliderGroup());
                            }
                        }
                        checkEdge.getWalls().removeAll(wallsToRemoveFromCheckEdge);
                        wallsToRemoveFromCheckEdge.clear();
                        //new edge to the newRoom
                        //a szoba ami az oldroommal noderoom volt, kell a newRoom noderoomjaihoz
                        Room oldRoomNodeRoom;
                        if (checkEdge.getNodeRooms().get(0).getID() != oldRoomID) {
                            oldRoomNodeRoom = checkEdge.getNodeRooms().get(0);
                        } else{
                            oldRoomNodeRoom = checkEdge.getNodeRooms().get(1);
                        }
                        EdgeBetweenRooms edgeToAdd = new EdgeBetweenRooms(newRoom, oldRoomNodeRoom);
                        edgesToAdd.add(edgeToAdd);
                        //uj ajto ha deleteltuk az oldroombol
                        if (doorRemoved) addDoor(oldRoom, oldRoomNodeRoom, isten);

                    } else if (!wasInOldRoom && wasInNewRoom) { //na csak az ujban --> csak a noderoom valtozik regirol az ujra
                        checkEdge.getNodeRooms().remove(oldRoom);
                        checkEdge.getNodeRooms().add(newRoom);
                    }
                    //ha csak a regiben akkor marad, minden
                    //egyikben sem, ez nem lehetseges
                }

            }

        }
        for(EdgeBetweenRooms edge : edgesToAdd){
            addEdge(edge, isten);
            addDoor(edge.getNodeRooms().get(0), edge.getNodeRooms().get(1), isten);
        }

    }
    public void addEdge(EdgeBetweenRooms newEdge, Isten isten){
        Vec2 horizontalScale = new Vec2(1f, 0.1f); //vizszintes
        Vec2 verticalScale = new Vec2(0.1f, 1f); //fuggoleges
        Room r1 = newEdge.getNodeRooms().get(0);
        Room r2 = newEdge.getNodeRooms().get(1);
        roomEdges.add(newEdge);
        //the same as in the mapgenerator
        for(UnitRoom wallFinderUnitRoom: r1.getUnitRooms()){
            if(wallFinderUnitRoom.getTopNeighbor() != null && wallFinderUnitRoom.getTopNeighbor().getOwnerRoom().equals(r2)){
                Vec2 wallTopPos = new Vec2(wallFinderUnitRoom.getColNum(), wallFinderUnitRoom.getRowNum() + 0.5f);
                newEdge.addNewWall(wallTopPos, horizontalScale, wallFinderUnitRoom ,wallFinderUnitRoom.getTopNeighbor(), isten);
            }
            if(wallFinderUnitRoom.getBottomNeighbor() != null && wallFinderUnitRoom.getBottomNeighbor().getOwnerRoom().equals(r2)){
                Vec2 wallBottomPos = new Vec2(wallFinderUnitRoom.getColNum(), wallFinderUnitRoom.getRowNum() - 0.5f);
                newEdge.addNewWall(wallBottomPos, horizontalScale, wallFinderUnitRoom, wallFinderUnitRoom.getBottomNeighbor(),isten);
            }
            if(wallFinderUnitRoom.getLeftNeighbor() != null && wallFinderUnitRoom.getLeftNeighbor().getOwnerRoom().equals(r2)){
                Vec2 wallLeftPos = new Vec2(wallFinderUnitRoom.getColNum() - 0.5f, wallFinderUnitRoom.getRowNum());
                newEdge.addNewWall(wallLeftPos, verticalScale, wallFinderUnitRoom, wallFinderUnitRoom.getLeftNeighbor() ,isten);
            }
            if(wallFinderUnitRoom.getRightNeighbor() != null && wallFinderUnitRoom.getRightNeighbor().getOwnerRoom().equals(r2)){
                Vec2 wallRightPos = new Vec2(wallFinderUnitRoom.getColNum() + 0.5f, wallFinderUnitRoom.getRowNum());
                newEdge.addNewWall(wallRightPos, verticalScale, wallFinderUnitRoom, wallFinderUnitRoom.getRightNeighbor(),isten);
            }
        }
        isten.getPhysicsEngine().addColliderGroup(newEdge.getColliderGroup());
    }
    //this function add one door to an edge
    //meg nics kezelve arra az esetre ha tobb ajtot akarunk
    public void addDoor(Room r1, Room r2, Isten isten) {
        EdgeBetweenRooms edge = getEdgeBetweenRooms(r1, r2);
        if(edge == null){
            System.out.println("door is not added");
            return;
        }
        Random rand = new Random();
        int randomIndex = rand.nextInt(edge.getWalls().size());
        edge.switchWallToDoor(edge.getWalls().get(randomIndex), isten);
    }
    public void initDoors(Isten isten){
        //TODO
        //optimalizaljuk hogy ne legyen egy unitroomhoz tobb ajto
        Random rand = new Random();
        for(EdgeBetweenRooms edge : roomEdges){
            int randomIndex = rand.nextInt(edge.getWalls().size());
            //informaciowasztes?!
            edge.switchWallToDoor(edge.getWalls().get(randomIndex), isten);
        }
    }
    public ArrayList<EdgeBetweenRooms> getRoomEdges() {
        return roomEdges;
    }



}
