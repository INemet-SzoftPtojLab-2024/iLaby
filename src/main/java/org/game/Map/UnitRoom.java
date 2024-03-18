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
        boolean topIsDoor = false, bottomIsDoor = false, leftIsDoor = false, rightIsDoor = false;
        for(UnitRoom neighbour: adjacentUnitRooms) {
            if(neighbour.getOwnerRoom().getID() != ownerRoom.getID()) {
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

        String path = "./assets/rooms/11.png";
        String doorPath = "./assets/rooms/8.png";
        if(!hasTop) {
            top = createWallWithCollider(isten, new Vec2(position.x, position.y + 0.5f), new Vec2(1, 0.1f), path);
        }
        if(!hasBottom) {
            bottom = createWallWithCollider(isten, new Vec2(position.x, position.y - 0.9f + 0.4f), new Vec2(1, 0.1f), path);
        }
        if(!hasRight) {
            right = createWallWithCollider(isten, new Vec2(position.x + 0.9f - 0.4f, position.y), new Vec2(0.1f, 1f), path);
        }
        if(!hasLeft) {
            left = createWallWithCollider(isten, new Vec2(position.x - 0.5f, position.y), new Vec2(0.1f, 1f), path);
        }
    }

    private Wall createWallWithCollider(Isten isten, Vec2 pos, Vec2 scale, String imgPath) {
        Collider collider = new Collider(pos, scale);
        Image img = new Image(pos, scale, imgPath);
        isten.getRenderer().addRenderable(img);
        ownerRoom.addCollider(collider);
        return new Wall();
    }
}
