package main.java.org.game;

import main.java.org.game.Camera.Camera;
import main.java.org.game.Graphics.*;

import main.java.org.entities.player.Player;

import main.java.org.game.Input.Input;
import main.java.org.game.Map.Map;
import main.java.org.game.physics.PhysicsEngine;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;

/**
 * The main class representing the game part of the program.
 */
public class Isten {
    private final PhysicsEngine physicsEngine;
    private final GameRenderer renderer;
    private final ArrayList<Updatable> updatables;
    private final Input inputHandler;
    private final Camera camera;

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
        updatables.add(new Map(40,30));
    }

    /**
     * Method to update the game state.
     *
     * @param deltaTime The time elapsed since the last update
     */
    public void update(double deltaTime) {

        inputHandler.update();

        physicsEngine.step(deltaTime);

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

        if(inputHandler.isKeyDown(65) ){
            camera.Pan(new Vec2(-0.01f,0));
        }
        if(inputHandler.isKeyDown(68) ){
            camera.Pan(new Vec2(0.01f,0));
        }
        if(inputHandler.isKeyDown(87) ){
            camera.Pan(new Vec2(0,0.01f));
        }
        if(inputHandler.isKeyDown(83) ){
            camera.Pan(new Vec2(0,-0.01f));
        }
        if(inputHandler.isKeyDown(78)) {
            camera.Zoom(1.0f / 1.001f);
        }
        if(inputHandler.isKeyDown(90)) {
            camera.Zoom(1.001f);
        }


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
    private void addRenderables() {

    }

    /**
     * Method to add updatable objects to the game.
     */
    private void addUpdatables()
    {
        updatables.add(new Player());
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
}
