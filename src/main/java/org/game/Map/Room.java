package main.java.org.game.Map;

import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;

import java.util.ArrayList;


public class Room extends Updatable implements Graph<Room>{
    private int ID;
    private ArrayList<UnitRoom> unitRooms;
    private ArrayList<Room> physicallyAdjacentRooms;
    private ArrayList<Room> throughDoorAdjacentRooms;
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
        throughDoorAdjacentRooms = new ArrayList<>();
        //hasDoorWith = new ArrayList<>();
        roomType = RoomType.getRandomRoomtype();

    }
    public  Room(){}

    @Override
    public void onStart(Isten isten) {

    }

    public ArrayList<Room> getThroughDoorAdjacentRooms() {
        return throughDoorAdjacentRooms;
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {

    }

    @Override
    public void onDestroy() {

    }
    public void setThroughDoorAdjacentRooms(ArrayList<Room> rooms){
        throughDoorAdjacentRooms=new ArrayList<>(rooms);
    }

    public boolean isAdjacent(Room room) {
        // if(this.equals(room)) return false; egynelore nem kell lekezelni
        for (Room checkRoom : throughDoorAdjacentRooms) {
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
    public void setAdjacentRooms(){
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
