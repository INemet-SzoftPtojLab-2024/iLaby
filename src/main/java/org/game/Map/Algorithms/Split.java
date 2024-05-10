package main.java.org.game.Map.Algorithms;

import main.java.org.game.Isten;
import main.java.org.game.Map.Map;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.UnitRoom;

import java.util.ArrayList;

public class Split {
    public static int splitRooms(Room r1, Map map)
    {
        if(r1.getUnitRooms().size() < map.getMinRoomSize()) return -1;
        //egyenlőre minden szoba ami splittel lesz createlve ilyen type-val rendelkezik
        int newID = generateNewRoomID(map);
        Room newRoom = new Room(newID,r1.getMaxPlayerCount());
        ArrayList<UnitRoom> addableUnitRooms = new ArrayList<>();
        int distance = 0;
        ArrayList<Integer> minMaxRowColValues = r1.getMinMaxRowColValues();
        int highestColIdx = minMaxRowColValues.get(0);
        int lowestColIdx = minMaxRowColValues.get(1);
        int highestRowIdx = minMaxRowColValues.get(2);
        int lowestRowIdx = minMaxRowColValues.get(3);
        //ha a szoba inkább fuggolegesen nagy, akkor a sorokat figyeljuk
        if((highestRowIdx - lowestRowIdx) > (highestColIdx - lowestColIdx)) {
            //int lowestRowIdx = getRoomWithLowestRowIdx(r1);
            ArrayList<UnitRoom> UnitRoomsWithDistanceXFromLowestRow;
            //addig, amíg az új szoba a méret fele nem lesz
            while (addableUnitRooms.size() < r1.getUnitRooms().size() / 2) {
                UnitRoomsWithDistanceXFromLowestRow = r1.getUnitRoomsWithXDistanceFromLowestRowIdxInOrderByColumn(lowestRowIdx, distance++); //tavolsag novelese, es igy soronkent egyesevel balrol jobbra az osszes unitroom hozzaadasa, amig kell
                for (UnitRoom addableUnitRoom : UnitRoomsWithDistanceXFromLowestRow) {
                    if (addableUnitRooms.size() < r1.getUnitRooms().size() / 2) {
                        addableUnitRooms.add(addableUnitRoom);
                    }

                }
            }
        }else{
            ArrayList<UnitRoom> UnitRoomsWithDistanceXFromLowestColumn;
            while (addableUnitRooms.size() < r1.getUnitRooms().size() / 2) {
                UnitRoomsWithDistanceXFromLowestColumn = r1.getUnitRoomsWithXDistanceFromLowestColumnIdxInOrderByRow(lowestColIdx, distance++); //tavolsag novelese, es igy soronkent egyesevel balrol jobbra az osszes unitroom hozzaadasa, amig kell
                for (UnitRoom addableUnitRoom : UnitRoomsWithDistanceXFromLowestColumn) {
                    if (addableUnitRooms.size() < r1.getUnitRooms().size() / 2) {
                        addableUnitRooms.add(addableUnitRoom);
                    }

                }
            }
        }
        // a fenti ket ag helyett meg lehetne csinalni a max ertekekkel is, ha nem lesz coherens az eredmeny
        ArrayList<UnitRoom> oldRoomWithoutNewRoom = getDifference(r1.getUnitRooms(),addableUnitRooms);
        //ellenorzom, hogy osszefuggoek lennének-e: ha igen:
        //id mindegy micsoda, itt igazabol nem hasznalom
        if( map.kruskalForCheckingIfGraphIsCoherent(oldRoomWithoutNewRoom) && map.kruskalForCheckingIfGraphIsCoherent(addableUnitRooms)) {
            // removeoljuk a szomszedos roomok szomszedossagi listaibol a szobat fizikalisan, es a func vegen hozzaadjuk a ket szetvalasztott szoba egyiket/mindekettot
            for(Room physicalNeighbourRoom : r1.getPhysicallyAdjacentRooms()){
                physicalNeighbourRoom.getPhysicallyAdjacentRooms().remove(r1);
            }
            //itt pedig az ajtos szomszeddossagi listabol is removeoljuk
            for(Room byDoorNeighbourRoom: r1.getDoorAdjacentRooms()){
                byDoorNeighbourRoom.getDoorAdjacentRooms().remove(r1);
            }
            for (UnitRoom addUnitRoomToNewRoom : addableUnitRooms) {
                //kivesszük az előző szobából a  unitroomot
                r1.getUnitRooms().remove(addUnitRoomToNewRoom);
                // addUnitRoomToNewRoom.getOwnerRoom().getUnitRooms().remove(addUnitRoomToNewRoom);
                addUnitRoomToNewRoom.setOwnerRoom(newRoom);
                //hozzáadjuk az új szobához a unitroomot
                newRoom.getUnitRooms().add(addUnitRoomToNewRoom);
            }
            map.getRooms().add(newRoom);
            newRoom.setPhysicallyAdjacentRooms();
            newRoom.getPhysicallyAdjacentRooms().remove(r1);
            r1.setPhysicallyAdjacentRooms();
            map.getEdgeManager().updateEdgesAfterSplit(r1, newRoom);


            newRoom.setByDoorAdjacentRooms(map);
            //hogy csak egyszer addoljuk hozza, de csak ket iranyu ajtoknal van igy
            newRoom.getDoorAdjacentRooms().remove(r1);
            r1.setByDoorAdjacentRooms(map);
            return newID;
        }
        return -1;
    }
    private static int generateNewRoomID(Map map){
        int newID = 0;
        while(true){
            int roomCnt = 0;
            for(Room room : map.getRooms()){
                if(room.getID() == newID){
                    newID++;
                    break; //not found, try the next ID
                }
                roomCnt++;
            }
            if(roomCnt == map.getRooms().size()){
                return newID;
            }


        }
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

}
