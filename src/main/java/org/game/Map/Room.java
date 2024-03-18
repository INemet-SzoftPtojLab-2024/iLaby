package main.java.org.game.Map;

import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.game.updatable.Updatable;

import java.util.ArrayList;

public class Room extends Updatable {
    int ID;
    private ArrayList<UnitRoom> unitRooms;
    private ArrayList<Room> adjacentRooms;
    int playerCount;
    //ArrayList<Player> players;
    boolean discovered;
    RoomType roomType;
    private ArrayList<Integer> hasDoorWith;

    ColliderGroup roomColliders;

    public Room(int ID){
        this.ID = ID;
        unitRooms = new ArrayList<>();
        adjacentRooms = new ArrayList<>();
        roomColliders=new ColliderGroup();
        hasDoorWith = new ArrayList<>();
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
}
