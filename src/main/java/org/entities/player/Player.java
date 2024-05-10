package main.java.org.entities.player;


import main.java.org.entities.villain.Villain;
import main.java.org.game.Audio.AudioManager;
import main.java.org.game.Audio.Sound;
import main.java.org.game.Graphics.*;

import main.java.org.entities.Entity;

import main.java.org.game.Graphics.PP.PP_FogOfWar;
import main.java.org.game.Isten;
import main.java.org.game.Map.RoomType;
import main.java.org.game.UI.Inventory;
import main.java.org.game.UI.TimeCounter;
import main.java.org.game.physics.Collider;
import main.java.org.items.Item;
import main.java.org.items.usable_items.Gasmask;
import main.java.org.items.usable_items.Logarlec;
import main.java.org.linalg.Vec2;
import main.java.org.networking.Packet03Animation;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.PlayerPrefs.PlayerPrefs;
import main.java.org.game.updatable.Updatable;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The player class, makes almost everything related to the player.
 */
public class Player extends Entity {

    Isten isten;
    Collider playerCollider;
    ArrayList<Image> playerImage;
    ImageUI death;
    ImageUI winBgn;
    int activeImage;
    TextUI motivational;
    TextUI sieg;
    float time;
    Text playerName;
    boolean alive;  //Bool variable, to store status of player: ded or alive
    boolean won;
    Sound playerSound = null;
    double faintingTime;
    boolean isFainted;
    boolean isInGasRoomButHasMask;
    float speed;
    protected Vec2 spawnPosition;
    protected int run = 1;
    protected int skinID;
    public boolean localPlayer = false;
    public Room currentRoom = null;
    private boolean isInGasRoom = false;
    private boolean changedRoom = false;

    protected Inventory inventory;

    private PP_FogOfWar fogOfWar=null;
    private BufferedImage fogOfWarImage=null;
    private int[] fogOfWarRaw=null;
    private char[] fogOfWarHelper=null; private int mapX, mapY;
    private boolean fogOfWarDrawing=false;
    private final Object fogOfWarSync=new Object();
    private final float fogDistance=3;

    public Player(String name, Isten isten) {
        this.isten = isten;
        inventory = new Inventory(5);
        isten.addUpdatable(inventory);

        playerCollider = null;
        playerImage = null;
        death = null;
        winBgn = null;
        activeImage = 0;
        motivational = null;
        sieg = null;
        time = 0.0f;
        playerName = new Text(name, new Vec2(0, 0), "./assets/Monocraft.ttf", 15, 0, 0, 255);
        playerName.setShadowOn(false);
        alive = true;
        won = false;
        spawnPosition = new Vec2(0, 0);
        faintingTime = 0;
        isFainted = false;
        speed=2;
    }

    public Player(String name, Vec2 spawnPosition, Isten isten) {
        this(name,isten);
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

        winBgn = new ImageUI(new Vec2(spawnPosition.x, spawnPosition.y), new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()), "./assets/character/passed.png");
        winBgn.setSortingLayer(-70);
        winBgn.setVisibility(false);
        winBgn.setAlignment(Renderable.CENTER, Renderable.CENTER);
        isten.getRenderer().addRenderable(winBgn);

        motivational = new TextUI("Skill issue.", new Vec2(0, -170), 38, 200, 200, 200);
        motivational.setSortingLayer(-71);
        motivational.setVisibility(false);
        motivational.setAlignment(Renderable.CENTER, Renderable.CENTER);
        isten.getRenderer().addRenderable(motivational);

        sieg = new TextUI("You shall have the opportunity to get your Diploma.", new Vec2(0, -110), 34, 239, 239, 239);
        sieg.setSortingLayer(-71);
        sieg.setVisibility(false);
        sieg.setAlignment(Renderable.CENTER, Renderable.CENTER);
        isten.getRenderer().addRenderable(sieg);

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
        AudioManager.preloadSound("./assets/audio/won.ogg");

        //fog of war
        fogOfWar=new PP_FogOfWar(fogOfWarImage);
        mapX=isten.getMap().getMapRowSize();
        mapY=isten.getMap().getMapColumnSize();
        fogOfWarHelper=new char[mapX*mapY];
        Arrays.fill(fogOfWarHelper,(char)127);
        isten.getRenderer().registerPostProcessingEffect(fogOfWar);
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        //called every frame
        if(won){ //if the logarlec is in the inventory
            if(!sieg.getVisibility()){
                AudioManager.closeSound(playerSound);
                sieg.setVisibility(true);
                winBgn.setVisibility(true);
                if(!AudioManager.isPlaying(playerSound))
                    playerSound = AudioManager.playSound("./assets/audio/won.ogg");
            }
        }
        else if (alive) {

            if (isFainted) {
                if (faintingTime > 10) {
                    faintingTime = 0;
                    isFainted = false;
                    speed = 2;
                }
            }

            for (int i = 0; i < inventory.getSize(); i++) {
                if (inventory.getStoredItems().get(i) instanceof Logarlec) {
                    won = true;
                    break;
                }
            }

            //Room currentRoom = getPlayerRoom(isten,playerCollider.getPosition());
            if(changedRoom) {
                //beallitani a playerCountjat a szobanak:: (akar kiszervezheto fv-be)

                //Lasd Inventory canAvoidVillain member var
                inventory.setCanAvoidVillain(false);
                //Ha szobat valt a player, akkor a kovetkezo alkalommar, amikor gegnerrel talalkozik hasznalodnia kell a Tvsz-nek
                inventory.resetShouldUseChargeForTvsz();
                changedRoom = false;
            }
            if (isInGasRoom) {
                isInGasRoom = false;
                if (!inventory.getExistenceOfGasMask()) {
                    faintingTime = 0;
                    isFainted = true;
                    speed = 1;
                    if(localPlayer) {
                        for (int i = 0; i < 5; i++) {
                            if (inventory.getStoredItems().get(i) != null) {
                                //TODO
                                // Should send item dropped to all clients.
                                // When on client, throws nullpointer exception
                                if(isten.getSocketServer() != null) inventory.getStoredItems().get(i).dropOnGround(new Vec2(currentRoom.getUnitRooms().get(i + 1).getPosition().x, currentRoom.getUnitRooms().get(i + 1).getPosition().y));
                            }
                        }
                        inventory.dropAllItems(isten);
                    }

                    isInGasRoomButHasMask = false;
                }
                else {
                    int index=0;
                    for(;index<inventory.getSize();index++){
                        if(inventory.getStoredItems().get(index) instanceof Gasmask) break;
                    }
                    isInGasRoomButHasMask = true;
                    inventory.getStoredItems().get(index).use(deltaTime);
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
        winBgn.setScale(new Vec2(isten.getRenderer().getWidth(), isten.getRenderer().getHeight()));

        if(alive)
        {
            synchronized (fogOfWarSync)
            {
                if(!fogOfWarDrawing)
                {
                    Thread thread=new Thread(()->drawFogOfWar(isten));
                    thread.start();
                }
            }
        }
    }

    public boolean checkIfPlayerInVillainRoom(Isten isten,double deltaTime) {
        for (Updatable u : isten.getUpdatables()) {
            if (u.getClass().equals(Villain.class)) {
                Villain villain = (Villain) u;
                if ((currentRoom != null && currentRoom.equals(villain.getRoom())) && currentRoom.getRoomType() != RoomType.GAS&&!villain.getIsFainted()) {
                   //Ha van akkora szerencsenk, hogy van item nalunk, ami megmentene megse halunk meg
                    if(!inventory.avoidVillain(deltaTime)){
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
    public Room getPlayerRoom(Isten isten, Vec2 playerPos){
        int x = (int)(playerPos.x + 0.5f);
        int y = (int)(playerPos.y + 0.5f);
        //System.out.println(x + " " + y + " ownerroorm pozi " +  isten.getMap().getUnitRooms()[y][x].getColNum() + " " + isten.getMap().getUnitRooms()[y][x].getRowNum());
        return isten.getMap().getUnitRooms()[y][x].getOwnerRoom();

    }

    private void drawFogOfWar(Isten isten)
    {
        //check for newly discovered area
        final Vec2 pos=playerCollider.getPosition();
        //clearFogDistance and transparencyHelper are made to reduce the number of float operations
        final float clearFogDistance=0.6f*fogDistance;
        final float transparencyHelper=1/(0.4f*fogDistance);
        final float onePer127=1/127.0f;

        int minX=Math.round(pos.x-(float)Math.ceil(fogDistance));
        int maxX=Math.round(pos.x+(float)Math.ceil(fogDistance));

        int minY=Math.round(pos.y-(float)Math.ceil(fogDistance));
        int maxY=Math.round(pos.y+(float)Math.ceil(fogDistance));

        if(minX<0)
            minX=0;
        if(minY<0)
            minY=0;
        if(maxX>=mapX)
            maxX=mapX-1;
        if(maxY>=mapY)
            maxY=mapY-1;

        for(int i=minY; i<maxY;i++)
        {
            int currentIndex=i*mapX+minX;
            for(int j=minX;j<maxX;j++, currentIndex++)
            {
                float distance=(float)Math.sqrt((pos.x-j)*(pos.x-j)+(pos.y-i)*(pos.y-i));
                if(distance>fogDistance)
                    continue;
                if(distance>clearFogDistance)
                {
                    char opaqueness=(char)(127*transparencyHelper*(distance-clearFogDistance));
                    if(opaqueness<fogOfWarHelper[currentIndex])
                        fogOfWarHelper[currentIndex]=opaqueness;
                    continue;
                }
                fogOfWarHelper[currentIndex]=0;
            }
        }



        //draw image
        if(fogOfWarImage==null)
        {
            if(isten.getRenderer().getWidth()>3&&isten.getRenderer().getHeight()>3)
            {
                fogOfWarImage=new BufferedImage(isten.getRenderer().getWidth()/4,isten.getRenderer().getHeight()/4,BufferedImage.TYPE_INT_ARGB);
                fogOfWarRaw=new int[4*fogOfWarImage.getWidth()*fogOfWarImage.getHeight()];
                fogOfWar.setImage(fogOfWarImage);
            }
            else
                return;
        }
        else if(fogOfWarImage.getWidth()!=isten.getRenderer().getWidth()/4||fogOfWarImage.getHeight()!=isten.getRenderer().getHeight()/4)
        {
            fogOfWarImage=new BufferedImage(isten.getRenderer().getWidth()/4,isten.getRenderer().getHeight()/4,BufferedImage.TYPE_INT_ARGB);
            fogOfWarRaw=new int[4*fogOfWarImage.getWidth()*fogOfWarImage.getHeight()];
            fogOfWar.setImage(fogOfWarImage);
        }

        Arrays.fill(fogOfWarRaw,0);

        Vec2 imageStart=Vec2.sum(pos, new Vec2(-2/isten.getCamera().getPixelsPerUnit()*fogOfWarImage.getWidth(),2/isten.getCamera().getPixelsPerUnit()*fogOfWarImage.getHeight()));
        Vec2 delta=new Vec2(4/isten.getCamera().getPixelsPerUnit(),-4/isten.getCamera().getPixelsPerUnit());

        int currentIndex=3;//offset to alpha
        Vec2 currentPos=imageStart.clone();
        int lastX=-1000000000, currentX=0, currentY=0;
        float topLeft=0, topRight=0, bottomLeft=0, bottomRight=0;
        Vec2 topLeftPos=new Vec2(), topRightPos=new Vec2(), bottomLeftPos=new Vec2(), bottomRightPos=new Vec2();

        for(int i=0;i<fogOfWarImage.getHeight();i++, currentPos.y+=delta.y)
        {
            currentPos.x=imageStart.x;
            currentY=Math.round(currentPos.y+0.5f);

            for(int j=0;j<fogOfWarImage.getWidth();j++, currentIndex+=4, currentPos.x+=delta.x)
            {
                currentX=Math.round(currentPos.x-0.5f);
                if(currentX!=lastX)
                {
                    lastX=currentX;
                    boolean topNO=currentY<0||currentY>=mapY;
                    boolean bottomNO=currentY<1||currentY>mapY;
                    boolean leftNO=currentX<0||currentX>=mapX;
                    boolean rightNO=currentX<-1||!(currentX<mapX);

                    topLeft=0;
                    if(!(topNO||leftNO))
                        topLeft=onePer127*fogOfWarHelper[currentY*mapX+currentX];
                    topLeftPos.x=currentX; topLeftPos.y=currentY;

                    topRight=0;
                    if(!(topNO||rightNO))
                        topRight=onePer127*fogOfWarHelper[currentY*mapX+currentX+1];
                    topRightPos.x=currentX+1; topRightPos.y=currentY;;

                    bottomLeft=0;
                    if(!(bottomNO||leftNO))
                        bottomLeft=onePer127*fogOfWarHelper[(currentY-1)*mapX+currentX];
                    bottomLeftPos.x=currentX; bottomLeftPos.y=currentY-1;

                    bottomRight=0;
                    if(!(bottomNO||rightNO))
                        bottomRight=onePer127*fogOfWarHelper[(currentY-1)*mapX+currentX+1];
                    bottomRightPos.x=currentX+1; bottomRightPos.y=currentY-1;
                }

                fogOfWarRaw[currentIndex]= (int)(255*0.25f*(
                        bottomRight*(currentPos.x-bottomLeftPos.x)+
                        bottomLeft*(bottomRightPos.x-currentPos.x)+
                        topRight*(currentPos.x-topLeftPos.x)+
                        topLeft*(topRightPos.x-currentPos.x)+
                        bottomRight*(topRightPos.y-currentPos.y)+
                        bottomLeft*(topLeftPos.y-currentPos.y)+
                        topRight*(currentPos.y-bottomRightPos.y)+
                        topLeft*(currentPos.y-bottomLeftPos.y)
                ));
            }
        }

        fogOfWarImage.getRaster().setPixels(0,0, fogOfWarImage.getWidth(), fogOfWarImage.getHeight(),fogOfWarRaw);

        synchronized (fogOfWarSync)
        {
            fogOfWarDrawing=false;
        }
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
    public void isInGasRoom(boolean b) {
        isInGasRoom = b;
    }

    public boolean isInGasRoom() {
        return isInGasRoom;
    }

    public void changedRoom(boolean b) {
        changedRoom = b;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public Inventory getInventory() { return inventory; }
}