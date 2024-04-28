package main.java.org.items.usable_items;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.UnitRoom;
import main.java.org.items.Item;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;

public class Camembert extends Item {
    private final ArrayList<Image> explosion;
    private int explosionCount;
    double time;
    private Vec2 prevPos;
    private boolean isExplosionPositionCalculated;

    public Camembert(Isten isten){
        super(isten,new Vec2(0.5f,0.4f));
        imagePath="./assets/items/item_camembert.png";
        image = new Image(new Vec2(-10,-10), scale, imagePath);
        isten.getRenderer().addRenderable(image);
        image.setVisibility(false);

        explosion = new ArrayList<>();
        explosionCount=0;
        time = 0;
        prevPos = null;
        isExplosionPositionCalculated = false;

        Image exp;
        for(float i = 0.3f; i <= 6.3f; i+=0.2f) {
            exp = new Image(new Vec2(),
                    new Vec2(i, i), "./assets/items/camembert/explosion_camembert.png");
            exp.setSortingLayer(-67);
            exp.setVisibility(false);
            isten.getRenderer().addRenderable(exp);
            explosion.add(exp);
        }
    }
    public void useCamembert(double deltaTime){
        dropOnGround(isten.getPlayer().getPlayerCollider().getPosition());
        if (!isExplosionPositionCalculated){
            for(int i = 0; i < 30; i++) {
                explosion.get(i).setPosition(position);
            }
            isExplosionPositionCalculated = true;
        }
        time += deltaTime;
        if (time > 1) {
            if (explosionCount == 0) {
                prevPos = isten.getPlayer().getPlayerCollider().getPosition();
            }

            if (explosionCount == 1) {
                Room currentRoom = getPrevRoom();
                if (currentRoom != null) {
                    currentRoom.setRoomTypeToGas();
                    for (UnitRoom unitRoom : currentRoom.getUnitRooms()) {
                        unitRoom.setUnitRoomToGasUnitRoom(isten);
                    }
                }
            }

            explosionCount++;
            if (explosionCount <= 30) {
                if (explosionCount > 1) {
                    explosion.get(explosionCount - 2).setVisibility(false);
                    explosion.get(explosionCount - 1).setVisibility(true);
                }
                else {
                    explosion.get(0).setVisibility(true);
                }
            }
            else {
                image.setVisibility(false);
                isten.getRenderer().deleteRenderable(image);
                for(Image image : explosion){
                    image.setVisibility(false);
                    isten.getRenderer().deleteRenderable(image);
                }
                explosionCount = 0;
                time = 0;
                isten.getInventory().setCamembertTriggered(false);
                isten.getInventory().setCamembert(null);
                isExplosionPositionCalculated = false;
                isten.getInventory().removeCamembert();
            }
        }
    }

    private Room getPrevRoom() {
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
        return currentRoom;
    }
}
