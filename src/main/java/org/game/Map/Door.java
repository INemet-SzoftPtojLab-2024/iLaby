package main.java.org.game.Map;

import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.linalg.Vec2;

import static java.lang.Math.sqrt;

public class Door extends EdgePiece {
    private double timeSinceOpen = 0.0f;
    public Door(Collider collider, Vec2 position , UnitRoom ur1, UnitRoom ur2){ //does not set the image!!!!
        super(collider, position, ur1, ur2);
    }

    @Override
    public boolean isDoor() {
        return true;
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
    public boolean isOneWay(){
        Room r1 = unitRoomsBetween.get(0).getOwnerRoom();
        Room r2 = unitRoomsBetween.get(1).getOwnerRoom();
        if(r1.getDoorAdjacentRooms().contains(r2) && r2.getDoorAdjacentRooms().contains(r1)) return true;
        return false;
    }
    //ez a fuggveny azt adja vissza hogy atmehetunk-e egy ajton, es ha igen akkor az a szobat kapjuk ahova kijukanunk
    public Room direction(){
        if(!isOneWay()) return  null;
        Room r1 = unitRoomsBetween.get(0).getOwnerRoom();
        Room r2 = unitRoomsBetween.get(1).getOwnerRoom();
        if (r1.getDoorAdjacentRooms().contains(r2)) return r2;
        else return r1;
    }
    public boolean isPlayerAtDoor(Isten isten){
        //egy adott sugaru körben ha benne van az ajto kozepehez kepest
        //player es a pont tavolsaga
        Vec2 playerPos = Vec2.sum(isten.getPlayer().getPlayerCollider().getPosition(), new Vec2(0.5f));
        Vec2 playerDoorVector = Vec2.subtract(getMidPosition(),playerPos);
        double playerDoorDistance = sqrt(Vec2.dot(playerDoorVector,playerDoorVector));
        if(playerDoorDistance < 0.5) return true;
        else return false;
    }
    public void open(){
        collider.setSolidity(false);
    }
    public void close(){
        collider.setSolidity(true);
    }
    public boolean isOpened(){
        return !collider.isSolid();
    }
    public boolean isClosed(){
        return collider.isSolid();
    }
    public void manageOpenDoor(double delta){
        timeSinceOpen += delta;
        if(timeSinceOpen > 2.0f){
            close();
            timeSinceOpen = 0.0f;
        }
    }

}
