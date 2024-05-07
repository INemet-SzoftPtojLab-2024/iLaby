package main.java.org.game.Map;

import lombok.Getter;
import lombok.Setter;
import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;

@Getter
@Setter
/**
 * This class is responsible for all the Walls(edges), between the rooms. It contains the doors too.
 */
//otlet: minden roomba a hozza tartozo edget eltarolni(lehet folosleges), vagy ez mar atvenne az edge manager szerepet?!
public class EdgeManager extends Updatable {
    private Isten isten;
    private ArrayList<EdgeBetweenRooms> roomEdges;
    public EdgeManager(Isten isten){
        this.isten = isten;
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
    //egy adott szoba összes edgének meghatározása
    //ha egy szobanak a szomszedait akarom meghatarozni akkor hasznos
    public ArrayList<EdgeBetweenRooms> getAllEdgeForARoom(Room r1){
        ArrayList<EdgeBetweenRooms> ret = new ArrayList<>();
        //vegigiteral aaz osszes edgen
        for (EdgeBetweenRooms edgeBetweenRooms : roomEdges){
            //ha tartalmazza hozzaad
            if(edgeBetweenRooms.getNodeRooms().contains(r1))
            {
                ret.add(edgeBetweenRooms);
            }

        }
        return ret;
    }


    public void deleteEdge(Room r1, Room r2){
        EdgeBetweenRooms edgeToDelete = getEdgeBetweenRooms(r1, r2);
        for(int i = 0; i < roomEdges.size(); i++){
            if(edgeToDelete.equals(roomEdges.get(i))){
                //remove all the wallpiece from the edge.
                for(EdgePiece wallsToDelete : edgeToDelete.getWalls()){
                    wallsToDelete.removeEdgePiece(isten, edgeToDelete.getColliderGroup());
                }
                //edgeToDelete.getNodeRooms().clear(); //it's not required
                //edgeToDelete.getWalls().clear(); //it's not required

                // removeing the collidergroup from the physics engine
                //edgeToDelete.getColliderGroup().getColliders().clear(); //it's not required
                //isten.getPhysicsEngine().removeColliderGroup(edgeToDelete.getColliderGroup().id);
                roomEdges.remove(i);
                return;
            }
        }
    }

    //sets new neighbor to r1
    //ahol r2 van oda r1-et állítunk (merge miatt)
    public void updateEdgesAfterMerge(Room remaining, Room deleted){ //mergenel hasznaljuk csak
        //r1 szoba marad
        //r2 torlodik (ahol szerepl, ki kell cserelni r1-re)
        ArrayList<EdgeBetweenRooms> edgeToRemove = new ArrayList<>();
        for(EdgeBetweenRooms edgeBetweenRoom : roomEdges){
            if(edgeBetweenRoom.getNodeRooms().contains(deleted)){

                int indexOfDeletedNode = edgeBetweenRoom.getNodeRooms().indexOf(deleted);
                int index = 0;
                if(indexOfDeletedNode == 0) index = 1;
                if(remaining.isPhysicallyAdjacent(edgeBetweenRoom.getNodeRooms().get(index))){//itt mar csak egy eleme lesz(az a szoba ami ramovednak is meg a remainignek is szomszedja)
                    // get the other indexNodeRoomINdex
                    Room RDAdjacent = edgeBetweenRoom.getNodeRooms().get(index);//szoba amia remainingnek es a deletednek is szomszadja
                    EdgeBetweenRooms edgeBetweenRAndRDAdjacent = getEdgeBetweenRooms(remaining, RDAdjacent);

                    //atpakuljuk az egyik edgebol a masikba, hogy majd kitorolhessuk a vegen az egyiket
                    for(int i = 0; i < edgeBetweenRoom.getWalls().size(); i++){
                        //ha ajto akkor kicsereljuk falra, de csak akkor ha a masikon mar van ajto, hogy egy edgen ne legyen ketto
                        if(edgeBetweenRoom.getWalls().get(i).isDoor() && edgeBetweenRAndRDAdjacent.hasDoor()) edgeBetweenRoom.switchDoorToWall(edgeBetweenRoom.getWalls().get(i), isten);
                        edgeBetweenRAndRDAdjacent.getWalls().add(edgeBetweenRoom.getWalls().get(i));
                        //edgeBetweenRAndRDAdjacent.getColliderGroup().addCollider(edgeBetweenRoom.getWalls().get(i).collider);
                    }
                    //isten.getPhysicsEngine().removeColliderGroup(edgeBetweenRoom.getColliderGroup().id);
                    //edgeBetweenRoom.getColliderGroup().getColliders().clear();
                    edgeToRemove.add(edgeBetweenRoom);
                }
                else{
                    edgeBetweenRoom.getNodeRooms().remove(deleted);
                    edgeBetweenRoom.getNodeRooms().add(remaining);
                }
            }
        }
        for(EdgeBetweenRooms edgeToDelete : edgeToRemove){
            roomEdges.remove(edgeToDelete);
        }

    }
    public void updateEdgesAfterSplit(Room oldRoom, Room newRoom){
        //and colliders(walls and edges)
        EdgeBetweenRooms newEdge = new EdgeBetweenRooms(oldRoom,newRoom);
        addEdge(newEdge);
        //az uj szobabol meg nem minden szobaba lesz ajto
        //TODO
        //ha e nem lehet ajtot addolni ellenorizni kell az összefuggoseget es ha nem lenne az akkor a split meghiusul
        addDoor(getEdgeBetweenRooms(oldRoom, newRoom));

        int oldRoomID = oldRoom.getID();
        int newRoomID = newRoom.getID();
        //vegigmegyunk az osszes olyan edgen ami tartelmazza az regi szobát,
        // mivel meg semmi sincs atallitva ezert az split elotti edgeklesznek ervenyben
        ArrayList<EdgeBetweenRooms> edgesToAdd = new ArrayList<>();
        for(EdgeBetweenRooms checkEdge : roomEdges){

            if(checkEdge.getNodeRooms().contains(oldRoom)) {
                //ha az edge a ket vizsgalt szoba kozott van, akkor nem kell tovabb vizsgalodni, mert ezt mar beallitottuk az addolasnal
                if (!getEdgeBetweenRooms(newRoom, oldRoom).equals(checkEdge)) {
                    //az edge falainak a unitroomjaitnak az ownerroomjait nezzuk
                    boolean wasInOldRoom = false;
                    boolean wasInNewRoom = false;
                    for (EdgePiece checkEdgeWall : checkEdge.getWalls()) {
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
                        ArrayList<EdgePiece> wallsToRemoveFromCheckEdge = new ArrayList<>();
                        boolean doorRemoved = false;
                        for (EdgePiece checkEdgeWall : checkEdge.getWalls()) {
                            //the wall is between the newRoom and the other one --> delete wall from checkEdge
                            //a maradek lesz az oldroom edge-e
                            if (checkEdgeWall.getUnitRoomsBetween().get(0).getOwnerRoom().getID() == newRoomID
                                    || checkEdgeWall.getUnitRoomsBetween().get(1).getOwnerRoom().getID() == newRoomID) {

                                if (checkEdgeWall.isDoor())//ha ajtot torlunk
                                    doorRemoved = true;
                                wallsToRemoveFromCheckEdge.add(checkEdgeWall);
                                checkEdgeWall.removeEdgePiece(isten, checkEdge.getColliderGroup());
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
                        //TODO
                        if(!getEdgeBetweenRooms(oldRoom, oldRoomNodeRoom).hasDoor()) addDoor(getEdgeBetweenRooms(oldRoom, oldRoomNodeRoom));

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
            addEdge(edge);
            //TODO
            addDoor(getEdgeBetweenRooms(edge.getNodeRooms().get(0), edge.getNodeRooms().get(1)));
        }

    }

    //ajto nelkul ad egy falat a ket szoba koze
    public void addEdge(EdgeBetweenRooms newEdge){
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
        //isten.getPhysicsEngine().addColliderGroup(newEdge.getColliderGroup());
    }
    //this function add one door to an edge
    //ha mar minden erintett unitroomhoz tartozik ajot, akkor nem addol es hamissal ter vissza
    //fontos hogy a dooradjacenteket is frissitsuk
    public boolean addDoor(EdgeBetweenRooms edge) {
        if(edge == null){
            System.out.println("door is no edge between the given rooms");
            return false;
        }
        if(edge.hasDoor()) return false;

        ArrayList<Integer> random = new ArrayList<>();
        for(int i = 0; i < edge.getWalls().size(); i++){
            random.add(i);
        }
        Collections.shuffle(random);
        for(Integer randomIndex : random)
        {
            //egy unitroomban csek egyik iranyba nyilhat ajto!
            if(edge.switchWallToDoor(edge.getWalls().get(randomIndex), isten)) {
                return true;
            }
        }
        return false;
    }

    //elofordolhat hogy egy edge nem kap ajtot!!
    //egy unitroomhoz csak egy ajto tartozhat!!
    public void initDoors(){
        for(EdgeBetweenRooms edge : roomEdges){
            if(edge.getWalls().size() == 1){
                if(edge.switchWallToDoor(edge.getWalls().get(0), isten)){
                    //ha tudtunk ajtot adni, akkor mehet az ajtoszomszedossagi listaba
                    edge.getNodeRooms().get(0).getDoorAdjacentRooms().add(edge.getNodeRooms().get(1));
                    edge.getNodeRooms().get(1).getDoorAdjacentRooms().add(edge.getNodeRooms().get(0));
                }
            }
        }
        Collections.shuffle(roomEdges);
        for(EdgeBetweenRooms edge : roomEdges){
            if(!edge.hasDoor()) {
                if(addDoor(edge)) {
                    edge.getNodeRooms().get(0).getDoorAdjacentRooms().add(edge.getNodeRooms().get(1));
                    edge.getNodeRooms().get(1).getDoorAdjacentRooms().add(edge.getNodeRooms().get(0));
                }

            }
        }
    }
    public int getDoorNum(){
        int cnt =  0;
        for(EdgeBetweenRooms edge : roomEdges){
            if(edge.hasDoor()) cnt++;
        }
        return cnt;
    }
    public ArrayList<EdgeBetweenRooms> getRoomEdges() {
        return roomEdges;
    }


    @Override
    public void onStart(Isten isten) {

    }


    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        for(EdgeBetweenRooms edge: roomEdges){
            for(EdgePiece ep : edge.getWalls()){
                if(ep.isDoor()){
                    Door door = (Door)ep;
                    if(door.isOpened()){
                        door.manageOpenDoor(deltaTime);
                    }
                }
            }
        }

    }

    public Vec2 OpenDoor(Vec2 playerPosition) {
        for(EdgeBetweenRooms edge: roomEdges){
            for(EdgePiece ep : edge.getWalls()){
                if(ep.isDoor()){
                    Door door = (Door)ep;
                    if(door.isPlayerAtDoor(isten, playerPosition)){
                        if(door.canBeOpened(isten)){
                            door.open();
                            return door.position;
                        }
                        else {
                            System.out.println("Nem lehet kinyitni mert egyiranyu");
                        }
                    }
                }
            }
        }
        return new Vec2(-1,-1);
    }

    @Override
    public void onDestroy() {

    }
}
