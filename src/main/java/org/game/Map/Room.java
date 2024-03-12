package main.java.org.game.Map;

import java.util.ArrayList;

public class Room {
    ArrayList<UnitRoom> unitRooms;
    int playerCount;
    //ArrayList<Player> players;
    boolean discovered;
    RoomType roomType;
    boolean isAdjacent(Room room) {
        return false;
    }
}
