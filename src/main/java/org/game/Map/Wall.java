package main.java.org.game.Map;

import lombok.Getter;
import lombok.Setter;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;

public class Wall {

    protected Collider collider;

    protected Image image;

    protected Vec2 position;
    //has maximal two elements, it contains the unitrooms between the wallpiece is
    protected ArrayList<UnitRoom> unitRoomsBetween;

    public Wall(Collider collider, Image image) { //old
        this.collider = collider;
        this.image = image;
        unitRoomsBetween = new ArrayList<>();

    }
    public Wall(Collider collider,Vec2 position ,Image image, UnitRoom ur1, UnitRoom ur2) {
        this.collider = collider;
        this.image = image;
        this.position = position;
        unitRoomsBetween = new ArrayList<>();
        unitRoomsBetween.add(ur1);
        unitRoomsBetween.add(ur2);
    }
    public Wall() {

    }
    public void removeWall(Isten isten, ColliderGroup colliderGroup){
        isten.getRenderer().deleteRenderable(image);
        isten.getPhysicsEngine().getColliderGroup(colliderGroup.id).removeCollider(collider);
        image = null;
        collider = null;
        position = null;

    }

    public void setCollider(Collider c) {collider = c;}

    public Collider getCollider() {return collider;}

    public void setImage(Image i) {image = i;}

}
