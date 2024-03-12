package main.java.org.entities.player;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

public class Player extends Updatable {

    Collider playerCollider;
    Image playerImage;

    public Player() {
        playerCollider = null;
        playerImage = null;
    }

    @Override
    public void onStart(Isten isten) {
        //called when the player is initialized

        playerCollider = new Collider(new Vec2(0, 0), new Vec2(1, 1));
        playerCollider.setMovability(true);
        isten.getPhysicsEngine().addCollider(playerCollider);//register collider in the physics engine

        playerImage = new Image(new Vec2(), 1, 1, new Vec2(50, 50), "./assets/character_right1.jpg");
        isten.getRenderer().addRenderable(playerImage);//register image in the renderer

    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        //called every frame

        //move image
        //the origin of the image is in its top right corner, therefore the imagePos looks like this: screenSpace(collider position) - 0.5*imageScale
        Vec2 imageScale = Vec2.scale(playerCollider.getScale(), 50);
        Vec2 imagePos = isten.convertWorldToScreen(playerCollider.getPosition(), new Vec2(), 50);
        imagePos.x -= 0.5f * imageScale.x;
        imagePos.y -= 0.5f * imageScale.y;
        playerImage.setPosition(imagePos);
    }

    @Override
    public void onDestroy() {
        //not implemented yet
    }
}
