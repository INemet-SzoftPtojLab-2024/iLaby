package main.java.org.game;

import main.java.org.entities.villain.Villain;
import main.java.org.game.Camera.Camera;
import main.java.org.game.Graphics.*;

import main.java.org.entities.player.Player;

import main.java.org.game.Input.Input;
import main.java.org.game.Map.Map;
import main.java.org.game.UI.*;
import main.java.org.game.physics.PhysicsEngine;
import main.java.org.game.updatable.Updatable;
import main.java.org.items.ChestManager;
import main.java.org.items.ItemManager;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;

/**
 * The main class representing the game part of the program.
 */
public class Isten {
    private final PhysicsEngine physicsEngine;
    protected final GameRenderer renderer;
    protected final ArrayList<Updatable> updatables;
    private final ArrayList<Updatable> pendingAddedUpdatables;
    private final ArrayList<Updatable> pendingRemovedUpdatables;
    private Player player;
    private Map map;
    private Inventory inventory;
    private final Input inputHandler;
    private final ItemManager itemManager;
    private final Camera camera;

    /**
     * Constructor for Isten.
     * Initializes the physics engine, game renderer, and list of updatables.
     */
    public Isten() {
        inventory=new Inventory(5);
        map=new Map(10, 10, 15);
        player = new Player("II. Németh Szilárd");
        itemManager=new ItemManager();
        inputHandler = new Input();
        camera = new Camera();
        physicsEngine = new PhysicsEngine();
        renderer = new GameRenderer(camera, inputHandler);
        updatables = new ArrayList<>();
        pendingAddedUpdatables = new ArrayList<>();
        pendingRemovedUpdatables = new ArrayList<>();
    }

    /**
     * Method to update the game state.
     *
     * @param deltaTime The time elapsed since the last update
     */
    public void update(double deltaTime) {

        inputHandler.update();


        physicsEngine.step(deltaTime);


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

        //calculate render positions, check for UI inputs and then render
        renderer.calculateRenderedPositions();
        renderer.processUIInputs(inputHandler);
        renderer.repaint();
    }

    /**
     * Method to initialize the game.
     */
    public void init() {
        addUpdatables();
        addRenderables();
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
        updatables.add(new ChestManager(500));//majd a játékba nem kell 500 láda, csak szemléltetésképp kell ilyen sok

        updatables.add(new Player("II. Németh Szilárd"));
        updatables.add(new Villain("Gonosz1", new Vec2(8,7), "./assets/villain/villain1.png"));
        updatables.add(new Villain("Gonosz2", new Vec2(5,5), "./assets/villain/villain2.png"));
        updatables.add(new Villain("Gonosz3", new Vec2(3,3), "./assets/villain/villain3.png"));
        updatables.add(new TimeCounter());
        updatables.add(new Help());
        updatables.add(new GameMenu());
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
}
