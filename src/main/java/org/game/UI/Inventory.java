package main.java.org.game.UI;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Isten;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.updatable.Updatable;
import main.java.org.items.Item;
import main.java.org.items.usable_items.Camambert;
import main.java.org.items.usable_items.Gasmask;
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
    private boolean hasGasMask;
    private ArrayList<Image> explosion;
    private int explosionCount;
    double time;
    private Vec2 prevPos;
    private Camambert camembert;


    public Inventory(int size){
        this.size = size;
        inventoryIcons = new ArrayList<>();
        itemIcons=new ArrayList<>();
        storedItems=new ArrayList<>();
        for (int i = 0; i < 5; i++) {storedItems.add(null);}
        for (int i = 0; i < 5; i++) {itemIcons.add(null);}
        selectedSlot=1;
        hasGasMask=false;
        explosion = new ArrayList<>();
        explosionCount=0;
        time=0;
        prevPos=new Vec2();
        camembert=null;
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
        Image exp=null;
        for(float i = 0.3f; i <= 6.3f; i+=0.2f) {
            exp = new Image(new Vec2(),
                    new Vec2(i, i), "./assets/items/camembert/explosion_camembert.png");
            exp.setSortingLayer(-67);
            exp.setVisibility(false);
            isten.getRenderer().addRenderable(exp);
            explosion.add(exp);
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
        if(isten.getInputHandler().isKeyDown(KeyEvent.VK_F)){
            useSelectedItem();
        }
        if(isten.getInputHandler().isKeyDown(KeyEvent.VK_R)&&storedItems.get(selectedSlot-1)!=null){
            if( storedItems.get(selectedSlot-1) instanceof Gasmask)hasGasMask=false;
            if(storedItems.get(selectedSlot-1) instanceof Camambert)
            {
                camembert=(Camambert)storedItems.get(selectedSlot-1);
                for(int i = 0; i< 30;i++)
                {
                    explosion.get(i).setPosition(isten.getPlayer().getPlayerCollider().getPosition());
                }
                prevPos=isten.getPlayer().getPlayerCollider().getPosition();
                explosionCount++;
                time=0;
            }
            storedItems.get(selectedSlot - 1).dropOnGround(isten.getPlayer().getPlayerCollider().getPosition());
            storedItems.set(selectedSlot - 1, null);
            tmp = new ImageUI(getSlotLocation(selectedSlot ), new Vec2(iconSize), "./assets/ui/inventorySlot_Selected.png");
            inventoryIcons.set(selectedSlot - 1, tmp);
            tmp.setAlignment(Renderable.CENTER, Renderable.BOTTOM);
            tmp.setVisibility(true);
            tmp.setSortingLayer(-68);
            isten.getRenderer().addRenderable(tmp);
            itemIcons.get(selectedSlot-1).setVisibility(false);

        }
        time+=deltaTime;
            if (time > 1 && explosionCount>0) {
                if(explosionCount==1){
                    Room currentRoom = null;
                    for (Room room : isten.getMap().getRooms()) {
                        for (UnitRoom unitRoom : room.getUnitRooms()) {
                            if (prevPos.x >= unitRoom.getPosition().x - 0.5 &&
                                    prevPos.x <= unitRoom.getPosition().x + 0.5 &&
                                    prevPos.y >= unitRoom.getPosition().y - 0.5 &&
                                    prevPos.y <= unitRoom.getPosition().y + 0.5) {
                                currentRoom = room;
                            }
                        }
                    }
                    if (currentRoom != null) {
                        currentRoom.setRoomTypeToGas();
                        for (UnitRoom unitRoom : currentRoom.getUnitRooms()) {
                            unitRoom.setUnitRoomToGasUnitRoom(isten);
                        }
                    }
                }
                if (explosionCount > 0 && explosionCount <= 30) {
                    if (explosionCount > 1) {
                        explosion.get(explosionCount - 2).setVisibility(false);
                        explosion.get(explosionCount - 1).setVisibility(true);
                    } else {
                        explosion.get(0).setVisibility(true);
                    }
                    explosionCount++;
                } else if (explosionCount > 30) {
                    camembert.dropCamembertOnGround();
                    explosion.get(explosionCount - 2).setVisibility(false);
                    explosionCount = 0;
                    time = 0;
                }
            }
    }

    @Override
    public void onDestroy() {

    }
    public void addItem(Item item){
        ImageUI tmp=null;
        for (int i = 0; i < 5; i++) {
            if(storedItems.get(i)==null){
                storedItems.set(i,item);
                tmp=new ImageUI(getSlotLocation(i+1),new Vec2(iconSize-10,iconSize-10),item.getImagePath());
                itemIcons.set(i,tmp);
                break;
            }
        }
        if(tmp==null) {
            if( storedItems.get(selectedSlot-1) instanceof Gasmask)hasGasMask=false;
            storedItems.get(selectedSlot-1).dropOnGround(isten.getPlayer().getPlayerCollider().getPosition());
            storedItems.set(selectedSlot - 1, item);
            itemIcons.get(selectedSlot-1).setVisibility(false);
            tmp=new ImageUI(getSlotLocation(selectedSlot),new Vec2(iconSize-10,iconSize-10),item.getImagePath());
            itemIcons.set(selectedSlot - 1,tmp);
        }
        tmp.setAlignment(Renderable.CENTER,Renderable.BOTTOM);
        tmp.setVisibility(true);
        tmp.setSortingLayer(-69);
        isten.getRenderer().addRenderable(tmp);
        if(item instanceof Gasmask)
        {
            hasGasMask=true;
        }
    }

    /**
     *
     * @param slot Nr.of the slot, 1-5
     */
    private Vec2 getSlotLocation(int slot){
        int spacing = 25;
        int firstIconXOffset = -((size*iconSize + (size-1)*spacing)/2) + iconSize/2;
        int xOffset = firstIconXOffset + (iconSize + spacing)*(slot-1);
        return new Vec2(xOffset, 50);
    }

    public Vec2 getStoringSlotPosition(Item item){
        for(int i = 0; i < storedItems.size(); i++){
            if(storedItems.get(i) == item){
                return inventoryIcons.get(i).getPosition();
            }
        }
        return new Vec2();
    }

    public void useSelectedItem(){
        Item selectedItem = storedItems.get(selectedSlot-1);
        if(selectedItem != null){
            selectedItem.use();
        }
    }

    public List<Item> getStoredItems() {
        return storedItems;
    }
    public int getStoredItemsSize()
    {
        int count=0;
        for(int i = 0; i < 5 ; i++)
        {
            if(storedItems.get(i) !=null)
            {
                count++;
            }
        }
        return count;
    }
    public void dropAllItems(Isten isten)
    {
        storedItems.clear();
        for (int i = 0; i < 5; i++) {storedItems.add(null);}
        for (Image im : itemIcons) {
            isten.getRenderer().deleteRenderable(im);
        }
        selectedSlot=1;
        hasGasMask=false;
    }
    public boolean getExistenceOfGasMask()
    {
        return hasGasMask;
    }
    public void removeStoredItem(Item item){
        storedItems.remove(item);
    }

    public void destroyGasMask(){
        if (hasGasMask){
            for (int i = 0; i < storedItems.size(); i++){
                if (storedItems.get(i) != null && storedItems.get(i).getClass().equals(Gasmask.class)){
                    storedItems.remove(i);
                    storedItems.add(i, null);
                    isten.getRenderer().deleteRenderable(itemIcons.get(i));
                }
            }
            hasGasMask = false;
        }
    }
}
