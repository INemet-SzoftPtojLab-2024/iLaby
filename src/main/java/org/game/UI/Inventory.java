package main.java.org.game.UI;

import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.items.Item;
import main.java.org.linalg.Vec2;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Inventory extends Updatable {
    private ArrayList<ImageUI> inventoryIcons;
    private ArrayList<ImageUI> itemIcons;
    private List<Item> storedItems;
    private int size;
    private final int iconSize=50;
    /**
     * 1-5
     */
    private int selectedSlot;
    private  Isten isten;


    public Inventory(int size){
        this.size = size;
        inventoryIcons = new ArrayList<>();
        itemIcons=new ArrayList<>();
        storedItems=new ArrayList<>();
        selectedSlot=1;
    }

    @Override
    public void onStart(Isten isten) {
        this.isten=isten;
        int windowWidth = isten.getRenderer().getWidth();
        int windowHeight = isten.getRenderer().getHeight();

        ImageUI inventoryIcon= new ImageUI(getSlotLocation(1),
                new Vec2(iconSize,iconSize), "./assets/ui/inventorySlot_Selected.png") ;
        inventoryIcon.setAlignment(Renderable.CENTER,Renderable.BOTTOM);
        inventoryIcon.setSortingLayer(-68);
        inventoryIcon.setVisibility(true);
        isten.getRenderer().addRenderable(inventoryIcon);
        inventoryIcons.add(inventoryIcon);
        for(int i = 1; i < size; i++){
            //System.out.println(xOffset);
             inventoryIcon= new ImageUI(getSlotLocation(i+1),
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
        int previousSelectedSlot=selectedSlot;
        if(isten.getInputHandler().isKeyDown(KeyEvent.VK_1)) selectedSlot=1;
        else if(isten.getInputHandler().isKeyDown(KeyEvent.VK_2)) selectedSlot=2;
        else if(isten.getInputHandler().isKeyDown(KeyEvent.VK_3)) selectedSlot=3;
        else if(isten.getInputHandler().isKeyDown(KeyEvent.VK_4)) selectedSlot=4;
        else if(isten.getInputHandler().isKeyDown(KeyEvent.VK_5)) selectedSlot=5;

        ImageUI tmp;
        if(selectedSlot!=previousSelectedSlot) {
            tmp = new ImageUI(getSlotLocation(previousSelectedSlot ), new Vec2(iconSize), "./assets/ui/inventorySlot.png");
            inventoryIcons.set(previousSelectedSlot - 1, tmp);
            tmp.setAlignment(Renderable.CENTER, Renderable.BOTTOM);
            tmp.setVisibility(true);
            tmp.setSortingLayer(-68);
            isten.getRenderer().addRenderable(tmp);

            tmp = new ImageUI(getSlotLocation(selectedSlot ), new Vec2(iconSize), "./assets/ui/inventorySlot_Selected.png");
            inventoryIcons.set(selectedSlot - 1, tmp);
            tmp.setAlignment(Renderable.CENTER, Renderable.BOTTOM);
            tmp.setVisibility(true);
            tmp.setSortingLayer(-68);
            isten.getRenderer().addRenderable(tmp);
        }
    }

    @Override
    public void onDestroy() {

    }
    public void addItem(Item item){
        ImageUI tmp;
        if(storedItems.size()<size){
            storedItems.add(item);
             tmp=new ImageUI(getSlotLocation(storedItems.size()),new Vec2(iconSize,iconSize),item.getImagePath());
            itemIcons.add(tmp);
        }
        else {
            storedItems.set(selectedSlot - 1, item);
             tmp=new ImageUI(getSlotLocation(selectedSlot),new Vec2(iconSize,iconSize),item.getImagePath());
             itemIcons.get(selectedSlot-1).setVisibility(false);
            itemIcons.set(selectedSlot - 1,tmp);
        }
        tmp.setAlignment(Renderable.CENTER,Renderable.BOTTOM);
        tmp.setVisibility(true);
        tmp.setSortingLayer(-69);
        isten.getRenderer().addRenderable(tmp);
    }

    /**
     *
     * @param slot Nr.of the slot, 1-5
     */
    private Vec2 getSlotLocation(int slot){
        int spacing = 25;
        int firstIconXOffset = -((size*iconSize + (size-1)*spacing)/2) + iconSize/2;
        int xOffset = firstIconXOffset + (iconSize + spacing)*slot;
        return new Vec2(xOffset, 50);
    }
}
