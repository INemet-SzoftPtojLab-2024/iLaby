package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Mapgenerator {
    ArrayList<Room> rooms;
    private UnitRoom[][] unitRooms;
    private int mapRowSize;
    private int mapColumnSize;
    private Isten isten;
    public Mapgenerator(ArrayList<Room> rooms, UnitRoom[][] unitRooms, int mapRowSize, int mapColumnSize, Isten isten){
        this.isten = isten;
        this.rooms = rooms;
        this.mapColumnSize = mapColumnSize;
        this.mapRowSize = mapRowSize;
        generateUnitRooms();
    }

    public ArrayList<Room> getRooms() {return rooms;}

    public UnitRoom[][] getUnitRooms() {return unitRooms;}

    public void generate(){

        for(int i = 0; i<mapRowSize;i++)
        {
            for(int j = 0;j<mapColumnSize;j++)
            {
                if(i>0) unitRooms[i][j].getAdjacentUnitRooms().add(unitRooms[i-1][j]);
                if(j>0) unitRooms[i][j].getAdjacentUnitRooms().add(unitRooms[i][j-1]);
                if(i<mapRowSize-1) unitRooms[i][j].getAdjacentUnitRooms().add(unitRooms[i+1][j]);
                if(j<mapColumnSize-1) unitRooms[i][j].getAdjacentUnitRooms().add(unitRooms[i][j+1]);
            }
        }

        int[] randomNums = shuffleUnitRooms();
        for(int number : randomNums ){
            int i = (int)(number / mapColumnSize);
            int j = (number % mapColumnSize);
            chosenUnitRoom(unitRooms[i][j]);
        }

        //merge some room to get bigger rooms
        //merge the rooms until every room has minimumm size of the given number
        mergeRoomsUntilGivenSizeReached(15);
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
    public int[] shuffleUnitRooms(){
        int n = mapRowSize * mapColumnSize;
        int numbers[] = new int[n];
        for(int i = 0; i < n; i++){
            numbers[i] = i;
        }
        Random rand = new Random();
        for (int i = n - 1; i >= 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = numbers[j];
            numbers[j] = numbers[i];
            numbers[i] = temp;
        }
        return numbers;
    }
    public void chosenUnitRoom(UnitRoom unitRoom){
        int minimalRoomSize  = Integer.MAX_VALUE;
        Room minimalRoom = new Room(1);
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
            Room newRoom = new Room(unitRoom.getRowNum()*mapRowSize+unitRoom.getColNum());
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
        for(Room room: rooms) {
            switch (room.getRoomType()){
                case GAS -> j = 1;
                case SHADOW -> j = 2;
                case CURSED -> j = 3;
                case BASIC -> j = 4;
                default -> j = 0;
            }
            String path = "./assets/rooms/" + j + ".png";
            for(UnitRoom unitRoom: room.getUnitRooms()) {

                img = new Image(unitRoom.getPosition(), new Vec2(1,1), path);
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
}
