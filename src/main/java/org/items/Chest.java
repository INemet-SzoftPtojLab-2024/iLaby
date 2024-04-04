package main.java.org.items;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;
import java.util.List;

public class Chest {
    private final int maxAmountOfItems=3;
    private final Heading heading;//0=left, 1=up, 2=right, 3=down
    private final Isten isten;
    private boolean isOpened;//once a chest is opened, cant be closed anymore; default:false
    private ArrayList<Item> storedItems;
    private Image chestImage;
    private final Vec2 pos;
    private final Vec2 scale=new Vec2(0.4f,0.4f);
    /**
     * @param heading 0=left, 1=up, 2=right, 3=down
     * @param items The minimum 1 and maximum 3 items stored in the chest
     */
    public Chest(Vec2 position, Isten isten,int heading,ArrayList<Item> items){
        this.isten = isten;
        this.heading=Heading.values()[heading];
        this.pos=position;
        storedItems=new ArrayList<Item>();
        if(items.isEmpty()) System.err.println("There is no item in the chest!");
        if(items.size()>maxAmountOfItems){
            System.err.println("So much items cant be stored in a chest, max amount of storable items is "+maxAmountOfItems);
            for (int i = 0; i < maxAmountOfItems; i++) {storedItems.set(i, items.get(i));}
        }
        else storedItems=items;
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
    public void open() {
        chestImage.setVisibility(false);
        switch(heading){
            case RIGHT: {
                chestImage = new Image(pos, scale, "./assets/items/chest/chest_opened_right.png");
                if(storedItems.size()==1||storedItems.size()==3)storedItems.get(0).dropOnGround(new Vec2(pos.x+0.4f,pos.y));
                else {
                    storedItems.get(0).dropOnGround(new Vec2(pos.x+0.3f,pos.y-0.3f));
                    storedItems.get(1).dropOnGround(new Vec2(pos.x+0.3f,pos.y+0.3f));
                }
                if(storedItems.size()==3){
                    storedItems.get(1).dropOnGround(new Vec2(pos.x+0.3f,pos.y-0.4f));
                    storedItems.get(2).dropOnGround(new Vec2(pos.x+0.3f,pos.y+0.4f));
                }
                break;
            }
            case DOWN: {
                chestImage = new Image(pos, scale, "./assets/items/chest/chest_opened_down.png");
                if(storedItems.size()==1||storedItems.size()==3)storedItems.get(0).dropOnGround(new Vec2(pos.x,pos.y-0.4f));
                else {
                    storedItems.get(0).dropOnGround(new Vec2(pos.x+0.3f,pos.y-0.3f));
                    storedItems.get(1).dropOnGround(new Vec2(pos.x-0.3f,pos.y-0.3f));
                }
                if(storedItems.size()==3){
                    storedItems.get(1).dropOnGround(new Vec2(pos.x+0.4f,pos.y-0.3f));
                    storedItems.get(2).dropOnGround(new Vec2(pos.x-0.4f,pos.y-0.3f));
                }
                break;
            }
            case LEFT: {
                chestImage = new Image(pos, scale, "./assets/items/chest/chest_opened_left.png");
                if(storedItems.size()==1||storedItems.size()==3)storedItems.get(0).dropOnGround(new Vec2(pos.x-0.4f,pos.y));
                else {
                    storedItems.get(0).dropOnGround(new Vec2(pos.x-0.3f,pos.y+0.3f));
                    storedItems.get(1).dropOnGround(new Vec2(pos.x-0.3f,pos.y-0.3f));
                }
                if(storedItems.size()==3){
                    storedItems.get(1).dropOnGround(new Vec2(pos.x-0.3f,pos.y+0.4f));
                    storedItems.get(2).dropOnGround(new Vec2(pos.x-0.3f,pos.y-0.4f));
                }
                break;
            }
            case UP: {
                chestImage = new Image(pos, scale, "./assets/items/chest/chest_opened_up.png");
                if(storedItems.size()==1||storedItems.size()==3)storedItems.get(0).dropOnGround(new Vec2(pos.x,pos.y+0.4f));
                else {
                    storedItems.get(0).dropOnGround(new Vec2(pos.x-0.3f,pos.y+0.3f));
                    storedItems.get(1).dropOnGround(new Vec2(pos.x+0.3f,pos.y+0.3f));
                }
                if(storedItems.size()==3){
                    storedItems.get(1).dropOnGround(new Vec2(pos.x-0.4f,pos.y+0.3f));
                    storedItems.get(2).dropOnGround(new Vec2(pos.x+0.4f,pos.y+0.3f));
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

    public boolean isOpened(){return isOpened;}

    private enum Heading{
        RIGHT,
        DOWN,
        LEFT,
        UP
    }

}
