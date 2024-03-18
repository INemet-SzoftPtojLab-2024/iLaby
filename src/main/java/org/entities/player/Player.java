package main.java.org.entities.player;

import main.java.org.game.Camera.Camera;
import main.java.org.game.Graphics.GameRenderer;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.Text;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;

public class Player extends Entity {

    Collider playerCollider;
    ArrayList<Image> playerImage;
    int activeImage;
    float time;
    Text playerName;

    public Player() {
        playerCollider = null;
        playerImage = null;
        activeImage = 0;
        time = 0.0f;
        playerName = null;
    }

    public Player(String name) {
        playerCollider = null;
        playerImage = null;
        activeImage = 0;
        time = 0.0f;
        playerName = new Text(name, new Vec2(0,0), 15, 255,255,255);;
    }

    @Override
    public void onStart(Isten isten) {
        //called when the player is initialized

        playerCollider = new Collider(new Vec2(0, 0), new Vec2(1, 1));
        playerCollider.setMovability(true);
        isten.getPhysicsEngine().addCollider(playerCollider);//register collider in the physics engine

        playerImage = new ArrayList<>();
        playerImage.add(new Image(new Vec2(), new Vec2(1, 1), "./assets/character_right1.png"));
        playerImage.add(new Image(new Vec2(), new Vec2(1, 1), "./assets/character_right2.png"));
        playerImage.add(new Image(new Vec2(), new Vec2(1, 1), "./assets/character_left1.png"));
        playerImage.add(new Image(new Vec2(), new Vec2(1, 1), "./assets/character_left2.png"));

        for (Image im : playerImage) {
            im.setVisibility(false);
            isten.getRenderer().addRenderable(im);//register images in the renderer
        }

        activeImage = 1;
        playerImage.get(activeImage).setVisibility(true);

        if(playerName != null){
            playerName.setVisibility(true);
            isten.getRenderer().addRenderable(playerName);
        }

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

        if (time > 0.2f / run) { //after this much time does the animation changes
            int prev = activeImage;
            if (playerCollider.getVelocity().x > 0) activeImage = 0;
            else if (playerCollider.getVelocity().x < 0) activeImage = 2;
            else if (prev > 1) activeImage = 2;
            else activeImage = 0;
            if (prev % 2 == 0 || playerCollider.getVelocity().magnitude() == 0.0f) activeImage++;
            playerImage.get(prev).setVisibility(false);
            playerImage.get(activeImage).setVisibility(true);
            time = 0.0f;
        }

        //move image
        //the origin of the image is in its top right corner, therefore the imagePos looks like this: screenSpace(collider position) - 0.5*imageScale

        for (int i = 0; i < 4; i++) {
            Vec2 playerPosition = playerCollider.getPosition();
            playerImage.get(i).setPosition(playerPosition);

            playerName.setPosition(Vec2.sum(playerPosition, new Vec2( 0,(float)0.8)));
        }
    }

    @Override
    public void onDestroy() {
        //not implemented yet
    }

    public void setPlayerName(Text playerName){
        this.playerName = playerName;
    }
}