package main.java.org.items.usable_items;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Graphics.TextUI;
import main.java.org.game.Isten;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.UI.Inventory;
import main.java.org.items.Item;
import main.java.org.linalg.Vec2;

public class Transistor extends Item {

    private TextUI countText;
    private Image activatedImage;

    boolean used = false;
    public Transistor(Isten isten){
        super(isten,new Vec2(0.4f,0.5f));
        imagePath="./assets/items/item_transistor.png";
        image = new Image(new Vec2(-10,-10), scale, imagePath);
        activatedImage = new Image(new Vec2(-10,-10), scale, "./assets/items/item_transistor_activated.png");
        isten.getRenderer().addRenderable(image);
        isten.getRenderer().addRenderable(activatedImage);
        image.setVisibility(false);
        activatedImage.setVisibility(false);

        countText = new TextUI("2", new Vec2(0,0), "./assets/Bavarian.otf", 15, 255, 255, 255);
        countText.setVisibility(false);
        countText.setAlignment(Renderable.CENTER, Renderable.BOTTOM);
        countText.setSortingLayer(-80);
        countText.setShadowOn(false);
        isten.getRenderer().addRenderable(countText);

    }

    @Override
    public void pickUpInInventory(){
        super.pickUpInInventory();
        Inventory inv = isten.getInventory();
        Vec2 slotPosition = inv.getStoringSlotPosition(this);
        Vec2 textPosition = new Vec2(slotPosition.x -10, slotPosition.y-7);
        if(!used){
            countText.setPosition(textPosition);
            countText.setVisibility(true);
        }

    }

    @Override
    public void use(double deltatime){
        //exception handling a szoba szama miatt
         Room r = getActiveTransistorRoom();
         if(r != null && (r.getMaxPlayerCount()<= r.getPlayerCount())){
                 System.err.println("Nem volt eleg hely a szobaban a tranzisztor hasznalatakor");
                 return;
         }
        if(!used){
            countText.setVisibility(false);
            Vec2 playerPosition = isten.getPlayer().getPlayerCollider().getPosition();
            activatedImage.setPosition(playerPosition);
            activatedImage.setVisibility(true);
            used=true;
        }
        else{
            isten.getPlayer().getPlayerCollider().setPosition(activatedImage.getPosition());
        }

    }
    @Override
    public void dropOnGround(Vec2 pos){
        super.dropOnGround(pos);
        countText.setVisibility(false);
    }
    public Room getActiveTransistorRoom(){
        UnitRoom[][] unitRooms= isten.getMap().getUnitRooms();
        for(int i = 0; i < unitRooms.length;i++){
                    for(int j = 0; j<unitRooms[i].length;j++){
                        if (activatedImage.getPosition().x >= unitRooms[i][j].getPosition().x - 0.5 &&
                                activatedImage.getPosition().x <= unitRooms[i][j].getPosition().x + 0.5 &&
                                activatedImage.getPosition().y >= unitRooms[i][j].getPosition().y - 0.5 &&
                                activatedImage.getPosition().y <= unitRooms[i][j].getPosition().y + 0.5)
                        {
                            return unitRooms[i][j].getOwnerRoom();
                }
            }
        }
        return null;
    }

}
