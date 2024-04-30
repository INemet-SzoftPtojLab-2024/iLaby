package main.java.org.entities.villain;

import main.java.org.entities.Entity;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.Text;
import main.java.org.game.Isten;

import main.java.org.game.Map.Map;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.RoomType;

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
    ArrayList<Image> images;
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
    int stillImage;
    double dTime;
    private int random1, random2;

    public Villain() {
        villainCollider = null;
        villainImage = null;
        time = 0.0f;
        villainName = null;
        direction = 1;
        velocity = 0.5f;
        sum = 0.0;
        room = null;
    }

    public Villain(String name, String iP) {
        this(name, new Vec2(0, 0), iP);
    }

    public Villain(String name, Vec2 pos, String iP) {
        villainCollider = null;
        villainImage = null;
        time = 0.0f;
        villainName = new Text(name, new Vec2(0, 0), "./assets/Monocraft.ttf", 15, 255, 0, 0);
        villainName.setShadowOn(false);
        images = new ArrayList<>();
        stillImage = 0;
        dTime = 0.0;
        direction = 1;
        position = pos;
        imagePath = iP;
        velocity = 0.5f;
        sum = 0.0;
        room = null;
    }

    @Override
    public void onStart(Isten isten) {

        Vec2 villainScale = new Vec2(0.6f, 0.6f);
        Vec2 faintedScale = new Vec2(0.7f, 0.7f);

        images.add(new Image(new Vec2(), villainScale, "./assets/villain/villain" + imagePath + ".png"));
        images.add(new Image(new Vec2(), faintedScale, "./assets/villain/villain" + imagePath + "_fainted1.png"));
        images.add(new Image(new Vec2(), faintedScale, "./assets/villain/villain" + imagePath + "_fainted2.png"));

        float[] data = randomPositions(isten.getMap().getRooms());

        position = new Vec2(data[0], data[1]);
        villainCollider = new Collider(position, villainScale);
        villainCollider.setMovability(true);
        isten.getPhysicsEngine().addCollider(villainCollider);

        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                villainImage = images.get(0);
                images.get(i).setSortingLayer(-60);
                images.get(i).setVisibility(true);
                isten.getRenderer().addRenderable(images.get(i));
            } else {
                images.get(i).setSortingLayer(-60);
                images.get(i).setVisibility(false);
                isten.getRenderer().addRenderable(images.get(i));
            }
        }

        if (villainName != null) {
            villainName.setVisibility(true);

            villainName.setSortingLayer(-60);

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
        Vec2 villainPosition = villainCollider.getPosition();
        villainImage.setPosition(villainPosition);
        villainName.setPosition(Vec2.sum(villainPosition, new Vec2(0, (float) 0.5)));
    }

    public void move(Isten isten, double deltaTime) {
        if (!isInGasRoom(isten)) {
            sum += deltaTime;
            Vec2 playerPosition = villainCollider.getPosition();
            Random random = new Random();
            villainImage.setPosition(playerPosition);
            villainName.setPosition(Vec2.sum(playerPosition, new Vec2(0, (float) 0.5)));


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
        } else {
            dTime -= deltaTime;
            images.get(0).setVisibility(false);
            if (stillImage == 0) {
                images.get(1).setPosition(villainCollider.getPosition());
                images.get(2).setPosition(villainCollider.getPosition());
                images.get(1).setVisibility(true);
            }
            if (stillImage % 2 == 0 && dTime < 0.0) {
                images.get(1).setVisibility(false);
                images.get(2).setVisibility(true);
                stillImage++;
                dTime = 0.2;
            } else if (stillImage % 2 == 1 && dTime < 0.0) {
                images.get(2).setVisibility(false);
                images.get(1).setVisibility(true);
                stillImage++;
                dTime = 0.2;
            }
        }
    }

    public float[] randomPositions(ArrayList<Room> rooms) {
        //Collections.shuffle(rooms);
        Random rand = new Random();

        Room selectedRoom;

        do {
            selectedRoom = rooms.get(rand.nextInt(rooms.size() - 1));
        } while (roomsWithVillains.contains(selectedRoom) || isStartUnitRoomInRoom(selectedRoom) || selectedRoom.getRoomType() == RoomType.GAS);

        random2 = rand.nextInt(selectedRoom.getUnitRooms().size());
        UnitRoom selectedUnitRoom = selectedRoom.getUnitRooms().get(random2);
        roomsWithVillains.add(selectedRoom);
        room = selectedRoom;

        return new float[]{random1, random2, selectedUnitRoom.getPosition().x, selectedUnitRoom.getPosition().y};
    }

    public void setRoomForVillain(ArrayList<Room> rooms, int selectedRoomIndex, int selectedUnitRoomIndex) {
        Room selectedRoom = rooms.get(selectedRoomIndex);
        UnitRoom selectedUnitRoom = selectedRoom.getUnitRooms().get(selectedUnitRoomIndex);
        roomsWithVillains.add(selectedRoom);
        room = selectedRoom;
    }

    boolean isStartUnitRoomInRoom(Room room) {
        for (UnitRoom unitRoom : room.getUnitRooms()) {
            if (unitRoom.getPosition().x == 0 && unitRoom.getPosition().y == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean isInGasRoom(Isten isten) {
        Room currentRoom = null;
        for (Room room : isten.getMap().getRooms()) {
            for (UnitRoom unitRoom : room.getUnitRooms()) {
                if (villainCollider.getPosition().x >= unitRoom.getPosition().x - 0.5 &&
                        villainCollider.getPosition().x <= unitRoom.getPosition().x + 0.5 &&
                        villainCollider.getPosition().y >= unitRoom.getPosition().y - 0.5 &&
                        villainCollider.getPosition().y <= unitRoom.getPosition().y + 0.5) {
                    currentRoom = room;
                }
            }
        }
        if (currentRoom != null && currentRoom.getRoomType() == RoomType.GAS) return true;
        else return false;
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

    public void setVillainImage(Isten isten, Image image) {
        villainImage = image;
        isten.getRenderer().addRenderable(villainImage);
    }

    public Room getRoom() {
        return room;
    }

    public void setPosition(Vec2 vec2) {
        position = vec2;
    }

    public int getRandom1() {
        return random1;
    }

    public int getRandom2() {
        return random2;
    }

    public void setRandom1(int random1) {
        this.random1 = random1;
    }

    public void setRandom2(int random2) {
        this.random2 = random2;
    }
}
