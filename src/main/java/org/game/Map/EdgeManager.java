package main.java.org.game.Map;

import lombok.Getter;
import lombok.Setter;
import main.java.org.game.Isten;

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
    public void deleteEdge(Room r1, Room r2, Isten isten){
        EdgeBetweenRooms edgeToDelete = getEdgeBetweenRooms(r1, r2);
        for(int i = 0; i < roomEdges.size(); i++){
            if(edgeToDelete.equals(roomEdges.get(i))){
                //remove all the wallpiece from the edge.
                for(Wall wallsToDelete : edgeToDelete.getWalls()){
                    wallsToDelete.removeWall(isten, edgeToDelete.getColliderGroup());
                }
                // removeing the collidergroup from the physics engine
                //edgeToDelete.getColliderGroup().getColliders().clear(); //its not required
                isten.getPhysicsEngine().removeColliderGroup(edgeToDelete.getColliderGroup().id);

                roomEdges.remove(i);
                break;
            }
        }
    }

}
