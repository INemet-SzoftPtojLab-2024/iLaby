package main.java.org.game;

import main.java.org.game.Graphics.GameRenderer;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.Text;
import main.java.org.game.physics.PhysicsEngine;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;

public class Isten {
    private PhysicsEngine physicsEngine;
    private GameRenderer renderer;
    private ArrayList<Updatable> updatables;

    public Isten() {
        physicsEngine=new PhysicsEngine();
        renderer=new GameRenderer();
        updatables=new ArrayList<>();
    }

    public void update(float deltaTime) {
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

    public void init() {
        addUpdatables();
        addRenderables();
    }

    private void addRenderables() {

        String imagePath = "./assets/cube.jpg";

        renderer.addRenderable(new Text("Hello!", new Vec2(150, 100), "./assets/Monocraft.ttf", 68, 255, 255, 255));
        renderer.addRenderable(new Image(new Vec2(200,200), 1, 1, new Vec2(100,100), imagePath));
    }

    private void addUpdatables()
    {

    }

    public GameRenderer getRenderer() {
        return renderer;
    }

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
