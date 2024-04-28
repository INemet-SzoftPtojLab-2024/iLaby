package main.java.org.entities.player;


import main.java.org.entities.villain.Villain;
import main.java.org.game.Audio.AudioManager;
import main.java.org.game.Audio.Sound;
import main.java.org.game.Graphics.*;

import main.java.org.entities.Entity;

import main.java.org.game.Isten;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.PlayerPrefs.PlayerPrefs;
import main.java.org.game.UI.TimeCounter;
import main.java.org.game.physics.Collider;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * The player class, makes almost everything related to the player.
 */
public class Player extends Entity {

    Collider playerCollider;
    ArrayList<Image> playerImage;
    ImageUI death;
    TextUI motivational;
    int activeImage;
    float time;
    Text playerName;
    boolean alive;  //Bool variable, to store status of player: ded or alive

    Sound playerSound = null;

    public Player() {
        playerCollider = null;
        playerImage = null;
        death = null;
        motivational = null;
        activeImage = 0;
        time = 0.0f;
        playerName = new Text(PlayerPrefs.getString("name"), new Vec2(0, 0), "./assets/Bavarian.otf", 15, 0, 0, 255);
        playerName.setShadowOn(false);
        alive = true;
    }

    public Player(String name) {
        playerCollider = null;
        playerImage = null;
        death = null;
        motivational = null;
        activeImage = 0;
        time = 0.0f;
        playerName = new Text(name, new Vec2(0, 0), "./assets/Bavarian.otf", 15, 0, 0, 255);
        playerName.setShadowOn(false);
        alive = true;
    }

    @Override
    public void onStart(Isten isten) {
        //called when the player is initialized

        Vec2 playerScale = new Vec2(0.5f, 0.5f);

        playerCollider = new Collider(new Vec2(0, 0), playerScale);
        playerCollider.setMovability(true);
        isten.getPhysicsEngine().addCollider(playerCollider);//register collider in the physics engine

        playerImage = new ArrayList<>();
        playerImage.add(new Image(new Vec2(), playerScale, "./assets/character/character"+PlayerPrefs.getInt("skin")+"_right1.png"));
        playerImage.add(new Image(new Vec2(), playerScale, "./assets/character/character"+PlayerPrefs.getInt("skin")+"_right2.png"));
        playerImage.add(new Image(new Vec2(), playerScale, "./assets/character/character"+PlayerPrefs.getInt("skin")+"_left1.png"));
        playerImage.add(new Image(new Vec2(), playerScale, "./assets/character/character"+PlayerPrefs.getInt("skin")+"_left2.png"));
        playerImage.add(new Image(new Vec2(), playerScale, "./assets/character/character"+PlayerPrefs.getInt("skin")+"_ded.png"));

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

        for (Image im : playerImage) {
            im.setSortingLayer(-69);
            im.setVisibility(false);
            isten.getRenderer().addRenderable(im);//register images in the renderer
        }
        playerImage.get(playerImage.size() - 1).setSortingLayer(-67);

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

    public void isInSameRoom(Villain v) {

    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
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
                    if (currentRoom != null && currentRoom.equals(villain.getRoom())) {
                        alive = false;
                        AudioManager.closeSound(playerSound);
                    }
                }
            }

            //move the character
            int run = 1;
            boolean w = isten.getInputHandler().isKeyDown(KeyEvent.VK_W);
            boolean a = isten.getInputHandler().isKeyDown(KeyEvent.VK_A);
            boolean s = isten.getInputHandler().isKeyDown(KeyEvent.VK_S);
            boolean d = isten.getInputHandler().isKeyDown(KeyEvent.VK_D);

            if (isten.getInputHandler().isKeyDown(KeyEvent.VK_SHIFT)) run *= 2;//Shift is run

            if (w) {
                playerCollider.getVelocity().y = 2 * run;
            } else if (!w) playerCollider.getVelocity().y = 0;
            if (a) {
                playerCollider.getVelocity().x = -2 * run;
            } else if (!a) playerCollider.getVelocity().x = 0;
            if (s) {
                playerCollider.getVelocity().y = -2 * run;
            } else if (!s && !w) playerCollider.getVelocity().y = 0;
            if (d) {
                playerCollider.getVelocity().x = 2 * run;
            } else if (!d && !a) playerCollider.getVelocity().x = 0;

            //animation

            time += deltaTime;

            if (time > 0.2f / run) { //after this much time does the animation changes
                int prev = activeImage;
                if (playerCollider.getVelocity().x > 0) activeImage = 0;
                else if (playerCollider.getVelocity().x < 0) activeImage = 2;
                else if (prev > 1) activeImage = 2;
                else activeImage = 0;
                if (prev % 2 == 0 || playerCollider.getVelocity().magnitude() == 0.0f) activeImage++;
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