package main.java.org.entities.villain;

import main.java.org.entities.Entity;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.Text;
import main.java.org.game.Isten;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.physics.Collider;
import main.java.org.linalg.Vec2;

import java.util.Random;

/**
 * The villain class, makes almost everything related to the villain.
 */
public class Villain extends Entity {
    Image villainImage;
    Collider villainCollider;
    Text villainName;
    float time;
    int direction;
    Vec2 position;
    String imagePath;
    public Villain() {
        villainCollider = null;
        villainImage = null;
        time = 0.0f;
        villainName = null;
        direction=1;
    }
    public Villain(String name, Vec2 pos, String iP) {
        villainCollider = null;
        villainImage = null;
        time = 0.0f;
        villainName = new Text(name, new Vec2(0, 0), "./assets/Monocraft.ttf", 15, 255, 0, 0);
        villainName.setShadowOn(false);
        direction=1;
        position = pos;
        imagePath = iP;

    }
    @Override
    public void onStart(Isten isten) {
        System.out.println("ONSTART VILLAIN");
        Vec2 playerScale = new Vec2(0.6f, 0.6f);
        villainCollider = new Collider(position, playerScale);
        villainCollider.setMovability(true);
        isten.getPhysicsEngine().addCollider(villainCollider);//register collider in the physics engine

        villainImage = new Image(new Vec2(), playerScale, imagePath);
        villainImage.setSortingLayer(-50);
        villainImage.setVisibility(true);
        isten.getRenderer().addRenderable(villainImage);//register images in the renderer

        if (villainName != null) {
            villainName.setVisibility(true);
            villainName.setSortingLayer(-50);
            isten.getRenderer().addRenderable(villainName);
        }
        isten.getCamera().setPixelsPerUnit(100);
    }
    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        Vec2 playerPosition = villainCollider.getPosition();
        villainImage.setPosition(playerPosition);
        villainName.setPosition(Vec2.sum(playerPosition, new Vec2(0, (float) 0.5)));
    }

    public void move() {
        Random random = new Random();
        if(villainCollider.getVelocity().x==0)
        {
            int randomNumber = random.nextInt(4);
            switch (randomNumber)
            {
                case 0:
                    villainCollider.getVelocity().x = direction * -0.7f;
                    break;
                case 1:
                    villainCollider.getVelocity().y = direction * -0.7f;
                    break;
                case 2:
                    villainCollider.getVelocity().x = direction * 0.7f;
                    break;
                case 3:
                    villainCollider.getVelocity().y = direction * 0.7f;
                    break;
            }
            direction *= -1;
        }
    }

    @Override
    public void onDestroy() {
    }

    public String getVillainName() {
        return villainName.getText();
    }

    public Vec2 getPosition() {
        return position;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Collider getVillainCollider() {
        return villainCollider;
    }

    public void setVillainCollider(Isten isten, Collider collider) {
        villainCollider = collider;
        isten.getPhysicsEngine().addCollider(villainCollider);//register collider in the physics engine
    }
}
