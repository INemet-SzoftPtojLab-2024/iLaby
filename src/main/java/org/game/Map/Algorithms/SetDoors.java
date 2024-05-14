package main.java.org.game.Map.Algorithms;

import main.java.org.game.Isten;
import main.java.org.game.Map.EdgeBetweenRooms;
import main.java.org.game.Map.EdgePiece;
import main.java.org.game.Map.Map;
import main.java.org.game.Map.Room;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;
import java.util.Collections;

public class SetDoors {

    public static Vec2 TakeOutDoor(Isten isten, boolean oneWay, Map map){

        boolean alreadyOneWay = false;
        // ez a visszaaddolas miatt kell, kicsit bonyi, hogy miert, trust me
        boolean r2WasRemovedFromR1 = false;
        boolean r1WasRemovedFromR2 = false;
        Collections.shuffle(map.getEdgeManager().getRoomEdges());
        //mindig nagyon ugyanonnan fog maajd ajtot kivenni
        for(EdgeBetweenRooms chosenEdge : map.getEdgeManager().getRoomEdges()){

            //csak akkor veszek ki ajtót, ha mindkét szobának ami között van az edge van legalább 2 szomszédja
            Room r1 = chosenEdge.getNodeRooms().get(0);
            Room r2 = chosenEdge.getNodeRooms().get(1);
            Room start = null;
            Room end = null;
            //ha van ajtaja
            if(chosenEdge.hasDoor()) {
                //csak akkor, ha van legalabb 2 ajtaja mindkettonekj, egyebkent mindenkepp szar lenne
                if (r1.getDoorAdjacentRooms().size() >= 2 &&
                        (r2.getDoorAdjacentRooms().size() >= 2)) {
                    //megnezem, hogy ha egyiranyura akarom allitani, akkor mar egyiranyu-e alapbol, mert ha igen, akkor ki lesz veve
                    if( !r1.getDoorAdjacentRooms().contains(r2) || !r2.getDoorAdjacentRooms().contains(r1)) alreadyOneWay = true;
                    //ha nincs benne nem kell lekezelni, max falseot dob
                    if(r1.getDoorAdjacentRooms().remove(r2)){
                        start = r1;
                        end = r2;
                        //roomToCheckWithKruskal = r1;
                        r2WasRemovedFromR1 = true;
                    }
                    if(alreadyOneWay || !oneWay){
                        if(r2.getDoorAdjacentRooms().remove(r1)) {
                            start = r2;
                            end = r1;
                            //roomToCheckWithKruskal = r2;
                            r1WasRemovedFromR2 = true;
                        }
                    }
                    //megnézem, hogy osszefuggo lenne-e az uj graph
                    long startTimeMillis = System.currentTimeMillis();
                    if(isReachable(start,end)){
                    //if (map.kruskalForCheckingIfGraphIsCoherent(map.getRooms(),roomToCheckWithKruskal)) {
                    //if(map.isGraphKoherent(map.getRooms())){
                        System.out.println("isGraphKoherent: " + (System.currentTimeMillis() - startTimeMillis) + " ms");
                        EdgeBetweenRooms edgeBeingModified = map.getEdgeManager().getEdgeBetweenRooms(r1, r2);//ez pont a chosenEdge
                        ArrayList<EdgePiece> edgePieces = edgeBeingModified.getWalls();
                        for (EdgePiece edgePiece : edgePieces) {
                            if (edgePiece.isDoor()) {

                                //ha ezek egyike igaz, akkor szedem csak ki, és csak ilyenkor returneolok positiont -1, -1. en kivul
                                if(alreadyOneWay || !oneWay){
                                    edgeBeingModified.switchDoorToWall(edgePiece, isten);
                                    //System.out.println("kiszedek egy ajtot");
                                    return edgePiece.getPosition();
                                }
                                //mert nem kell allitani a map kirajzolasan, az adjacencylistet nem kell updatelni (remelem)
                                //System.out.println("egyiranyura allitom az ajtot");
                                return new Vec2(-1,-1);
                            }
                        }
                    }
                    else
                    {
                        //System.out.println("Nem lett volna koherens");
                        //igy a legegyszerubb talan
                        if( r2WasRemovedFromR1) r1.getDoorAdjacentRooms().add(r2);
                        if(r1WasRemovedFromR2)r2.getDoorAdjacentRooms().add(r1);
                    }
                }
                else {
                    //System.out.println("nem volt eleg ajto");
                }
            }else {
                //System.out.println("nincs ajto itt");
            }
        }
        return new Vec2(-1,-1);
    }

    //fv ami az ajtok hozzaadasat valositja meg
    public static Vec2 addDoorToEdgeWithoutDoor(Isten isten, Map map){
        //vegigiteralok az eleken
        Collections.shuffle(map.getEdgeManager().getRoomEdges());
        for(EdgeBetweenRooms chosenEdge: map.getEdgeManager().getRoomEdges()){
            //csak olyan el erdekel, amin nincs ajto, ergo a ket szoba nem atjarhato
            if(!chosenEdge.hasDoor()){
                Room r1 = chosenEdge.getNodeRooms().get(0);
                Room r2 = chosenEdge.getNodeRooms().get(1);
                //vegigmegyek a falakon, mert elkepzelheto, hogy van olyan, amire nem illesztheto aajto
                for(EdgePiece chosenPiece: chosenEdge.getWalls()){
                    //ha tudok raa ajtot illeszteni, aakkor ezt megteszem
                    if(chosenEdge.switchWallToDoor(chosenPiece,isten))
                    {
                        //frissitem a szomszedossagi listakat
                        r1.getDoorAdjacentRooms().add(r2);
                        r2.getDoorAdjacentRooms().add(r1);
                        //System.out.println("Ajto hozzaadva");
                        return chosenPiece.getPosition();
                    }
                }
            }
        }
        //ha nem tudtam ajtot hozzaadni, akkor teli a map
        //System.out.println("Teli a map");
        return new Vec2(-1,-1);
    }
    public static boolean isReachable(Room start, Room end) {
        ArrayList<Room> visited = new ArrayList<>();
        dfs(start, visited);
        return visited.contains(end);
    }
    private static void dfs(Room vertex, ArrayList<Room> visited) {
        if (visited.contains(vertex))
            return;

        visited.add(vertex);
        ArrayList<Room>neighbors = vertex.getDoorAdjacentRooms();
        for (Room neighbor : neighbors) {
            dfs(neighbor, visited);
        }
    }
}
