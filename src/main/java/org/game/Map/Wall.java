package main.java.org.game.Map;

import lombok.Getter;
import lombok.Setter;
import main.java.org.game.Graphics.Image;
import main.java.org.game.physics.Collider;
import main.java.org.linalg.Vec2;

public class Wall {

    protected Collider collider;

    protected Image image;

    protected Vec2 position;

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
