package main.java.org.entities.player;


import main.java.org.entities.villain.Villain;
import main.java.org.game.Audio.AudioManager;
import main.java.org.game.Audio.Sound;
import main.java.org.game.Graphics.*;

import main.java.org.entities.Entity;

import main.java.org.game.Isten;
import main.java.org.game.Map.RoomType;
import main.java.org.game.UI.TimeCounter;
import main.java.org.game.physics.Collider;
import main.java.org.items.Item;
import main.java.org.items.usable_items.Gasmask;
import main.java.org.linalg.Vec2;
import main.java.org.networking.Packet03Animation;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.PlayerPrefs.PlayerPrefs;
import main.java.org.game.updatable.Updatable;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * The player class, makes almost everything related to the player.
 */
public class Player extends Entity {

    Collider playerCollider;
    ArrayList<Image> playerImage;
    ImageUI death;
    int activeImage;
    TextUI motivational;
    float time;
    Text playerName;
    boolean alive;  //Bool variable, to store status of player: ded or alive
    Sound playerSound = null;
    double faintingTime;
    boolean isFainted;
    boolean isInGasRoomButHasMask;
    float speed;
    protected Vec2 spawnPosition;
    protected int run = 1;
    protected int skinID;
    public boolean localPlayer = false;
    private Room currentRoom = null;

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
        spawnPosition = new Vec2(0, 0);
        faintingTime = 0;
        isFainted = false;
        isInGasRoomButHasMask = false;
        speed=2;
    }

    public Player(String name) {
        playerCollider = null;
        playerImage = null;
        death = null;
        activeImage = 0;
        motivational = null;
        time = 0.0f;
        playerName = new Text(name, new Vec2(0, 0), "./assets/Monocraft.ttf", 15, 0, 0, 255);
        playerName.setShadowOn(false);
        alive = true;
        spawnPosition = new Vec2(0, 0);
        faintingTime = 0;
        isFainted = false;
        speed=2;
    }

    public Player(String name, Vec2 spawnPosition) {
        this(name);
        this.spawnPosition.x = spawnPosition.x;
        this.spawnPosition.y = spawnPosition.y;
    }

    @Override
    public void onStart(Isten isten) {
        //called when the player is initialized

        Vec2 playerScale = new Vec2(0.5f, 0.5f);
        Vec2 faintedScale = new Vec2(0.6f, 0.6f);

        playerCollider = new Collider(new Vec2(spawnPosition.x, spawnPosition.y), playerScale);
        playerCollider.setMovability(true);
        isten.getPhysicsEngine().addCollider(playerCollider);//register collider in the physics engine

        playerImage = new ArrayList<>();
        playerImage.add(new Image(new Vec2(), playerScale, "./assets/character/character" + skinID + "_right1.png"));
        playerImage.add(new Image(new Vec2(), playerScale, "./assets/character/character" + skinID + "_right2.png"));
        playerImage.add(new Image(new Vec2(), playerScale, "./assets/character/character" + skinID + "_left1.png"));
        playerImage.add(new Image(new Vec2(), playerScale, "./assets/character/character" + skinID + "_left2.png"));
        playerImage.add(new Image(new Vec2(), faintedScale, "./assets/character/character" + skinID + "_right1_fainted.png"));
        playerImage.add(new Image(new Vec2(), faintedScale, "./assets/character/character" + skinID + "_right2_fainted1.png"));
        playerImage.add(new Image(new Vec2(), faintedScale, "./assets/character/character" + skinID + "_left1_fainted.png"));
        playerImage.add(new Image(new Vec2(), faintedScale, "./assets/character/character" + skinID + "_left2_fainted1.png"));
        playerImage.add(new Image(new Vec2(), faintedScale, "./assets/character/character" + skinID + "_right2_fainted2.png"));
        playerImage.add(new Image(new Vec2(), faintedScale, "./assets/character/character" + skinID + "_left2_fainted2.png"));
        playerImage.add(new Image(new Vec2(), faintedScale, "./assets/character/character" + skinID + "_right1_mask.png"));
        playerImage.add(new Image(new Vec2(), faintedScale, "./assets/character/character" + skinID + "_right2_mask.png"));
        playerImage.add(new Image(new Vec2(), faintedScale, "./assets/character/character" + skinID + "_left1_mask.png"));
        playerImage.add(new Image(new Vec2(), faintedScale, "./assets/character/character" + skinID + "_left2_mask.png"));
        playerImage.add(new Image(new Vec2(), playerScale, "./assets/character/character" + skinID + "_ded.png"));

        for (Image im : playerImage) {
            im.setSortingLayer(-69);
            im.setVisibility(false);
            isten.getRenderer().addRenderable(im);//register images in the renderer
        }
        playerImage.get(playerImage.size() - 1).setSortingLayer(-67);

        activeImage = 1;
        playerImage.get(activeImage).setVisibility(true);

        death = new ImageUI(new Vec2(spawnPosition.x, spawnPosition.y), new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()), "./assets/character/ded.png");
        death.setSortingLayer(-70);
        death.setVisibility(false);
        death.setAlignment(Renderable.CENTER, Renderable.CENTER);
        isten.getRenderer().addRenderable(death);

        motivational = new TextUI("Try again loser.", new Vec2(0, -170), 26, 200, 200, 200);
        motivational.setSortingLayer(-71);
        motivational.setVisibility(false);
        motivational.setAlignment(Renderable.CENTER, Renderable.CENTER);
        isten.getRenderer().addRenderable(motivational);

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

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        //called every frame
        if (alive) {

            if (isFainted) {
                if (faintingTime > 10) {
                    faintingTime = 0;
                    isFainted = false;
                    speed = 2;
                }
            }

            Room currentRoom = null;

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
            if(currentRoom != this.currentRoom){
                //Lasd Inventory canAvoidVillain member var
                isten.getInventory().setCanAvoidVillain(false);
                //Ha szobat valt a player, akkor a kovetkezo alkalommar, amikor gegnerrel talalkozik hasznalodnia kell a Tvsz-nek
                isten.getInventory().resetShouldUseChargeForTvsz();
                this.currentRoom = currentRoom;
            }
            if (currentRoom != null && currentRoom.getRoomType() == RoomType.GAS) {
                if (!isten.getInventory().getExistenceOfGasMask()) {
                    faintingTime = 0;
                    isFainted = true;
                    speed = 1;
                    for (int i = 0; i < 5; i++) {
                        if (isten.getInventory().getStoredItems().get(i) != null) {
                            isten.getInventory().getStoredItems().get(i).dropOnGround(new Vec2(currentRoom.getUnitRooms().get(i + 1).getPosition().x, currentRoom.getUnitRooms().get(i + 1).getPosition().y));
                        }
                    }
                    isten.getInventory().dropAllItems(isten);
                    isInGasRoomButHasMask = false;
                }
                else {
                    int index=0;
                    for(;index<isten.getInventory().getSize();index++){
                        if(isten.getInventory().getStoredItems().get(index) instanceof Gasmask) break;
                    }
                    isInGasRoomButHasMask = true;
                    isten.getInventory().getStoredItems().get(index).use(deltaTime);
                }
            } else {
                faintingTime += deltaTime;
                isInGasRoomButHasMask = false;
            }

            //move the character
            run = 1;
            boolean w = isten.getInputHandler().isKeyDown(KeyEvent.VK_W);
            boolean a = isten.getInputHandler().isKeyDown(KeyEvent.VK_A);
            boolean s = isten.getInputHandler().isKeyDown(KeyEvent.VK_S);
            boolean d = isten.getInputHandler().isKeyDown(KeyEvent.VK_D);

            if (isten.getInputHandler().isKeyDown(KeyEvent.VK_SHIFT) && !isFainted) run *= 2;//Shift is run

            if (localPlayer) {
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
            }
            //animation

            time += deltaTime;
            if (time > 0.2f / run) { //after this much time does the animation changes
                int prev = activeImage;
                boolean leftFacing = prev > 1 && prev < 4 || prev > 5 && prev < 8 || prev > 11 && prev < 14;
                if (isFainted && playerCollider.getVelocity().magnitude() == 0.0f) { //fainted animation
                    if (leftFacing|| prev == 9) activeImage = 7;
                    else activeImage = 5;
                    if (prev == 5) activeImage = 8;
                    if (prev == 7) activeImage = 9;
                }
                else if(isInGasRoomButHasMask){ //mask animation
                    if (playerCollider.getVelocity().x > 0) activeImage = 10;
                    else if (playerCollider.getVelocity().x < 0) activeImage = 12;
                    else if (leftFacing) activeImage = 12;
                    else activeImage = 10;
                    if (prev % 2 == 0 || playerCollider.getVelocity().magnitude() == 0.0f) activeImage++;
                }
                else { //normal animation
                    if (playerCollider.getVelocity().x > 0) activeImage = 0;
                    else if (playerCollider.getVelocity().x < 0) activeImage = 2;
                    else if (leftFacing) activeImage = 2;
                    else activeImage = 0;
                    if (prev % 2 == 0 || playerCollider.getVelocity().magnitude() == 0.0f) activeImage++;
                    if (isFainted) activeImage += 4;
                }
                playerImage.get(prev).setVisibility(false);
                playerImage.get(activeImage).setVisibility(true);
                time = 0.0f;
                sendAnimationData(isten);
            }

            //move image
            //the origin of the image is in its top right corner, therefore the imagePos looks like this: screenSpace(collider position) - 0.5*imageScale

            Vec2 playerPosition = playerCollider.getPosition();
            for (int i = 0; i < playerImage.size(); i++) {
                playerImage.get(i).setPosition(playerPosition);
            }
            playerName.setPosition(Vec2.sum(playerPosition, new Vec2(0, (float) 0.5)));

            //move camera
            if (localPlayer) isten.getCamera().setPosition(playerCollider.getPosition());

            //play sound
            if (!AudioManager.isPlaying(playerSound) && localPlayer)
                playerSound = AudioManager.playSound("./assets/audio/playersound.ogg");

            if (TimeCounter.getTimeRemaining() < 0 && alive) {
                alive = false;
                AudioManager.closeSound(playerSound);
            }

        }
        else {

            if (!AudioManager.isPlaying(playerSound) && localPlayer)
                playerSound = AudioManager.playSound("./assets/audio/died.ogg");

            if (activeImage != 14) {
                playerCollider.setVelocity(new Vec2(0));
                playerImage.get(activeImage).setVisibility(false);
                activeImage = 14;
                playerImage.get(activeImage).setVisibility(true);
                if (localPlayer) {
                    death.setVisibility(true);
                    motivational.setVisibility(true);
                }
            }
        }
        death.setScale(new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()));
    }

    public boolean checkIfPlayerInVillainRoom(Isten isten,double deltaTime) {
        Room currentRoom = null;
        for (Updatable u : isten.getUpdatables()) {
            if (u.getClass().equals(Villain.class)) {
                currentRoom = getPlayerRoom(isten);
                Villain villain = (Villain) u;
                if ((currentRoom != null && currentRoom.equals(villain.getRoom())) && currentRoom.getRoomType() != RoomType.GAS&&!villain.getIsFainted()) {
                   //Ha van akkora szerencsenk, hogy van item nalunk, ami megmentene megse halunk meg
                    if(!isten.getInventory().avoidVillain(deltaTime)){
                        if (localPlayer && playerSound != null)
                            AudioManager.closeSound(playerSound);
                        return true;
                    }

                }
            }
        }
        return false;
    }

    //kiszerveztem a fenti fv-t, mert nekem is kellett, és máshol később is hasznos lehet, ha kell, unitRoomra is ki lehetne szervezni
    public Room getPlayerRoom(Isten isten){
        UnitRoom[][] unitRooms= isten.getMap().getUnitRooms();
        for(int i = 0; i < unitRooms.length;i++){
            for(int j = 0; j<unitRooms[i].length;j++){
                if (playerCollider.getPosition().x >= unitRooms[i][j].getPosition().x - 0.5 &&
                        playerCollider.getPosition().x <= unitRooms[i][j].getPosition().x + 0.5 &&
                        playerCollider.getPosition().y >= unitRooms[i][j].getPosition().y - 0.5 &&
                        playerCollider.getPosition().y <= unitRooms[i][j].getPosition().y + 0.5)
                {
                    return unitRooms[i][j].getOwnerRoom();
                }
            }
        }
        return null;
    }
    @Override
    public void onDestroy() {
        //not implemented yet
    }
    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
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

    //Needed for instant animation change
    protected void sendAnimationData(Isten isten) {
        //implemented in PlayerMP -> override
    }

    public void setSkinID(int skinID) {
        this.skinID = skinID;
    }

    public int getSkinID() {
        return skinID;
    }

    public boolean isFainted() {
        return isFainted;
    }
}