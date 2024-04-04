package main.java.org.items.usable_items;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.items.Item;
import main.java.org.linalg.Vec2;

public class Transistor extends Item {
    public Transistor(Isten isten){
        super(isten);
        imagePath="./assets/items/item_transistor.png";
        image = new Image(new Vec2(-10,-10), scale, imagePath);
        isten.getRenderer().addRenderable(image);
        image.setVisibility(false);

    }
}
