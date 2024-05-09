package main.java.org.items;

import lombok.Setter;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.items.usable_items.*;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Chest {
    private Heading heading;//0=left, 1=up, 2=right, 3=down
    private final Isten isten;
    private boolean isOpened;//once a chest is opened, cant be closed anymore; default:false
    private ArrayList<Item> storedItems;
    private Image chestImage;
    private Vec2 pos;
    private final Vec2 scale = new Vec2(0.4f, 0.4f);
    private ChestType chestType;
    private boolean isOnRightPlace; //ha ajto mellett lenne, vagy pedig nem fal mellett lenne
    private Collider collider;
    private UnitRoom unitRoom;
    private int idx;

    ///**
     //* @param this.heading 0=left, 1=up, 2=right, 3=down
     //*/
    public Chest(Vec2 pos, Isten isten, int chestType,int idx) {
        this.isten = isten;
        this.idx = idx;
        this.chestType = (Chest.ChestType) Arrays.stream(Chest.ChestType.values()).toArray()[chestType];
        storedItems = new ArrayList<Item>();
        fillChest();
        /*if(items.isEmpty()) System.err.println("There is no item in the chest!");
        if(items.size()>maxAmountOfItems){
            System.err.println("So much items cant be stored in a chest, max amount of storable items is "+maxAmountOfItems);
            for (int i = 0; i < maxAmountOfItems; i++) {storedItems.set(i, items.get(i));}
        }
        else storedItems=items;*/
        setUnitRoom(pos);
    }

    public void setChestImage() {
        if(chestImage!=null){
            isten.getRenderer().deleteRenderable(chestImage);
        }
        isOpened = false;
        switch (this.heading) {
            case RIGHT:
                chestImage = new Image(pos, scale, "./assets/items/chest/chest_closed_right.png");
                break;
            case DOWN:
                chestImage = new Image(pos, scale, "./assets/items/chest/chest_closed_down.png");
                break;
            case LEFT:
                chestImage = new Image(pos, scale, "./assets/items/chest/chest_closed_left.png");
                break;
            case UP:
                chestImage = new Image(pos, scale, "./assets/items/chest/chest_closed_up.png");
                break;
        }
        chestImage.setVisibility(true);
        isten.getRenderer().addRenderable(chestImage);
    }

    public void fillChest () {//max 2 item mehet bele
        switch (chestType) {//ha megváltoztatnád a chestTypeok számát, akk
            case ONE:
                storedItems.add(new Camembert(isten));
                storedItems.add(new Transistor(isten));
                break;
            case TWO:
                storedItems.add(new Sorospohar(isten));
                storedItems.add(new Tvsz(isten));
                break;
            case THREE:
                storedItems.add(new Gasmask(isten));
                storedItems.add(new Camembert(isten));
                break;
            case FOUR:
                storedItems.add(new Gasmask(isten));
                storedItems.add(new Rongy(isten));
                break;
            case FIVE:
                storedItems.add(new Transistor(isten));
                storedItems.add(new Tvsz(isten));
                break;
            case SIX:
                storedItems.add(new Logarlec(isten));
                break;
        }
    }
    private WallLocation wallInUnitRoomPicker (UnitRoom unitRoom){
        WallLocation wall;//0=left, 1=top, 2=right, 3=bottom
        ArrayList<WallLocation> walls = new ArrayList<>();
        if (unitRoom.isLeftWall()) walls.add(WallLocation.LEFT);
        if (unitRoom.isTopWall()) walls.add(WallLocation.TOP);
        if (unitRoom.isRightWall()) walls.add(WallLocation.RIGHT);
        if (unitRoom.isBottomWall()) walls.add(WallLocation.BOTTOM);
        Random random = new Random();
        return walls.get(random.nextInt(walls.size()));
    }
    public void setUnitRoom(Vec2 pos){
        this.pos = pos;
        this.unitRoom = getUnitRoom(pos);
        WallLocation wall = wallInUnitRoomPicker(unitRoom);
        setPos(wall);
        setHeading(wall);
        chestImage = null;
        setChestImage();
        unitRoom.setHasChest(true);
    }
    public void open() {
        chestImage.setVisibility(false);
        if(storedItems.size()>2) System.out.println("There are more than 2 items in the chest!");
        switch(heading){
            case RIGHT: {
                chestImage = new Image(pos, scale, "./assets/items/chest/chest_opened_right.png");
                if(storedItems.size()==1)storedItems.get(0).dropOnGround(new Vec2(pos.x+0.4f,pos.y));
                else {
                    storedItems.get(0).dropOnGround(new Vec2(pos.x+0.3f,pos.y-0.3f));
                    storedItems.get(1).dropOnGround(new Vec2(pos.x+0.3f,pos.y+0.3f));
                }
                break;
            }
            case DOWN: {
                chestImage = new Image(pos, scale, "./assets/items/chest/chest_opened_down.png");
                if(storedItems.size()==1)storedItems.get(0).dropOnGround(new Vec2(pos.x,pos.y-0.4f));
                else {
                    storedItems.get(0).dropOnGround(new Vec2(pos.x+0.3f,pos.y-0.3f));
                    storedItems.get(1).dropOnGround(new Vec2(pos.x-0.3f,pos.y-0.3f));
                }
                break;
            }
            case LEFT: {
                chestImage = new Image(pos, scale, "./assets/items/chest/chest_opened_left.png");
                if(storedItems.size()==1)storedItems.get(0).dropOnGround(new Vec2(pos.x-0.4f,pos.y));
                else {
                    storedItems.get(0).dropOnGround(new Vec2(pos.x-0.3f,pos.y+0.3f));
                    storedItems.get(1).dropOnGround(new Vec2(pos.x-0.3f,pos.y-0.3f));
                }
                break;
            }
            case UP: {
                chestImage = new Image(pos, scale, "./assets/items/chest/chest_opened_up.png");
                if(storedItems.size()==1)storedItems.get(0).dropOnGround(new Vec2(pos.x,pos.y+0.4f));
                else {
                    storedItems.get(0).dropOnGround(new Vec2(pos.x-0.3f,pos.y+0.3f));
                    storedItems.get(1).dropOnGround(new Vec2(pos.x+0.3f,pos.y+0.3f));
                }
                break;
            }
        }
        isten.getRenderer().addRenderable(chestImage);
        chestImage.setVisibility(true);
        isOpened = true;

    }
    public Vec2 getPosition(){
        return pos;
    }
    public Vec2 getScale(){
        return scale;
    }

    public void setPos(WallLocation wall){
        switch (wall) {//0=left, 1=top, 2=right, 3=bottom
            case LEFT:
                pos = new Vec2(unitRoom.getPosition().x - 0.3f, unitRoom.getPosition().y);
                break;
            case TOP:
                pos = new Vec2(unitRoom.getPosition().x, unitRoom.getPosition().y + 0.3f);
                break;
            case RIGHT:
                pos = new Vec2(unitRoom.getPosition().x + 0.3f, unitRoom.getPosition().y);
                break;
            case BOTTOM:
                pos = new Vec2(unitRoom.getPosition().x, unitRoom.getPosition().y - 0.3f);
                break;
        }
    }
    public void setHeading(WallLocation wall){
        this.heading = Heading.values()[wall.ordinal()];
    }
    public int getHeadingInt() {return heading.ordinal(); }
    public boolean isOpened(){return isOpened;}
    public void setPosition(Vec2 p) { this.pos = p; }


    public int getChestType() {
        return chestType.ordinal();
    }
    public UnitRoom getUnitRoom(Vec2 pos){
        UnitRoom chestUnitRoom=null;
        for (int i = 0; i < isten.getMap().getUnitRooms().length; i++) {
            for (int j = 0; j < isten.getMap().getUnitRooms()[i].length; j++) {
                UnitRoom unitRoom = isten.getMap().getUnitRooms()[i][j];
                if (pos.x >= unitRoom.getPosition().x - 0.5 &&
                        pos.x <= unitRoom.getPosition().x + 0.5 &&
                        pos.y >= unitRoom.getPosition().y - 0.5 &&
                        pos.y <= unitRoom.getPosition().y + 0.5) {
                    chestUnitRoom=unitRoom;
                }
            }
        }
       /* int x = (int)(playerPos.x + 0.5f);
        int y = (int)(playerPos.y + 0.5f);
        //System.out.println(x + " " + y + " ownerroorm pozi " +  isten.getMap().getUnitRooms()[y][x].getColNum() + " " + isten.getMap().getUnitRooms()[y][x].getRowNum());
        return isten.getMap().getUnitRooms()[y][x].getOwnerRoom();*/
        return chestUnitRoom;
    }
    public boolean getIsOnRightPlace(){
        UnitRoom unitRoom = getUnitRoom(pos);
        if (!unitRoom.hasDoor()//ha egyik fal sem ajtó
                && (unitRoom.isTopWall() || unitRoom.isRightWall() || unitRoom.isBottomWall() || unitRoom.isLeftWall())//ha egyik oldalán legalább fal van
        ){
            //System.out.println(" true");
            return true;
        }
        else {
            //System.out.println("false");
            return false;
        }
    }
    public void setIsOnRightPlace(boolean isOnRightPlace){
        this.isOnRightPlace = isOnRightPlace;
    }

    private enum Heading{
        RIGHT,
        DOWN,
        LEFT,
        UP
    }
    public enum ChestType{ONE,TWO,THREE,FOUR,FIVE,SIX}

    public void setCollider() {

        this.collider = new Collider(pos,new Vec2(0.3f,0.3f));
    }
    public Collider getCollider(){
        return collider;
    }
    public enum WallLocation {
        LEFT,
        TOP,
        RIGHT,
        BOTTOM
    }
    private void setidx(int i){
        this.idx = i;
    }

}
