package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.awt.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Map extends Updatable {
    ArrayList<Room> rooms;

    private UnitRoom[][] unitRooms;
    private int mapRowSize;
    private int mapColumnSize;

    @Override
    public void onStart(Isten isten) {
        setUnitRooms(mapRowSize, mapColumnSize);
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
        for(int i = 0;i<mapRowSize;i++){ //test
            for(int j = 0; j<mapColumnSize;j++){
                int printer = mapRowSize*i+j;
                System.out.println("Room: "+  printer + ",   " + i+ "row" + j +"column. Position:"+ unitRooms[i][j].getPosition().x + ": " + unitRooms[i][j].getPosition().y);
            }
        }
        int[] randomNums = shuffleUnitRooms();
        for(int number : randomNums ){
            int i = (int)(number/ mapRowSize);
            int j = number - i * mapRowSize;
            chosenUnitRoom(unitRooms[i][j]);
        }

        addImages(isten);

        for(int i = 0;i<mapRowSize;i++) {
            for (int j = 0; j < mapColumnSize; j++) {
                unitRooms[i][j].createWalls(isten);
            }
        }

        //test
        /*int one = 0;
        int two = 0;
        int tree = 0;
        int four = 0;
        for(Room room : rooms){
            if(room.getUnitRooms().size() == 4) four++;
            if(room.getUnitRooms().size() == 3) tree++;
            if(room.getUnitRooms().size() == 2) two++;
            if(room.getUnitRooms().size() == 1) one++;
        }
        System.out.println(one + " " + two +" " + tree + " " + four + " Sum:" + (int)(one + two + tree+ four));
        System.out.println("Roomnum: " + rooms.size());
        */

    }
    public Map(int rowNumber, int columnNumber){
        mapRowSize = rowNumber;
        mapColumnSize = columnNumber;
        rooms = new ArrayList<>();
    }

    private void addImages(Isten isten) {

        Image img;
        int i = 1;
        for(Room room: rooms) {
            String path = "./assets/rooms/" + i + ".png";
            for(UnitRoom unitRoom: room.getUnitRooms()) {
                img = new Image(unitRoom.getPosition(), new Vec2(1,1), path);
                isten.getRenderer().addRenderable(img);
            }
            i++;
            i = i % 10 + 1;
        }
    }


    @Override
    public void onUpdate(Isten isten, double deltaTime) {

    }

    @Override
    public void onDestroy() {

    }

    private void mergeRooms() {

    }

    private void splitRooms() {

    }

    public void setUnitRooms(int rowNumber, int columnNumber){
        unitRooms = new UnitRoom[rowNumber][columnNumber];
        for(int i = 0; i<rowNumber;i++)
        {
            for(int j = 0;j<columnNumber;j++)
            {
                unitRooms[i][j] = new UnitRoom(new Vec2(i,j));
            }
        }
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
    public int[] shuffleUnitRooms(){
        int n = mapRowSize * mapRowSize;
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
}
