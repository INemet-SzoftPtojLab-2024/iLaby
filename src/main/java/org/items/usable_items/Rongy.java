package main.java.org.items.usable_items;

import main.java.org.entities.player.Player;
import main.java.org.entities.villain.Villain;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.updatable.Updatable;
import main.java.org.items.Item;
import main.java.org.linalg.Vec2;
import main.java.org.networking.Packet13ItemDropped;
import main.java.org.networking.Packet18Rongy;

public class Rongy extends Item {
    private Image stink;
    private double impactTime;
    public Rongy(Isten isten){
        super(isten,new Vec2(0.5f,0.4f));
        imagePath="./assets/items/item_rongy.png";
        image = new Image(new Vec2(-10,-10), scale, imagePath);
        isten.getRenderer().addRenderable(image);
        image.setVisibility(false);
        stink=new Image(new Vec2(),new Vec2(0.8f,0.8f),"./assets/items/stink_rongy.png");
        stink.setVisibility(false);
        isten.getRenderer().addRenderable(stink);
        impactTime=20;//sec

    }
    @Override
    public void use(Player player, double deltaTime){
        used=true;
        dropOnGround(player.getPlayerCollider().getPosition());

        Packet13ItemDropped packet = new Packet13ItemDropped(getItemIndex(), position, player.getPlayerName().getText(), inSlot+1, false);
        packet.writeData(isten.getSocketClient());

        Vec2 playerPosition = player.getPlayerCollider().getPosition();
        Packet18Rongy packet18Rongy = new Packet18Rongy(playerPosition.x, playerPosition.y, getItemIndex(), player.getPlayerName().getText(), impactTime);
        packet18Rongy.writeData(isten.getSocketClient());

        if(player.localPlayer) player.getInventory().deleteItem(this);
    }

    public void stinkAnimation() {
        stink.setPosition(Vec2.sum(position,new Vec2(0.0f,0.25f)));
        stink.setVisibility(true);
        Runnable stinkThread=()->{//thread azért kell, hogy ha már nem hat a rongy, akkor eltüntesse a füstfelhőt
            try {
                Thread.sleep((long) (impactTime*1000));
                stink.setVisibility(false);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        Thread t=new Thread(stinkThread);
        t.start();
    }

    private Room getRoom() {
        Room currentRoom = null;
        for (Room room : isten.getMap().getRooms()) {
            for (UnitRoom unitRoom : room.getUnitRooms()) {
                if (position.x >= unitRoom.getPosition().x - 0.5 &&
                        position.x <= unitRoom.getPosition().x + 0.5 &&
                        position.y >= unitRoom.getPosition().y - 0.5 &&
                        position.y <= unitRoom.getPosition().y + 0.5) {
                    currentRoom = room;
                }
            }
        }
        return currentRoom;
    }
}
