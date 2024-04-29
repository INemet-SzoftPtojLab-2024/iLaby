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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * The villain class, makes almost everything related to the villain.
 */
public class Villain extends Entity {
    static ArrayList<Room> roomsWithVillains = new ArrayList<>();
    private Image villainImage;
    private Collider villainCollider;
    private Text villainName;
    private float time;
    private Vec2 position;
    private Room room;
    private UnitRoom currentUnitRoom;
    private UnitRoom prevUnitRoom;
    private ArrayList<Image> images;
    private float velocity;
    private double sum;
    private int id;
    private int stillImage;
    private double dTime;

    public Villain(String name, int ID) {
        villainCollider = null;
        villainImage = null;
        time = 0.0f;
        villainName = new Text(name, new Vec2(0, 0), "./assets/Monocraft.ttf", 15, 255, 0, 0);
        villainName.setShadowOn(false);
        images=new ArrayList<>();
        velocity = 0.5f;
        sum = 0.0;
        room = null;
        id=ID;
        stillImage=0;
        dTime=0.0;
    }

    @Override
    public void onStart(Isten isten) {

        Vec2 villainScale = new Vec2(0.6f, 0.6f);
        Vec2 faintedScale = new Vec2(0.7f, 0.7f);

        images.add(new Image(new Vec2(), villainScale, "./assets/villain/villain"+id+".png"));
        images.add(new Image(new Vec2(), faintedScale, "./assets/villain/villain"+id+"_fainted1.png"));
        images.add(new Image(new Vec2(), faintedScale, "./assets/villain/villain"+id+"_fainted2.png"));

        position = randomPositions(isten.getMap().getRooms());
        villainCollider = new Collider(position, villainScale);

        villainCollider.setMovability(true);
        isten.getPhysicsEngine().addCollider(villainCollider);

        for (int i = 0; i< 3; i++) {

            if(i == 0)
            {
                villainImage= images.get(0);
                images.get(i).setSortingLayer(-60);
                images.get(i).setVisibility(true);
                isten.getRenderer().addRenderable(images.get(i));
            }
            else{
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
        if(!isInGasRoom(isten)) {
            sum += deltaTime;
            Vec2 villainPosition = villainCollider.getPosition();
            Random random = new Random();
            villainImage.setPosition(villainPosition);
            villainName.setPosition(Vec2.sum(villainPosition, new Vec2(0, (float) 0.5)));


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
        else{
            dTime-=deltaTime;
            images.get(0).setVisibility(false);
            if(stillImage==0)
            {
                images.get(1).setPosition(villainCollider.getPosition());
                images.get(2).setPosition(villainCollider.getPosition());
                images.get(1).setVisibility(true);
            }
            if(stillImage % 2 == 0 && dTime<0.0)
            {
                images.get(1).setVisibility(false);
                images.get(2).setVisibility(true);
                stillImage++;
                dTime=0.2;
            }
            else if(stillImage % 2 == 1 && dTime<0.0){
                images.get(2).setVisibility(false);
                images.get(1).setVisibility(true);
                stillImage++;
                dTime=0.2;
            }

        }
    }

    public Vec2 randomPositions(ArrayList<Room> rooms) {
        Collections.shuffle(rooms);
        Random rand = new Random();

        Room selectedRoom;

        do {
            selectedRoom = rooms.get(rand.nextInt(rooms.size() - 1));
        } while (roomsWithVillains.contains(selectedRoom) || isStartUnitRoomInRoom(selectedRoom)|| selectedRoom.getRoomType()== RoomType.GAS);

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
    public boolean isInGasRoom(Isten isten)
    {
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
        if(currentRoom!= null && currentRoom.getRoomType()== RoomType.GAS)return true;
        else return false;
    }
    @Override
    public void onDestroy() {
    }

    public Room getRoom() {
        return room;
    }
}