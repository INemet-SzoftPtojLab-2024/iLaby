package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.awt.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class Map extends Updatable {
    private Mapgenerator mapgenerator;
    ArrayList<Room> rooms;
    private UnitRoom[][] unitRooms;
    private int mapRowSize;
    private int mapColumnSize;

    @Override
    public void onStart(Isten isten) {
        mapgenerator = new Mapgenerator(rooms,unitRooms, mapRowSize, mapColumnSize, isten);
        mapgenerator.generate();
        unitRooms = mapgenerator.getUnitRooms();
        rooms = mapgenerator.getRooms();
    }

    public Map(int rowNumber, int columnNumber){
        this.mapRowSize = rowNumber;
        this.mapColumnSize = columnNumber;
        //unitrooms is set in the generator --> onstart
        this.rooms = new ArrayList<>();

    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {

    }

    @Override
    public void onDestroy() {

    }

    private void splitRooms() {

    }

    //ez a fv a mapgenerátorban is hasonlóan szerepel
    private void mergeRooms(Room r1, Room r2) {
        if(!r1.isAdjacent(r2) || r1.getID() == r2.getID()){
            System.err.println("cant be merged");
            return;
        }
        //remove r2 and keep r1
        for(UnitRoom unitroom : r2.getUnitRooms()){
            r1.getUnitRooms().add(unitroom);
            unitroom.setOwnerRoom(r1);
            //image update!!
            //update the Walls!!

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
        //r1.setDiscovered(r2.isDiscovered());
        //r1.setPlayerCount(r1.getPlayerCount() + r2.getPlayerCount());
        //r1.setRoomType(r2.getRoomType());
        r1.setMaxPlayerCount(r1.getMaxPlayerCount() + r2.getMaxPlayerCount());
        rooms.remove(r2);
    }




    //merge the rooms until every room has minimumm size of the given number
    private void printMap(){
        for(int i = 0;i < mapRowSize;i++){ //test
            for(int j = 0; j< mapColumnSize;j++){
                if(unitRooms[i][j].getOwnerRoom().getID() < 10) {
                    //System.out.print(unitRooms[i][j].getOwnerRoom().getID() + "  ");
                }
                else {
                    //System.out.print(unitRooms[i][j].getOwnerRoom().getID() + " ");
                }
            }
            //System.out.println();
        }
    }



    public void setRooms(ArrayList<Room> rooms) {this.rooms = rooms;}

    public void setUnitRooms(UnitRoom[][] unitRooms) {
        this.unitRooms = unitRooms;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public UnitRoom[][] getUnitRooms() {
        return unitRooms;
    }
}
