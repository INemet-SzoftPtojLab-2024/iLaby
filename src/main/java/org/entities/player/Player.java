package main.java.org.entities.player;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.game.updatable.Updatable;

public class Player extends Updatable {

    Collider playerCollider;
    Image playerImage;

    public Player()
    {
        playerCollider=null;
        playerImage=null;
    }

    @Override
    public void onStart(Isten isten) {
        //called when the player is initialized
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        //called every frame
    }

    @Override
    public void onDestroy() {
        //not implemented yet
    }
}
