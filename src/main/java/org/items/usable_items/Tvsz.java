package main.java.org.items.usable_items;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Isten;
import main.java.org.items.Item;
import main.java.org.linalg.Vec2;

public class Tvsz extends Item {
    public Tvsz(Isten isten){
        super(isten,new Vec2(0.3f,0.3f));
        imagePath="./assets/items/item_tvsz.png";
        image = new Image(new Vec2(-10,-10), scale, imagePath);
        image.setVisibility(true);
        isten.getRenderer().addRenderable(image);
        image.setVisibility(false);

        inventoryImage = new ImageUI(new Vec2(-10,-10), new Vec2(),imagePath);
        isten.getRenderer().addRenderable(inventoryImage);
        inventoryImage.setVisibility(false);
    }
}
