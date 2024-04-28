package main.java.org.items.usable_items;

import lombok.Getter;
import lombok.Setter;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Graphics.TextUI;
import main.java.org.game.Isten;
import main.java.org.game.UI.Inventory;
import main.java.org.items.Item;
import main.java.org.linalg.Vec2;

public class Gasmask extends Item {
    private ImageUI capacityBar;
    private ImageUI capacityBarBackground;
    private float capacity;
    private boolean equipped;
    public Gasmask(Isten isten){
        super(isten,new Vec2(0.4f,0.45f));
        imagePath="./assets/items/item_gasmask.png";
        image = new Image(new Vec2(-10,-10), scale, imagePath);
        isten.getRenderer().addRenderable(image);
        image.setVisibility(false);
        capacity = 100.0f;
        equipped = false;
        capacityBar = new ImageUI();
        capacityBarBackground = new ImageUI();
        capacityBar.setVisibility(false);
        capacityBarBackground.setVisibility(false);
    }
    @Override
    public void pickUpInInventory(){
        super.pickUpInInventory();
        Inventory inv = isten.getInventory();

        Vec2 slotPosition = inv.getStoringSlotPosition(this);
        Vec2 barPosition = new Vec2(slotPosition.x, slotPosition.y + 35);


        capacityBar = new ImageUI(null, new Vec2(60.0f / 100.0f * capacity,40), "./assets/ui/gasmask_capacity_bar.png");
        capacityBarBackground = new ImageUI(null, new Vec2(65,50), "./assets/ui/gasmask_capacity_bar_background.png");
        capacityBar.setAlignment(Renderable.CENTER, Renderable.BOTTOM);
        capacityBarBackground.setAlignment(Renderable.CENTER, Renderable.BOTTOM);
        capacityBar.setSortingLayer(-80);
        capacityBarBackground.setSortingLayer(-79);
        isten.getRenderer().addRenderable(capacityBar);
        isten.getRenderer().addRenderable(capacityBarBackground);

        capacityBar.setPosition(barPosition);
        capacityBarBackground.setPosition(barPosition);
        capacityBar.setVisibility(false);
        capacityBarBackground.setVisibility(false);
    }
    @Override
    public void dropOnGround(Vec2 pos){
        super.dropOnGround(pos);
        if (capacityBar != null && capacityBarBackground != null){
            capacityBar.setVisibility(false);
            capacityBarBackground.setVisibility(false);
            equipped = false;
        }
    }
    public void useMask(double deltaTime) {
        if (equipped){
            float usageRate = 5.0f;
            capacity -= (float) (deltaTime * usageRate);
            if (capacity <= 0) {
                capacity = 0;
                capacityBar.setVisibility(false);
                capacityBarBackground.setVisibility(false);
            }
            resizeBar(capacity);
        }
    }
    @Override
    public void use(){
        if (!isten.getInventory().getExistenceOfGasMask()) {
            equipped = true;
            isten.getInventory().setGasmaskEquipped(true);
            capacityBar.setVisibility(true);
            capacityBarBackground.setVisibility(true);
        }
    }
    public void resizeBar(float percent){
        float width = 60.0f / 100.0f * percent;
        capacityBar.setScale(new Vec2(width, 40));
    }

    public float getCapacity() {
        return capacity;
    }

    public boolean isEquipped() {
        return equipped;
    }
     public void setEquipped(boolean b){
        equipped = b;
     }
}
