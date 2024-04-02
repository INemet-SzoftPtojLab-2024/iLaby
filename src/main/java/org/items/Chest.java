package main.java.org.items;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.linalg.Vec2;

import java.util.List;

public class Chest {
    private final int heading;//0=left, 1=up, 2=right, 3=down
    private boolean isOpened;//once a chest is opened, cant be closed anymore; default:false
    private List<Item> storedItems;
    private Image chestImage;
    private final Vec2 pos;
    private final Vec2 scale=new Vec2(0.4f,0.4f);
    /**
     * @param heading 0=left, 1=up, 2=right, 3=down
     */
    public Chest(Vec2 position, Isten isten,int heading){
        this.heading=heading;
        this.pos=position;
        isOpened=false;
        chestImage=null;
        switch(heading){
            case 0:  chestImage=new Image(pos,scale,"./assets/items/chest/chest_closed_right.png");break;
            case 1:  chestImage=new Image(pos,scale,"./assets/items/chest/chest_closed_down.png");break;
            case 2:  chestImage=new Image(pos,scale,"./assets/items/chest/chest_closed_left.png");break;
            case 3:  chestImage=new Image(pos,scale,"./assets/items/chest/chest_closed_up.png");break;
        }
        chestImage.setVisibility(true);
        isten.getRenderer().addRenderable(chestImage);
    }
    public void open(){

    }
    public Vec2 getPosition(){
        return pos;
    }
    public Vec2 getScale(){
        return scale;
    }

}
