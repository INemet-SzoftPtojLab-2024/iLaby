package main.java.org.game.Map;

import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;

import java.util.ArrayList;


public class Room extends Updatable implements Graph<Room>{
    private int ID;
    private ArrayList<UnitRoom> unitRooms;
    private ArrayList<Room> physicallyAdjacentRooms;
    private ArrayList<Room> doorAdjacentRooms;
    private int maxPlayerCount = 5;
    int playerCount;

    //ArrayList<Player> players;
    boolean discovered = false;

    RoomType roomType;

    //this is maybe usefull, but not yet
    //private ArrayList<Integer> hasDoorWith;


    public Room(int ID){
        this.ID = ID;
        unitRooms = new ArrayList<>();
        physicallyAdjacentRooms = new ArrayList<>();
        doorAdjacentRooms = new ArrayList<>();
        //hasDoorWith = new ArrayList<>();
        roomType = RoomType.getRandomRoomtype();

    }
    public  Room(){}

    @Override
    public void onStart(Isten isten) {

    }

    public ArrayList<Room> getDoorAdjacentRooms() {
        return doorAdjacentRooms;
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {

    }

    @Override
    public void onDestroy() {

    }
    public void setDoorAdjacentRooms(ArrayList<Room> rooms){
        doorAdjacentRooms =new ArrayList<>(rooms);
    }

    //ez az ajton keresztuli
    public boolean isAdjacent(Room room) {
        // if(this.equals(room)) return false; egynelore nem kell lekezelni
        for (Room checkRoom : doorAdjacentRooms) {
            if (room.equals(checkRoom)) return true;
        }
        return  false;
    }
    //isAdjacent átnevezve, mert az isAdjacentre szukseg van a generikus implementacio miatt a kruskall algoban
    public boolean isPhysicallyAdjacent(Room room) {
        // if(this.equals(room)) return false; egynelore nem kell lekezelni
        for(Room checkRoom : physicallyAdjacentRooms) {
            if (room.equals(checkRoom)) return true;
        }
        return  false;
    }
    public void setPhysicallyAdjacentRooms(){
        physicallyAdjacentRooms.clear();
        for(UnitRoom unitRoom : unitRooms){
            for(UnitRoom neighbourUnitRoom: unitRoom.getAdjacentUnitRooms()){
                if(!neighbourUnitRoom.getOwnerRoom().getPhysicallyAdjacentRooms().contains(this) && !neighbourUnitRoom.getOwnerRoom().equals(this)){
                    physicallyAdjacentRooms.add(neighbourUnitRoom.getOwnerRoom());
                    neighbourUnitRoom.getOwnerRoom().getPhysicallyAdjacentRooms().add(this);
                }
            }
        }
    }

    public ArrayList<UnitRoom> getUnitRooms() {
        return unitRooms;
    }
    public ArrayList<Room> getPhysicallyAdjacentRooms() {
        return physicallyAdjacentRooms;
    }

    public int getID() {
        return ID;
    }

    public int getMaxPlayerCount() {return maxPlayerCount;}

    public void setMaxPlayerCount(int maxPlayerCount) {this.maxPlayerCount = maxPlayerCount;}
    public RoomType getRoomType() {return roomType;}



}
