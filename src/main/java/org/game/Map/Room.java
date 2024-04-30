package main.java.org.game.Map;

import main.java.org.game.Isten;
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

        roomType = RoomType.getRandomRoomtype(false);
    }

    public void setRoomType(boolean startRoom) {
        roomType =  RoomType.getRandomRoomtype(startRoom);
    }
    public void setRoomTypeToGas()
    {
        roomType=RoomType.GAS;
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
  
    public boolean isUnitRoomInSameRoomAsStartRoom(Vec2 position)
    {
        boolean isUnitRoomInRoom=false;
        boolean isStartUnitRoomInRoom =false;
        for(UnitRoom unitRoom1 : unitRooms)
        {
            if(unitRoom1.getPosition().x == position.x && unitRoom1.getPosition().y== position.y) {
                isUnitRoomInRoom = true;
            }
            if (unitRoom1.getPosition().x == 0 && unitRoom1.getPosition().y == 0) {
                isStartUnitRoomInRoom = true;
            }
        }
        return isStartUnitRoomInRoom && isUnitRoomInRoom;
    }

    public void setRoomType(RoomType type) {
        this.roomType = type;
    }

}
