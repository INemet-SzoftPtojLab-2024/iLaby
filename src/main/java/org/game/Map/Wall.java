package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.linalg.Vec2;

public class Wall extends EdgePiece{


    public Wall(Collider collider,Vec2 position , UnitRoom ur1, UnitRoom ur2) { //does not set the image!!!!!
        super(collider, position, ur1,ur2);
    }
    // this c'tor if responsible for the sideWalls only
    public Wall(Collider collider,Vec2 position ,UnitRoom ur1) { // does not set the image!!!!
        super(collider, position, ur1);
    }

    @Override
    public  boolean isDoor(){
        return false;
    } // abstract osnel nem a collidert kell nezni!
}
