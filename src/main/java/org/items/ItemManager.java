package main.java.org.items;

import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static java.lang.Math.sqrt;

public class ItemManager extends Updatable {
    private Isten isten;
    private ArrayList<Item> items;
    public void addItem(Item item){
        items.add(item);
    }

    @Override
    public void onStart(Isten isten) {
        this.isten=isten;
        items=new ArrayList<>();
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        if(isten.getInputHandler().isKeyDown(KeyEvent.VK_E)){
            Vec2 playerPostion = isten.getPlayer().getPlayerCollider().getPosition();
            for(var item : items){
                if(item.location== Item.Location.GROUND) {
                    Vec2 playerItemVector = Vec2.subtract(playerPostion, item.getPosition());
                    double playerItemDistance = sqrt(Vec2.dot(playerItemVector, playerItemVector));
                    if (playerItemDistance <= 0.3) {
                        item.pickUpInInventory();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {

    }
}
