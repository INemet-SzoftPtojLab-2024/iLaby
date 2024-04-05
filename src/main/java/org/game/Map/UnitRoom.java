package main.java.org.game.Map;

import lombok.Getter;
import lombok.Setter;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;

public class UnitRoom implements Graph<UnitRoom>{

    private Vec2 position;

    private Wall topWall = null, leftWall = null, rightWall = null, bottomWall = null;

    private Item item;
    public Image image;
    private ArrayList<UnitRoom> adjacentUnitRooms;
    private UnitRoom TopNeigbour = null;
    private UnitRoom BottomNeigbour = null;
    private UnitRoom LeftNeigbour = null;
    private UnitRoom RightNeigbour = null;
    private Room  ownerRoom;
    //this stores information only for generating
    private boolean inRoom;

    private boolean topIsDoor = false, bottomIsDoor = false, leftIsDoor = false, rightIsDoor = false;

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
        return (int)position.y;
    }
    public int getColNum(){
        return (int)position.x;
    }

    public Vec2 getPosition() {
        return position;
    }


    public void createWalls(Isten isten) {
        boolean hasTop = false, hasBottom = false, hasLeft = false, hasRight = false;
        for(UnitRoom neighbour: adjacentUnitRooms) {
            if(neighbour.getOwnerRoom().getID() != ownerRoom.getID()) {
                if(!ownerRoom.hasDoorWith(neighbour.getOwnerRoom().getID())
                        && !topIsDoor
                        && !bottomIsDoor
                        && !leftIsDoor
                        && !rightIsDoor
                        && !neighbour.isTopIsDoor()
                        && !neighbour.isBottomIsDoor()
                        && !neighbour.isLeftIsDoor()
                        && !neighbour.isRightIsDoor()
                ) {
                    if(neighbour.getPosition().x < position.x) {
                        leftIsDoor = true;
                        neighbour.setRightIsDoor(true);
                    }
                    else if(neighbour.getPosition().x > position.x) {
                        rightIsDoor = true;
                        neighbour.setLeftIsDoor(true);
                    }
                    else if(neighbour.getPosition().y > position.y) {
                        topIsDoor = true;
                        neighbour.setBottomIsDoor(true);
                    }
                    else if(neighbour.getPosition().y < position.y) {
                        bottomIsDoor = true;
                        neighbour.setTopIsDoor(true);
                    }
                    ownerRoom.addHasDoorWith(neighbour.getOwnerRoom().getID());
                    neighbour.getOwnerRoom().addHasDoorWith(ownerRoom.getID());
                    ownerRoom.currDoorCount++;
                    neighbour.getOwnerRoom().currDoorCount++;
                }
                continue;
            }

            if(neighbour.getPosition().x < position.x) {
                hasLeft = true;
            }
            if(neighbour.getPosition().x > position.x) {
                hasRight = true;
            }
            if(neighbour.getPosition().y > position.y) {
                hasTop = true;
            }
            if(neighbour.getPosition().y < position.y) {
                hasBottom = true;
            }
        }


        String wallPath = "./assets/rooms/11.png";
        String doorPath = "./assets/rooms/10.png";
        Vec2 midColliderScale = new Vec2(1, 0.1f);
        Vec2 outerColliderScale = new Vec2(0.1f, 1);
        Vec2 wallTopPos = new Vec2(position.x, position.y + 0.5f);
        Vec2 wallBottomPos = new Vec2(position.x, position.y - 0.5f);
        Vec2 wallRightPos = new Vec2(position.x + 0.5f, position.y);
        Vec2 wallLeftPos = new Vec2(position.x - 0.5f, position.y);

        if(topIsDoor) {

            topWall = createDoorWithoutCollider(isten, wallTopPos, midColliderScale, doorPath);
        }
        else if(!hasTop) {
            topWall = createWallWithCollider(isten, wallTopPos, midColliderScale, wallPath);
        }
        if(bottomIsDoor) {
            bottomWall = createDoorWithoutCollider(isten, wallBottomPos, midColliderScale, doorPath);
        }
        else if(!hasBottom) {
            bottomWall = createWallWithCollider(isten, wallBottomPos, midColliderScale, wallPath);
        }
        if(rightIsDoor) {
            rightWall = createDoorWithoutCollider(isten, wallRightPos, outerColliderScale, doorPath);
        }
        else if(!hasRight) {
            rightWall = createWallWithCollider(isten, wallRightPos, outerColliderScale, wallPath);
        }
        if(leftIsDoor) {
            leftWall = createDoorWithoutCollider(isten, wallLeftPos, outerColliderScale, doorPath);
        }
        else if(!hasLeft) {
            leftWall = createWallWithCollider(isten, wallLeftPos, outerColliderScale, wallPath);
        }
    }

    private Wall createWallWithCollider(Isten isten, Vec2 pos, Vec2 scale, String imgPath) {
        Collider collider = new Collider(pos, scale);
        Image img = new Image(pos, scale, imgPath);
        isten.getRenderer().addRenderable(img);
        ownerRoom.addCollider(collider);
        return new Wall(collider, img);
    }

    private Door createDoorWithoutCollider(Isten isten, Vec2 pos, Vec2 scale, String imgPath) {
        Image img = new Image(pos, scale, imgPath);
        isten.getRenderer().addRenderable(img);
        return new Door(img);
    }
    public boolean isAdjacent(UnitRoom unitRoom){
        for (UnitRoom adjacentUnitRoom :this.getAdjacentUnitRooms()){
            if(adjacentUnitRoom.equals(unitRoom)){
                return true;
            }

        }
        return false;
    }
    public void addImages(Isten isten, RoomType t){
        int j;
        Image img;

        switch (ownerRoom.roomType){
            case GAS -> j = 1;
            case SHADOW -> j = 2;
            case CURSED -> j = 3;
            case BASIC -> j = 4;
            default -> j = 0;
        }
        String path = "./assets/rooms/" + j + ".png";

            if(image == null){
                img = new Image(position, new Vec2(1,1), path);
                img.setSortingLayer(40);
                setImage(img);
                isten.getRenderer().addRenderable(img);
            }
    }


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
    public Wall getTopWall() {return topWall;}

    public Wall getLeftWall() {return leftWall;}

    public Wall getRightWall() {return rightWall;}

    public Wall getBottomWall() {return bottomWall;}

    public void setTopWall(Wall topWall) {this.topWall = topWall;}

    public void setLeftWall(Wall leftWall) {this.leftWall = leftWall;}

    public void setRightWall(Wall rightWall) {this.rightWall = rightWall;}

    public void setBottomWall(Wall bottomWall) {this.bottomWall = bottomWall;}

    public void setTopNeigbour(UnitRoom topNeigbour) {
        TopNeigbour = topNeigbour;
    }

    public void setBottomNeigbour(UnitRoom bottomNeigbour) {
        BottomNeigbour = bottomNeigbour;
    }

    public void setLeftNeigbour(UnitRoom leftNeigbour) {
        LeftNeigbour = leftNeigbour;
    }

    public void setRightNeigbour(UnitRoom rightNeigbour) {
        RightNeigbour = rightNeigbour;
    }

    public UnitRoom getTopNeigbour() {
        return TopNeigbour;
    }

    public UnitRoom getBottomNeigbour() {
        return BottomNeigbour;
    }

    public UnitRoom getLeftNeigbour() {
        return LeftNeigbour;
    }

    public UnitRoom getRightNeigbour() {
        return RightNeigbour;
    }
}
