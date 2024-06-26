package main.java.org.items.usable_items;

import main.java.org.entities.player.Player;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Graphics.TextUI;
import main.java.org.game.Isten;
import main.java.org.game.Map.Room;
import main.java.org.game.UI.Inventory;
import main.java.org.items.Item;
import main.java.org.linalg.Vec2;
import main.java.org.networking.PlayerMP;

import java.util.ArrayList;
import java.util.List;

public class Tvsz extends Item {

    private int charges;
    private TextUI countText;
    private boolean shouldUseCharge = true;


    public Tvsz(Isten isten){
        super(isten,new Vec2(0.3f,0.3f));
        charges = 3;

        imagePath="./assets/items/item_tvsz.png";
        image = new Image(new Vec2(-10,-10), scale, imagePath);
        image.setVisibility(true);
        isten.getRenderer().addRenderable(image);
        image.setVisibility(false);


        countText = new TextUI(String.valueOf(charges), new Vec2(0,0), 15, 255, 255, 255);
        countText.setVisibility(false);
        countText.setAlignment(Renderable.CENTER, Renderable.BOTTOM);
        countText.setSortingLayer(-80);
        countText.setShadowOn(false);
        isten.getRenderer().addRenderable(countText);
    }

    @Override
    public void use(Player player, double deltaTime){
        //Csak akkor használodik, amikor belép a player egy szobába, ahol gegner van

        if(shouldUseCharge){
            System.out.println("should use charge");
            shouldUseCharge = false;
            if(charges > 1) {
                charges--;
                countText.setText(String.valueOf(charges));
            } else{
                player.getInventory().setCanAvoidVillain(true);
                player.getInventory().deleteItem(this);
                isten.getItemManager().removeItem(this);
                delete();
            }
        }

    }
    public void delete(){
        isten.getRenderer().deleteRenderable(image);
        isten.getRenderer().deleteRenderable(countText);
    }

    @Override
    public void pickUpInInventory(PlayerMP player, int selectedSlotByClient) {
        super.pickUpInInventory(player, selectedSlotByClient);
        if(player.localPlayer) setUI(player);

    }

    private void setUI(PlayerMP player) {
        Inventory inv = player.getInventory();
        Vec2 slotPosition = inv.getStoringSlotPosition(this);
        Vec2 textPosition = new Vec2(slotPosition.x -10, slotPosition.y-7);
        countText.setPosition(textPosition);
        countText.setVisibility(true);
        countText.setText(String.valueOf(charges));
    }
    @Override
    public void dropOnGround(Vec2 pos){
        super.dropOnGround(pos);
        countText.setVisibility(false);
    }
    public void setShouldUseCharge(boolean value){shouldUseCharge = value;}

    public int getCharges() {
        return charges;
    }

    public void setCharges(int charges) {
        this.charges = charges;
    }
}
