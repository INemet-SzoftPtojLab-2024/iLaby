package main.java.org.items.usable_items;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Isten;
import main.java.org.game.UI.Inventory;
import main.java.org.items.Item;
import main.java.org.linalg.Vec2;

public class Sorospohar extends Item {
    private ImageUI capacityBar;
    private ImageUI capacityBarBackground;
    private float capacity;
    private boolean equipped;

    public Sorospohar(Isten isten){
        super(isten,new Vec2(0.4f,0.5f));
        imagePath="./assets/items/item_sorospohar.png";
        image = new Image(new Vec2(-10,-10), scale, imagePath);
        isten.getRenderer().addRenderable(image);
        image.setVisibility(false);

        capacity = 100.0f;
        equipped = false;
        capacityBar = new ImageUI(new Vec2(-10, -10), new Vec2(60.0f / 100.0f * capacity, 40), "./assets/ui/gasmask_capacity_bar.png");
        capacityBarBackground = new ImageUI(new Vec2(-10, -10), new Vec2(65, 50), "./assets/ui/gasmask_capacity_bar_background.png");
        capacityBar.setVisibility(false);
        capacityBarBackground.setVisibility(false);

        isten.getRenderer().addRenderable(capacityBar);
        isten.getRenderer().addRenderable(capacityBarBackground);
    }
    public void pickUpInInventory() {
        super.pickUpInInventory();

        capacityBar.setAlignment(Renderable.CENTER, Renderable.BOTTOM);
        capacityBarBackground.setAlignment(Renderable.CENTER, Renderable.BOTTOM);
        capacityBar.setSortingLayer(-80);
        capacityBarBackground.setSortingLayer(-79);

        Vec2 slotPosition = isten.getPlayer().getInventory().getStoringSlotPosition(this);
        if (slotPosition.x == 0.0 && slotPosition.y == 0.0) return;
        Vec2 barPosition = new Vec2(slotPosition.x, slotPosition.y + 35);

        capacityBar.setPosition(barPosition);
        capacityBarBackground.setPosition(barPosition);
        capacityBar.setVisibility(true);
        capacityBarBackground.setVisibility(true);
    }
    @Override
    public void use(double deltaTime) {
        if (capacity == 100.0f) {
            capacityBarBackground.setVisibility(true);
            capacityBar.setVisibility(true);
        }
        float usageRate = 25.0f;
        capacity -= (float) (deltaTime * usageRate);
        if (capacity <= 0) {
            Inventory inventory = isten.getPlayer().getInventory();
            capacity = 0;
            deleteCapacityBar();
            int index = 0;
            for (; index < inventory.getSize(); index++) {
                if (inventory.getStoredItems().get(index) instanceof Sorospohar) break;
            }
            inventory.getStoredItems().remove(index);
            inventory.getStoredItems().add(index, null);
            isten.getRenderer().deleteRenderable(inventory.getItemIcons().get(index));
            //inventory.setGasmaskEquipped(false);
        }
        resizeBar(capacity);
    }
    @Override
    public void dropOnGround(Vec2 pos) {
        super.dropOnGround(pos);
        if (capacityBar != null && capacityBarBackground != null) {
            capacityBar.setVisibility(false);
            capacityBarBackground.setVisibility(false);
            isten.getPlayer().getInventory().setGasmaskEquipped(false);
            equipped = false;
        }
    }
    public void setCapacityBar() {

        capacityBar.setVisibility(true);
        capacityBarBackground.setVisibility(true);
    }
    public void deleteCapacityBar() {
        capacityBar.setVisibility(false);
        capacityBarBackground.setVisibility(false);
    }
    public void resizeBar(float percent) {
        float width = 60.0f / 100.0f * percent;
        capacityBar.setScale(new Vec2(width, 40));
    }
}
