package main.java.org.items.usable_items;

import main.java.org.entities.player.Player;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.RoomType;
import main.java.org.game.Map.UnitRoom;
import main.java.org.items.Item;
import main.java.org.linalg.Vec2;
import main.java.org.networking.Packet17Camembert;

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
                    new Vec2(i, i), "./assets/items/explosion_camembert.png");
            exp.setSortingLayer(-67);
            exp.setVisibility(false);
            isten.getRenderer().addRenderable(exp);
            explosion.add(exp);
        }
    }
    @Override
    public void use(Player player, double deltaTime){
        used=true;
        dropOnGround(player.getPlayerCollider().getPosition());

        Packet17Camembert packet17Camembert = new Packet17Camembert(player.getPlayerCollider().getPosition().x,
                player.getPlayerCollider().getPosition().y);
        packet17Camembert.writeData(isten.getSocketClient());

        if (!isExplosionPositionCalculated){
            for(int i = 0; i < 30; i++) {
                explosion.get(i).setPosition(position);
            }
            prevPos = player.getPlayerCollider().getPosition();
            isExplosionPositionCalculated = true;
        }
        time += deltaTime;

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

