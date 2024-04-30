package main.java.org.items;

import main.java.org.entities.villain.Villain;
import main.java.org.game.Isten;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.RoomType;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static java.lang.Math.sqrt;

public class ItemManager extends Updatable {
    private Isten isten;
    private ArrayList<Item> items = new ArrayList<>();
    public void addItem(Item item){
        items.add(item);
    }

    @Override
    public void onStart(Isten isten) {
        this.isten=isten;
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {

        Room currentRoom = null;
        for (Updatable u : isten.getUpdatables()) {
            if (u.getClass().equals(Villain.class)) {
                for (Room room : isten.getMap().getRooms()) {
                    for (UnitRoom unitRoom : room.getUnitRooms()) {
                        if (isten.getPlayer().getPlayerCollider().getPosition().x >= unitRoom.getPosition().x - 0.5 &&
                                isten.getPlayer().getPlayerCollider().getPosition().x <= unitRoom.getPosition().x + 0.5 &&
                                isten.getPlayer().getPlayerCollider().getPosition().y >= unitRoom.getPosition().y - 0.5 &&
                                isten.getPlayer().getPlayerCollider().getPosition().y <= unitRoom.getPosition().y + 0.5) {
                            currentRoom = room;
                        }
                    }
                }
            }
        }
        if(currentRoom!=null && currentRoom.getRoomType()== RoomType.GAS && isten.getInventory().getExistenceOfGasMask() || currentRoom!=null && currentRoom.getRoomType()!=RoomType.GAS)
        {
            if(isten.getInputHandler().isKeyDown(KeyEvent.VK_E)){
                Vec2 playerPostion = isten.getPlayer().getPlayerCollider().getPosition();
                for(int i = 0; i < items.size(); i++){
                    if(items.get(i).location== Item.Location.GROUND) {
                        Vec2 playerItemVector = Vec2.subtract(playerPostion, items.get(i).getPosition());
                        double playerItemDistance = sqrt(Vec2.dot(playerItemVector, playerItemVector));
                        if (playerItemDistance <= 0.3) {
                            items.get(i).pickUpInInventory();
                            break;
                        }

                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {}

    @Override
    public ArrayList<Item> getItems() { return items; }
}
