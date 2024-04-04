package main.java.org.items;

import main.java.org.game.Isten;
import main.java.org.game.Map.Map;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.Map.Wall;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.game.updatable.Updatable;
import main.java.org.items.usable_items.Camambert;
import main.java.org.items.usable_items.Gasmask;
import main.java.org.items.usable_items.Transistor;
import main.java.org.linalg.Vec2;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import static java.lang.Math.sqrt;

/**
 * This class generates chests around the map randomly.
 * Rules: There aren't any chests in front of doors.
 * Every chest is next to a wall and headed against the wall.
 */
public class ChestManager extends Updatable {
    private Vector<Chest>chests=new Vector<>();
    private  int chestCount;
    private  Map map;
    private Vector<UnitRoom> placeableUnitRooms = new Vector<>();//UnitRoom-ok, amelyikbe helyezhető láda (mivel ajtó mellé nem rakható láda)
    private Vector<Boolean> isThereChest=new Vector<>();//igaz=van az azonos sorszámú unitroomban láda
    /**
     * @param n How many chests should be generated randomly across the map?
     */
    public ChestManager(int n){
        chestCount=n;
        map=null;
    }
    @Override
    public void onStart(Isten isten) {

        map=isten.getMap();
        for (int i = 0; i < map.getUnitRooms().length; i++) {
            for (int j = 0; j < map.getUnitRooms()[i].length; j++) {
                UnitRoom unitRoomTmp=map.getUnitRooms()[i][j];
                if((!(unitRoomTmp.isBottomIsDoor()||unitRoomTmp.isTopIsDoor()||unitRoomTmp.isRightIsDoor()||unitRoomTmp.isLeftIsDoor()))//ha egyik fal sem ajtó
                        &&(unitRoomTmp.getTopIsWall()|| unitRoomTmp.getRightIsWall()|| unitRoomTmp.getBottomIsWall()|| unitRoomTmp.getLeftIsWall()))//ha egyik oldalán legalább fal van
                {
                    placeableUnitRooms.add(unitRoomTmp);
                    isThereChest.add(false);
                }
            }
        }
        if(chestCount> placeableUnitRooms.size()) {
            System.err.println("So many chests cant be generated!");
            chestCount=placeableUnitRooms.size();
        }
        Random rand=new Random();
        int randomUnitRoom=rand.nextInt(placeableUnitRooms.size());
        for (int h = 0; h < chestCount; h++) {
            while(isThereChest.get(randomUnitRoom)){
                randomUnitRoom=rand.nextInt(placeableUnitRooms.size());
            }
            WallLocation wall= wallInUnitRoomPicker(placeableUnitRooms.get(randomUnitRoom));
            Vec2 chestPos=null;
            switch (wall) {//0=left, 1=top, 2=right, 3=bottom
                case LEFT: chestPos=new Vec2(placeableUnitRooms.get(randomUnitRoom).getPosition().x - 0.3f, placeableUnitRooms.get(randomUnitRoom).getPosition().y);break;
                case TOP :chestPos=new Vec2(placeableUnitRooms.get(randomUnitRoom).getPosition().x, placeableUnitRooms.get(randomUnitRoom).getPosition().y + 0.3f);break;
                case RIGHT: chestPos=new Vec2(placeableUnitRooms.get(randomUnitRoom).getPosition().x + 0.3f, placeableUnitRooms.get(randomUnitRoom).getPosition().y);break;
                case BOTTOM: chestPos=new Vec2(placeableUnitRooms.get(randomUnitRoom).getPosition().x, placeableUnitRooms.get(randomUnitRoom).getPosition().y - 0.3f);break;
            };

            chests.add( new Chest(chestPos,isten, wall.ordinal(),new ArrayList<Item>(Arrays.asList(new Gasmask(isten),new Camambert(isten),new Transistor(isten)))));
            isThereChest.set(randomUnitRoom,true);
        }
        ColliderGroup chestColliders=new ColliderGroup();
        for (int i = 0; i < chests.size(); i++) {
            Collider c=new Collider(chests.get(i).getPosition(),new Vec2(0.15f,0.15f));
            chestColliders.addCollider(c);
        }
        isten.getPhysicsEngine().addColliderGroup(chestColliders);
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        if(isten.getInputHandler().isKeyDown(KeyEvent.VK_E)){
            Vec2 playerPostion = isten.getPlayer().getPlayerCollider().getPosition();
            for(var chest : chests){
                Vec2 playerChestVector = Vec2.subtract(playerPostion,chest.getPosition());
                double playerChestDistance = sqrt(Vec2.dot(playerChestVector,playerChestVector));
                if(playerChestDistance <= 0.5 && !chest.isOpened()){
                    chest.open();
                    break;
                }
            }
        }
    }

    @Override
    public void onDestroy() {

    }
    private WallLocation wallInUnitRoomPicker(UnitRoom unitRoom ){

        WallLocation wall;//0=left, 1=top, 2=right, 3=bottom
        ArrayList<WallLocation> walls = new ArrayList<>();
        if(unitRoom.getLeftIsWall())walls.add(WallLocation.LEFT);
        if(unitRoom.getTopIsWall())walls.add(WallLocation.TOP);
        if(unitRoom.getRightIsWall())walls.add(WallLocation.RIGHT);
        if(unitRoom.getBottomIsWall()) walls.add(WallLocation.BOTTOM);

        Random random = new Random();
        return walls.get(random.nextInt(walls.size()));

//        if(unitRoom.getOwnerRoom().getID()%4==0){
//            if(unitRoom.getLeftIsWall())wall= WallLocation.LEFT;
//            else if(unitRoom.getTopIsWall())wall=WallLocation.TOP;
//            else if(unitRoom.getRightIsWall())wall=WallLocation.RIGHT;
//            else wall= WallLocation.BOTTOM;
//        }
//        else if(unitRoom.getOwnerRoom().getID()%4==1){
//            if(unitRoom.getTopIsWall())wall=WallLocation.TOP;
//            else if(unitRoom.getRightIsWall())wall=WallLocation.RIGHT;
//            else if(unitRoom.getBottomIsWall())wall=WallLocation.BOTTOM;
//            else wall= WallLocation.LEFT;
//        }
//        else if(unitRoom.getOwnerRoom().getID()%4==2){
//            if(unitRoom.getRightIsWall())wall=WallLocation.RIGHT;
//            else if(unitRoom.getBottomIsWall())wall=WallLocation.BOTTOM;
//            else if(unitRoom.getLeftIsWall())wall= WallLocation.LEFT;
//            else wall=WallLocation.TOP;
//        }
//        else{
//            if(unitRoom.getBottomIsWall())wall=WallLocation.BOTTOM;
//            else if(unitRoom.getLeftIsWall())wall= WallLocation.LEFT;
//            else if(unitRoom.getTopIsWall())wall= WallLocation.TOP;
//            else wall=WallLocation.RIGHT;
//        }


    }

    public enum WallLocation {
        LEFT,
        TOP,
        RIGHT,
        BOTTOM
    }
}
