package main.java.org.items;

import main.java.org.entities.player.Player;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Isten;
import main.java.org.linalg.Vec2;
import main.java.org.networking.PlayerMP;

import java.time.LocalDateTime;

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
    protected final Vec2 scale;
    protected Location location;
    protected Vec2 position;
    protected Image image;
    protected final Isten isten;
    protected String imagePath;
    protected boolean used;
    private int itemIndex;

    private LocalDateTime droppedAt;
    public Item(Isten isten,Vec2 scale){
        this.scale=scale;
        location=Location.CHEST;
        position=null;
        droppedAt = null;
        this.isten=isten;
        itemIndex = isten.getItemManager().getItems().size();
        used=false;
        isten.getItemManager().addItem(this);
    }
    public void dropOnGround(Vec2 pos){
        if(!location.equals(Location.GROUND)){
            location = Location.GROUND;
            position = pos;
            image.setPosition(pos);
            image.setVisibility(true);
            droppedAt = LocalDateTime.now();
        }
    }
    public void pickUpInInventory(PlayerMP player, int selectedSlotByClient){
        //Pics up an item if it is not in the inventory, and it has been dropped for more than 200 millisec
        //1 ms = 1000000 ns :)
        if((!location.equals(Location.INVENTORY) && droppedAt.isBefore((LocalDateTime.now()).minusNanos(200000000)))) {
            if(!used) {
                location = Location.INVENTORY;
                image.setVisibility(false);
                if(player.localPlayer) player.getInventory().addItem(this);
                else player.getInventory().addItemToClient(this, selectedSlotByClient);
            }
        }
    }

    public void use(Player player, double deltatime){

    }
    public String getImagePath(){return imagePath;}

    public Vec2 getPosition(){return position;}
    public void setPosition(Vec2 pos) { position=pos; }
    public Location getLocation(){return location;}
    public void setLocation(Location loc) { location = loc; }

    public Image getImage() { return image; }

    public int getItemIndex() { return itemIndex; }
    public boolean isUsed(){return used;}
    public void setCapacity(float capacity) {}
    public void resizeBar(float percent) {}
}
