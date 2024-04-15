package main.java.org.game.Map;

import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;
import java.util.Collections;

/**
 * this class is only used at the beginning of a game, when a map needs to be generated
 */
public class Mapgenerator {
    private Map map;
    private Isten isten;
    public Mapgenerator(Map map, Isten isten){
        this.isten = isten;
        this.map = map;
    }


    public void generate(int minRoomSize){
        //collidergroup for the sideWalls
        ColliderGroup sideWallColliders = new ColliderGroup();
        Vec2 horizontalScale = new Vec2(1f, 0.1f); //vizszintes
        Vec2 verticalScale = new Vec2(0.1f, 1f); //fuggoleges
        String wallPath = "./assets/wall_mid.png";
        for(int i = 0; i< map.getMapRowSize(); i++)
        {
            for(int j = 0;j<map.getMapColumnSize();j++)
            {
                UnitRoom actualUnitRoom = map.getUnitRooms()[i][j];
                if(i>0){
                    actualUnitRoom.getAdjacentUnitRooms().add(map.getUnitRooms()[i-1][j]);
                    map.getUnitRooms()[i][j].setBottomNeighbor(map.getUnitRooms()[i-1][j]);
                }
                if(j>0){
                    actualUnitRoom.getAdjacentUnitRooms().add(map.getUnitRooms()[i][j-1]);
                    actualUnitRoom.setLeftNeighbor(map.getUnitRooms()[i][j - 1]);
                }
                if(i< map.getMapRowSize() -1){
                    actualUnitRoom.getAdjacentUnitRooms().add(map.getUnitRooms()[i+1][j]);
                    actualUnitRoom.setTopNeighbor(map.getUnitRooms()[i + 1][j]);
                }
                if(j<map.getMapColumnSize()-1) {
                    actualUnitRoom.getAdjacentUnitRooms().add(map.getUnitRooms()[i][j+1]);
                    actualUnitRoom.setRightNeighbor(map.getUnitRooms()[i][j + 1]);
                }


                //let's generate also the sideWalls, because they are fix
                //note: that the corner unitrooms can fulfill two conditions
                if(actualUnitRoom.getRightNeighbor() == null){ //if the unitroom hasn't got right neigbour
                    Vec2 wallRightPos = new Vec2(actualUnitRoom.getColNum() + 0.5f, actualUnitRoom.getRowNum());
                    Collider wallCollider = new Collider(wallRightPos, verticalScale);
                    sideWallColliders.addCollider(wallCollider);
                    Wall newWall = new Wall(wallCollider,wallRightPos , actualUnitRoom);
                    newWall.setNewImage(wallPath, verticalScale, isten);
                }
                if(actualUnitRoom.getLeftNeighbor() == null){ //if the unitroom hasn't got left neigbour
                    Vec2 wallLeftPos = new Vec2(actualUnitRoom.getColNum() - 0.5f, actualUnitRoom.getRowNum());
                    Collider wallCollider = new Collider(wallLeftPos, verticalScale);
                    sideWallColliders.addCollider(wallCollider);
                    Wall newWall = new Wall(wallCollider,wallLeftPos , actualUnitRoom);
                    newWall.setNewImage(wallPath, verticalScale, isten);
                }
                if(actualUnitRoom.getTopNeighbor() == null){ //if the unitroom hasn't got top neigbour
                    Vec2 wallTopPos = new Vec2(actualUnitRoom.getColNum(), actualUnitRoom.getRowNum() + 0.5f);
                    Collider wallCollider = new Collider(wallTopPos, horizontalScale);
                    sideWallColliders.addCollider(wallCollider);
                    Wall newWall = new Wall(wallCollider,wallTopPos , actualUnitRoom);
                    newWall.setNewImage(wallPath, horizontalScale, isten);
                }
                if(actualUnitRoom.getBottomNeighbor() == null){ //if the unitroom hasn't got bottom neigbour
                    Vec2 wallBottomPos = new Vec2(actualUnitRoom.getColNum(), actualUnitRoom.getRowNum() - 0.5f);
                    Collider wallCollider = new Collider(wallBottomPos, horizontalScale);
                    sideWallColliders.addCollider(wallCollider);
                    Wall newWall = new Wall(wallCollider,wallBottomPos , actualUnitRoom);
                    newWall.setNewImage(wallPath, horizontalScale, isten);
                }

            }
        }
        //add the collidergroup to the system
        isten.getPhysicsEngine().addColliderGroup(sideWallColliders);

        ArrayList<Integer> shuffledNums = new ArrayList<>();
        for(int i = 0; i < map.getMapRowSize() * map.getMapColumnSize(); i++){
            shuffledNums.add(i);
        }
        Collections.shuffle(shuffledNums);

        for(int number : shuffledNums ){
            int i = (int)(number / map.getMapColumnSize());
            int j = (number % map.getMapColumnSize());
            chosenUnitRoom(map.getUnitRooms()[i][j]);
        }

        //merge some room to get bigger rooms
        //merge the rooms until every room has minimumm size of the given number
        mergeRoomsUntilGivenSizeReached(minRoomSize);
        //add the images to the unitrooms
        addImages();
        //create the walls of the rooms
        defineEdges();

        map.getEdgeManager().initDoors();

        for(Room room: map.getRooms()) {
            isten.addUpdatable(room);
        }
    }


    public void chosenUnitRoom(UnitRoom unitRoom){
        int minimalRoomSize  = Integer.MAX_VALUE;
        Room minimalRoom = new Room();
        ArrayList<Room> unitRoomNeighbourRooms = new ArrayList<>(); // itt taroljuk majd azokat, amik a unitroom szomszedos, de nem kerultek kivalasztasra
        for(UnitRoom neighbour : unitRoom.getAdjacentUnitRooms()){
            if(neighbour.isInRoom()){
                unitRoomNeighbourRooms.add(neighbour.getOwnerRoom());
                if(neighbour.getOwnerRoom().getUnitRooms().size()<minimalRoomSize)
                {
                    minimalRoomSize = neighbour.getOwnerRoom().getUnitRooms().size();
                    minimalRoom = neighbour.getOwnerRoom();
                }
            }
        }
        if(unitRoomNeighbourRooms.isEmpty()){
            //System.out.println("Új szoba kerül felvételre ID-val:" + unitRoom.getRowNum()*mapRowSize+unitRoom.getColNum());
            Room newRoom = new Room(unitRoom.getRowNum()* map.getMapRowSize() +unitRoom.getColNum());
            unitRoom.setOwnerRoom(newRoom);
            newRoom.getUnitRooms().add(unitRoom);
            map.getRooms().add(newRoom);
            //System.out.println("A szoba tömb mérete: " + rooms.size());
        }
        else{
           /* System.out.println("Unitroomot egy roomba addoljuk, aminek az ID-ja" + minimalRoom.getID() + "A UnitRoom ID-ja: " +
                    unitRoom.getRowNum()*mapRowSize+unitRoom.getColNum() +
                    " A minimalroom unitroomszáma addolás előtt: " + minimalRoom.getUnitRooms().size());
            */
            unitRoom.setOwnerRoom(minimalRoom);
            minimalRoom.getUnitRooms().add(unitRoom);
            if(unitRoomNeighbourRooms.size()>1){
                for(Room unitRoomNeighbourRoom: unitRoomNeighbourRooms) {
                    if (!unitRoomNeighbourRoom.equals(minimalRoom) && !unitRoomNeighbourRoom.isAdjacent(minimalRoom)) {
                        minimalRoom.getAdjacentRooms().add(unitRoomNeighbourRoom);
                        unitRoomNeighbourRoom.getAdjacentRooms().add(minimalRoom);
                    }
                }

            }
            //System.out.println(" A minimalroom unitroomszáma addolás után: " + minimalRoom.getUnitRooms().size());
        }

    }
    void mergeRoomsUntilGivenSizeReached(int size){
        int minSize;
        Room r1;
        Room r2;
        boolean merge = true;
        while(merge) {

            //System.out.println("RoomNum befor merge: " + rooms.size());
            Collections.shuffle(map.getRooms());
            for (int i = 0; i < map.getRooms().size(); i++) {
                if (map.getRooms().get(i).getUnitRooms().size() <= size) { //if smaller than "size" unitrooms --> merge
                    r1 = map.getRooms().get(i);

                    //search the smallest adjacentroom or the adjacentroomwith the longest edge
                    //ezzel kevesbe lesznek elfajulo szobak
                    r2 = getLongestEdgeNeighbour(r1);
                    //r2 = getSmallestNeighbour(r1);

                    mergeRooms(r1,r2);
                    break; // we have found the two mergeable rooms

                }
                if (i == map.getRooms().size() - 1) {
                    merge = false;
                    break;
                }
            }
            //System.out.println("RoomNum after merge: " + rooms.size());
            //System.out.println();
        }
    }

    private Room getLongestEdgeNeighbour(Room r){
        //ebbe a listaba taroljuk hogy mejk szomszednak milyen hosszu a szomszedja
        int[] edgeLengths = new int[r.getAdjacentRooms().size()];
        for(Integer edgelen : edgeLengths) edgelen = 0;
        for(UnitRoom ur : r.getUnitRooms()){
            for(UnitRoom neighbour : ur.getAdjacentUnitRooms()){
                if(neighbour.getOwnerRoom().getID() != r.getID()){
                    edgeLengths[r.getAdjacentRooms().indexOf(neighbour.getOwnerRoom())]++;
                }
            }
        }
        int longestIdx = 0;
        for(int i = 0; i < edgeLengths.length; i++){
            if(edgeLengths[i] > edgeLengths[longestIdx]  /*&& r.getAdjacentRooms().get(i).getUnitRooms().size() < 20*/){ // ezen meg kell tokeletesiteni ha hasznalni akarjuk!!
                longestIdx = i;
            }
        }
        return r.getAdjacentRooms().get(longestIdx);
    }

    private Room getSmallestNeighbour(Room r1){
        Room r2 = map.getRooms().get(0);
        int minSize = Integer.MAX_VALUE;
        for (int j = 0; j < r1.getAdjacentRooms().size(); j++) {
            if (r1.getAdjacentRooms().get(j).getUnitRooms().size() < minSize) {
                minSize = r1.getAdjacentRooms().get(j).getUnitRooms().size();
                r2 = r1.getAdjacentRooms().get(j);
            }
        }
        return r2;
    }
    public void addImages() {
        for(Room room: map.getRooms()) {
            for(UnitRoom unitRoom: room.getUnitRooms()) {
                unitRoom.addRightImage(isten);
            }
        }
    }
    private void mergeRooms(Room r1, Room r2) {
        if(!r1.isAdjacent(r2) || r1.getID() == r2.getID()){
            System.err.println("cant be merged");
            return;
        }
        //remove r2 and keep r1
        for(UnitRoom unitroom : r2.getUnitRooms()){
            r1.getUnitRooms().add(unitroom);
            unitroom.setOwnerRoom(r1);
        }
        r2.getUnitRooms().clear();

        r1.getAdjacentRooms().remove(r2);
        r2.getAdjacentRooms().remove(r1);
        for(Room adj : r2.getAdjacentRooms()){
            if(!r1.getAdjacentRooms().contains(adj) && !adj.equals(r1)){
                //System.out.println("adjroom added in r1: " + adj.getID());
                r1.getAdjacentRooms().add(adj);
            }
            adj.getAdjacentRooms().remove(r2);
            if(!adj.getAdjacentRooms().contains(r1)) {
                adj.getAdjacentRooms().add(r1);
            }
        }

        r2.getAdjacentRooms().clear();
        map.getRooms().remove(r2);
    }

    public void defineEdges(){
        for(Room r1: map.getRooms()) {
            for (Room r2 : map.getRooms()) {
                //ha szomszédosak, és még nincs a két szoba kozott definialva az edge (vagyis a falak)
                if(r1.isAdjacent(r2) && !map.getEdgeManager().getRoomEdges().contains(map.getEdgeManager().getEdgeBetweenRooms(r1,r2))){
                    EdgeBetweenRooms newEdge = new EdgeBetweenRooms(r1,r2);
                    map.getEdgeManager().getRoomEdges().add(newEdge);
                    // this collidergroup will be filled up, when the walls are created
                    isten.getPhysicsEngine().addColliderGroup(newEdge.getColliderGroup());
                }
            }
        }
        fillUpEdgesBetweenRooms();
    }
    public void fillUpEdgesBetweenRooms(){
        Vec2 horizontalScale = new Vec2(1f, 0.1f); //vizszintes
        Vec2 verticalScale = new Vec2(0.1f, 1f); //fuggoleges
        for(EdgeBetweenRooms edgeBetweenRoom: map.getEdgeManager().getRoomEdges()){
            Room r1 = edgeBetweenRoom.getNodeRooms().get(0);
            Room r2 = edgeBetweenRoom.getNodeRooms().get(1);
            for(UnitRoom wallFinderUnitRoom: r1.getUnitRooms()){
                if(wallFinderUnitRoom.getTopNeighbor() != null && wallFinderUnitRoom.getTopNeighbor().getOwnerRoom().equals(r2)){
                    Vec2 wallTopPos = new Vec2(wallFinderUnitRoom.getColNum(), wallFinderUnitRoom.getRowNum() + 0.5f);
                    edgeBetweenRoom.addNewWall(wallTopPos, horizontalScale, wallFinderUnitRoom ,wallFinderUnitRoom.getTopNeighbor(), isten);
                }
                if(wallFinderUnitRoom.getBottomNeighbor() != null && wallFinderUnitRoom.getBottomNeighbor().getOwnerRoom().equals(r2)){
                    Vec2 wallBottomPos = new Vec2(wallFinderUnitRoom.getColNum(), wallFinderUnitRoom.getRowNum() - 0.5f);
                    edgeBetweenRoom.addNewWall(wallBottomPos, horizontalScale, wallFinderUnitRoom, wallFinderUnitRoom.getBottomNeighbor(),isten);
                }
                if(wallFinderUnitRoom.getLeftNeighbor() != null && wallFinderUnitRoom.getLeftNeighbor().getOwnerRoom().equals(r2)){
                    Vec2 wallLeftPos = new Vec2(wallFinderUnitRoom.getColNum() - 0.5f, wallFinderUnitRoom.getRowNum());
                    edgeBetweenRoom.addNewWall(wallLeftPos, verticalScale, wallFinderUnitRoom, wallFinderUnitRoom.getLeftNeighbor() ,isten);
                }
                if(wallFinderUnitRoom.getRightNeighbor() != null && wallFinderUnitRoom.getRightNeighbor().getOwnerRoom().equals(r2)){
                    Vec2 wallRightPos = new Vec2(wallFinderUnitRoom.getColNum() + 0.5f, wallFinderUnitRoom.getRowNum());
                    edgeBetweenRoom.addNewWall(wallRightPos, verticalScale, wallFinderUnitRoom, wallFinderUnitRoom.getRightNeighbor(),isten);
                }
            }
        }
    }




}
