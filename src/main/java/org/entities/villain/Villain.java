package main.java.org.entities.villain;

import main.java.org.entities.Entity;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.Text;
import main.java.org.game.Isten;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.physics.Collider;
import main.java.org.linalg.Vec2;

import main.java.org.game.Map.Map;
import main.java.org.game.Map.Room;

import java.util.ArrayList;
import java.util.Collections;

import java.util.Random;

/**
 * The villain class, makes almost everything related to the villain.
 */
public class Villain extends Entity {
    static ArrayList<Room> roomsWithVillains = new ArrayList<>();
    Image villainImage;
    Collider villainCollider;
    Text villainName;
    float time;
    int direction;
    Vec2 position;
    String imagePath;
    float velocity;
    double sum;
    Room room;
    UnitRoom currentUnitRoom;
    UnitRoom prevUnitRoom;

    public Villain() {
        villainCollider = null;
        villainImage = null;
        time = 0.0f;
        villainName = null;
        direction=1;
        velocity = 0.5f;
        sum = 0.0;
        room = null;
    }
    public Villain(String name, String iP) {
        this(name, new Vec2(0,0), iP);
    }

    public Villain(String name, Vec2 pos, String iP) {
        villainCollider = null;
        villainImage = null;
        time = 0.0f;
        villainName = new Text(name, new Vec2(0, 0), "./assets/Monocraft.ttf", 15, 255, 0, 0);
        villainName.setShadowOn(false);
        direction=1;
        position = pos;
        imagePath = iP;
        velocity = 0.5f;
        sum = 0.0;
        room = null;

    }
    @Override
    public void onStart(Isten isten) {
        Vec2 playerScale = new Vec2(0.6f, 0.6f);

        villainCollider = new Collider(position, playerScale);
        villainCollider.setMovability(true);
        isten.getPhysicsEngine().addCollider(villainCollider);//register collider in the physics engine

        villainImage = new Image(new Vec2(), playerScale, imagePath);
        villainImage.setSortingLayer(-50);
        villainImage.setVisibility(true);
        isten.getRenderer().addRenderable(villainImage);//register images in the renderer

        if (villainName != null) {
            villainName.setVisibility(true);
            villainName.setSortingLayer(-50);
            isten.getRenderer().addRenderable(villainName);
        }
        //isten.getCamera().setPixelsPerUnit(100);

        Map map = isten.getMap();

        for (Room room : map.getRooms()) {
            for (UnitRoom unitRoom : room.getUnitRooms()) {
                if (position.x >= unitRoom.getPosition().x - 0.5 &&
                        position.x <= unitRoom.getPosition().x + 0.5 &&
                        position.y >= unitRoom.getPosition().y - 0.5 &&
                        position.y <= unitRoom.getPosition().y + 0.5) {
                    currentUnitRoom = unitRoom;
                }
            }
        }
        prevUnitRoom = currentUnitRoom;
    }
    @Override
    public void onUpdate(Isten isten, double deltaTime) {


    }

    public void move(Isten isten, double deltaTime) {
        sum += deltaTime;
        Vec2 playerPosition = villainCollider.getPosition();
        Random random = new Random();
        if(villainImage != null) villainImage.setPosition(playerPosition);
        if(villainName != null) villainName.setPosition(Vec2.sum(playerPosition, new Vec2(0, (float) 0.5)));
        if(villainCollider == null) return;

        if (sum < 2) return;
        Map map = isten.getMap();

        for (Room room : map.getRooms()) {
            for (UnitRoom unitRoom : room.getUnitRooms()) {
                if (villainCollider.getPosition().x >= unitRoom.getPosition().x - 0.5 &&
                        villainCollider.getPosition().x <= unitRoom.getPosition().x + 0.5 &&
                        villainCollider.getPosition().y >= unitRoom.getPosition().y - 0.5 &&
                        villainCollider.getPosition().y <= unitRoom.getPosition().y + 0.5) {
                    currentUnitRoom = unitRoom;
                }
            }
        }
        int randomNumber = random.nextInt(3);
        if (villainCollider.getVelocity().x < 0) {
            if (currentUnitRoom.isLeftDoor()) {
                villainCollider.getVelocity().x = 0;
                switch (randomNumber) {
                    case 0:
                        villainCollider.getVelocity().x = velocity;
                        break;
                    case 1:
                        villainCollider.getVelocity().y = velocity;
                        break;
                    case 2:
                        villainCollider.getVelocity().y = -velocity;
                        break;
                }
            }
        }
        if (villainCollider.getVelocity().x > 0) {
            if (currentUnitRoom.isRightDoor()) {
                villainCollider.getVelocity().x = 0;
                switch (randomNumber) {
                    case 0:
                        villainCollider.getVelocity().x = -velocity;
                        break;
                    case 1:
                        villainCollider.getVelocity().y = velocity;
                        break;
                    case 2:
                        villainCollider.getVelocity().y = -velocity;
                        break;
                }
            }
        }
        if (villainCollider.getVelocity().y > 0) {
            if (currentUnitRoom.isTopDoor()) {
                villainCollider.getVelocity().y = 0;
                switch (randomNumber) {
                    case 0:
                        villainCollider.getVelocity().x = velocity;
                        break;
                    case 1:
                        villainCollider.getVelocity().y = -velocity;
                        break;
                    case 2:
                        villainCollider.getVelocity().x = -velocity;
                        break;
                }
            }
        }
        if (villainCollider.getVelocity().y < 0) {
            if (currentUnitRoom.isBottomDoor()) {
                villainCollider.getVelocity().y = 0;
                switch (randomNumber) {
                    case 0:
                        villainCollider.getVelocity().x = velocity;
                        break;
                    case 1:
                        villainCollider.getVelocity().y = velocity;
                        break;
                    case 2:
                        villainCollider.getVelocity().x = -velocity;
                        break;
                }
            }
        }
        if (villainCollider.getVelocity().x == 0 && villainCollider.getVelocity().y == 0) {
            randomNumber = random.nextInt(4);
            switch (randomNumber) {
                case 0:
                    villainCollider.getVelocity().x = velocity;
                    villainCollider.getVelocity().y = 0.0f;
                    break;
                case 1:
                    villainCollider.getVelocity().x = -velocity;
                    villainCollider.getVelocity().y = 0.0f;
                    break;
                case 2:
                    villainCollider.getVelocity().y = velocity;
                    villainCollider.getVelocity().x = 0.0f;
                    break;
                case 3:
                    villainCollider.getVelocity().y = -velocity;
                    villainCollider.getVelocity().x = 0.0f;
                    break;
            }
        }
    }

    public Vec2 randomPositions(ArrayList<Room> rooms) {
        Collections.shuffle(rooms);
        Random rand = new Random();

        Room selectedRoom;

        do {
            selectedRoom = rooms.get(rand.nextInt(rooms.size() - 1));
        } while (roomsWithVillains.contains(selectedRoom) || isStartUnitRoomInRoom(selectedRoom));

        UnitRoom selectedUnitRoom = selectedRoom.getUnitRooms().get(rand.nextInt(selectedRoom.getUnitRooms().size()));
        roomsWithVillains.add(selectedRoom);
        room = selectedRoom;
        return new Vec2(selectedUnitRoom.getPosition().x, selectedUnitRoom.getPosition().y);
    }

    boolean isStartUnitRoomInRoom(Room room) {
        for (UnitRoom unitRoom : room.getUnitRooms()) {
            if (unitRoom.getPosition().x == 0 && unitRoom.getPosition().y == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
    }


    public String getVillainName() {
        return villainName.getText();
    }

    public Vec2 getPosition() {
        return position;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Collider getVillainCollider() {
        return villainCollider;
    }

    public void setVillainCollider(Isten isten, Collider collider) {
        villainCollider = collider;
        isten.getPhysicsEngine().addCollider(villainCollider);//register collider in the physics engine
    }

    public Room getRoom() {
        return room;
    }

    public void setPosition(Vec2 vec2) {
        position = vec2;
    }
}
