package main.java.org.entities.player;

import main.java.org.entities.Entity;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;

public class Player extends Entity {

    Collider playerCollider;
    ArrayList<Image> playerImage;
    int activeImage;
    float time;

    public Player() {
        playerCollider = null;
        playerImage = null;
        activeImage = 0;
        time = 0.0f;
    }

    @Override
    public void onStart(Isten isten) {
        //called when the player is initialized

        Vec2 playerScale = new Vec2(0.5f, 0.5f);

        playerCollider = new Collider(new Vec2(0, 0), playerScale);
        playerCollider.setMovability(true);
        isten.getPhysicsEngine().addCollider(playerCollider);//register collider in the physics engine

        playerImage = new ArrayList<>();
        playerImage.add(new Image(new Vec2(), playerScale, "./assets/character_right1.png"));
        playerImage.add(new Image(new Vec2(), playerScale, "./assets/character_right2.png"));
        playerImage.add(new Image(new Vec2(), playerScale, "./assets/character_left1.png"));
        playerImage.add(new Image(new Vec2(), playerScale, "./assets/character_left2.png"));

        for(Image i:playerImage)
            i.setSortingLayer(-69);

        for (Image im : playerImage) {
            im.setVisibility(false);
            isten.getRenderer().addRenderable(im);//register images in the renderer
        }

        activeImage = 1;
        playerImage.get(activeImage).setVisibility(true);

        //adjust camera zoom
        isten.getCamera().setPixelsPerUnit(100);
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        //called every frame

        //move the character
        int run = 1;
        boolean w = isten.getInputHandler().isKeyDown('W');
        boolean a = isten.getInputHandler().isKeyDown('A');
        boolean s = isten.getInputHandler().isKeyDown('S');
        boolean d = isten.getInputHandler().isKeyDown('D');

        if (isten.getInputHandler().isKeyDown(16)) run *= 2;//Shift is run

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

        //animation

        time += deltaTime;

        if (time > 0.2f/run) { //after this much time does the animation changes
            int prev = activeImage;
            if (playerCollider.getVelocity().x > 0) activeImage = 0;
            else if (playerCollider.getVelocity().x < 0) activeImage = 2;
            else if (prev > 1) activeImage = 2;
            else activeImage = 0;
            if (prev % 2 == 0 || playerCollider.getVelocity().magnitude()==0.0f ) activeImage++;
            playerImage.get(prev).setVisibility(false);
            playerImage.get(activeImage).setVisibility(true);
            time = 0.0f;
        }

        //move image
        //the origin of the image is in its top right corner, therefore the imagePos looks like this: screenSpace(collider position) - 0.5*imageScale

        for (int i = 0; i < 4; i++) playerImage.get(i).setPosition(playerCollider.getPosition());

        //move camera
        isten.getCamera().setPosition(playerCollider.getPosition());
    }

    @Override
    public void onDestroy() {
        //not implemented yet
    }
}