package main.java.org.game.Map;

import lombok.Getter;
import lombok.Setter;
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
    private ArrayList<EdgePiece> walls;
    private ColliderGroup colliderGroup;

    public EdgeBetweenRooms(Room r1, Room r2){
        nodeRooms = new ArrayList<>();
        nodeRooms.add(r1);
        nodeRooms.add(r2);
        walls = new ArrayList<>();
        this.colliderGroup = new ColliderGroup();
    }

    public void addNewWall(Vec2 position, Vec2 scale,UnitRoom ur1 ,UnitRoom ur2 , Isten isten){
        String wallPath = "./assets/walls/wall_mid.png";
        Collider wallCollider = new Collider(position,scale);
        colliderGroup.addCollider(wallCollider);
        Wall newWall = new Wall(wallCollider, position, ur1, ur2);
        newWall.setNewImage(wallPath, scale, isten);
        walls.add(newWall);

    }
    public boolean switchWallToDoor(EdgePiece wallToSwitch, Isten isten){
        //check if door is addable
        //egy unitroomba csak az egyik iranyba nyilhat ajto
        if(wallToSwitch.getUnitRoomsBetween().get(0).hasDoor() || wallToSwitch.getUnitRoomsBetween().get(1).hasDoor()) return false;

        String doorPath = "./assets/doors/doors_leaf_closed.png";
        wallToSwitch.collider.setSolidity(false);

        //wallToSwitch.setNewImage(doorPath, wallToSwitch.image.getScale(),isten);

        isten.getRenderer().deleteRenderable(wallToSwitch.image);
        Door newDoor = new Door(wallToSwitch.getCollider(), wallToSwitch.getPosition(),
                wallToSwitch.unitRoomsBetween.get(0), wallToSwitch.unitRoomsBetween.get(1));
        newDoor.setNewImage(doorPath, wallToSwitch.getCollider().getScale(), isten);

        walls.add(newDoor);
        newDoor.getUnitRoomsBetween().get(0).setHasDoor(true);
        newDoor.getUnitRoomsBetween().get(1).setHasDoor(true);
        walls.remove(wallToSwitch);
        return true;
    }
    public void switchDoorToWall(EdgePiece doorToSwitch, Isten isten){
        String wallPath = "./assets/walls/wall_mid.png";
        doorToSwitch.collider.setSolidity(true);
        isten.getRenderer().deleteRenderable(doorToSwitch.image);

        Wall newWall = new Wall(doorToSwitch.getCollider(), doorToSwitch.getPosition(),
                doorToSwitch.unitRoomsBetween.get(0), doorToSwitch.unitRoomsBetween.get(1));
        newWall.setNewImage(wallPath, doorToSwitch.getCollider().getScale(), isten);

        walls.add(newWall);
        newWall.getUnitRoomsBetween().get(0).setHasDoor(false);
        newWall.getUnitRoomsBetween().get(1).setHasDoor(false);
        walls.remove(doorToSwitch);

    }
    public void removeWallPiece(Wall wallToRemove, Isten isten){
        wallToRemove.getUnitRoomsBetween().get(0).setHasDoor(false);
        wallToRemove.getUnitRoomsBetween().get(1).setHasDoor(false);
        for(int i = 0; i < walls.size();i++){
            if(wallToRemove.equals(walls.get(i))){
                walls.get(i).removeEdgePiece(isten, colliderGroup);
                walls.remove(i);
                return;
            }
        }
    }
    public int doorNum(){
        int cnt = 0;
        for(EdgePiece wall : walls){
            if(wall.isDoor()) cnt++;
        }
        return cnt;
    }

    public ArrayList<Room> getNodeRooms() {
        return nodeRooms;
    }

    public ArrayList<EdgePiece> getWalls() {
        return walls;
    }

    public ColliderGroup getColliderGroup() {
        return colliderGroup;
    }
}
