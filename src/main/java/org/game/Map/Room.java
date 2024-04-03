package main.java.org.game.Map;

import lombok.Setter;
import lombok.Getter;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;

public class Room extends Updatable implements Graph<Room>{
    int ID;
    int maxDoorCount = 2;
    int currDoorCount = 0;
    private ArrayList<UnitRoom> unitRooms;
    private ArrayList<Room> adjacentRooms;
    int playerCount;
    private int maxPlayerCount = 5;
    private int mapRowSize;
    private int mapColSize;


    //ArrayList<Player> players;
    boolean discovered = false;

    RoomType roomType;

    //for generating the doors
    private ArrayList<Integer> hasDoorWith;

    ColliderGroup roomColliders;

    public Room(int ID, int mapRowSize, int mapColSize){
        this.ID = ID;
        unitRooms = new ArrayList<>();
        adjacentRooms = new ArrayList<>();
        roomColliders=new ColliderGroup();
        hasDoorWith = new ArrayList<>();
        roomType = RoomType.getRandomRoomtype();
        this.mapRowSize = mapRowSize;
        this.mapColSize = mapColSize;

    }

    @Override
    public void onStart(Isten isten) {

        isten.getPhysicsEngine().addColliderGroup(roomColliders);
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {

    }

    @Override
    public void onDestroy() {

    }

    public boolean isAdjacent(Room room) {
        // if(this.equals(room)) return false; egynelore nem kell lekezelni
        for(Room checkRoom : adjacentRooms) {
            if (room.equals(checkRoom)) return true;
        }
        return  false;
    }
    public void setAdjacentRooms(){
        adjacentRooms.clear();
        for(UnitRoom unitRoom : unitRooms){
            for(UnitRoom neighbourUnitRoom: unitRoom.getAdjacentUnitRooms()){
                if(!neighbourUnitRoom.getOwnerRoom().getAdjacentRooms().contains(this) && !neighbourUnitRoom.getOwnerRoom().equals(this)){
                    adjacentRooms.add(neighbourUnitRoom.getOwnerRoom());
                    neighbourUnitRoom.getOwnerRoom().getAdjacentRooms().add(this);
                }
            }
        }
    }
    public void deleteCommonWallsWith(Isten isten, int deletedRoomID){
        for(UnitRoom unitRoom : unitRooms){
            for(UnitRoom neigbourUnitRoom : unitRoom.getAdjacentUnitRooms()){
                //hogyha egy szobán beluli unitRoomokrol van szo
                // es van koztuk collider vagy fal
                if(neigbourUnitRoom.getOwnerRoom().getID() == deletedRoomID){
                    System.out.println(unitRoom.getColNum() + "," + unitRoom.getRowNum());
                    if(unitRoom.getTopWall() != null && unitRoom.getRowNum() != mapRowSize -1){
                        roomColliders.removeCollider(unitRoom.getTopWall().getCollider());
                        unitRoom.setTopIsDoor(false);
                        isten.getRenderer().deleteRenderable(unitRoom.getTopWall().image);
                        unitRoom.setTopWall(null);
                    }
                    if(unitRoom.getLeftWall() != null && unitRoom.getColNum() != 0){
                        roomColliders.removeCollider(unitRoom.getLeftWall().getCollider());
                        unitRoom.setLeftIsDoor(false);
                        isten.getRenderer().deleteRenderable(unitRoom.getLeftWall().image);
                        unitRoom.setLeftWall(null);
                    }
                    if(unitRoom.getRightWall() != null && unitRoom.getColNum() != mapColSize-1){
                        roomColliders.removeCollider(unitRoom.getRightWall().getCollider());
                        unitRoom.setRightIsDoor(false);
                        isten.getRenderer().deleteRenderable(unitRoom.getRightWall().image);
                        unitRoom.setRightWall(null);
                    }
                    if(unitRoom.getBottomWall() != null && unitRoom.getRowNum() != 0){
                        roomColliders.removeCollider(unitRoom.getBottomWall().getCollider());
                        unitRoom.setBottomIsDoor(false);
                        isten.getRenderer().deleteRenderable(unitRoom.getBottomWall().image);
                        unitRoom.setBottomWall(null);
                    }

                    //delete the neigbours wall and collider
                    if(neigbourUnitRoom.getTopWall() != null && unitRoom.getRowNum() != mapRowSize-1){
                        roomColliders.removeCollider(neigbourUnitRoom.getTopWall().getCollider());
                        neigbourUnitRoom.setTopIsDoor(false);
                        isten.getRenderer().deleteRenderable(neigbourUnitRoom.getTopWall().image);
                        neigbourUnitRoom.setTopWall(null);
                    }
                    if(neigbourUnitRoom.getLeftWall() != null  && unitRoom.getColNum() != 0) {
                        roomColliders.removeCollider(neigbourUnitRoom.getLeftWall().getCollider());
                        neigbourUnitRoom.setLeftIsDoor(false);
                        isten.getRenderer().deleteRenderable(neigbourUnitRoom.getLeftWall().image);
                        neigbourUnitRoom.setLeftWall(null);
                    }
                    if(neigbourUnitRoom.getRightWall() != null  && unitRoom.getColNum() != mapColSize-1){
                        roomColliders.removeCollider(neigbourUnitRoom.getRightWall().getCollider());
                        neigbourUnitRoom.setRightIsDoor(false);
                        isten.getRenderer().deleteRenderable(neigbourUnitRoom.getRightWall().image);
                        neigbourUnitRoom.setRightWall(null);
                    }
                    if(neigbourUnitRoom.getBottomWall() != null && unitRoom.getRowNum() != 0){
                        roomColliders.removeCollider(neigbourUnitRoom.getBottomWall().getCollider());
                        neigbourUnitRoom.setBottomIsDoor(false);
                        isten.getRenderer().deleteRenderable(neigbourUnitRoom.getBottomWall().image);
                        neigbourUnitRoom.setBottomWall(null);
                    }
                }
            }
        }
    }


    public ArrayList<UnitRoom> getUnitRooms() {
        return unitRooms;
    }
    public ArrayList<Room> getAdjacentRooms() {
        return adjacentRooms;
    }

    public int getID() {
        return ID;
    }

    public void addCollider(Collider c){roomColliders.addCollider(c);}

    public void addHasDoorWith(int i) {
        hasDoorWith.add(i);
    }

    public boolean hasDoorWith(int i) {
        return hasDoorWith.contains(i);
    }

    public boolean hasDoor() {
        return (!hasDoorWith.isEmpty());
    }
    public int getMaxPlayerCount() {return maxPlayerCount;}

    public void setMaxPlayerCount(int maxPlayerCount) {this.maxPlayerCount = maxPlayerCount;}
    public RoomType getRoomType() {return roomType;}



}
