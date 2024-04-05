package main.java.org.game.Map;

import lombok.Getter;
import lombok.Setter;
import main.java.org.game.Isten;

import java.util.ArrayList;
import java.util.Random;

@Getter
@Setter
/**
 * This class is responsible for all the Walls(edges), between the rooms. It contains the doors too.
 */
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


    public void deleteEdge(Room r1, Room r2, Isten isten){
        EdgeBetweenRooms edgeToDelete = getEdgeBetweenRooms(r1, r2);
        for(int i = 0; i < roomEdges.size(); i++){
            if(edgeToDelete.equals(roomEdges.get(i))){
                //remove all the wallpiece from the edge.
                for(Wall wallsToDelete : edgeToDelete.getWalls()){
                    wallsToDelete.removeWall(isten, edgeToDelete.getColliderGroup());
                }
                //edgeToDelete.getNodeRooms().clear(); //it's not required
                //edgeToDelete.getWalls().clear(); //it's not required

                // removeing the collidergroup from the physics engine
                //edgeToDelete.getColliderGroup().getColliders().clear(); //it's not required
                isten.getPhysicsEngine().removeColliderGroup(edgeToDelete.getColliderGroup().id);
                roomEdges.remove(i);
                return;
            }
        }
    }

    //sets new neighbor to r1
    //ahol r2 van oda r1-et állítunk (merge miatt)
    public void updateEdges(Room remaining, Room removed){ //mergenel hasznaljuk csak
        //r1 szoba marad
        //r2 torlodik (ahol szerepl, ki kell cserelni r1-re)
        for(EdgeBetweenRooms edgeBetweenRoom : roomEdges){
            if(edgeBetweenRoom.getNodeRooms().contains(removed)){
                edgeBetweenRoom.getNodeRooms().remove(removed);
                edgeBetweenRoom.getNodeRooms().add(remaining);
            }
        }
    }
    public void initDoors(Isten isten){
        //egyelore a door is wall csak az imageben kulonbozik es a colliderje solidra van allitva
        Random rand = new Random();
        for(EdgeBetweenRooms edge : roomEdges){
            int randomIndex = rand.nextInt(edge.getWalls().size());
            //ha ki akarjuk cserelni ténylegesen door objektumra én(Áron) megcsinalom, csak a switchWallToDoort kell atirni egy kicsit
            //szolj nekem, ha ez probléma, addig nem kell külön Door objektum
            edge.switchWallToDoor(edge.getWalls().get(randomIndex), isten);
        }
    }
    public ArrayList<EdgeBetweenRooms> getRoomEdges() {
        return roomEdges;
    }

}
