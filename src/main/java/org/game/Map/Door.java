package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.physics.Collider;
import main.java.org.linalg.Vec2;

public class Door extends Wall {
    public Door(Collider collider, Vec2 position , UnitRoom ur1, UnitRoom ur2){ //does not set the image!!!!
        super(collider, position, ur1, ur2);
    }

}
