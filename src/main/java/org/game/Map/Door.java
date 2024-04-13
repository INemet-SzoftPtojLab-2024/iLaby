package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.linalg.Vec2;
//TODO abstrakt os az ajtonak meg a wallnak
//az edgemanagerben a updateaftermergeben van egy door castolas, azt ki kell venni ha megvan az abstrakt os!!!!!!!!
public class Door extends Wall {
    public Door(Image i){ //odl!!  do not use!!!
        super(null, i);
    }
}
