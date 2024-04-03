package main.java.org.game.UI;

import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;

public class Inventory extends Updatable {
    private ArrayList<ImageUI> inventoryIcons;
    private int size;


    public Inventory(int size){
        this.size = size;
        inventoryIcons = new ArrayList<>();
    }

    @Override
    public void onStart(Isten isten) {
        int windowWidth = isten.getRenderer().getWidth();
        int windowHeight = isten.getRenderer().getHeight();
        int spacing = 25;
        int iconSize = 50;
        int firstIconXOffset = -((size*iconSize + (size-1)*spacing)/2) + iconSize/2;

        for(int i = 0; i < size; i++){
            int xOffset = firstIconXOffset + (iconSize + spacing)*i;
            //System.out.println(xOffset);
            ImageUI inventoryIcon= new ImageUI(new Vec2(xOffset, 50),
                    new Vec2(iconSize,iconSize), "./assets/ui/inventorySlot.png") ;
            inventoryIcon.setAlignment(Renderable.CENTER,Renderable.BOTTOM);
            inventoryIcon.setSortingLayer(-68);
            inventoryIcon.setVisibility(true);
            isten.getRenderer().addRenderable(inventoryIcon);
            inventoryIcons.add(inventoryIcon);
        }

    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {

    }

    @Override
    public void onDestroy() {

    }
}
