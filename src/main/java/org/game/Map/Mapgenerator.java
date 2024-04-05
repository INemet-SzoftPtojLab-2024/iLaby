package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
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

        for(int i = 0; i< map.getMapRowSize(); i++)
        {
            for(int j = 0;j<map.getMapColumnSize();j++)
            {
                if(i>0){
                    map.getUnitRooms()[i][j].getAdjacentUnitRooms().add(map.getUnitRooms()[i-1][j]);
                    map.getUnitRooms()[i][j].setBottomNeigbour(map.getUnitRooms()[i-1][j]);
                }
                if(j>0){
                    map.getUnitRooms()[i][j].getAdjacentUnitRooms().add(map.getUnitRooms()[i][j-1]);
                    map.getUnitRooms()[i][j].setLeftNeigbour(map.getUnitRooms()[i][j - 1]);
                }
                if(i< map.getMapRowSize() -1){
                    map.getUnitRooms()[i][j].getAdjacentUnitRooms().add(map.getUnitRooms()[i+1][j]);
                    map.getUnitRooms()[i][j].setTopNeigbour(map.getUnitRooms()[i + 1][j]);
                }
                if(j<map.getMapColumnSize()-1) {
                    map.getUnitRooms()[i][j].getAdjacentUnitRooms().add(map.getUnitRooms()[i][j+1]);
                    map.getUnitRooms()[i][j].setRightNeigbour(map.getUnitRooms()[i][j + 1]);
                }
            }
        }

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

        for(Room room: map.getRooms()) {
            isten.addUpdatable(room);
        }
    }


    public void chosenUnitRoom(UnitRoom unitRoom){
        int minimalRoomSize  = Integer.MAX_VALUE;
        Room minimalRoom = new Room(1, map.getMapRowSize(), map.getMapColumnSize());
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
            Room newRoom = new Room(unitRoom.getRowNum()* map.getMapRowSize() +unitRoom.getColNum(), map.getMapRowSize(), map.getMapColumnSize());
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
        Room r2 = map.getRooms().get(0);
        boolean merge = true;
        while(merge) {
            minSize = Integer.MAX_VALUE;
            //System.out.println("RoomNum befor merge: " + rooms.size());
            for (int i = 0; i < map.getRooms().size(); i++) {
                if (map.getRooms().get(i).getUnitRooms().size() <= size) { //if smaller than 6 unitrooms --> merge
                    r1 = map.getRooms().get(i);
                    //search the smallest adjacentroom
                    //System.out.println("adjroomsize:" + rooms.get(i).getAdjacentRooms().size());
                    for (int j = 0; j < map.getRooms().get(i).getAdjacentRooms().size(); j++) {
                        if (map.getRooms().get(i).getAdjacentRooms().get(j).getUnitRooms().size() < minSize) {
                            minSize = map.getRooms().get(i).getAdjacentRooms().get(j).getUnitRooms().size();
                            r2 = map.getRooms().get(i).getAdjacentRooms().get(j);
                        }
                    }
                    //System.out.println("minsize: " + minSize);
                    //System.out.println("merge: " + r1.getID() + " " + r2.getID());
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
    public void addImages() {
        Image img;
        int j;
        int i = 0;
        for(Room room: map.getRooms()) {
            switch (room.getRoomType()){
                case GAS -> j = 1;
                case SHADOW -> j = 2;
                case CURSED -> j = 3;
                case BASIC -> j = 4;
                default -> j = 0;
            }
            //j = (i % 5) + 1; //test rooms with more roomcolors to see better the borders
            //i++;
            //if(room.getID() == 999) j = 5; //for testing the split algo the new gets a different color
            String path = "./assets/rooms/" + j + ".png";
            for(UnitRoom unitRoom: room.getUnitRooms()) {
                img = new Image(unitRoom.getPosition(), new Vec2(1,1), path);
                img.setSortingLayer(40);
                unitRoom.setImage(img);
                isten.getRenderer().addRenderable(img);
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

    /*
    public void createWallsForMap() {
        ArrayList<Integer> randomizedMapRow = new ArrayList<>();
        ArrayList<Integer> randomizedMapColumn = new ArrayList<>();
        for(int i = 0; i < map.getMapRowSize(); i++) {
            randomizedMapRow.add(i);
        }
        for(int i = 0; i < map.getMapColumnSize(); i++) {
            randomizedMapColumn.add(i);
        }
        Collections.shuffle(randomizedMapRow);
        Collections.shuffle(randomizedMapColumn);

        for(int i: randomizedMapRow) {
            // System.out.println("i: " + i);
            for (int j: randomizedMapColumn) {
                map.getUnitRooms()[i][j].createWalls(isten);
            }
        }
    }

     */

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
                if(wallFinderUnitRoom.getTopNeigbour() != null && wallFinderUnitRoom.getTopNeigbour().getOwnerRoom().equals(r2)){
                    Vec2 wallTopPos = new Vec2(wallFinderUnitRoom.getColNum(), wallFinderUnitRoom.getRowNum() + 0.5f);
                    edgeBetweenRoom.addNewWall(wallTopPos, horizontalScale, wallFinderUnitRoom ,wallFinderUnitRoom.getTopNeigbour(), isten);
                }
                if(wallFinderUnitRoom.getBottomNeigbour() != null && wallFinderUnitRoom.getBottomNeigbour().getOwnerRoom().equals(r2)){
                    Vec2 wallBottomPos = new Vec2(wallFinderUnitRoom.getColNum(), wallFinderUnitRoom.getRowNum() - 0.5f);
                    edgeBetweenRoom.addNewWall(wallBottomPos, horizontalScale, wallFinderUnitRoom, wallFinderUnitRoom.getBottomNeigbour(),isten);
                }
                if(wallFinderUnitRoom.getLeftNeigbour() != null && wallFinderUnitRoom.getLeftNeigbour().getOwnerRoom().equals(r2)){
                    Vec2 wallLeftPos = new Vec2(wallFinderUnitRoom.getColNum() - 0.5f, wallFinderUnitRoom.getRowNum());
                    edgeBetweenRoom.addNewWall(wallLeftPos, verticalScale, wallFinderUnitRoom, wallFinderUnitRoom.getLeftNeigbour() ,isten);
                }
                if(wallFinderUnitRoom.getRightNeigbour() != null && wallFinderUnitRoom.getRightNeigbour().getOwnerRoom().equals(r2)){
                    Vec2 wallRightPos = new Vec2(wallFinderUnitRoom.getColNum() + 0.5f, wallFinderUnitRoom.getRowNum());
                    edgeBetweenRoom.addNewWall(wallRightPos, verticalScale, wallFinderUnitRoom, wallFinderUnitRoom.getRightNeigbour(),isten);
                }
            }
        }
    }

}
