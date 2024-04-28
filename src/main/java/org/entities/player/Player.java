package main.java.org.entities.player;


import main.java.org.entities.villain.Villain;
import main.java.org.game.Audio.AudioManager;
import main.java.org.game.Audio.Sound;
import main.java.org.game.Graphics.*;

import main.java.org.entities.Entity;

import main.java.org.game.Isten;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.RoomType;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.PlayerPrefs.PlayerPrefs;
import main.java.org.game.UI.TimeCounter;
import main.java.org.game.physics.Collider;
import main.java.org.game.updatable.Updatable;
import main.java.org.items.usable_items.Gasmask;
import main.java.org.linalg.Vec2;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * The player class, makes almost everything related to the player.
 */
public class Player extends Entity {

    Collider playerCollider;
    ArrayList<Image> playerImage;
    ArrayList<Image> playerImageNormal;
    ArrayList<Image> playerImageFainted;
    ImageUI death;
    TextUI motivational;
    int activeImage;
    float time;
    Text playerName;
    boolean alive;  //Bool variable, to store status of player: ded or alive
    Sound playerSound = null;
    double faintingTime;
    int setPlayerImage;
    boolean isFainted;
    float speed;
    int stillImage1;
    int stillImage2;


    public Player() {
        playerCollider = null;
        playerImage = null;
        playerImageNormal=null;
        playerImageFainted=null;
        death = null;
        motivational = null;
        activeImage = 0;
        time = 0.0f;
        playerName = new Text(PlayerPrefs.getString("name"), new Vec2(0, 0), "./assets/Monocraft.ttf", 15, 0, 0, 255);
        playerName.setShadowOn(false);
        alive = true;
        faintingTime = 0;
        setPlayerImage = 0;
        isFainted = false;
        speed = 2;
        stillImage1 = 0;
        stillImage2 = 0;
    }

    public Player(String name) {
        playerCollider = null;
        playerImage = null;
        playerImageNormal=null;
        playerImageFainted=null;
        death = null;
        motivational = null;
        activeImage = 0;
        time = 0.0f;
        playerName = new Text(name, new Vec2(0, 0), "./assets/Monocraft.ttf", 15, 0, 0, 255);
        playerName.setShadowOn(false);
        alive = true;
        faintingTime = 0;
        setPlayerImage = 0;
        isFainted=false;
        speed = 2;
        stillImage1 =0;
        stillImage2 =0;
    }

    @Override
    public void onStart(Isten isten) {
        //called when the player is initialized

        Vec2 playerScale = new Vec2(0.5f, 0.5f);
        Vec2 faintedScale = new Vec2(0.6f, 0.6f);

        playerCollider = new Collider(new Vec2(0, 0), playerScale);
        playerCollider.setMovability(true);
        isten.getPhysicsEngine().addCollider(playerCollider);//register collider in the physics engine

        playerImageNormal = new ArrayList<>();
        playerImageNormal.add(new Image(new Vec2(), playerScale, "./assets/character/character"+PlayerPrefs.getInt("skin")+"_right1.png"));
        playerImageNormal.add(new Image(new Vec2(), playerScale, "./assets/character/character"+PlayerPrefs.getInt("skin")+"_right2.png"));
        playerImageNormal.add(new Image(new Vec2(), playerScale, "./assets/character/character"+PlayerPrefs.getInt("skin")+"_left1.png"));
        playerImageNormal.add(new Image(new Vec2(), playerScale, "./assets/character/character"+PlayerPrefs.getInt("skin")+"_left2.png"));
        playerImageNormal.add(new Image(new Vec2(), playerScale, "./assets/character/character"+PlayerPrefs.getInt("skin")+"_ded.png"));

        for (Image im : playerImageNormal) {
            im.setSortingLayer(-69);
            im.setVisibility(false);
            isten.getRenderer().addRenderable(im);//register images in the renderer
        }
        playerImageNormal.get(playerImageNormal.size() - 1).setSortingLayer(-67);

        playerImageFainted=new ArrayList<>();
        playerImageFainted.add(new Image(new Vec2(), faintedScale, "./assets/character/character"+PlayerPrefs.getInt("skin")+"_right1_fainted.png"));
        playerImageFainted.add(new Image(new Vec2(), faintedScale, "./assets/character/character"+PlayerPrefs.getInt("skin")+"_right2_fainted1.png"));
        playerImageFainted.add(new Image(new Vec2(), faintedScale, "./assets/character/character"+PlayerPrefs.getInt("skin")+"_left1_fainted.png"));
        playerImageFainted.add(new Image(new Vec2(), faintedScale, "./assets/character/character"+PlayerPrefs.getInt("skin")+"_left2_fainted1.png"));
        playerImageFainted.add(new Image(new Vec2(), playerScale, "./assets/character/character"+PlayerPrefs.getInt("skin")+"_ded.png"));
        playerImageFainted.add(new Image(new Vec2(), faintedScale, "./assets/character/character"+PlayerPrefs.getInt("skin")+"_right2_fainted2.png"));
        playerImageFainted.add(new Image(new Vec2(), faintedScale, "./assets/character/character"+PlayerPrefs.getInt("skin")+"_left2_fainted2.png"));

        for (Image im : playerImageFainted) {
            im.setSortingLayer(-69);
            im.setVisibility(false);
            isten.getRenderer().addRenderable(im);//register images in the renderer
        }
        playerImageFainted.get(playerImageFainted.size() - 1).setSortingLayer(-67);


        playerImage=playerImageNormal;

        death = new ImageUI(new Vec2(0, 0), new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()), "./assets/character/ded.png");
        death.setSortingLayer(-70);
        death.setVisibility(false);
        death.setAlignment(Renderable.CENTER, Renderable.CENTER);
        isten.getRenderer().addRenderable(death);

        motivational = new TextUI("Try again loser.", new Vec2(0, -170), 26, 200, 200, 200);
        motivational.setSortingLayer(-71);
        motivational.setVisibility(false);
        motivational.setAlignment(Renderable.CENTER, Renderable.CENTER);
        isten.getRenderer().addRenderable(motivational);

        activeImage = 1;
        playerImage.get(activeImage).setVisibility(true);

        if (playerName != null) {
            playerName.setVisibility(true);
            playerName.setSortingLayer(-69);
            isten.getRenderer().addRenderable(playerName);
        }

        //adjust camera zoom
        isten.getCamera().setPixelsPerUnit(100);

        //preload player sound
        AudioManager.preloadSound("./assets/audio/playersound.ogg");
        AudioManager.preloadSound("./assets/audio/died.ogg");
    }
    public void setImage(boolean isFainted)
    {
        if(isFainted)
        {
            playerImage.get(activeImage).setVisibility(false);
            playerImageFainted.get(activeImage).setVisibility(true);
            playerImage=playerImageFainted;
        }
        else{
            playerImage.get(activeImage).setVisibility(false);
            playerImageNormal.get(activeImage).setVisibility(true);
            playerImage=playerImageNormal;
        }
    }
    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        if(isFainted)
        {
            if(faintingTime > 15 )
            {
                faintingTime=0;
                setImage(false);
                isFainted = false;
                setPlayerImage++;
                speed=2;
            }
            else{
                faintingTime+=deltaTime;
            }
        }
        //called every frame
        if (alive) {

            Room currentRoom = null;
            for (Updatable u : isten.getUpdatables()) {
                if (u.getClass().equals(Villain.class)) {
                    for (Room room : isten.getMap().getRooms()) {
                        for (UnitRoom unitRoom : room.getUnitRooms()) {
                            if (playerCollider.getPosition().x >= unitRoom.getPosition().x - 0.5 &&
                                    playerCollider.getPosition().x <= unitRoom.getPosition().x + 0.5 &&
                                    playerCollider.getPosition().y >= unitRoom.getPosition().y - 0.5 &&
                                    playerCollider.getPosition().y <= unitRoom.getPosition().y + 0.5) {
                                currentRoom = room;
                            }
                        }
                    }
                    Villain villain = (Villain) u;
                    if (currentRoom != null && currentRoom.equals(villain.getRoom()) && currentRoom.getRoomType()!=RoomType.GAS) {
                        alive = false;
                        AudioManager.closeSound(playerSound);
                    }
                    if (currentRoom != null && currentRoom.getRoomType() == RoomType.GAS) {
                        if(!isten.getInventory().getExistenceOfGasMask()) {
                            if(setPlayerImage % 2 == 0)
                            {
                                setImage(true);
                                isFainted=true;
                                setPlayerImage++;
                                speed = 1;
                            }
                            for(int i = 0; i< 5; i++)
                            {
                                if(isten.getInventory().getStoredItems().get(i) != null) {
                                    isten.getInventory().getStoredItems().get(i).dropOnGround(new Vec2(currentRoom.getUnitRooms().get(i+1).getPosition().x, currentRoom.getUnitRooms().get(i+1).getPosition().y));
                                }
                            }
                            isten.getInventory().dropAllItems(isten);
                        }
                        else{
                            for(main.java.org.items.Item item : isten.getInventory().getStoredItems()){
                                if (item != null && item.getClass().equals(Gasmask.class)){
                                    Gasmask gasmask = (Gasmask) item;
                                    gasmask.useMask(deltaTime);
                                    if (gasmask.getCapacity() <= 0){
                                        isten.getInventory().destroyGasMask();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //move the character
            int run = 1;
            boolean w = isten.getInputHandler().isKeyDown(KeyEvent.VK_W);
            boolean a = isten.getInputHandler().isKeyDown(KeyEvent.VK_A);
            boolean s = isten.getInputHandler().isKeyDown(KeyEvent.VK_S);
            boolean d = isten.getInputHandler().isKeyDown(KeyEvent.VK_D);
            if(!isFainted)
            {
                if (isten.getInputHandler().isKeyDown(KeyEvent.VK_SHIFT)) run *= 2;//Shift is run
            }

            if (w) {
                playerCollider.getVelocity().y = speed * run;
            } else if (!w) playerCollider.getVelocity().y = 0;
            if (a) {
                playerCollider.getVelocity().x = -speed * run;
            } else if (!a) playerCollider.getVelocity().x = 0;
            if (s) {
                playerCollider.getVelocity().y = -speed * run;
            } else if (!s && !w) playerCollider.getVelocity().y = 0;
            if (d) {
                playerCollider.getVelocity().x = speed * run;
            } else if (!d && !a) playerCollider.getVelocity().x = 0;

            //animation

            time += deltaTime;

            if(isFainted && playerCollider.getVelocity().magnitude() == 0.0f)
            {
                if(activeImage == 1){
                    if(stillImage1 % 50 == 0)
                    {
                        playerImage.get(5).setVisibility(false);
                        playerImage.get(activeImage).setVisibility(true);
                    }
                    else if(stillImage1 % 101 == 0){
                        playerImage.get(activeImage).setVisibility(false);
                        playerImage.get(5).setVisibility(true);
                    }
                    stillImage1++;
                    time = 0.0f;
                }
                if(activeImage == 3){
                    if(stillImage2 % 50 == 0)
                    {
                        playerImage.get(6).setVisibility(false);
                        playerImage.get(activeImage).setVisibility(true);
                    }
                    else if(stillImage2 % 101 == 0){
                        playerImage.get(activeImage).setVisibility(false);
                        playerImage.get(6).setVisibility(true);
                    }
                    stillImage2++;
                    time = 0.0f;
                }
            }

            if (time > 0.2f / run) { //after this much time does the animation changes
                if(isFainted) {
                    playerImage.get(5).setVisibility(false);
                    playerImage.get(6).setVisibility(false);
                }
                int prev = activeImage;
                if (playerCollider.getVelocity().x > 0) activeImage = 0;
                else if (playerCollider.getVelocity().x < 0) activeImage = 2;
                else if (prev > 1 && prev < 5) activeImage = 2;
                else activeImage = 0;
                if ((prev % 2 == 0 || playerCollider.getVelocity().magnitude() == 0.0f) && prev!=5 && prev!=6) activeImage++;
                playerImage.get(prev).setVisibility(false);
                playerImage.get(activeImage).setVisibility(true);
                time = 0.0f;
            }

            //move image
            //the origin of the image is in its top right corner, therefore the imagePos looks like this: screenSpace(collider position) - 0.5*imageScale

            Vec2 playerPosition = playerCollider.getPosition();
            for (int i = 0; i < playerImage.size(); i++) {
                playerImage.get(i).setPosition(playerPosition);
            }
            playerName.setPosition(Vec2.sum(playerPosition, new Vec2(0, (float) 0.5)));

            //move camera
            isten.getCamera().setPosition(playerCollider.getPosition());

            //play sound
            if (!AudioManager.isPlaying(playerSound) && alive)
                playerSound = AudioManager.playSound("./assets/audio/playersound.ogg");

            if (TimeCounter.getTimeRemaining() < 0 && alive) {
                alive = false;
                AudioManager.closeSound(playerSound);
            }

        } else {
            if (!AudioManager.isPlaying(playerSound) && activeImage != 4)
                playerSound = AudioManager.playSound("./assets/audio/died.ogg");

            if (activeImage != 4) {
                playerImage.get(activeImage).setVisibility(false);
                activeImage = 4;
                playerImage.get(activeImage).setVisibility(true);
                death.setVisibility(true);
                motivational.setVisibility(true);
            }
        }
        death.setScale(new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()));
    }

    @Override
    public void onDestroy() {
        //not implemented yet
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setPlayerName(Text playerName) {
        this.playerName = playerName;
    }

    public Collider getPlayerCollider() {
        return playerCollider;
    }

    public ArrayList<Image> getPlayerImage() {
        return playerImage;
    }

    public float getTime() {
        return time;
    }

    public int getActiveImage() {
        return activeImage;
    }

    public Text getPlayerName() {
        return playerName;
    }

    public void setPlayerCollider(Collider collider) {
        this.playerCollider = collider;
    }

    public void setActiveImage(int activeImage) {
        this.activeImage = activeImage;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public void setPlayerImage(ArrayList<Image> playerImage) {
        this.playerImage = playerImage;
    }
}