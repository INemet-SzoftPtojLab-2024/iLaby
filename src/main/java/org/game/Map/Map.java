package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.awt.*;
import java.util.ArrayList;


public class Map extends Updatable {
    private ArrayList<Room> rooms;
    private UnitRoom[][] unitRooms;
    private int mapRowSize;
    private int mapColumnSize;

    @Override
    public void onStart(Isten isten) {
        createTestMap(isten);
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
            Room newRoom = new Room(unitRoom.getRowNum()*mapRowSize+unitRoom.getColNum());
            unitRoom.setOwnerRoom(newRoom);
            newRoom.getUnitRooms().add(unitRoom);
            rooms.add(newRoom);
        }
        else{
            unitRoom.setOwnerRoom(minimalRoom);
            minimalRoom.getUnitRooms().add(unitRoom);
            boolean isElement = false;
            if(unitRoomNeighbourRooms.size()>1){
                for(Room unitRoomNeighbourRoom: unitRoomNeighbourRooms) {
                    isElement = false;
                    for(Room adjacentRoomsFromMinimalRoom: minimalRoom.getAdjacentRooms()){
                        if(unitRoomNeighbourRoom.equals(adjacentRoomsFromMinimalRoom)) isElement = true;
                    }
                    if(!isElement){
                        minimalRoom.getAdjacentRooms().add(unitRoomNeighbourRoom);
                        unitRoomNeighbourRoom.getAdjacentRooms().add(minimalRoom);
                    }

                }

            }

        }

    }
    public void drawMap(Graphics2D g2) {
        for (Room r: rooms) {
           for (UnitRoom u: r.getUnitRooms()) {
               u.image.render(g2);
           }
        }
    }
}
