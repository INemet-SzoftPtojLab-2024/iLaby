package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.linalg.Vec2;

import java.nio.file.Files;
import java.util.ArrayList;

public class UnitRoom {

    private Vec2 position;
    private UnitRoomFrame top,left,right,down;
    private Item item;
    public Image image;
    private ArrayList<UnitRoom> adjacentUnitRooms;
    private Room  ownerRoom;
    private boolean inRoom;

    public UnitRoom(Vec2 pos) {
        this.position = pos;
        this.inRoom = false;
        adjacentUnitRooms = new ArrayList<>();
    }
    public void setImage(Image image) {
        this.image = image;
    }

    public void setOwnerRoom(Room ownerRoom){
        this.ownerRoom = ownerRoom;
        inRoom = true;
    }
    public boolean isInRoom(){
        return inRoom;
    }
    public ArrayList<UnitRoom> getAdjacentUnitRooms(){
        return adjacentUnitRooms;
    }

    public Room getOwnerRoom() {
        return ownerRoom;
    }
    public int getRowNum(){
        return (int)position.y/64;
    }
    public int getColNum(){
        return (int)position.x/64;
    }

    public Vec2 getPosition() {
        return position;
    }
}
