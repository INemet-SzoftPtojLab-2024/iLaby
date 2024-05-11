package main.java.org.items;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.physics.Collider;
import main.java.org.items.usable_items.*;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Chest {
    //private Heading heading;//0=left, 1=up, 2=right, 3=down
    private WallLocation wallLocation;
    private final Isten isten;
    private boolean isOpened;//once a chest is opened, cant be closed anymore; default:false
    private ArrayList<Item> storedItems;
    private Image chestImage;
    private Vec2 pos;
    private final Vec2 scale = new Vec2(0.4f, 0.4f);
    private ChestType chestType;
    private boolean isOnRightPlace; //ha ajto mellett lenne, vagy pedig nem fal mellett lenne
    private Collider collider;
    private UnitRoom unitRoom; //csak a posiciojat hasznaljuk, lehet csak az eleg tarolni, igazabol meg az is folosleges
    private int idx;

    ///**
     //* @param this.heading 0=left, 1=up, 2=right, 3=down
     //*/
    public Chest(Vec2 pos, Isten isten, int chestType,int wallLocation, int idx) {
        this.isten = isten;
        this.isOpened = false;
        //a heading es a WallLocation ugyan azt az adatot tarolja, overhead
        this.setWallLocation(wallLocation);
        this.idx = idx;
        this.chestType = (Chest.ChestType) Arrays.stream(Chest.ChestType.values()).toArray()[chestType];
        storedItems = new ArrayList<Item>();
        /*if(items.isEmpty()) System.err.println("There is no item in the chest!");
        if(items.size()>maxAmountOfItems){
            System.err.println("So much items cant be stored in a chest, max amount of storable items is "+maxAmountOfItems);
            for (int i = 0; i < maxAmountOfItems; i++) {storedItems.set(i, items.get(i));}
        }
        else storedItems=items;*/

        //a cleinsen csak a posicioja van tarolva a unitroomnak
        this.unitRoom = isten.getMap().getUnitRooms()[(int)pos.y][(int)pos.x];
        this.unitRoom.setHasChest(true);
        setPos(); // a unitroom pos-bol convertal rendes post, ezert az utobbi sor ez utan mar nem allna meg a helyet

        //a cleinsen csak a posicioja van
        //ez a cliensen teljesen rossz
        //this.wallLocation = wallInUnitRoomPicker(unitRoom);

        //ezt meg addolni kell a grouphoz
        this.collider = new Collider(pos,new Vec2(0.3f,0.3f));

        //setUnitRoom(pos);

        //graphics
        chestImage = null;
        fillChest();

    }

    public void setNewChestImage() {
        if(chestImage!=null){
            isten.getRenderer().deleteRenderable(chestImage);
        }
        switch (wallLocation) {
            case LEFT:
                if(!isOpened) chestImage = new Image(pos, scale, "./assets/items/chest/chest_closed_right.png");
                else chestImage = new Image(pos, scale, "./assets/items/chest/chest_opened_right.png");
                break;
            case TOP:
                if(!isOpened) chestImage = new Image(pos, scale, "./assets/items/chest/chest_closed_down.png");
                else chestImage = new Image(pos, scale, "./assets/items/chest/chest_opened_down.png");
                break;
            case RIGHT:
                if(!isOpened) chestImage = new Image(pos, scale, "./assets/items/chest/chest_closed_left.png");
                else  chestImage = new Image(pos, scale, "./assets/items/chest/chest_opened_left.png");
                break;
            case BOTTOM:
                if(!isOpened) chestImage = new Image(pos, scale, "./assets/items/chest/chest_closed_up.png");
                else chestImage = new Image(pos, scale, "./assets/items/chest/chest_opened_up.png");
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
        this.unitRoom = isten.getMap().getUnitRooms()[(int)pos.y][(int)pos.x];
        WallLocation wall = wallInUnitRoomPicker(unitRoom);
        //a heading es a pos ugyan azt az adatot tarolja, overhead
        setPos();
        chestImage = null;
        setNewChestImage();
        unitRoom.setHasChest(true);
    }
    public void open(Item item1, Item item2) {
        if(storedItems.size()>2) System.out.println("There are more than 2 items in the chest!");
        switch(wallLocation){
            case LEFT: {
                if(storedItems.size()==1)storedItems.get(0).dropOnGround(new Vec2(pos.x+0.4f,pos.y));
                else {
                    if(item1 != null) item1.dropOnGround(new Vec2(pos.x+0.3f,pos.y-0.3f));
                    if(item2 != null) item2.dropOnGround(new Vec2(pos.x+0.3f,pos.y+0.3f));
                }
                break;
            }
            case TOP: {
                if(storedItems.size()==1)storedItems.get(0).dropOnGround(new Vec2(pos.x,pos.y-0.4f));
                else {
                    if(item1 != null) item1.dropOnGround(new Vec2(pos.x+0.3f,pos.y-0.3f));
                    if(item2 != null) item2.dropOnGround(new Vec2(pos.x-0.3f,pos.y-0.3f));
                }
                break;
            }
            case RIGHT: {
                if(storedItems.size()==1)storedItems.get(0).dropOnGround(new Vec2(pos.x-0.4f,pos.y));
                else {
                    if(item1 != null) item1.dropOnGround(new Vec2(pos.x-0.3f,pos.y+0.3f));
                    if(item2 != null) item2.dropOnGround(new Vec2(pos.x-0.3f,pos.y-0.3f));
                }
                break;
            }
            case BOTTOM: {
                if(storedItems.size()==1)storedItems.get(0).dropOnGround(new Vec2(pos.x,pos.y+0.4f));
                else {
                    if(item1 != null) item1.dropOnGround(new Vec2(pos.x-0.3f,pos.y+0.3f));
                    if(item2 != null) item2.dropOnGround(new Vec2(pos.x+0.3f,pos.y+0.3f));
                }
                break;
            }
        }
        isOpened = true;
        setNewChestImage();
    }


    public void setPos(){
        switch (wallLocation) {//0=left, 1=top, 2=right, 3=bottom
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

    //public int getHeadingInt() {return heading.ordinal(); }
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
       /* int x = (int)(pos.x + 0.5f);
        int y = (int)(pos.y + 0.5f);
        //System.out.println(x + " " + y + " ownerroorm pozi " +  isten.getMap().getUnitRooms()[y][x].getColNum() + " " + isten.getMap().getUnitRooms()[y][x].getRowNum());
        return isten.getMap().getUnitRooms()[y][x];*/
        return chestUnitRoom;
    }
    public boolean isOnRightPlace(){// csak serveren lehet
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

    public void setIsOpen(boolean isOpened) {
        this.isOpened = isOpened;
    }

    //private enum Heading{RIGHT, DOWN, LEFT, UP}
    /*public void setHeading(WallLocation wall){
        this.heading = Heading.values()[wall.ordinal()];
    }*/

    public enum WallLocation {LEFT, TOP, RIGHT, BOTTOM}
    public enum ChestType{ONE,TWO,THREE,FOUR,FIVE,SIX}

    public void replaceChest(Vec2 newURPos, int newWallLocation){
        //a setHasChest allitasa a cliensen folosleges, de maradhat
        setWallLocation(newWallLocation);
        unitRoom.setHasChest(false);
        unitRoom = isten.getMap().getUnitRooms()[(int)newURPos.y][(int)newURPos.x];
        unitRoom.setHasChest(true);
        setPos(); // a unitroom alapjan beallitja a rendes positiont
        collider.setPosition(pos);
        setNewChestImage();
    }
    public void setCollider() {// a collider marad csak a pozicioja valtozik meg
        this.collider = new Collider(pos,new Vec2(0.3f,0.3f));
    }
    public Collider getCollider(){
        return collider;
    }
    private void setidx(int i){
        this.idx = i;
    }
    //az athelyezesnel kell, mert a klensek nem tudjal liszamolni
    public void setWallLocation(int wallLocation){
        //LEFT -> 0
        //TOP -> 1
        //RIGHT -> 2
        //BOTTOM -> 3
        this.wallLocation = (Chest.WallLocation) Arrays.stream(Chest.WallLocation.values()).toArray()[wallLocation];
    }

    public WallLocation getWallLocation() {
        return wallLocation;
    }

    public Vec2 getPosition(){
        return pos;
    }
    public Vec2 getScale(){
        return scale;
    }
    public int getHeadingInt() {
        return 0;
    }
    public Vec2 getUnitRoomPosition(){
        return unitRoom.getPosition();
    }

    public ArrayList<Item> getStoredItems() {
        return storedItems;
    }
}
