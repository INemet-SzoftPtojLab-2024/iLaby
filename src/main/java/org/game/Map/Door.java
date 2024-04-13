package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.physics.Collider;
import main.java.org.linalg.Vec2;
//TODO abstrakt os az ajtonak meg a wallnak
//az edgemanagerben a updateaftermergeben van egy door castolas, azt ki kell venni ha megvan az abstrakt os!!!!!!!!
public class Door extends Wall {
    public Door(Collider collider, Vec2 position , UnitRoom ur1, UnitRoom ur2){ //does not set the image!!!!
        super(collider, position, ur1, ur2);
    }
    //TODO AKOS
    //implement open and close (set the colliers)
    //and implement isOpened
    //isDoor funktion that return true (if it has abstract base class)
    //mineden mas ami a wallban van, mehet az abstrakt osben
    // a remove Wallban van kulonbseg

}
