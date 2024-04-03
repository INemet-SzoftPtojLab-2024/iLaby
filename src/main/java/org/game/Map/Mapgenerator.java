package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * this class is only used at the beginning of a game, when a map needs to be generated
 */
public class Mapgenerator {

    ArrayList<Room> rooms;
    private EdgeManager edgeManager;
    private UnitRoom[][] unitRooms;
    private int mapRowSize;
    private int mapColumnSize;
    private Isten isten;
    public Mapgenerator(ArrayList<Room> rooms, UnitRoom[][] unitRooms, int mapRowSize, int mapColumnSize, Isten isten){
        this.isten = isten;
        this.rooms = rooms;
        this.mapColumnSize = mapColumnSize;
        this.mapRowSize = mapRowSize;
        edgeManager = new EdgeManager();
        generateUnitRooms();
    }

    public EdgeManager getEdgeManager() {
        return edgeManager;
    }

    public void defineEdges(){
        for(Room r1: rooms) {
            for (Room r2 : rooms) {
                if(r1.isAdjacent(r2) && !edgeManager.getRoomEdges().contains(edgeManager.getEdgeBetweenRooms(r1,r2))){
                    edgeManager.getRoomEdges().add(new EdgeBetweenRooms(r1,r2));
                }
            }
        }
    }

    public ArrayList<Room> getRooms() {return rooms;}

    public UnitRoom[][] getUnitRooms() {return unitRooms;}

    public void generate(int minRoomSize){

        for(int i = 0; i<mapRowSize;i++)
        {
            for(int j = 0;j<mapColumnSize;j++)
            {
                if(i>0){
                    unitRooms[i][j].getAdjacentUnitRooms().add(unitRooms[i-1][j]);
                    unitRooms[i][j].setBottomNeigbour(unitRooms[i-1][j]);
                }
                if(j>0){
                    unitRooms[i][j].getAdjacentUnitRooms().add(unitRooms[i][j-1]);
                    unitRooms[i][j].setLeftNeigbour(unitRooms[i][j - 1]);
                }
                if(i<mapRowSize-1){
                    unitRooms[i][j].getAdjacentUnitRooms().add(unitRooms[i+1][j]);
                    unitRooms[i][j].setTopNeigbour(unitRooms[i + 1][j]);
                }
                if(j<mapColumnSize-1) {
                    unitRooms[i][j].getAdjacentUnitRooms().add(unitRooms[i][j+1]);
                    unitRooms[i][j].setRightNeigbour(unitRooms[i][j + 1]);
                }
            }
        }

        ArrayList<Integer> shuffledNums = new ArrayList<>();
        for(int i = 0; i < mapRowSize * mapColumnSize; i++){
            shuffledNums.add(i);
        }
        Collections.shuffle(shuffledNums);

        for(int number : shuffledNums ){
            int i = (int)(number / mapColumnSize);
            int j = (number % mapColumnSize);
            chosenUnitRoom(unitRooms[i][j]);
        }

        //merge some room to get bigger rooms
        //merge the rooms until every room has minimumm size of the given number
        mergeRoomsUntilGivenSizeReached(minRoomSize);
        //add the images to the unitrooms
        addImages();
        //create the walls of the rooms
        createWallsForMap();

        for(Room room: rooms) {
            isten.addUpdatable(room);
        }
    }
    public void generateUnitRooms(){
        unitRooms = new UnitRoom[mapRowSize][mapColumnSize];
        for(int i = 0; i<mapRowSize;i++)
        {
            for(int j = 0;j<mapColumnSize;j++)
            {
                unitRooms[i][j] = new UnitRoom(new Vec2(i,j));
            }
        }
    }

    public void chosenUnitRoom(UnitRoom unitRoom){
        int minimalRoomSize  = Integer.MAX_VALUE;
        Room minimalRoom = new Room(1, mapRowSize, mapColumnSize);
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
            Room newRoom = new Room(unitRoom.getRowNum()*mapRowSize+unitRoom.getColNum(), mapRowSize, mapColumnSize);
            unitRoom.setOwnerRoom(newRoom);
            newRoom.getUnitRooms().add(unitRoom);
            rooms.add(newRoom);
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
        Room r2 = rooms.get(0);
        boolean merge = true;
        while(merge) {
            minSize = Integer.MAX_VALUE;
            //System.out.println("RoomNum befor merge: " + rooms.size());
            for (int i = 0; i < rooms.size(); i++) {
                if (rooms.get(i).getUnitRooms().size() <= size) { //if smaller than 6 unitrooms --> merge
                    r1 = rooms.get(i);
                    //search the smallest adjacentroom
                    //System.out.println("adjroomsize:" + rooms.get(i).getAdjacentRooms().size());
                    for (int j = 0; j < rooms.get(i).getAdjacentRooms().size(); j++) {
                        if (rooms.get(i).getAdjacentRooms().get(j).getUnitRooms().size() < minSize) {
                            minSize = rooms.get(i).getAdjacentRooms().get(j).getUnitRooms().size();
                            r2 = rooms.get(i).getAdjacentRooms().get(j);
                        }
                    }
                    //System.out.println("minsize: " + minSize);
                    //System.out.println("merge: " + r1.getID() + " " + r2.getID());
                    mergeRooms(r1,r2);
                    break; // we have found the two mergeable rooms

                }
                if (i == rooms.size() - 1) {
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
        int roomImageCount = 9;
        int j;
        int i = 0;
        for(Room room: rooms) {
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
        rooms.remove(r2);
    }
    public void createWallsForMap() {
        ArrayList<Integer> randomizedMapRow = new ArrayList<>();
        ArrayList<Integer> randomizedMapColumn = new ArrayList<>();
        for(int i = 0; i < mapRowSize; i++) {
            randomizedMapRow.add(i);
        }
        for(int i = 0; i < mapColumnSize; i++) {
            randomizedMapColumn.add(i);
        }
        Collections.shuffle(randomizedMapRow);
        Collections.shuffle(randomizedMapColumn);

        for(int i: randomizedMapRow) {
            // System.out.println("i: " + i);
            for (int j: randomizedMapColumn) {
                unitRooms[i][j].createWalls(isten);
            }
        }
    }
    public void fillUpEdgesBetweenRooms(){

        for(EdgeBetweenRooms edgesBetweenRoom: edgeManager.getRoomEdges()){
            Room r1 = edgesBetweenRoom.getNodeRooms().get(0);
            Room r2 = edgesBetweenRoom.getNodeRooms().get(1);
            for(UnitRoom wallFinderUnitRoom: r1.getUnitRooms()){
                if(wallFinderUnitRoom.getTopNeigbour().getOwnerRoom().equals(r2)){
                    //implementélni ezt, vagy egy másikat a helyére rakni
                    edgesBetweenRoom.addWall();
                }
                if(wallFinderUnitRoom.getBottomNeigbour().getOwnerRoom().equals(r2)){
                    edgesBetweenRoom.addWall();
                }
                if(wallFinderUnitRoom.getLeftNeigbour().getOwnerRoom().equals(r2)){
                    edgesBetweenRoom.addWall();
                }
                if(wallFinderUnitRoom.getRightNeigbour().getOwnerRoom().equals(r2)){
                    edgesBetweenRoom.addWall();
                }
            }
        }
    }

}
