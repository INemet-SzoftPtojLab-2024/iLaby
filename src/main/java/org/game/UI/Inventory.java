package main.java.org.game.UI;

import main.java.org.entities.player.Player;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.items.Item;
import main.java.org.items.usable_items.Camembert;
import main.java.org.items.usable_items.Gasmask;
import main.java.org.items.usable_items.Sorospohar;
import main.java.org.items.usable_items.Tvsz;
import main.java.org.linalg.Vec2;
import main.java.org.networking.Packet12ItemPickedUp;
import main.java.org.networking.Packet13ItemDropped;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Inventory extends Updatable {
    private ArrayList<ImageUI> inventoryIcons;
    private ArrayList<ImageUI> itemIcons;
    private List<Item> storedItems;
    private int size;
    private final int iconSize = 50;
    /**
     * 1-5
     */
    private int selectedSlot;
    private Isten isten;
    private boolean hasGasmaskEquipped;
    private boolean camembertTriggered;
    private Camembert camembert;
    //Azert van ra szukseg, hogy ne haljon meg a player, ha 1 Tvsz charge-dzsal bemegyek egy gegnerhez
    private boolean canAvoidVillain = false;
    private Player owner;

    public Inventory(int size, Player owner) {
        this.size = size;
        inventoryIcons = new ArrayList<>();
        itemIcons = new ArrayList<>();
        storedItems = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            storedItems.add(null);
        }
        for (int i = 0; i < 5; i++) {
            itemIcons.add(null);
        }
        selectedSlot = 1;
        hasGasmaskEquipped = false;
        this.owner = owner;
    }

    @Override
    public void onStart(Isten isten) {
        this.isten = isten;

        ImageUI inventoryIcon = new ImageUI(getSlotLocation(1),
                new Vec2(iconSize, iconSize), "./assets/ui/inventorySlot_Selected.png");
        inventoryIcon.setAlignment(Renderable.CENTER, Renderable.BOTTOM);
        inventoryIcon.setSortingLayer(-68);
        inventoryIcon.setVisibility(true);
        isten.getRenderer().addRenderable(inventoryIcon);
        inventoryIcons.add(inventoryIcon);
        for (int i = 1; i < size; i++) {
            //System.out.println(xOffset);
            inventoryIcon = new ImageUI(getSlotLocation(i + 1),
                    new Vec2(iconSize, iconSize), "./assets/ui/inventorySlot.png");
            inventoryIcon.setAlignment(Renderable.CENTER, Renderable.BOTTOM);
            inventoryIcon.setSortingLayer(-68);
            inventoryIcon.setVisibility(true);
            isten.getRenderer().addRenderable(inventoryIcon);
            inventoryIcons.add(inventoryIcon);
        }

    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        int previousSelectedSlot = selectedSlot;
        if (isten.getInputHandler().isKeyDown(KeyEvent.VK_1)) selectedSlot = 1;
        else if (isten.getInputHandler().isKeyDown(KeyEvent.VK_2)) selectedSlot = 2;
        else if (isten.getInputHandler().isKeyDown(KeyEvent.VK_3)) selectedSlot = 3;
        else if (isten.getInputHandler().isKeyDown(KeyEvent.VK_4)) selectedSlot = 4;
        else if (isten.getInputHandler().isKeyDown(KeyEvent.VK_5)) selectedSlot = 5;

        ImageUI tmp;
        if (selectedSlot != previousSelectedSlot) {
            tmp = new ImageUI(getSlotLocation(previousSelectedSlot), new Vec2(iconSize), "./assets/ui/inventorySlot.png");
            inventoryIcons.set(previousSelectedSlot - 1, tmp);
            tmp.setAlignment(Renderable.CENTER, Renderable.BOTTOM);
            tmp.setVisibility(true);
            tmp.setSortingLayer(-68);
            isten.getRenderer().addRenderable(tmp);

            tmp = new ImageUI(getSlotLocation(selectedSlot), new Vec2(iconSize), "./assets/ui/inventorySlot_Selected.png");
            inventoryIcons.set(selectedSlot - 1, tmp);
            tmp.setAlignment(Renderable.CENTER, Renderable.BOTTOM);
            tmp.setVisibility(true);
            tmp.setSortingLayer(-68);
            isten.getRenderer().addRenderable(tmp);
        }
        if (owner.localPlayer && isten.getInputHandler().isKeyDown(KeyEvent.VK_F)) {
            useSelectedItem(deltaTime);
        }
        if (owner.localPlayer && isten.getInputHandler().isKeyReleased(KeyEvent.VK_R) && storedItems.get(selectedSlot - 1) != null) {
            Item actItem = storedItems.get(selectedSlot - 1);
            Vec2 actPos = isten.getPlayer().getPlayerCollider().getPosition();
            //actItem.dropOnGround(actPos);
            hasGasmaskEquipped = getExistenceOfGasMask();

            Packet13ItemDropped packet = new Packet13ItemDropped(actItem.getItemIndex(), actPos, owner.getPlayerName().getText(), selectedSlot);
            packet.writeData(isten.getSocketClient());
            //isten.getSocketClient().sendData(("13" + actItem.getItemIndex() + "," + actPos.x + "," + actPos.y).getBytes());
            if(actItem.getClass() == Gasmask.class)
                isten.getSocketClient().sendData(("14" + actItem.getItemIndex() + "," + ((Gasmask) actItem).getCapacity()).getBytes());


            if(owner.localPlayer) {
                tmp = new ImageUI(getSlotLocation(selectedSlot), new Vec2(iconSize), "./assets/ui/inventorySlot_Selected.png");
                inventoryIcons.set(selectedSlot - 1, tmp);
                tmp.setAlignment(Renderable.CENTER, Renderable.BOTTOM);
                tmp.setVisibility(true);
                tmp.setSortingLayer(-68);
                isten.getRenderer().addRenderable(tmp);
                itemIcons.get(selectedSlot - 1).setVisibility(false);
            }

        }
        if (camembertTriggered && camembert != null) {
            camembert.use(owner, deltaTime);
        }
    }

    public void dropItem(Item item, Vec2 pos, int selectedSlot) {
        item.dropOnGround(pos);
        storedItems.set(selectedSlot - 1, null);
    }

    @Override
    public void onDestroy() {

    }

    public void addItem(Item item) {

        ImageUI tmp = null;
        for (int i = 0; i < 5; i++) {
            if (storedItems.get(i) == null) {
                storedItems.set(i, item);
                tmp = new ImageUI(getSlotLocation(i + 1), new Vec2(iconSize - 10, iconSize - 10), item.getImagePath());
                itemIcons.set(i, tmp);
                if (item.getClass().equals(Gasmask.class)) {
                    hasGasmaskEquipped = true;
                }
                break;
            }
        }
        if (isFull()) {
            storedItems.get(selectedSlot - 1).dropOnGround(isten.getPlayer().getPlayerCollider().getPosition());
            storedItems.set(selectedSlot - 1, item);
            if (item.getClass().equals(Gasmask.class)) {
                hasGasmaskEquipped = true;
            }
            itemIcons.get(selectedSlot - 1).setVisibility(false);
            tmp = new ImageUI(getSlotLocation(selectedSlot), new Vec2(iconSize - 10, iconSize - 10), item.getImagePath());
            itemIcons.set(selectedSlot - 1, tmp);
        }
        tmp.setAlignment(Renderable.CENTER, Renderable.BOTTOM);
        tmp.setVisibility(true);
        tmp.setSortingLayer(-69);
        isten.getRenderer().addRenderable(tmp);

    }
    public void addItemToClient(Item item, int selectedSlotByClient) {

        for (int i = 0; i < 5; i++) {
            if (storedItems.get(i) == null) {
                storedItems.set(i, item);
                if (item.getClass().equals(Gasmask.class)) {
                    hasGasmaskEquipped = true;
                }
                break;
            }
        }
        if (isFull()) {
            //storedItems.get(selectedSlot - 1).dropOnGround(isten.getPlayer().getPlayerCollider().getPosition());
            storedItems.set(selectedSlotByClient - 1, item);
            if (item.getClass().equals(Gasmask.class)) {
                hasGasmaskEquipped = true;
            }
        }
    }


    private boolean isFull() {
        for(int i = 0; i < size; i++) {
            if(storedItems.get(i) == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param slot Nr.of the slot, 1-5
     */
    private Vec2 getSlotLocation(int slot) {
        int spacing = 25;
        int firstIconXOffset = -((size * iconSize + (size - 1) * spacing) / 2) + iconSize / 2;
        int xOffset = firstIconXOffset + (iconSize + spacing) * (slot - 1);
        return new Vec2(xOffset, 50);
    }

    public Vec2 getStoringSlotPosition(Item item) {
        for (int i = 0; i < storedItems.size(); i++) {
            if (storedItems.get(i) == item) {
                return inventoryIcons.get(i).getPosition();
            }
        }
        return new Vec2();
    }

    public void useSelectedItem(double deltatime) {
        Item selectedItem = storedItems.get(selectedSlot - 1);
        if (selectedItem != null && selectedItem.getClass().equals(Camembert.class)) {
            camembert = (Camembert) selectedItem;
            camembertTriggered = true;
        } else if (selectedItem != null) {
            selectedItem.use(owner, deltatime);
        }
    }

    public boolean getExistenceOfGasMask() {
        for (int i = 0; i < 5; i++) {
            if (storedItems.get(i) instanceof Gasmask) {
                return true;
            }
        }
        return false;
    }

    public void dropAllItems(Isten isten) {
        storedItems.clear();
        for (int i = 0; i < 5; i++) {
            storedItems.add(null);
        }
        for (Image im : itemIcons) {
            isten.getRenderer().deleteRenderable(im);
        }
        selectedSlot = 1;
        hasGasmaskEquipped = false;
    }

    public void deleteItem(Item item) {
        int index = 0;
        for (; index < storedItems.size(); index++) {
            if (storedItems.get(index) == item) break;
        }
        isten.getRenderer().deleteRenderable(itemIcons.get(index));
        storedItems.set(index, null);
        itemIcons.set(index, null);
    }

    //Megnézi van-e item, ami megvéd a gonoszoktól, ha talál használja és true-val tér vissza, amúgy nem azzal
    public boolean avoidVillain(double deltaTime) {
        if (canAvoidVillain) return true;
        for (Item item : storedItems) {
            if (item instanceof Tvsz) {
                item.use(owner, deltaTime);
                return true;
            }
            if (item instanceof Sorospohar) {
                item.use(owner, deltaTime);
                return true;
            }
        }
        return false;
    }

    public List<Item> getStoredItems() {
        return storedItems;
    }

    public int getSize() {
        return size;
    }

    public void setGasmaskEquipped(boolean hasGasmaskEquipped) {
        this.hasGasmaskEquipped = hasGasmaskEquipped;
    }

    public ArrayList<ImageUI> getItemIcons() {
        return itemIcons;
    }

    public void removeCamembert() {
        for (int i = 0; i < storedItems.size(); i++) {
            if (storedItems.get(i) != null && storedItems.get(i).getClass().equals(Camembert.class)) {
                storedItems.remove(i);
                storedItems.add(i, null);
                isten.getRenderer().deleteRenderable(itemIcons.get(i));
            }
        }
    }

    public void setCamembertTriggered(boolean camembertTriggered) {
        this.camembertTriggered = camembertTriggered;
    }

    public void setCamembert(Camembert camembert) {
        this.camembert = camembert;
    }

    public void setCanAvoidVillain(boolean value) {
        canAvoidVillain = value;
    }

    public void resetShouldUseChargeForTvsz() {
        for (Item i : storedItems) {
            if (i instanceof Tvsz) {
                ((Tvsz) i).setShouldUseCharge(true);
            }
        }
    }


    public int getSelectedSlot() {
        return selectedSlot;
    }
}
