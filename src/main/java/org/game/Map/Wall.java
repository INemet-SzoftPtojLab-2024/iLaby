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
    //sorting layer: 39

    protected Collider collider;

    protected Image image;

    protected Vec2 position;
    //has maximal two elements, it contains the unitrooms between the wallpiece is
    protected ArrayList<UnitRoom> unitRoomsBetween;

    public Wall(Collider collider,Vec2 position , UnitRoom ur1, UnitRoom ur2) { //does not set the image!!!!!
        this.collider = collider;
        this.position = position;
        unitRoomsBetween = new ArrayList<>();
        unitRoomsBetween.add(ur1);
        unitRoomsBetween.add(ur2);
    }
    // this c'tor if responsible for the sideWalls only
    public Wall(Collider collider,Vec2 position ,UnitRoom ur1) { // does not set the image!!!!
        this.collider = collider;
        this.position = position;
        unitRoomsBetween = new ArrayList<>();
        unitRoomsBetween.add(ur1);
    }
    public void removeWall(Isten isten, ColliderGroup colliderGroup){
        isten.getRenderer().deleteRenderable(image);
        isten.getPhysicsEngine().getColliderGroup(colliderGroup.id).removeCollider(collider);
        //colliderGroup.removeCollider(collider); //its the same
        image = null;
        collider = null;
        position = null;

        //itt kulonbozik az ajto es a faltol!!
        //overridolni kell az abstrakt osnel
        unitRoomsBetween.get(0).setHasDoor(false);
        unitRoomsBetween.get(1).setHasDoor(false);
    }

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
    public boolean isDoor(){
        return !collider.isSolid();
    } // abstract osnel nem a collidert kell nezni!
}
