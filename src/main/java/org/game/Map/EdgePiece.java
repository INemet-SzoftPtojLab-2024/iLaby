package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;

/**
 * This class is the parent class of the Wall and Door
 */
public abstract class EdgePiece {
    //sorting layer: 39

    protected Collider collider;

    protected Image image;

    protected Vec2 position;
    //has maximal two elements, it contains the unitrooms between the wallpiece is
    protected ArrayList<UnitRoom> unitRoomsBetween;

    public EdgePiece(Collider collider, Vec2 position , UnitRoom ur1, UnitRoom ur2){
        this.collider = collider;
        this.position = position;
        unitRoomsBetween = new ArrayList<>();
        unitRoomsBetween.add(ur1);
        unitRoomsBetween.add(ur2);
    }
    public EdgePiece(Collider collider, Vec2 position , UnitRoom ur1){ //this is only for sideWall
        this.collider = collider;
        this.position = position;
        unitRoomsBetween = new ArrayList<>();
        unitRoomsBetween.add(ur1);
    }

    public void removeEdgePiece(Isten isten, ColliderGroup colliderGroup){
        isten.getRenderer().deleteRenderable(image);
        isten.getPhysicsEngine().getColliderGroup(colliderGroup.id).removeCollider(collider);
        //colliderGroup.removeCollider(collider); //its the same
        image = null;
        collider = null;
        position = null;
    }
    public abstract boolean isDoor();
    public void setNewImage(String imgPath, Vec2 scale,  Isten isten) {
        Image newImage = new Image(position, scale, imgPath);
        //ha ki akajuk cserélni a képet akkor ki kell venni  a renderablek kozol
        if(image != null){
            isten.getRenderer().deleteRenderable(image);
        }
        this.image = newImage;
        //this is under a lot of things, but over the unitrooms(40)
        newImage.setSortingLayer(39);
        isten.getRenderer().addRenderable(newImage);
    }

    public void setCollider(Collider c) {collider = c;}

    public Collider getCollider() {return collider;}

    public Vec2 getPosition() {
        return position;
    }

    public ArrayList<UnitRoom> getUnitRoomsBetween() {
        return unitRoomsBetween;
    }
}
