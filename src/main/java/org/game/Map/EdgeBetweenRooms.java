package main.java.org.game.Map;

import lombok.Getter;
import lombok.Setter;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;

@Setter
@Getter
public class EdgeBetweenRooms {
    //the two room, between the edge is.
    private ArrayList<Room> nodeRooms;
    //the wall pieces
    private ArrayList<Wall> walls;
    private ColliderGroup colliderGroup;

    public EdgeBetweenRooms(Room r1, Room r2){
        nodeRooms = new ArrayList<>();
        nodeRooms.add(r1);
        nodeRooms.add(r2);
        walls = new ArrayList<>();
        this.colliderGroup = new ColliderGroup();
    }

    public void addNewWall(Vec2 position, Vec2 scale,UnitRoom ur1 ,UnitRoom ur2 ,Isten isten){
        String wallPath = "./assets/rooms/11.png";
        Collider wallCollider = new Collider(position,scale);
        colliderGroup.addCollider(wallCollider);
        Image image = new Image(position, scale, wallPath);
        isten.getRenderer().addRenderable(image);
        Wall newWall = new Wall(wallCollider, position, image, ur1, ur2);
        walls.add(newWall);

    }

    public ArrayList<Room> getNodeRooms() {
        return nodeRooms;
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public ColliderGroup getColliderGroup() {
        return colliderGroup;
    }
}
