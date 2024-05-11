package main.java.org.entities.villain;

import main.java.org.entities.Entity;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.Text;
import main.java.org.game.Isten;
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
    private Image villainImage;
    private Image faintedImage1;
    private Image faintedImage2;
    private Collider villainCollider;
    private Text villainName;
    private Vec2 position;
    private String imagePath;
    private float velocity;
    private double sum;
    private Room room;
    private double faintTime;
    private UnitRoom currentUnitRoom;
    private int random1, random2;
    private double dTime;
    private int stillImage;
    double timeElapsed;
    private boolean isInGasRoom = false;


    public Villain(String name, String iP) {
        this(name, new Vec2(0,0), iP);
    }

    public Villain(String name, Vec2 pos, String iP) {
        villainCollider = null;
        villainImage = null;
        villainName = new Text(name, new Vec2(0, 0), "./assets/Monocraft.ttf", 15, 255, 0, 0);
        villainName.setShadowOn(false);
        position = pos;
        imagePath = iP;
        velocity = 0.5f;
        sum = 0.0;
        room = null;
        dTime=0.0;
        stillImage=0;
        faintTime=0.0;
    }
    @Override
    public void onStart(Isten isten) {
        Vec2 villainScale = new Vec2(0.6f, 0.6f);
        Vec2 faintedScale = new Vec2(0.7f, 0.7f);

        villainImage=new Image(new Vec2(), villainScale, imagePath);
        faintedImage1=new Image(new Vec2(),faintedScale,imagePath.substring(0,imagePath.length()-4)+"_fainted1.png");
        faintedImage2=new Image(new Vec2(),faintedScale,imagePath.substring(0,imagePath.length()-4)+"_fainted2.png");
        /*System.out.println(imagePath);
        System.out.println(imagePath.substring(0,imagePath.length()-4)+"_fainted1.png");
        System.out.println(imagePath.substring(0,imagePath.length()-4)+"_fainted2.png");
        System.out.println();*/
        //position = randomPositions(isten.getMap().getRooms());
        villainCollider = new Collider(position, villainScale);

        villainCollider.setMovability(true);
        isten.getPhysicsEngine().addCollider(villainCollider);

        villainImage.setSortingLayer(-50);
        isten.getRenderer().addRenderable(villainImage);
        villainImage.setVisibility(true);

        faintedImage1.setSortingLayer(-50);
        isten.getRenderer().addRenderable(faintedImage1);
        faintedImage1.setVisibility(false);

        faintedImage2.setSortingLayer(-50);
        isten.getRenderer().addRenderable(faintedImage2);
        faintedImage2.setVisibility(false);


        if (villainName != null) {
            villainName.setVisibility(true);
            villainName.setSortingLayer(-50);
            isten.getRenderer().addRenderable(villainName);
        }
        //isten.getCamera().setPixelsPerUnit(100);

        Map map = isten.getMap();


    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        if(faintTime>0) {
            faintTime -= deltaTime;
            if (faintTime<0)faintTime=0;
        }
        timeElapsed+=deltaTime;
        Vec2 playerPosition = villainCollider.getPosition();
        villainImage.setPosition(playerPosition);
        faintedImage1.setPosition(playerPosition);
        faintedImage2.setPosition(playerPosition);
        villainName.setPosition(Vec2.sum(playerPosition, new Vec2(0, (float) 0.5)));

        if(faintTime > 0) {
            if ((timeElapsed*1000000) % 1000000 < 500000) {
                setVillainImage(isten,faintedImage1);
            }
            else {
                setVillainImage(isten,faintedImage2);
            }
        }

        if(!isInGasRoom&&faintTime==0) {
            stillImage = 0;
            villainCollider.setMovability(true);
            sum += deltaTime;
            Vec2 villainPosition = villainCollider.getPosition();
            faintedImage1.setVisibility(false);
            faintedImage2.setVisibility(false);
            villainImage.setPosition(villainPosition);
            villainImage.setVisibility(true);
            villainName.setPosition(Vec2.sum(villainPosition, new Vec2(0, (float) 0.5)));
        }
        else{
            villainCollider.setMovability(false);
            dTime-=deltaTime;
            villainImage.setVisibility(false);
            if(stillImage==0)
            {
                faintedImage1.setPosition(villainCollider.getPosition());
                faintedImage2.setPosition(villainCollider.getPosition());
                faintedImage1.setVisibility(true);
            }
            if(stillImage % 2 == 0 && dTime<0.0)
            {
                faintedImage1.setVisibility(true);
                faintedImage2.setVisibility(false);
                stillImage++;
                dTime=0.2;
            }
            else if(stillImage % 2 == 1 && dTime<0.0){
                faintedImage2.setVisibility(true);
                faintedImage1.setVisibility(false);
                stillImage++;
                dTime=0.2;
            }

        }
    }
    public void checkIfInGasRoom(Isten isten)
    {
        if(this.room == null) {
            return;
        }

        if(room.getRoomType()== RoomType.GAS) {
            isInGasRoom = true;
        }
        else {
            isInGasRoom = false;
        }
    }

    public void updateVillainOnServer(Isten isten, double deltaTime) {

        /*for (Room room : isten.getMap().getRooms()) {
            for (UnitRoom unitRoom : room.getUnitRooms()) {
                if (villainCollider.getPosition().x >= unitRoom.getPosition().x - 0.5 &&
                        villainCollider.getPosition().x <= unitRoom.getPosition().x + 0.5 &&
                        villainCollider.getPosition().y >= unitRoom.getPosition().y - 0.5 &&
                        villainCollider.getPosition().y <= unitRoom.getPosition().y + 0.5) {
                    currentUnitRoom = unitRoom;
                    this.room = currentUnitRoom.getOwnerRoom();
                }
            }
        }*/
        //shortcut:
        int x = (int)(villainCollider.getPosition().x + 0.3f);
        int y = (int)(villainCollider.getPosition().y + 0.3f);
        //System.out.println(x + " " + y + " ownerroorm pozi " +  isten.getMap().getUnitRooms()[y][x].getColNum() + " " + isten.getMap().getUnitRooms()[y][x].getRowNum());
        this.room = isten.getMap().getUnitRooms()[y][x].getOwnerRoom();
        this.currentUnitRoom = isten.getMap().getUnitRooms()[y][x];

        checkIfInGasRoom(isten);

        if(!isInGasRoom&&faintTime==0) {
            Random random = new Random();

            if (sum < 2) return;
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
    }
    public Vec2 randomPositions(ArrayList<Room> rooms) {
        Collections.shuffle(rooms);
        Random rand = new Random();

        Room selectedRoom;

        do {
            random1 = rand.nextInt(rooms.size() - 1);
            selectedRoom = rooms.get(random1);
        } while (roomsWithVillains.contains(selectedRoom) || isStartUnitRoomInRoom(selectedRoom)|| selectedRoom.getRoomType()== RoomType.GAS);

        random2 =rand.nextInt(selectedRoom.getUnitRooms().size()-1);
        UnitRoom selectedUnitRoom = selectedRoom.getUnitRooms().get(random2);
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

    public void setVillainImage(Isten isten, Image image) {
        villainImage = image;
    }
    public Room getRoom() {
        return room;
    }

    public void setPosition(Vec2 vec2) {
        position = vec2;
    }
    public double getFaintTime(){return faintTime;}
    public void setFainted(double time){
        faintTime=time;
        //villainImage.setVisibility(false);
    }
    public boolean getIsFainted(){
        return faintTime > 0;
    }
    public void setVelocity(float velocity) {
        villainCollider.setVelocity(new Vec2(velocity, velocity));
    }

    public void isInGasRoom(boolean b) {
        isInGasRoom = b;
    }
    public boolean isInGasRoom() {
        return isInGasRoom;
    }
}
