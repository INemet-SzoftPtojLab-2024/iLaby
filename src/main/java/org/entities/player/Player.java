package main.java.org.entities.player;


import main.java.org.entities.villain.Villain;
import main.java.org.game.Audio.AudioManager;
import main.java.org.game.Audio.Sound;
import main.java.org.game.Graphics.*;

import main.java.org.entities.Entity;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.PP.PP_FogOfWar;
import main.java.org.game.Isten;
import main.java.org.game.Map.Graph;
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
import main.java.org.networking.Packet25PlayerForDoorOpen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * The player class, makes almost everything related to the player.
 */
public class Player extends Entity {

    protected Isten isten;
    protected Inventory inventory;
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
    private boolean playerInVillainRoom = false;

    private PP_FogOfWar fogOfWar=null;
    private BufferedImage fogOfWarImage;
    private int mapX, mapY; private int rendererWidth=0, rendererHeight=0;
    private char[] fogOfWarHelper=null; private Vec2[] fogOfWarHelperOffsets=null;
    protected ArrayList<Integer> fogSyncIncoming=null, fogSyncOutgoing=null;
    private boolean fogOfWarDrawing=false;
    private final Object fogOfWarSync=new Object();
    private final float fogDistance=2;
    private final int fogMaxResolution=500;

    public Player(Isten isten) {
        playerCollider = null;
        playerImage = null;
        death = null;
        winBgn = null;
        motivational = null;
        sieg = null;
        activeImage = 0;
        time = 0.0f;
        playerName = new Text(PlayerPrefs.getString("name"), new Vec2(0, 0), "./assets/Bavarian.otf", 15, 0, 0, 255);
        playerName.setShadowOn(false);
        alive = true;
        won = false;
        spawnPosition = new Vec2(0, 0);
        faintingTime = 0;
        isFainted = false;
        isInGasRoomButHasMask = false;
        speed=2;
        inventory=new Inventory(5, this);
        isten.addUpdatable(inventory);

    }

    public Player(Isten isten, String name) {
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
        inventory=new Inventory(5, this);
        isten.addUpdatable(inventory);
    }

    public Player(Isten isten, String name, Vec2 spawnPosition) {
        this(isten,name);
        this.spawnPosition.x = spawnPosition.x;
        this.spawnPosition.y = spawnPosition.y;
    }

    @Override
    public void onStart(Isten isten) {
        //called when the player is initialized

        Vec2 playerScale = new Vec2(0.5f, 0.5f);
        Vec2 faintedScale = new Vec2(0.6f, 0.6f);
        if(skinID > 1)faintedScale = new Vec2(0.53f, 0.53f);

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
        if(localPlayer)
        {
            fogOfWarImage=null;
            fogOfWar=new PP_FogOfWar(fogOfWarImage);

            mapX=isten.getMap().getMapRowSize()+1;//a plusz 1 azert kell, hogy a falak is latszodjanak
            mapY=isten.getMap().getMapColumnSize()+1;
            fogOfWarHelper=new char[mapX*mapY];
            Arrays.fill(fogOfWarHelper,(char)22);

            fogOfWarHelperOffsets=new Vec2[mapX*mapY];

            fogSyncIncoming=new ArrayList<>();
            fogSyncOutgoing=new ArrayList<>();

            isten.getRenderer().registerPostProcessingEffect(fogOfWar);
        }
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

            //check if door is opened by player
            if(isten.getInputHandler().isKeyReleased(KeyEvent.VK_O)) {
                Packet25PlayerForDoorOpen packet = new Packet25PlayerForDoorOpen(isten.getPlayer().getPlayerName().getText());
                packet.writeData(isten.getSocketClient());
            }

            //Room currentRoom = getPlayerRoom(isten,playerCollider.getPosition());
            if(changedRoom) {

                if(playerInVillainRoom && !inventory.avoidVillain(deltaTime)){
                    if (localPlayer && playerSound != null) {
                        alive = false;
                        AudioManager.closeSound(playerSound);
                    }
                }
                //beallitani a playerCountjat a szobanak:: (akar kiszervezheto fv-be)
                System.out.println("Changed room");
                //Lasd Inventory canAvoidVillain member var
                inventory.setCanAvoidVillain(false);
                //Ha szobat valt a player, akkor a kovetkezo alkalommar, amikor gegnerrel talalkozik hasznalodnia kell a Tvsz-nek
                inventory.resetShouldUseChargeForTvsz();
                changedRoom = false;
            }
            else if(playerInVillainRoom && !inventory.hasTvsz() && inventory.hasSorospohar()) {
                inventory.getStoredItems().get(inventory.getSorospoharSlot()).use(this,deltaTime);
            }
            else if(playerInVillainRoom && !inventory.avoidVillain(deltaTime)) {
                if (localPlayer && playerSound != null) {
                    alive = false;
                    AudioManager.closeSound(playerSound);
                }
            }

            if(currentRoom!=null)isInGasRoom= currentRoom.getRoomType() == RoomType.GAS;
            if (isInGasRoom) {
               // isInGasRoom = false;
                if (!inventory.getExistenceOfGasMask()) {
                    faintingTime = 0;
                    isFainted = true;
                    speed = 1;
                    if(localPlayer && !inventory.isEmpty()) {
                        inventory.dropAllItems(isten);
                    }

                    isInGasRoomButHasMask = false;
                }
                else {
                    int index= 0;
                    for(int i = 0;i<inventory.getSize();i++){
                        if(inventory.getStoredItems().get(i) instanceof Gasmask) break;
                        index++;
                    }
                    isInGasRoomButHasMask = true;
                    inventory.getStoredItems().get(index).use(this, deltaTime);
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
                    if(!isFainted && (prev == 5 || prev == 8)) {
                        activeImage = 1;
                    }
                    else if(!isFainted && (prev == 7 || prev == 9)){
                        activeImage=3;
                    }else {
                        if (playerCollider.getVelocity().x > 0) activeImage = 0;
                        else if (playerCollider.getVelocity().x < 0) activeImage = 2;
                        else if (leftFacing) activeImage = 2;
                        else activeImage = 0;
                        if (prev % 2 == 0 || playerCollider.getVelocity().magnitude() == 0.0f) activeImage++;
                        if (isFainted) activeImage += 4;
                    }
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

        if(alive&&fogOfWar!=null)
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
                    return true;
                }
            }
        }
        return false;
    }

    //kiszerveztem a fenti fv-t, mert nekem is kellett, és máshol később is hasznos lehet, ha kell, unitRoomra is ki lehetne szervezni
    public Room getPlayerRoom(Isten isten, Vec2 playerPos){
        /*UnitRoom[][] unitRooms= isten.getMap().getUnitRooms();
        for(int i = 0; i < unitRooms.length;i++){
            for(int j = 0; j<unitRooms[i].length;j++){
                if (playerPos.x >= unitRooms[i][j].getPosition().x - 0.5 &&
                        playerPos.x <= unitRooms[i][j].getPosition().x + 0.5 &&
                        playerPos.y >= unitRooms[i][j].getPosition().y - 0.5 &&
                        playerPos.y <= unitRooms[i][j].getPosition().y + 0.5)
                {
                    return unitRooms[i][j].getOwnerRoom();
                }
            }
        }
        return null;*/
       ///efffektivebb megoldas
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
        final float clearFogDistance=0.25f*fogDistance;
        final float transparencyHelper=1/(0.75f*fogDistance);

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

        synchronized (fogSyncIncoming)//sync incoming
        {
            for(int i=0;i<fogSyncIncoming.size();i+=2)
            {
                if(fogOfWarHelper[fogSyncIncoming.get(i)]>(char)(int)fogSyncIncoming.get(i+1))
                    fogOfWarHelper[fogSyncIncoming.get(i)]=(char)(int)fogSyncIncoming.get(i+1);
            }

            fogSyncIncoming.clear();
        }

        ArrayList<Integer> fogChanged=new ArrayList<>();
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
                    char opaqueness=(char)(22*Math.sqrt(transparencyHelper*(distance-clearFogDistance)));
                    if(opaqueness<fogOfWarHelper[currentIndex])
                    {
                        fogOfWarHelper[currentIndex]=opaqueness;
                        fogChanged.add(currentIndex);
                        fogChanged.add((int)fogOfWarHelper[currentIndex]);
                    }
                    continue;
                }

                if(fogOfWarHelper[currentIndex]!=0)
                {
                    fogChanged.add(currentIndex);
                    fogChanged.add(0);
                    fogOfWarHelper[currentIndex]=0;
                }
            }
        }

        synchronized (fogSyncOutgoing)//sync outgoing
        {
            fogSyncOutgoing.addAll(fogChanged);
        }



        //draw image
        if(fogOfWarImage==null)
        {
            if(isten.getRenderer().getWidth()>0&&isten.getRenderer().getHeight()>0)
            {
                int x=1,y=1;
                if(isten.getRenderer().getWidth()>isten.getRenderer().getHeight())
                {
                    x=fogMaxResolution;
                    y=(fogMaxResolution*isten.getRenderer().getHeight())/isten.getRenderer().getWidth();
                }
                else
                {
                    y=fogMaxResolution;
                    x=(fogMaxResolution*isten.getRenderer().getWidth())/isten.getRenderer().getHeight();
                }
                fogOfWarImage=new BufferedImage(x,y,BufferedImage.TYPE_INT_ARGB);
                fogOfWar.setImage(fogOfWarImage);

                rendererWidth=isten.getRenderer().getWidth();
                rendererHeight=isten.getRenderer().getHeight();

                do{
                    final Vec2 maxRandomOffsetOrigin=new Vec2(
                            -0.25f*isten.getCamera().getPixelsPerUnit()*fogOfWarImage.getWidth()/(float)rendererWidth,
                            -0.25f*isten.getCamera().getPixelsPerUnit()*fogOfWarImage.getHeight()/(float)rendererHeight
                    );
                    final Vec2 maxRandomOffsetBound=new Vec2(-0.5f*maxRandomOffsetOrigin.x,-0.5f*maxRandomOffsetOrigin.y);
                    Random rand=new Random();
                    for(int i=0;i<fogOfWarHelperOffsets.length;i++)
                        fogOfWarHelperOffsets[i]=new Vec2(rand.nextFloat(maxRandomOffsetOrigin.x, maxRandomOffsetBound.x), rand.nextFloat(maxRandomOffsetOrigin.y, maxRandomOffsetBound.y));
                }while(69==420);
            }
            else
                return;
        }
        else if(rendererWidth!=isten.getRenderer().getWidth()||rendererHeight!=isten.getRenderer().getHeight())
        {
            int x=1,y=1;
            if(isten.getRenderer().getWidth()>isten.getRenderer().getHeight())
            {
                x=fogMaxResolution;
                y=(fogMaxResolution*isten.getRenderer().getHeight())/isten.getRenderer().getWidth();
            }
            else
            {
                y=fogMaxResolution;
                x=(fogMaxResolution*isten.getRenderer().getWidth())/isten.getRenderer().getHeight();
            }
            fogOfWarImage=new BufferedImage(x,y,BufferedImage.TYPE_INT_ARGB);
            fogOfWar.setImage(fogOfWarImage);

            rendererWidth=isten.getRenderer().getWidth();
            rendererHeight=isten.getRenderer().getHeight();


            do{
                final Vec2 maxRandomOffsetOrigin=new Vec2(
                        -0.25f*isten.getCamera().getPixelsPerUnit()*fogOfWarImage.getWidth()/(float)rendererWidth,
                        -0.25f*isten.getCamera().getPixelsPerUnit()*fogOfWarImage.getHeight()/(float)rendererHeight
                );
                final Vec2 maxRandomOffsetBound=new Vec2(-0.5f*maxRandomOffsetOrigin.x,-0.5f*maxRandomOffsetOrigin.y);
                Random rand=new Random();
                for(int i=0;i<fogOfWarHelperOffsets.length;i++)
                    fogOfWarHelperOffsets[i]=new Vec2(rand.nextFloat(maxRandomOffsetOrigin.x, maxRandomOffsetBound.x), rand.nextFloat(maxRandomOffsetOrigin.y, maxRandomOffsetBound.y));
            }while(69==420);
        }

        Vec2 imageStart=Vec2.sum(pos, new Vec2(-0.5f*rendererWidth/(float)isten.getCamera().getPixelsPerUnit(),0.5f*rendererHeight/(float)isten.getCamera().getPixelsPerUnit()));
        Vec2 imageEnd=Vec2.sum(pos, new Vec2(0.5f*rendererWidth/(float)isten.getCamera().getPixelsPerUnit(),-0.5f*rendererHeight/(float)isten.getCamera().getPixelsPerUnit()));

        minX=(int)imageStart.x; maxX=(int)imageEnd.x+1;
        minY=(int)imageEnd.y; maxY=(int)imageStart.y+1;
        if(minX<0) minX=0;
        if(maxX>=mapX) maxX=mapX-1;
        if(minY<0) minY=0;
        if(maxY>=mapY) maxY=mapY-1;

        final Vec2 fogPixelsPerUnit=new Vec2(
                isten.getCamera().getPixelsPerUnit()*fogOfWarImage.getWidth()/(float)rendererWidth,
                isten.getCamera().getPixelsPerUnit()*fogOfWarImage.getHeight()/(float)rendererHeight
        );

        final Vec2 fogUnitSizeInPixels=new Vec2(1.8f*fogPixelsPerUnit.x, 1.8f*fogPixelsPerUnit.y);

        final Vec2 imageStartInScreenSpace=new Vec2(
                ((float) fogOfWarImage.getWidth() /2)-(pos.x-minX)*fogPixelsPerUnit.x-0.5f*fogUnitSizeInPixels.x,
                ((float) fogOfWarImage.getHeight() /2)-(maxY-pos.y)*fogPixelsPerUnit.y-0.5f*fogUnitSizeInPixels.y
                );

        Vec2 currentPos=imageStartInScreenSpace.clone();


        ArrayList<PP_FogOfWar.FogUnitPP> ppUnits=new ArrayList<>();

        for(int i=maxY;i>=minY;i--, currentPos.y+=fogPixelsPerUnit.y)
        {
            int currentIndex=i*mapX+minX;
            currentPos.x=imageStartInScreenSpace.x;
            for(int j=minX;j<=maxX;j++, currentIndex++, currentPos.x+=fogPixelsPerUnit.x)
            {
                if(fogOfWarHelper[currentIndex]<22)
                {
                    ppUnits.add(new PP_FogOfWar.FogUnitPP(
                            (int)(currentPos.x+fogOfWarHelperOffsets[currentIndex].x),
                            (int)(currentPos.y+fogOfWarHelperOffsets[currentIndex].y),
                            (int)fogUnitSizeInPixels.x,
                            (int)fogUnitSizeInPixels.y,
                            fogOfWarHelper[currentIndex]));
                }
            }
        }

        ppUnits.sort((ppUnit1, ppUnit2) -> (ppUnit2.index - ppUnit1.index));

        fogOfWar.setUnitsToDraw(ppUnits);

        synchronized (fogOfWarSync)
        {
            fogOfWarDrawing=false;
        }
    }

    public void appendFogSyncInfo(int[] data)
    {
        if(fogSyncIncoming==null)
            return;

        synchronized (fogSyncIncoming)
        {
            for(int i=0;i<data.length;i++)
                fogSyncIncoming.add((Integer)data[i]);
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

    public Inventory getInventory() {
        return inventory;
    }

    public void setPlayerInVillainRoom(boolean isInRoomWithVillain) {
        playerInVillainRoom = isInRoomWithVillain;
    }

    public ImageUI getDeath() {
        return death;
    }

    public ImageUI getWinBgn() {
        return winBgn;
    }

    public TextUI getMotivational() {
        return motivational;
    }

    public TextUI getSieg() {
        return sieg;
    }

    public boolean isWon() {
        return won;
    }

    public Sound getPlayerSound() {
        return playerSound;
    }

    public double getFaintingTime() {
        return faintingTime;
    }

    public boolean isInGasRoomButHasMask() {
        return isInGasRoomButHasMask;
    }

    public float getSpeed() {
        return speed;
    }

    public Vec2 getSpawnPosition() {
        return spawnPosition;
    }

    public int getRun() {
        return run;
    }
}