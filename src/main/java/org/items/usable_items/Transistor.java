package main.java.org.items.usable_items;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Graphics.TextUI;
import main.java.org.game.Isten;
import main.java.org.game.UI.Inventory;
import main.java.org.items.Item;
import main.java.org.linalg.Vec2;

public class Transistor extends Item {

    private int count;
    private TextUI countText;
    public Transistor(Isten isten){
        super(isten,new Vec2(0.4f,0.5f));
        imagePath="./assets/items/item_transistor.png";
        image = new Image(new Vec2(-10,-10), scale, imagePath);
        isten.getRenderer().addRenderable(image);
        image.setVisibility(false);
        count = 2;
        countText = new TextUI(String.valueOf(count), new Vec2(0,0), "./assets/Monocraft.ttf", 15, 255, 255, 255);
        countText.setVisibility(false);
        countText.setAlignment(Renderable.CENTER, Renderable.BOTTOM);
        countText.setSortingLayer(-80);
        countText.setShadowOn(false);
        isten.getRenderer().addRenderable(countText);

    }

    @Override
    public void pickUpInInventory(){
        super.pickUpInInventory();
        Inventory inv = isten.getInventory();
        Vec2 slotPosition = inv.getStoringSlotPosition(this);
        Vec2 textPosition = new Vec2(slotPosition.x -10, slotPosition.y-7);
        countText.setPosition(textPosition);
        countText.setVisibility(true);
    }
}
