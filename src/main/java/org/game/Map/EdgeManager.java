package main.java.org.game.Map;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
public class EdgeManager {
    private ArrayList<EdgeBetweenRooms> roomEdges;
    public EdgeManager(){
        roomEdges = new ArrayList<>();
    }


    public EdgeBetweenRooms getEdgeBetweenRooms(Room r1, Room r2) {
        for(EdgeBetweenRooms edgeBetweenRooms: roomEdges){
            if(edgeBetweenRooms.getNodeRooms().contains(r1) && edgeBetweenRooms.getNodeRooms().contains(r2))
            {
                return edgeBetweenRooms;
            }
        }
        return null;
    }

    public ArrayList<EdgeBetweenRooms> getRoomEdges() {
        return roomEdges;
    }

}
