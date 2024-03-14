package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.physics.Collider;

public class Wall {

    private Collider collider;
    private Image image;

    public Wall(Collider collider, Image image) {

    }
    public Wall() {

    }

    public void setCollider(Collider c) {
        collider = c;
    }

    public void setImage(Image i) {
       image = i;
    }

}
