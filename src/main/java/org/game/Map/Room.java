package main.java.org.game.Map;

import lombok.Setter;
import lombok.Getter;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;

public class Room extends Updatable implements Graph<Room>{
    private int ID;
    private ArrayList<UnitRoom> unitRooms;
    private ArrayList<Room> adjacentRooms;
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
        adjacentRooms = new ArrayList<>();
        //hasDoorWith = new ArrayList<>();
        roomType = RoomType.getRandomRoomtype();

    }
    public  Room(){}

    @Override
    public void onStart(Isten isten) {

    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {

    }

    @Override
    public void onDestroy() {

    }

    public boolean isAdjacent(Room room) {
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
                if(!neighbourUnitRoom.getOwnerRoom().getAdjacentRooms().contains(this) && !neighbourUnitRoom.getOwnerRoom().equals(this)){
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

    public int getMaxPlayerCount() {return maxPlayerCount;}

    public void setMaxPlayerCount(int maxPlayerCount) {this.maxPlayerCount = maxPlayerCount;}
    public RoomType getRoomType() {return roomType;}
    public UnitRoom getUnitRoomWithPos(Vec2 pos){
        for(UnitRoom UnitRoom : unitRooms)
        {
            if(UnitRoom.getPosition().x == pos.x && UnitRoom.getPosition().y == pos.y)
            {
                return UnitRoom;
            }
        }
        return null;
    }
}
