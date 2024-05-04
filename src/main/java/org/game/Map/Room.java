package main.java.org.game.Map;

import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;
import java.util.Comparator;


public class Room extends Updatable implements Graph<Room>{
    private int ID;
    private ArrayList<UnitRoom> unitRooms;
    private ArrayList<Room> physicallyAdjacentRooms;
    private ArrayList<Room> doorAdjacentRooms;
    private int maxPlayerCount = 5;
    int playerCount;

    //ArrayList<Player> players;
    boolean discovered = false;

    RoomType roomType;

    //this is maybe usefull, but not yet
    //private ArrayList<Integer> hasDoorWith;


    public Room(int ID){
        this.ID = ID;
        unitRooms = new ArrayList<>();
        physicallyAdjacentRooms = new ArrayList<>();
        doorAdjacentRooms = new ArrayList<>();

        //hasDoorWith = new ArrayList<>();
        roomType = RoomType.getRandomRoomtype(false);
    }
    public void setRoomType(boolean startRoom) {
        roomType =  RoomType.getRandomRoomtype(startRoom);
    }
    public  Room(){}

    @Override
    public void onStart(Isten isten) {

    }


    @Override
    public void onUpdate(Isten isten, double deltaTime) {

    }

    @Override
    public void onDestroy() {

    }
    public void setDoorAdjacentRooms(ArrayList<Room> rooms){
        doorAdjacentRooms =new ArrayList<>(rooms);
    }

    //ez az ajton keresztuli
    public boolean isAdjacent(Room room) {
        // if(this.equals(room)) return false; egynelore nem kell lekezelni
        for (Room checkRoom : doorAdjacentRooms) {
            if (room.equals(checkRoom)) return true;
        }
        return  false;
    }
    //isAdjacent átnevezve, mert az isAdjacentre szukseg van a generikus implementacio miatt a kruskall algoban
    public boolean isPhysicallyAdjacent(Room room) {
        // if(this.equals(room)) return false; egynelore nem kell lekezelni
        for(Room checkRoom : physicallyAdjacentRooms) {
            if (room.equals(checkRoom)) return true;
        }
        return  false;
    }
    public void setPhysicallyAdjacentRooms(){
        physicallyAdjacentRooms.clear();
        for(UnitRoom unitRoom : unitRooms){
            for(UnitRoom neighbourUnitRoom: unitRoom.getAdjacentUnitRooms()){
                if(!neighbourUnitRoom.getOwnerRoom().getPhysicallyAdjacentRooms().contains(this) && !neighbourUnitRoom.getOwnerRoom().equals(this)){
                    physicallyAdjacentRooms.add(neighbourUnitRoom.getOwnerRoom());
                    neighbourUnitRoom.getOwnerRoom().getPhysicallyAdjacentRooms().add(this);
                }
            }
        }
    }

    public ArrayList<Integer> getMinMaxRowColValues(){
        ArrayList<Integer> minMaxRowColValues = new ArrayList<>();
        int maxColNum, maxRowNum, minColNum, minRowNum;
        maxColNum = minColNum = unitRooms.get(0).getColNum();
        maxRowNum = minRowNum = unitRooms.get(0).getRowNum();
        for(UnitRoom unitRoom : unitRooms){
            if(maxColNum < unitRoom.getColNum()) maxColNum = unitRoom.getColNum();
            if(minColNum > unitRoom.getColNum()) minColNum = unitRoom.getColNum();
            if(maxRowNum < unitRoom.getRowNum()) maxRowNum = unitRoom.getRowNum();
            if(minRowNum > unitRoom.getRowNum()) minRowNum = unitRoom.getRowNum();
        }
        minMaxRowColValues.add(maxColNum);
        minMaxRowColValues.add(minColNum);
        minMaxRowColValues.add(maxRowNum);
        minMaxRowColValues.add(minRowNum);
        return minMaxRowColValues;
    }
    //function hogy megtalaljam azon UnitRoomokat, amik egy adott szamu soraban vannak a szobanaka alulrol nezve, amit a distance hataroz meg
    // splitRooms func-on belül használva
    public ArrayList<UnitRoom> getUnitRoomsWithXDistanceFromLowestRowIdxInOrderByColumn(int lowestRowIdx, int distance) {
        ArrayList<UnitRoom> ret = new ArrayList<>();
        for(UnitRoom unitRoom: unitRooms){
            if(unitRoom.getRowNum()==lowestRowIdx+distance){
                ret.add(unitRoom);
            }
        }
        ret.sort(Comparator.comparing(UnitRoom::getColNum));
        return ret;
    }
    public ArrayList<UnitRoom> getUnitRoomsWithXDistanceFromLowestColumnIdxInOrderByRow(int lowestColIdx, int distance) {
        ArrayList<UnitRoom> ret = new ArrayList<>();
        for(UnitRoom unitRoom: unitRooms){
            if(unitRoom.getColNum()==lowestColIdx+distance){
                ret.add(unitRoom);
            }
        }
        ret.sort(Comparator.comparing(UnitRoom::getRowNum));
        return ret;
    }
    public ArrayList<Room> getDoorAdjacentRooms() {
        return doorAdjacentRooms;
    }

    public ArrayList<UnitRoom> getUnitRooms() {
        return unitRooms;
    }
    public ArrayList<Room> getPhysicallyAdjacentRooms() {
        return physicallyAdjacentRooms;
    }

    public int getID() {
        return ID;
    }

    public int getMaxPlayerCount() {return maxPlayerCount;}

    public void setMaxPlayerCount(int maxPlayerCount) {this.maxPlayerCount = maxPlayerCount;}
    public RoomType getRoomType() {return roomType;}

    public void setRoomType(RoomType type) {
        this.roomType = type;
    }
    public boolean isUnitRoomInSameRoomAsStartRoom(Vec2 position)
    {
        boolean isUnitRoomInRoom=false;
        boolean isStartUnitRoomInRoom =false;
        for(UnitRoom unitRoom1 : unitRooms)
        {
            if(unitRoom1.getPosition().x == position.x && unitRoom1.getPosition().y== position.y) {
                isUnitRoomInRoom = true;
            }
            if (unitRoom1.getPosition().x == 0.0 && unitRoom1.getPosition().y == 0.0) {
                isStartUnitRoomInRoom = true;
            }
        }
        return isStartUnitRoomInRoom && isUnitRoomInRoom;
    }
}
