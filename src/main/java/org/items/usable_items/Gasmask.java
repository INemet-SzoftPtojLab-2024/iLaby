package main.java.org.items.usable_items;

import main.java.org.entities.player.Player;
import main.java.org.game.Graphics.*;
import main.java.org.game.Isten;
import main.java.org.game.UI.Inventory;
import main.java.org.items.Item;
import main.java.org.linalg.Vec2;
import main.java.org.networking.PlayerMP;

public class Gasmask extends Item {
    private ImageUI capacityBar;
    private ImageUI capacityBarBackground;
    private float capacity;
    private boolean equipped;

    public Gasmask(Isten isten) {
        super(isten, new Vec2(0.4f, 0.45f));
        imagePath = "./assets/items/item_gasmask.png";
        image = new Image(new Vec2(-10, -10), scale, imagePath);
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

    public void pickUpInInventory(PlayerMP player, int selectedSlotByClient) {
        super.pickUpInInventory(player, selectedSlotByClient);

        if(player.localPlayer) setUI(player);
    }

    private void setUI(PlayerMP player) {
        capacityBar.setAlignment(Renderable.CENTER, Renderable.BOTTOM);
        capacityBarBackground.setAlignment(Renderable.CENTER, Renderable.BOTTOM);
        capacityBar.setSortingLayer(-80);
        capacityBarBackground.setSortingLayer(-79);

        Vec2 slotPosition = player.getInventory().getStoringSlotPosition(this);
        if (slotPosition.x == 0.0 && slotPosition.y == 0.0) return;
        Vec2 barPosition = new Vec2(slotPosition.x, slotPosition.y + 35);

        capacityBar.setPosition(barPosition);
        capacityBarBackground.setPosition(barPosition);
        capacityBar.setVisibility(true);
        capacityBarBackground.setVisibility(true);
    }

    @Override
    public void use(Player player, double deltaTime) {
        if (capacity == 100.0f) {
            capacityBarBackground.setVisibility(true);
            capacityBar.setVisibility(true);
        }
        float usageRate = 5.0f;
        capacity -= (float) (deltaTime * usageRate);
        if (capacity <= 0) {
            Inventory inventory = player.getInventory();
            capacity = 0;
            deleteCapacityBar();
            int index = 0;
            System.out.println(inventory.getSize());
            for (int i = 0; i < inventory.getSize(); i++) {
                if(inventory.getStoredItems().get(index) != null) System.out.println("has item: " + inventory.getStoredItems().get(index).getClass());
                if (inventory.getStoredItems().get(index) instanceof Gasmask) break;
                index++;
            }
            inventory.getStoredItems().set(index, null);
            isten.getRenderer().deleteRenderable(inventory.getItemIcons().get(index));
            inventory.setGasmaskEquipped(false);
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

    public float getCapacity() { return capacity; }

    public void setCapacity(float capacity) { this.capacity = capacity; }

}
