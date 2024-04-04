package main.java.org.items;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Isten;
import main.java.org.linalg.Vec2;

/*TVSZ denevérbőrre nyomtatott példányai
Gasmask
Transistor
Logarléc
Söröspohár, rongy (adott ideig véd a boss-tól)
Camambert (gázszobát csinál, bénítja a boss-t)*/
public abstract class Item {
    public enum Location {
        CHEST,
        GROUND,
        INVENTORY
    };
    protected final Vec2 scale=new Vec2(0.4f,0.4f);
    protected Location location;
    protected Vec2 position;
    protected Image image;
    protected final Isten isten;
    protected String imagePath;
    public Item(Isten isten){
        location=Location.CHEST;
        position=null;
        this.isten=isten;
        isten.getItemManager().addItem(this);
    }
    public void dropOnGround(Vec2 pos){
        if(!location.equals(Location.GROUND)){
            location = Location.GROUND;
            position = pos;
            image = new Image(position, scale, imagePath);
            isten.getRenderer().addRenderable(image);
            image.setVisibility(true);
        }
    }
    public void pickUpInInventory(){
        if(!location.equals(Location.INVENTORY)) {
            location = Location.INVENTORY;
            image.setVisibility(false);
            isten.getInventory().addItem(this);
        }
    }
    public String getImagePath(){return imagePath;}
    public Vec2 getPosition(){return position;}
    public Location getLocation(){return location;}
}
