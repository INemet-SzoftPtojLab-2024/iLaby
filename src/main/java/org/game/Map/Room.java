package main.java.org.game.Map;

import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.game.updatable.Updatable;

import java.util.ArrayList;

public class Room extends Updatable {
    int ID;
    int maxDoorCount = 2;
    int currDoorCount = 0;
    private ArrayList<UnitRoom> unitRooms;
    private ArrayList<Room> adjacentRooms;
    int playerCount;
    private int maxPlayerCount = 5;



    //ArrayList<Player> players;
    boolean discovered = false;

    RoomType roomType;



    private ArrayList<Integer> hasDoorWith;

    ColliderGroup roomColliders;

    public Room(int ID){
        this.ID = ID;
        unitRooms = new ArrayList<>();
        adjacentRooms = new ArrayList<>();
        roomColliders=new ColliderGroup();
        hasDoorWith = new ArrayList<>();
        roomType = RoomType.getRandomRoomtype();

    }



    @Override
    public void onStart(Isten isten) {
        isten.getPhysicsEngine().addColliderGroup(roomColliders);
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {

    }

    @Override
    public void onDestroy() {

    }

    boolean isAdjacent(Room room) {
        // if(this.equals(room)) return false; egynelore nem kell lekezelni
        for(Room checkRoom : adjacentRooms) {
            if (room.equals(checkRoom)) return true;
        }
        return  false;
    }
    public void setAdjacentRooms(){
        adjacentRooms.clear();
        for(UnitRoom unitRoom : unitRooms){
            for(UnitRoom neighbourUnitRoom: unitRoom.getAdjacentUnitRooms()){
                if(!neighbourUnitRoom.getOwnerRoom().isAdjacent(this) && !neighbourUnitRoom.getOwnerRoom().equals(this)){
                    adjacentRooms.add(neighbourUnitRoom.getOwnerRoom());
                    neighbourUnitRoom.getOwnerRoom().getAdjacentRooms().add(this);
                }
            }
        }
    }


    public ArrayList<UnitRoom> getUnitRooms() {
        return unitRooms;
    }
    public ArrayList<Room> getAdjacentRooms() {
        return adjacentRooms;
    }

    public int getID() {
        return ID;
    }

    public void addCollider(Collider c){roomColliders.addCollider(c);}

    public void addHasDoorWith(int i) {
        hasDoorWith.add(i);
    }

    public boolean hasDoorWith(int i) {
        return hasDoorWith.contains(i);
    }

    public boolean hasDoor() {
        return (!hasDoorWith.isEmpty());
    }
    public int getMaxPlayerCount() {return maxPlayerCount;}

    public void setMaxPlayerCount(int maxPlayerCount) {this.maxPlayerCount = maxPlayerCount;}
    public RoomType getRoomType() {return roomType;}



}
