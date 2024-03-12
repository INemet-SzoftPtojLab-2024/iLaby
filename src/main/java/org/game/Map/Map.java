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
    private ArrayList<Room> rooms;
    private UnitRoom[][] unitRooms;
    private int mapRowSize;
    private int mapColumnSize;

    @Override
    public void onStart(Isten isten) {

    }

    private void createTestMap(Isten isten) {
        ArrayList<UnitRoom> unitRooms = new ArrayList<>();
        for (int i = 0; i< 13; i++) {
            for (int j = 0; j<10; j++) {
                unitRooms.add(new UnitRoom(new Vec2(i*64,j*64)));
                Image image = new Image(new Vec2(i*64,j*64),64,64,new Vec2(1,1),"./assets/tile.png");
                unitRooms.get(unitRooms.size()-1).setImage(image);
                isten.getRenderer().addRenderable(image);
            }
        }

    }
    public Map(int rowNumber, int columnNumber){
        mapRowSize = rowNumber;
        mapColumnSize = columnNumber;
        rooms = new ArrayList<>();
        setUnitRooms(rowNumber, columnNumber);
        for(int i = 0; i<rowNumber;i++)
        {
            for(int j = 0;j<columnNumber;j++)
            {
                if(i>0) unitRooms[i][j].getAdjacentUnitRooms().add(unitRooms[i-1][j]);
                if(j>0) unitRooms[i][j].getAdjacentUnitRooms().add(unitRooms[i][j-1]);
                if(i<rowNumber-1) unitRooms[i][j].getAdjacentUnitRooms().add(unitRooms[i+1][j]);
                if(j<columnNumber-1) unitRooms[i][j].getAdjacentUnitRooms().add(unitRooms[i][j+1]);
            }
        }
        for(int i = 0;i<rowNumber;i++){
            for(int j = 0; j<columnNumber;j++){
                int printer = rowNumber*i+j;
                System.out.println("Room: "+  printer + ",   " + i+ "row" + j +"column. Position:"+ unitRooms[i][j].getPosition().x + ": " + unitRooms[i][j].getPosition().y);
            }
        }
        int[] randomNums = shuffleUnitRooms();
        for(int number : randomNums ){
            int i = (int)(number/ rowNumber);
            int j = number - i * rowNumber;
            chosenUnitRoom(unitRooms[i][j]);
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
                unitRooms[i][j] = new UnitRoom(new Vec2(i*64,j*64));
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
            System.out.println("Új szoba kerül felvételre ID-val:" + unitRoom.getRowNum()*mapRowSize+unitRoom.getColNum());
            Room newRoom = new Room(unitRoom.getRowNum()*mapRowSize+unitRoom.getColNum());
            unitRoom.setOwnerRoom(newRoom);
            newRoom.getUnitRooms().add(unitRoom);
            rooms.add(newRoom);
            System.out.println("A szoba tömb mérete: " + rooms.size());
        }
        else{
            System.out.println("Unitroomot egy roomba addoljuk, aminek az ID-ja" + minimalRoom.getID() + "A UnitRoom ID-ja: " +
                    unitRoom.getRowNum()*mapRowSize+unitRoom.getColNum() +
                    " A minimalroom unitroomszáma addolás előtt: " + minimalRoom.getUnitRooms().size());
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
            System.out.println(" A minimalroom unitroomszáma addolás után: " + minimalRoom.getUnitRooms().size());
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
    public void drawMap(Graphics2D g2) {
        for (Room r: rooms) {
           for (UnitRoom u: r.getUnitRooms()) {
               u.image.render(g2);
           }
        }
    }
}
