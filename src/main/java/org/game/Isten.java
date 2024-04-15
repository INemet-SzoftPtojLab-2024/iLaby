package main.java.org.game;

import main.java.org.entities.villain.Villain;
import main.java.org.game.Camera.Camera;
import main.java.org.game.Graphics.*;

import main.java.org.entities.player.Player;

import main.java.org.game.Input.Input;
import main.java.org.game.Map.Map;
import main.java.org.game.Timer.TimeCounter;
import main.java.org.game.UI.GameMenu;
import main.java.org.game.UI.Help;
import main.java.org.game.UI.TimeCounter;
import main.java.org.game.physics.PhysicsEngine;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;
import main.java.org.networking.GameClient;
import main.java.org.networking.GameServer;
import main.java.org.networking.Packet00Login;
import main.java.org.networking.PlayerMP;

import javax.swing.*;
import java.util.ArrayList;

/**
 * The main class representing the game part of the program.
 */
public class Isten {
    private final PhysicsEngine physicsEngine;

    private final GameRenderer renderer;

    private final ArrayList<Updatable> updatables;
    private final ArrayList<Updatable> pendingAddedUpdatables;
    private final ArrayList<Updatable> pendingRemovedUpdatables;

    private final Input inputHandler;

    private final Camera camera;
    private GameClient socketClient;
    private GameServer socketServer;

    private PlayerMP player;

    /**
     * Constructor for Isten.
     * Initializes the physics engine, game renderer, and list of updatables.
     */
    public Isten() {
        inputHandler=new Input();
        camera=new Camera();
        physicsEngine=new PhysicsEngine();
        renderer=new GameRenderer(camera, inputHandler);
        updatables=new ArrayList<>();
        pendingAddedUpdatables=new ArrayList<>();
        pendingRemovedUpdatables=new ArrayList<>();
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
        for(Updatable u : pendingRemovedUpdatables)
            if(!u.isDestroyed())
            {
                u.setDestroyedTrue();
                u.onDestroy();
            }
        updatables.removeAll(pendingRemovedUpdatables);
        pendingRemovedUpdatables.clear();

        //add pending updatables to updatables
        updatables.addAll(pendingAddedUpdatables);
        pendingAddedUpdatables.clear();

        //check if updatable has been initialized
        for(Updatable u : updatables)
            if(!u.isInitialized())
            {
                u.setInitializedTrue();
                u.onStart(this);
            }

        //call onUpdates
        for(Updatable u : updatables)
            u.onUpdate(this,deltaTime);

        //calculate render positions, check for UI inputs and then render
        renderer.calculateRenderedPositions();
        renderer.processUIInputs(inputHandler);
        renderer.repaint();
    }

    /**
     * Method to initialize the game.
     */
    public void init() {
        player = new PlayerMP(JOptionPane.showInputDialog(this.getRenderer(),"Username"),null,-1);

        player.localPlayer = true;

        addUpdatables();
        addRenderables();

        Packet00Login loginPacket = new Packet00Login(player.getUsername());

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

    }

    /**
     * Method to add renderable objects to the game renderer.
     */
    private void addRenderables() {
    }

    /**
     * Method to add updatable objects to the game.
     */
    private void addUpdatables()
    {
        updatables.add(player);
        updatables.add(new Villain("Gonosz1", new Vec2(8,7), "./assets/villain/villain1.png"));
        updatables.add(new Villain("Gonosz2", new Vec2(5,5), "./assets/villain/villain2.png"));
        updatables.add(new Villain("Gonosz3", new Vec2(3,3), "./assets/villain/villain3.png"));
        updatables.add(new TimeCounter());
        updatables.add(new Help());
        updatables.add(new GameMenu());
        updatables.add(new Map(10, 10, 15));
    }

    /**
     * Method to get the game renderer.
     *
     * @return The game renderer
     */
    public GameRenderer getRenderer() {
        return renderer;
    }

    /** returns the physics engine of the isten */
    public PhysicsEngine getPhysicsEngine(){return physicsEngine;}

    /** returns the inputhandler of the isten */
    public Input getInputHandler(){return inputHandler;}

    /** returns the camera of the isten */
    public Camera getCamera(){return this.camera;}


    public void addUpdatable(Updatable u)
    {
        pendingAddedUpdatables.add(u);
    }

    public void removeUpdatable(Updatable u)
    {
        pendingRemovedUpdatables.add(u);
    }

    public void movePlayer(String username, float x, float y) {
        int index = getPlayerMPIndex(username);
        ((PlayerMP)this.updatables.get(index)).getPlayerCollider().setPosition(new Vec2(x,y));
    }

    private int getPlayerMPIndex(String username) {
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

    public GameClient getSocketClient() {
        return socketClient;
    }
}
