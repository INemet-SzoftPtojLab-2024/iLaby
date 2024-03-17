package main.java.org.game.Map;

import java.util.ArrayList;

public class Room {
    private int ID;
    private ArrayList<UnitRoom> unitRooms;
    private ArrayList<Room> adjacentRooms;
    private int playerCount;
    //ArrayList<Player> players;
    private boolean discovered;
    private RoomType roomType;
    public Room(int ID){
        this.ID = ID;
        unitRooms = new ArrayList<>();
        adjacentRooms = new ArrayList<>();
    }
    public boolean isAdjacent(Room room) {
       // if(this.equals(room)) return false; egynelore nem kell lekezelni
        for(Room checkRoom : adjacentRooms) {
            if (room.equals(checkRoom)) return true;
            //if(room.getID() == checkRoom.getID()) return true;
        }
        return  false;
    }


    //getters and setters
    public ArrayList<UnitRoom> getUnitRooms() {
        return unitRooms;
    }
    public ArrayList<Room> getAdjacentRooms() {
        return adjacentRooms;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }
    public boolean isDiscovered(){
        return discovered;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public RoomType getRoomType() {
        return roomType;
    }
}
