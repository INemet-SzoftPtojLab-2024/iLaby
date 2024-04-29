package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

public class Door extends EdgePiece {
    public Door(Collider collider, Vec2 position , UnitRoom ur1, UnitRoom ur2){ //does not set the image!!!!
        super(collider, position, ur1, ur2);
    }

    @Override
    public boolean isDoor() {
        return true;
    }

    public void open(){
        collider.setSolidity(false);
    }
    public void close(){
        collider.setSolidity(true);
    }

    @Override
    public void removeEdgePiece(Isten isten, ColliderGroup colliderGroup){
        isten.getRenderer().deleteRenderable(image);
        isten.getPhysicsEngine().getColliderGroup(colliderGroup.id).removeCollider(collider);
        //colliderGroup.removeCollider(collider); //its the same
        image = null;
        collider = null;
        position = null;

        //itt kulonbozik az ajto a faltol!!
        unitRoomsBetween.get(0).setHasDoor(false);
        unitRoomsBetween.get(1).setHasDoor(false);
    }
    public boolean isOpened(){
        return !collider.isSolid();
    }
    public boolean isClosed(){
        return collider.isSolid();
    }

}
