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
        Wall newWall = new Wall(wallCollider, position, ur1, ur2);
        newWall.setNewImage(wallPath, scale, isten);
        walls.add(newWall);

    }
    public void switchWallToDoor(Wall wallToSwitch, Isten isten){
        String doorPath = "./assets/rooms/10.png";
        wallToSwitch.collider.setSolidity(false);
        isten.getRenderer().deleteRenderable(wallToSwitch.image);

        Door newDoor = new Door(wallToSwitch.getCollider(), wallToSwitch.getPosition(),
                wallToSwitch.unitRoomsBetween.get(0), wallToSwitch.unitRoomsBetween.get(1));
        newDoor.setNewImage(doorPath, wallToSwitch.getCollider().getScale(), isten);

        walls.add(newDoor);//informaciovesztes!?
        walls.remove(wallToSwitch);
    }
    public void switchDoorToWall(Door doorToSwitch, Isten isten){
        String wallPath = "./assets/rooms/11.png";
        doorToSwitch.collider.setSolidity(true);
        isten.getRenderer().deleteRenderable(doorToSwitch.image);

        Wall newWall = new Wall(doorToSwitch.getCollider(), doorToSwitch.getPosition(),
                doorToSwitch.unitRoomsBetween.get(0), doorToSwitch.unitRoomsBetween.get(1));
        newWall.setNewImage(wallPath, doorToSwitch.getCollider().getScale(), isten);

        walls.add(newWall);
        walls.remove(doorToSwitch);

    }
    public void removeWallPiece(Wall wallToRemove, Isten isten){
        for(int i = 0; i < walls.size();i++){
            if(wallToRemove.equals(walls.get(i))){
                walls.get(i).removeWall(isten, colliderGroup);
                walls.remove(i);
                return;
            }
        }
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
