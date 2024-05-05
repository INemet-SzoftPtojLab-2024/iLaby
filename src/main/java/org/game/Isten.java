    package main.java.org.game;

import main.java.org.entities.villain.Villain;
import main.java.org.game.Camera.Camera;
import main.java.org.game.Graphics.*;

import main.java.org.entities.player.Player;

import main.java.org.game.Input.Input;
import main.java.org.game.Map.EdgeBetweenRooms;
import main.java.org.game.Map.EdgePiece;
import main.java.org.game.Map.Map;
import main.java.org.game.PlayerPrefs.PlayerPrefs;
import main.java.org.game.UI.*;
import main.java.org.game.physics.PhysicsEngine;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import main.java.org.items.ChestManager;
import main.java.org.items.ItemManager;

import main.java.org.networking.*;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

    /**
 * The main class representing the game part of the program.
 */
public class Isten {
    private HandlerManager handlerManager;
    private final PhysicsEngine physicsEngine;
    protected final GameRenderer renderer;
    protected final ArrayList<Updatable> updatables;
    private final ArrayList<Updatable> pendingAddedUpdatables;
    private final ArrayList<Updatable> pendingRemovedUpdatables;

    private Map map;
    private Inventory inventory;
    private final Input inputHandler;
    private final ItemManager itemManager;

    private final Camera camera;

    private GameClient socketClient;
    private GameServer socketServer;

    private PlayerMP player;
    private ChestManager chestManager;
    private Minimap minimap;

        //ONLY CONTAINS COLLIDERS AND IMAGES OF EDGEPIECES
        //DOES NOT REPLACE MAP
        private EdgeBetweenRooms edgeBetweenRooms;
    /**
     * Constructor for Isten.
     * Initializes the physics engine, game renderer, and list of updatables.
     */
    public Isten() {
        inventory=new Inventory(5);
        map=new Map(this, 50, 50, 10);
        chestManager = new ChestManager(175);
        itemManager=new ItemManager();
        inputHandler = new Input();
        camera = new Camera();
        physicsEngine = new PhysicsEngine();
        renderer = new GameRenderer(camera, inputHandler);
        updatables = new ArrayList<>();
        pendingAddedUpdatables = new ArrayList<>();
        pendingRemovedUpdatables = new ArrayList<>();

        edgeBetweenRooms = new EdgeBetweenRooms();
        physicsEngine.addColliderGroup(edgeBetweenRooms.getColliderGroup());
        handlerManager = new HandlerManager(this);
    }

    /**
     * Method to update the game state.
     *
     * @param deltaTime The time elapsed since the last update
     */
    public void update(double deltaTime) {


        inputHandler.update();

        if(socketServer == null || socketServer.isInitialized()) physicsEngine.step(deltaTime);


        //remove pending updatables from updatables
        for (Updatable u : pendingRemovedUpdatables)
            if (!u.isDestroyed()) {
                u.setDestroyedTrue();
                u.onDestroy();
            }
        updatables.removeAll(pendingRemovedUpdatables);
        pendingRemovedUpdatables.clear();

        //add pending updatables to updatables
        updatables.addAll(pendingAddedUpdatables);
        pendingAddedUpdatables.clear();

        //check if updatable has been initialized
        for (Updatable u : updatables)
            if (!u.isInitialized()) {
                u.setInitializedTrue();
                u.onStart(this);
            }


        //call onUpdates
        for (Updatable u : updatables)
            u.onUpdate(this, deltaTime);


        //ServerUpdate
        if(socketServer != null) {
            socketServer.updateServer(this, deltaTime);
        }



        //Manage handlers of client
        handlerManager.executeTasks();


        //calculate render positions, check for UI inputs and then render
        renderer.calculateRenderedPositions();
        renderer.processUIInputs(inputHandler);
        renderer.repaint();
    }

    /**
     * Method to initialize the game.
     */

    public void initMP() {
        //Set localPlayer to true, so that only this player can be moved and followed by the camera on this client
        player = new PlayerMP(JOptionPane.showInputDialog(this.getRenderer(),"Username"),null,-1);


        player.localPlayer = true;

        addUpdatables();
        addRenderables();

        int skinID = PlayerPrefs.getInt("skin");
        player.setSkinID(skinID);
        Packet00Login loginPacket = new Packet00Login(player.getUsername(), 0, 0, skinID);

        if(socketServer != null) {
            socketServer.addConnection(player,loginPacket);
        }

        if(JOptionPane.showConfirmDialog(this.getRenderer(), "Server?") == 0) {
            socketServer = new GameServer(this);
            socketServer.start();

        }
        socketClient = new GameClient(this, "localhost");
        socketClient.start();

        loginPacket.writeData(socketClient);

        update(0);
    }
    public void init() {
        //Create own player
        player = new PlayerMP(JOptionPane.showInputDialog(this.getRenderer(),"Username"),null,-1);

        player.localPlayer = true;

        addUpdatables();
        addRenderables();

        int skinID = PlayerPrefs.getInt("skin");
        player.setSkinID(skinID);
        Packet00Login loginPacket = new Packet00Login(player.getUsername(), 0, 0, skinID);

        if(socketServer != null) {
            socketServer.addConnection(player,loginPacket);
        }

        socketServer = new GameServer(this);
        socketServer.start();

        socketClient = new GameClient(this, "localhost");
        socketClient.start();

        loginPacket.writeData(socketClient);
    }

    /**
     * Method to add renderable objects to the game renderer.
     */
    protected void addRenderables() {
    }

    /**
     * Method to add updatable objects to the game.
     */
    protected void addUpdatables() {

        updatables.add(player);
        updatables.add(itemManager);
        updatables.add(inventory);
        updatables.add(map);

        updatables.add(chestManager);//majd a játékba nem kell 500 láda, csak szemléltetésképp kell ilyen sok


        updatables.add(new TimeCounter());
        updatables.add(new Help());
        updatables.add(new GameMenu());

        minimap = new Minimap(200,200,20,2);
        updatables.add(minimap);
    }

    /**
     * Method to get the game renderer.
     *
     * @return The game renderer
     */
    public GameRenderer getRenderer() {
        return renderer;
    }

    /**
     * returns the physics engine of the isten
     */
    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    /**
     * returns the inputhandler of the isten
     */
    public Input getInputHandler() {
        return inputHandler;
    }

    /**
     * returns the camera of the isten
     */
    public Camera getCamera() {
        return this.camera;
    }

    public Player getPlayer(){return player;}
    public Inventory getInventory(){return inventory;}
    public ItemManager getItemManager(){return itemManager;}
    public Map getMap(){return map;}

    public void addUpdatable(Updatable u) {
        pendingAddedUpdatables.add(u);
    }

    public void removeUpdatable(Updatable u) {
        pendingRemovedUpdatables.add(u);
    }

    public int getPlayerMPIndex(String username) {
        int index = 0;
        for(int i = 0; i < updatables.size(); i++) {
            Updatable u = updatables.get(i);
            if(u instanceof PlayerMP) {
                if(((PlayerMP)u).getUsername().equalsIgnoreCase(username)) {
                    break;
                }

            }
            index++;
        }
        return index;
    }

    public int getVillainIndex(String villainName) {
        int index = 0;
        for(int i = 0; i < updatables.size(); i++) {
            Updatable u = updatables.get(i);
            if(u instanceof Villain) {
                if(((Villain)u).getVillainName().equalsIgnoreCase(villainName)) {
                    break;
                }

            }
            index++;
        }
        return index;
    }
    public Updatable getUpdatable(int index) {
        if(index >= updatables.size()) return null;
        return updatables.get(index);
    }

    /**
     * returns an ArrayList of updatables of the given type <br>
     * <br>
     * how to use it:
     * ArrayList< Player> alma=new ArrayList<>();
     * alma = isten.getUpdatablesByType(Player.class);
     * @param type the Class of the elements
     * @return an array list of elements
     * @param <E> the type of the queried elements
     */
    public final <E extends Updatable> ArrayList<E> getUpdatablesByType(Class<E> type)
    {
        ArrayList<E> tempList=new ArrayList<>();
        for(int i=0;i<updatables.size();i++)
        {
            if(type.isInstance(updatables.get(i)))
                tempList.add((E)updatables.get(i));
        }

        return tempList;
    }

    public GameClient getSocketClient() {
        return socketClient;
    }

    public ArrayList<Updatable> getUpdatables() {
        return updatables;
    }

    public GameServer getSocketServer() {
        return socketServer;
    }

    public HandlerManager getHandlerManager() {
        return handlerManager;
    }

    public ChestManager getChestManager() { return chestManager; }
    public Minimap getMinimap() {
        return minimap;
    }

    //ONLY CONTAINS COLLIDERS AND IMAGES OF EDGEPIECES
    public EdgeBetweenRooms getEdgeBetweenRooms() {
        return edgeBetweenRooms;
    }
    public void addEdgePiece(EdgePiece piece) {
            edgeBetweenRooms.getWalls().add(piece);
    }

    public void removeEdgePiece(EdgePiece piece) {
        renderer.deleteRenderable(piece.getImage());
        edgeBetweenRooms.getColliderGroup().removeCollider(piece.getCollider());
        edgeBetweenRooms.getWalls().remove(piece);
    }
}
