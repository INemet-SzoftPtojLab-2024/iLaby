package main.java.org.items.usable_items;

import main.java.org.entities.player.Player;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.Renderable;
import main.java.org.game.Graphics.TextUI;
import main.java.org.game.Isten;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.UI.Inventory;
import main.java.org.items.Item;
import main.java.org.linalg.Vec2;
import main.java.org.networking.Packet19Transistor;
import main.java.org.networking.PlayerMP;

public class Transistor extends Item {

    private TextUI countText;
    private Image activatedImage;

    private boolean isActivated;

    public Transistor(Isten isten){
        super(isten,new Vec2(0.4f,0.5f));
        imagePath="./assets/items/item_transistor.png";
        image = new Image(new Vec2(-10,-10), scale, imagePath);
        activatedImage = new Image(new Vec2(-10,-10), scale, "./assets/items/item_transistor_activated.png");
        isten.getRenderer().addRenderable(image);
        isten.getRenderer().addRenderable(activatedImage);
        image.setVisibility(false);
        activatedImage.setVisibility(false);

        countText = new TextUI("2", new Vec2(0,0), "./assets/Monocraft.ttf", 15, 255, 255, 255);
        countText.setVisibility(false);
        countText.setAlignment(Renderable.CENTER, Renderable.BOTTOM);
        countText.setSortingLayer(-80);
        countText.setShadowOn(false);
        isten.getRenderer().addRenderable(countText);

    }

    @Override
    public void pickUpInInventory(PlayerMP player, int selectedSlotByClient) {
        super.pickUpInInventory(player, selectedSlotByClient);


        if(activatedImage!=null) activatedImage.setVisibility(true);
        if(player.localPlayer) setUI(player);
    }

    private void setUI(PlayerMP player) {
        Inventory inv = player.getInventory();
        Vec2 slotPosition = inv.getStoringSlotPosition(this);
        Vec2 textPosition = new Vec2(slotPosition.x -10, slotPosition.y-7);
        if(!isActivated){
            countText.setPosition(textPosition);
            countText.setVisibility(true);
        }
    }

    @Override
    public void use(Player player, double deltatime){
        //exception handling a szoba szama miatt
         Room r = getActiveTransistorRoom();
         if(r != null && (r.getMaxPlayerCount()<= r.getPlayerCount())){
                 System.err.println("Nem volt eleg hely a szobaban a tranzisztor hasznalatakor");
                 return;
         }
        if(!isActivated){
            countText.setVisibility(false);
            Vec2 playerPosition = player.getPlayerCollider().getPosition();
            activatedImage.setPosition(playerPosition);
            activatedImage.setVisibility(true);
            isActivated=true;
            Packet19Transistor packet19Transistor = new Packet19Transistor(playerPosition,getItemIndex());
            packet19Transistor.writeData(isten.getSocketClient());
        }
        else{
            player.getPlayerCollider().setPosition(activatedImage.getPosition());
        }

    }
    @Override
    public void dropOnGround(Vec2 pos){
        super.dropOnGround(pos);
        activatedImage.setVisibility(false);
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
    //For testing
    public TextUI getCountText() {
        return countText;
    }
    //For testing
    public Image getActivatedImage() {
        return activatedImage;
    }

    public boolean isActivated() { return isActivated; }
    public void setActivated(boolean bool) { isActivated = bool; }
}
