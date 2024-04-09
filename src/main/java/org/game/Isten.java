package main.java.org.game;

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
import main.java.org.manager.GameManager;

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

    private final Input inputHandler;

    private final Camera camera;
    private final Map map;
    private final Player player;
    private final Inventory inventory;
    private final ItemManager itemManager;

    /**
     * Constructor for Isten.
     * Initializes the physics engine, game renderer, and list of updatables.
     */
    public Isten() {
        inputHandler=new Input();
        camera=new Camera();
        inventory=new Inventory(5);
        physicsEngine=new PhysicsEngine();
        renderer=new GameRenderer(camera, inputHandler);
        updatables=new ArrayList<>();
        pendingUpdatables=new ArrayList<>();
        map=new Map(10, 10, 15);
        player=new Player("B"+(char)233+"la");
        itemManager=new ItemManager();
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
    private void addUpdatables()
    {
        updatables.add(player);
        updatables.add(itemManager);
        updatables.add(inventory);
        updatables.add(new TimeCounter(10));
        updatables.add(map);
        updatables.add(new ChestManager(500));//majd a játékba nem kell 500 láda, csak szemléltetésképp kell ilyen sok
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

    /** returns the camera of the isten */
    public Camera getCamera(){return this.camera;}
    /** returns the map of the isten */
    public Map getMap(){return this.map;}
    /** returns the player of the isten */
    public Player getPlayer(){return this.player;}
    public Inventory getInventory(){return this.inventory;}

    public ItemManager getItemManager() {return itemManager;}
    /**
     * returns the inputhandler of the isten
     */
    public Input getInputHandler() {
        return inputHandler;
    }

    

    public void addUpdatable(Updatable u) {
        pendingAddedUpdatables.add(u);
    }

    public void removeUpdatable(Updatable u) {
        pendingRemovedUpdatables.add(u);
    }
}
