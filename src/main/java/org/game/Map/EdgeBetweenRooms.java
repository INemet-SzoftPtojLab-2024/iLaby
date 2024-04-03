package main.java.org.game.Map;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
public class EdgeBetweenRooms {
    private ArrayList<Room> nodeRooms;
    private ArrayList<Wall> walls;

    public EdgeBetweenRooms(Room r1, Room r2){
        nodeRooms = new ArrayList<>();
        nodeRooms.add(r1);
        nodeRooms.add(r2);
        walls = new ArrayList<>();
    }
    //do adWall function
    public void addWall(){

    }
}
