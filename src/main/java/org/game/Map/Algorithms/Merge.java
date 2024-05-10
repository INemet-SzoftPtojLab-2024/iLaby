package main.java.org.game.Map.Algorithms;

import main.java.org.game.Isten;
import main.java.org.game.Map.EdgeManager;
import main.java.org.game.Map.Map;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.UnitRoom;

public class Merge {


    public static void mergeRooms(Room r1, Room r2, Map map) {
        if(!r1.isPhysicallyAdjacent(r2) || r1.getID() == r2.getID()){
            //System.err.println("cant be merged");
            return;
        }
        //System.out.println(r2.getID() + "(r2) is merged to (r1)" + r1.getID());
        //remove r2 and keep r1;

        map.getEdgeManager().deleteEdge(r1,r2);
        map.getEdgeManager().updateEdgesAfterMerge(r1,r2);

        for(UnitRoom unitRoom : r2.getUnitRooms()){
            //r1.getUnitRooms().add(unitroom);
            unitRoom.setOwnerRoom(r1);

            //setting the new images of the deleted room
            //this method cares about the renderable items too
            //unitRoom.addRightImage(isten);
        }

        r1.getUnitRooms().addAll(r2.getUnitRooms()); //insted of this: r1.getUnitRooms().add(unitroom);

        //kiszedjuk a torolt szobat mindket szomszedossagi listabol
        r1.getPhysicallyAdjacentRooms().remove(r2);
        r1.getDoorAdjacentRooms().remove(r2);

        //vagy ez kell ide, vagy a feltetel, de igazabol mindegy, hadd maradjon  mind2,de a feltetel jobb,
        // mert a kikommentelt kodresz egyel lejjebb lehet errort dobna
        r2.getDoorAdjacentRooms().remove(r1);
        r2.getPhysicallyAdjacentRooms().remove(r1);
        //vegigiteralunk az r2 fizikalis szomszedossagi listajan
        for(Room adj : r2.getPhysicallyAdjacentRooms()){
            //ha r1 nem tartalmazza r2 fiz szomszedjat, akkor hozzaadjuk r1 listajahoz
            if(!r1.getPhysicallyAdjacentRooms().contains(adj) ){
                //System.out.println("adjroom added in r1: " + adj.getID());
                r1.getPhysicallyAdjacentRooms().add(adj);
            }
            //kivesszük magának r2 szomszédjának a fiz szomszédossági listájából r2-t
            adj.getPhysicallyAdjacentRooms().remove(r2);
            adj.getDoorAdjacentRooms().remove(r2);//!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //vegul ha r1 nem tartalmazzaa a fiz szomszedossagi listajaban r2 szomszedjat, akkor r1 szomszedjava tesszuk
            if(!adj.getPhysicallyAdjacentRooms().contains(r1)) {
                adj.getPhysicallyAdjacentRooms().add(r1);
            }
        }
        //ugyanezt megcsinaljuk az ajton keresztuli szomszedossagra is.
        for(Room physicallyAdjacentRoom: r1.getPhysicallyAdjacentRooms()){
            if(map.getEdgeManager().getEdgeBetweenRooms(r1,physicallyAdjacentRoom).hasDoor()){
                if(!r1.getDoorAdjacentRooms().contains(physicallyAdjacentRoom))
                {
                    r1.getDoorAdjacentRooms().add(physicallyAdjacentRoom);
                }
                if(!physicallyAdjacentRoom.getDoorAdjacentRooms().contains(r1))
                {
                    physicallyAdjacentRoom.getDoorAdjacentRooms().add(r1);
                }

            }
        }
        r1.setMaxPlayerCount(Math.max(r1.getMaxPlayerCount(),r2.getMaxPlayerCount()));
        map.getRooms().remove(r2);
    }

}
