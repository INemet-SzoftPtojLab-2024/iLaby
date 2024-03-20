package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.awt.*;
import java.sql.SQLOutput;
import java.util.*;


public class Map extends Updatable {
    private Mapgenerator mapgenerator;
    ArrayList<Room> rooms;
    private UnitRoom[][] unitRooms;
    private int mapRowSize;
    private int mapColumnSize;

    @Override
    public void onStart(Isten isten) {
        mapgenerator = new Mapgenerator(rooms,unitRooms, mapRowSize, mapColumnSize, isten);
        mapgenerator.generate();
        unitRooms = mapgenerator.getUnitRooms();
        rooms = mapgenerator.getRooms();
    }

    public Map(int rowNumber, int columnNumber){
        this.mapRowSize = rowNumber;
        this.mapColumnSize = columnNumber;
        //unitrooms is set in the generator --> onstart
        this.rooms = new ArrayList<>();

    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {

    }

    @Override
    public void onDestroy() {

    }

    private boolean splitRooms(Room r1)
    {
        // removeoljuk a szomszedos roomok szomszedossagi listaibol a szobat, es a func vegen hozzaadjuk a ket szetvalasztott szoba egyiket/mindekettot
        for(Room neighbourRoom : r1.getAdjacentRooms()){
            neighbourRoom.getAdjacentRooms().remove(r1);
        }
        //egyenlőre minden szoba ami splittel lesz createlve ilyen type-val rendelkezik
        Room newRoom = new Room(999);
        int lowestRowIdx = getRoomWithLowestRowIdx(r1);
        ArrayList<UnitRoom> addableUnitRooms = new ArrayList<>();
        int distance = 0;
        ArrayList<UnitRoom> roomsWithDistanceXFromLowestRow;
        //addig, amíg az új szoba a méret fele nem lesz
        while(addableUnitRooms.size()<r1.getUnitRooms().size()/2){
            roomsWithDistanceXFromLowestRow=getUnitRoomsWithXDistanceFromLowestRowIdxInOrderByColumn(r1,lowestRowIdx,distance++); //tavolsag novelese, es igy soronkent egyesevel balrol jobbra az osszes unitroom hozzaadasa, amig kell
            for(UnitRoom addableUnitRoom:roomsWithDistanceXFromLowestRow){
                if(addableUnitRooms.size()<r1.getUnitRooms().size()/2 ) //&& addableUnitRooms.contains(addableUnitRoom) lehet kell
                {
                    addableUnitRooms.add(addableUnitRoom);
                }

            }
        }
        ArrayList<UnitRoom> oldRoomWithoutNewRoom = getDifference(r1.getUnitRooms(),addableUnitRooms);
        //ellenorzom, hogy osszefuggoek lennének-e: ha igen:
        if( wouldNewRoomBeCoherent(oldRoomWithoutNewRoom) && wouldNewRoomBeCoherent(addableUnitRooms)) {
            for (UnitRoom addUnitRoomToNewRoom : addableUnitRooms) {
                addUnitRoomToNewRoom.getOwnerRoom().getUnitRooms().remove(addUnitRoomToNewRoom);
                addUnitRoomToNewRoom.setOwnerRoom(newRoom);
                newRoom.getUnitRooms().add(addUnitRoomToNewRoom);
            }
            rooms.add(newRoom);
            newRoom.setAdjacentRooms();
            r1.setAdjacentRooms();
            return true;
        }
        return false;
    }


    public static ArrayList<UnitRoom> getDifference(ArrayList<UnitRoom> u1, ArrayList<UnitRoom> u2)
    {
        ArrayList<UnitRoom> difference = new ArrayList<>();
        for (UnitRoom element : u1) {
            if (!u2.contains(element)) {
                difference.add(element);
            }
        }
        return difference;
    }
    //nem biztos hogy így a legjobb
    private boolean wouldNewRoomBeCoherent(ArrayList<UnitRoom> newRoomUnits)
    {
        UnitRoom starterRoom = newRoomUnits.get(0);
        ArrayList<UnitRoom> coherentGraph = new ArrayList<>();
        coherentGraph.add(starterRoom);
        for(int i = 0;i<newRoomUnits.size();i++){
            for(UnitRoom unitRoomToBeAddedToGraph : newRoomUnits){
                 /*ha a size i-vel egyenlő, vagy kisebb nála, akkor tudjuk, hogy nem alkotnak összefüggő gráfot a UnitRoomok a Roomban,
                    mert különben az előző körhöz képest legalább 1-et fel kellett volna tuidjak venni, vagy pedig már előtte többet kellett volna tudjak felvenni,
                    rekurzív gondolat, mukodik (remelem)*/
                if(coherentGraph.size()> i)
                {
                    if (!unitRoomToBeAddedToGraph.equals(coherentGraph.get(i))
                            && !coherentGraph.contains(unitRoomToBeAddedToGraph)
                            && unitRoomToBeAddedToGraph.isAdjacent(coherentGraph.get(i)))
                    {
                        coherentGraph.add(unitRoomToBeAddedToGraph);
                        //break; ezzel valszeg effektivebb
                    }
                }
                else{
                    System.out.println("Nem lennenek koherensek a szobak");
                    return false;
                }
            }
        }
        //mivel minden indexen vegig tudtunk menni ezert tudunk truet returnolni, azert biztonsag kedveert meg egy kontrollt bennhagyok
        if(coherentGraph.size() == newRoomUnits.size()) return true;
            //hogyha nem egyenlok akkor false menjen ki, bar egyenloknek kene lenniuk
        else return false;
    }

    //function hogy megtalaljam a legkisebb sorindexet a tombben, viszonyitasi parameter lesz.
    // splitRooms func-on belül használva
    private int getRoomWithLowestRowIdx(Room r1) {
        UnitRoom min = unitRooms[mapRowSize-1][mapColumnSize-1];
        for(UnitRoom unitRoom : r1.getUnitRooms())
        {
            if(unitRoom.getRowNum()<min.getRowNum())
            {
                min = unitRoom;
            }
        }
        return min.getRowNum();
    }
    //function hogy megtalaljam azon UnitRoomokat, amik egy adott szamu soraban vannak a szobanaka alulrol nezve, amit a distance hataroz meg
    // splitRooms func-on belül használva
    private ArrayList<UnitRoom> getUnitRoomsWithXDistanceFromLowestRowIdxInOrderByColumn(Room r1, int lowestRowIdx, int distance) {
        ArrayList<UnitRoom> ret = new ArrayList<>();
        for(UnitRoom unitRoom: r1.getUnitRooms()){
            if(unitRoom.getRowNum()==lowestRowIdx+distance){
                ret.add(unitRoom);
            }
        }
        ret.sort(Comparator.comparing(UnitRoom::getColNum));
        return ret;
    }

    //ez a fv a mapgenerátorban is hasonlóan szerepel
    private void mergeRooms(Room r1, Room r2) {
        if(!r1.isAdjacent(r2) || r1.getID() == r2.getID()){
            System.err.println("cant be merged");
            return;
        }
        //remove r2 and keep r1
        for(UnitRoom unitroom : r2.getUnitRooms()){
            r1.getUnitRooms().add(unitroom);
            unitroom.setOwnerRoom(r1);
            //image update!!
            //update the Walls!!

        }
        r2.getUnitRooms().clear();

        r1.getAdjacentRooms().remove(r2);
        r2.getAdjacentRooms().remove(r1);
        for(Room adj : r2.getAdjacentRooms()){
            if(!r1.getAdjacentRooms().contains(adj) && !adj.equals(r1)){
                //System.out.println("adjroom added in r1: " + adj.getID());
                r1.getAdjacentRooms().add(adj);
            }
            adj.getAdjacentRooms().remove(r2);
            if(!adj.getAdjacentRooms().contains(r1)) {
                adj.getAdjacentRooms().add(r1);
            }
        }

        r2.getAdjacentRooms().clear();
        //r1.setDiscovered(r2.isDiscovered());
        //r1.setPlayerCount(r1.getPlayerCount() + r2.getPlayerCount());
        //r1.setRoomType(r2.getRoomType());
        r1.setMaxPlayerCount(r1.getMaxPlayerCount() + r2.getMaxPlayerCount());
        rooms.remove(r2);
    }




    //merge the rooms until every room has minimumm size of the given number
    private void printMap(){
        for(int i = 0;i < mapRowSize;i++){ //test
            for(int j = 0; j< mapColumnSize;j++){
                if(unitRooms[i][j].getOwnerRoom().getID() < 10) {
                    //System.out.print(unitRooms[i][j].getOwnerRoom().getID() + "  ");
                }
                else {
                    //System.out.print(unitRooms[i][j].getOwnerRoom().getID() + " ");
                }
            }
            //System.out.println();
        }
    }



    public void setRooms(ArrayList<Room> rooms) {this.rooms = rooms;}

    public void setUnitRooms(UnitRoom[][] unitRooms) {
        this.unitRooms = unitRooms;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public UnitRoom[][] getUnitRooms() {
        return unitRooms;
    }
}
