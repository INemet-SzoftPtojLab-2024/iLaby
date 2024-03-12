package main.java.org.game.Map;

import java.util.ArrayList;

public class Room {
    int ID;
    private ArrayList<UnitRoom> unitRooms;
    private ArrayList<Room> adjacentRooms;
    int playerCount;
    //ArrayList<Player> players;
    boolean discovered;
    RoomType roomType;
    public Room(int ID){
        this.ID = ID;
        unitRooms = new ArrayList<>();
        adjacentRooms = new ArrayList<>();
    }
    boolean isAdjacent(Room room) {
        return false;
    }

    public ArrayList<UnitRoom> getUnitRooms() {
        return unitRooms;
    }
    public ArrayList<Room> getAdjacentRooms() {
        return adjacentRooms;
    }

}
