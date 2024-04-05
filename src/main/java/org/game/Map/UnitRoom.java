package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;

public class UnitRoom implements Graph<UnitRoom>{
//sorting layer: 40
    private Vec2 position;

    private Wall topWall = null, leftWall = null, rightWall = null, bottomWall = null;

    private Item item;
    public Image image = null;
    private ArrayList<UnitRoom> adjacentUnitRooms;
    private UnitRoom TopNeighbor = null;
    private UnitRoom BottomNeighbor = null;
    private UnitRoom LeftNeighbor = null;
    private UnitRoom RightNeighbor = null;
    private Room  ownerRoom;
    //this stores information only for generating
    private boolean inRoom;
/*
lehet jol jon majd
    private boolean topIsWall = false, bottomIsWall = false, leftIsWall = false, rightIsWall = false;
    private boolean topIsDoor = false, bottomIsDoor = false, leftIsDoor = false, rightIsDoor = false;

 */

    public UnitRoom(Vec2 pos) {
        this.position = pos;
        this.inRoom = false;
        adjacentUnitRooms = new ArrayList<>();
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
        return (int)position.y;
    }
    public int getColNum(){
        return (int)position.x;
    }

    public Vec2 getPosition() {
        return position;
    }

    public boolean isAdjacent(UnitRoom unitRoom){
        for (UnitRoom adjacentUnitRoom :this.getAdjacentUnitRooms()){
            if(adjacentUnitRoom.equals(unitRoom)){
                return true;
            }

        }
        return false;
    }
    public void setNewImage(String imgPath, Isten isten) {
        Image newImage = new Image(position, new Vec2(1,1), imgPath);
        //ha ki akajuk cserélni a képet akkor ki kell venni  a renderablek kozol
        if(image != null){
            isten.getRenderer().deleteRenderable(image);
        }
        this.image = newImage;
        //this is under a lot of things
        newImage.setSortingLayer(40);
        isten.getRenderer().addRenderable(newImage);
    }
    public void addRightImage(Isten isten){

        int j;
        //TODO EVIKE
        switch (ownerRoom.roomType){
            case GAS -> j = 1;
            case SHADOW -> j = 2;
            case CURSED -> j = 3;
            case BASIC -> j = 4;
            default -> j = 0;
        }
        if(ownerRoom.getID() == 999) j = 5;
        String path = "./assets/rooms/" + j + ".png";
        setNewImage(path, isten);
    }

/*

    public boolean isTopIsDoor() {
        return topIsDoor;
    }

    public void setTopIsDoor(boolean topIsDoor) {
        this.topIsDoor = topIsDoor;
    }

    public boolean isBottomIsDoor() {
        return bottomIsDoor;
    }

    public void setBottomIsDoor(boolean bottomIsDoor) {
        this.bottomIsDoor = bottomIsDoor;
    }

    public boolean isLeftIsDoor() {
        return leftIsDoor;
    }

    public void setLeftIsDoor(boolean leftIsDoor) {
        this.leftIsDoor = leftIsDoor;
    }

    public boolean isRightIsDoor() {
        return rightIsDoor;
    }

    public void setRightIsDoor(boolean rightIsDoor) {
        this.rightIsDoor = rightIsDoor;
    }
    */
    public Wall getTopWall() {return topWall;}

    public Wall getLeftWall() {return leftWall;}

    public Wall getRightWall() {return rightWall;}

    public Wall getBottomWall() {return bottomWall;}

    public void setTopWall(Wall topWall) {this.topWall = topWall;}

    public void setLeftWall(Wall leftWall) {this.leftWall = leftWall;}

    public void setRightWall(Wall rightWall) {this.rightWall = rightWall;}

    public void setBottomWall(Wall bottomWall) {this.bottomWall = bottomWall;}

    public void setTopNeighbor(UnitRoom topNeighbor) {
        TopNeighbor = topNeighbor;
    }

    public void setBottomNeighbor(UnitRoom bottomNeighbor) {
        BottomNeighbor = bottomNeighbor;
    }

    public void setLeftNeighbor(UnitRoom leftNeighbor) {
        LeftNeighbor = leftNeighbor;
    }

    public void setRightNeighbor(UnitRoom rightNeighbor) {
        RightNeighbor = rightNeighbor;
    }

    public UnitRoom getTopNeighbor() {
        return TopNeighbor;
    }

    public UnitRoom getBottomNeighbor() {
        return BottomNeighbor;
    }

    public UnitRoom getLeftNeighbor() {
        return LeftNeighbor;
    }

    public UnitRoom getRightNeighbor() {
        return RightNeighbor;
    }
}
