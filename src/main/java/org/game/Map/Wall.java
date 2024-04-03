package main.java.org.game.Map;

import lombok.Getter;
import lombok.Setter;
import main.java.org.game.Graphics.Image;
import main.java.org.game.physics.Collider;

public class Wall {

    protected Collider collider;
    protected Image image;

    public Wall(Collider collider, Image image) {
        this.collider = collider;
        this.image = image;
    }
    public Wall() {

    }

    public void setCollider(Collider c) {collider = c;}

    public Collider getCollider() {return collider;}

    public void setImage(Image i) {image = i;}

}
