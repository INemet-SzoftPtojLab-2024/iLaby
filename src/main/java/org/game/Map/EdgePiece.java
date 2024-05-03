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

    protected Vec2 position; // ez egyenlo az image positiojaval
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

    public Image getImage() {
        return image;
    }

    public Vec2 getPosition() {
        return position;
    }
    public Vec2 getMidPosition(){
        Vec2 midPos = new Vec2(0);
        if(unitRoomsBetween.size() == 1){
            //jobb vagy bal szel
           if(position.x == -0.5f) midPos=  new Vec2(0,position.y + 0.5f);
           else if(position.x == unitRoomsBetween.get(0).getColNum() - 0.5f) midPos= new Vec2(unitRoomsBetween.get(0).getColNum(),position.y + 0.5f);
           //also vagy felso
           if(position.y == -0.5f) midPos= new Vec2(position.x + 0.5f, 0);
           else if(position.y == unitRoomsBetween.get(0).getRowNum() - 0.5f) midPos= new Vec2(position.x + 0.5f,unitRoomsBetween.get(0).getRowNum());
        }
        else{
            if(unitRoomsBetween.get(0).getRowNum() == unitRoomsBetween.get(1).getRowNum()){
                float x = Math.max(unitRoomsBetween.get(0).getColNum(), unitRoomsBetween.get(1).getColNum());
                midPos= new Vec2(x,unitRoomsBetween.get(0).getRowNum() + 0.5f);
            }
            else {
                float y =  Math.max(unitRoomsBetween.get(0).getRowNum(), unitRoomsBetween.get(1).getRowNum());
                midPos= new Vec2(unitRoomsBetween.get(0).getColNum() + 0.5f,y);
            }
        }
        return midPos;
    }

    public ArrayList<UnitRoom> getUnitRoomsBetween() {
        return unitRoomsBetween;
    }
}
