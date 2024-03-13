package main.java.org.game;

import main.java.org.game.Graphics.GameRenderer;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.Text;
import main.java.org.game.Map.Map;
import main.java.org.game.physics.PhysicsEngine;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;

/**
 * The main class representing the game part of the program.
 */
public class Isten {
    private PhysicsEngine physicsEngine;
    private GameRenderer renderer;
    private ArrayList<Updatable> updatables;

    /**
     * Constructor for Isten.
     * Initializes the physics engine, game renderer, and list of updatables.
     */
    public Isten() {
        physicsEngine=new PhysicsEngine();
        renderer=new GameRenderer();
        updatables=new ArrayList<>();
        updatables.add(new Map(20,20));
    }

    /**
     * Method to update the game state.
     *
     * @param deltaTime The time elapsed since the last update
     */
    public void update(double deltaTime) {

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

        String imagePath = "./assets/cube.jpg";

        renderer.addRenderable(new Text("Hello!", new Vec2(150, 100), "./assets/Monocraft.ttf", 68, 255, 255, 255));
        renderer.addRenderable(new Image(new Vec2(200,200), 1, 1, new Vec2(100,100), imagePath));
    }

    /**
     * Method to add updatable objects to the game.
     */
    private void addUpdatables()
    {

    }

    /**
     * Method to get the game renderer.
     *
     * @return The game renderer
     */
    public GameRenderer getRenderer() {
        return renderer;
    }

    /** returns the physics engine if the isten */
    public PhysicsEngine getPhysicsEngine(){return physicsEngine;}

    /**
     * Method to convert world coordinates to screen coordinates.
     *
     * @param worldSpaceCoords The world space coordinates to convert
     * @param centerOfScreenInWorldSpace The center of the screen in world space coordinates
     * @param pixelsPerWorldSpaceUnit The scale factor for converting world space to screen space
     * @return The screen space coordinates
     */
    public Vec2 convertWorldToScreen(Vec2 worldSpaceCoords,Vec2 centerOfScreenInWorldSpace, float pixelsPerWorldSpaceUnit)
    {
        Vec2 coords=Vec2.subtract(worldSpaceCoords,centerOfScreenInWorldSpace);
        coords.scale(pixelsPerWorldSpaceUnit);
        coords.x+=0.5f*this.renderer.getWidth();
        coords.y+=0.5f*this.renderer.getHeight();
        coords.y=this.renderer.getHeight()-coords.y;
        return coords;
    }
}
