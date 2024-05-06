package main.java.org.items;

import lombok.Setter;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.items.usable_items.*;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;
import java.util.List;

public class Chest {
    private Heading heading;//0=left, 1=up, 2=right, 3=down
    private final Isten isten;
    private boolean isOpened;//once a chest is opened, cant be closed anymore; default:false
    private ArrayList<Item> storedItems;
    private Image chestImage;
    private Vec2 pos;
    private final Vec2 scale=new Vec2(0.4f,0.4f);
    private int chestType;

    /**
     * @param heading 0=left, 1=up, 2=right, 3=down
     */
    public Chest(Vec2 position, Isten isten,int heading, int chestType){
        this.isten = isten;
        this.heading=Heading.values()[heading];
        this.pos=position;
        this.chestType = chestType;
        storedItems=new ArrayList<Item>();
        fillChest();
        /*if(items.isEmpty()) System.err.println("There is no item in the chest!");
        if(items.size()>maxAmountOfItems){
            System.err.println("So much items cant be stored in a chest, max amount of storable items is "+maxAmountOfItems);
            for (int i = 0; i < maxAmountOfItems; i++) {storedItems.set(i, items.get(i));}
        }
        else storedItems=items;*/
        isOpened=false;
        chestImage=null;
        switch(this.heading){
            case RIGHT:  chestImage=new Image(pos,scale,"./assets/items/chest/chest_closed_right.png");break;
            case DOWN:  chestImage=new Image(pos,scale,"./assets/items/chest/chest_closed_down.png");break;
            case LEFT:  chestImage=new Image(pos,scale,"./assets/items/chest/chest_closed_left.png");break;
            case UP:  chestImage=new Image(pos,scale,"./assets/items/chest/chest_closed_up.png");break;
        }
        chestImage.setVisibility(true);
        isten.getRenderer().addRenderable(chestImage);
    }

    public void fillChest() {//max 2 item mehet bele
        switch (chestType) {
            case 0:
                storedItems.add(new Camembert(isten));
                storedItems.add(new Transistor(isten));
                break;
            case 1:
                storedItems.add(new Sorospohar(isten));
                storedItems.add(new Tvsz(isten));
                break;
            case 2:
                storedItems.add(new Gasmask(isten));
                storedItems.add(new Camembert(isten));
                break;
            case 3:
                storedItems.add(new Gasmask(isten));
                storedItems.add(new Rongy(isten));
                break;
            case 4:
                storedItems.add(new Transistor(isten));
                storedItems.add(new Tvsz(isten));
                break;
            case 5:
                storedItems.add(new Logarlec(isten));
                break;
        }
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

    public int getHeadingInt() {return heading.ordinal(); }
    public boolean isOpened(){return isOpened;}

    public void setHeading(int h) { this.heading = Heading.values()[h];}
    public void setPosition(Vec2 p) { this.pos = p; }

    public ArrayList<Item> getStoredItems() {return storedItems; }
    public void setStoredItems(ArrayList<Item> items) {storedItems = items;}

    public int getChestType() {
        return chestType;
    }

    public void setChestType(int chestType) {
        this.chestType = chestType;
    }

    private enum Heading{
        RIGHT,
        DOWN,
        LEFT,
        UP
    }

}
