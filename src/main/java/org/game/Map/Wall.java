package main.java.org.game.Map;

import lombok.Getter;
import lombok.Setter;
import main.java.org.game.Graphics.Image;
import main.java.org.game.physics.Collider;
import main.java.org.linalg.Vec2;

public class Wall {

    protected Collider collider;
    protected Image image;
    protected Room[] roombetween = new Room[2];
    protected Vec2 position;
    protected final static int TOP = 1;
    protected final static int BOTTOM = 2;
    protected final static int LEFT = 3;
    protected final static int RIGHT = 4;

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
