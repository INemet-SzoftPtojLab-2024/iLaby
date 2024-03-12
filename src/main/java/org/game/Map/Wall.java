package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.linalg.Vec2;

import static main.java.org.game.Map.FramePosition.up;

public class Wall {
    protected Collider collider;
    protected Image image;
    public Wall(Isten isten, UnitRoom unitRoom, FramePosition framePosition) {
        Vec2 worldUnitRoomCoords = isten.convertScreenToWorld(unitRoom.getPosition(), new Vec2(0,0), 50);
        switch (framePosition) {
            case up:
                image = new Image(unitRoom.getPosition(),50,25,new Vec2(1,1),"./assets/wallupdown.png",true);
                collider = new Collider(worldUnitRoomCoords,new Vec2(1f,0.01f));
                break;
            case down:
                image = new Image(unitRoom.getPosition(),50,25,new Vec2(1,1),"./assets/wallupdown.png",true);
                collider = new Collider(Vec2.sum(worldUnitRoomCoords,new Vec2(0,1)),new Vec2(1f,0.01f));
                break;
            case left:
                image = new Image(unitRoom.getPosition(),50,25,new Vec2(1,1),"./assets/wallleftright.png",true);
                collider = new Collider(worldUnitRoomCoords,new Vec2(0.01f,1f));
                break;
            case right:
                image = new Image(unitRoom.getPosition(),50,25,new Vec2(1,1),"./assets/wallleftright.png",true);
                collider = new Collider(Vec2.sum(worldUnitRoomCoords,new Vec2(1,0)),new Vec2(0.01f,1f));
                break;
        }
        isten.getRenderer().addRenderable(image);
        ColliderGroup colliderGroup = new ColliderGroup();
        colliderGroup.addCollider(collider);
        isten.getPhysicsEngine().addColliderGroup(colliderGroup);
    }
}
