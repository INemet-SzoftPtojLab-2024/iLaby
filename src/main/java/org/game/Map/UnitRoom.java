package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.linalg.Vec2;

import java.nio.file.Files;
import java.util.ArrayList;

public class UnitRoom {

    private Vec2 position;
    private Wall top,left,right,bottom;
    private Item item;
    public Image image;
    private ArrayList<UnitRoom> adjacentUnitRooms;
    private Room  ownerRoom;
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
                if(!ownerRoom.hasDoorWith(neighbour.getOwnerRoom().getID())) {
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
        Vec2 wallBottomPos = new Vec2(position.x, position.y - (1 - midColliderScale.y) + (0.5f - midColliderScale.y));
        Vec2 wallRightPos = new Vec2(position.x + 0.5f, position.y);
        Vec2 wallLeftPos = new Vec2(position.x - 0.5f, position.y);
        if(topIsDoor) {
            top = createDoorWithoutCollider(isten, wallTopPos, midColliderScale, doorPath);
        }
        else if(!hasTop) {
            top = createWallWithCollider(isten, wallTopPos, midColliderScale, wallPath);
        }
        if(bottomIsDoor) {
            bottom = createDoorWithoutCollider(isten, wallBottomPos, midColliderScale, doorPath);
        }
        else if(!hasBottom) {
            bottom = createWallWithCollider(isten, wallBottomPos, midColliderScale, wallPath);
        }
        if(rightIsDoor) {
            right = createDoorWithoutCollider(isten, wallRightPos, outerColliderScale, doorPath);
        }
        else if(!hasRight) {
            right = createWallWithCollider(isten, wallRightPos, outerColliderScale, wallPath);
        }
        if(leftIsDoor) {
            left = createDoorWithoutCollider(isten, wallLeftPos, outerColliderScale, doorPath);
        }
        else if(!hasLeft) {
            left = createWallWithCollider(isten, wallLeftPos, outerColliderScale, wallPath);
        }
    }

    private Wall createWallWithCollider(Isten isten, Vec2 pos, Vec2 scale, String imgPath) {
        Collider collider = new Collider(pos, scale);
        Image img = new Image(pos, scale, imgPath);
        isten.getRenderer().addRenderable(img);
        ownerRoom.addCollider(collider);
        return new Wall();
    }

    private Door createDoorWithoutCollider(Isten isten, Vec2 pos, Vec2 scale, String imgPath) {
        Image img = new Image(pos, scale, imgPath);
        isten.getRenderer().addRenderable(img);
        return new Door();
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
}
