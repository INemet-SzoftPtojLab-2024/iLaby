package main.java.org.items;

import lombok.Getter;
import main.java.org.game.Isten;
import main.java.org.game.Map.Map;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.Map.Wall;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.game.updatable.Updatable;
import main.java.org.items.usable_items.*;
import main.java.org.linalg.Vec2;
import main.java.org.networking.Packet11ChestOpened;

import java.awt.event.KeyEvent;
import java.util.*;

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

    }

    public void init(Isten isten) {
        map=isten.getMap();
        for (int i = 0; i < map.getUnitRooms().length; i++) {
            for (int j = 0; j < map.getUnitRooms()[i].length; j++) {
                UnitRoom unitRoomTmp=map.getUnitRooms()[i][j];
                //megcsinaltam a unitroomot: van egy hasdoor valtozoja, es mindegyik uniroomrol le lehet kerdezni hogy melyik oldala fall
                //fontos az ajtot is falnak veszi!!
                //ezek a valtoztatasok a mapdrawing branchen elerhetoek(nem tudtam jol összrakni, nem vagyok jo git kezeko :) )
                if(!unitRoomTmp.hasDoor()//ha egyik fal sem ajtó
                        &&(unitRoomTmp.isTopWall()|| unitRoomTmp.isRightWall()|| unitRoomTmp.isBottomWall()|| unitRoomTmp.isLeftWall()))//ha egyik oldalán legalább fal van
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
            //CHEST TÍPUSOK, a networking miatt sokkal egyszerűbb így az itemeket átadni --> Chest.java/fillChest
            chests.add(new Chest(chestPos,isten,wall.ordinal(),rand.nextInt(5)));
            isThereChest.set(randomUnitRoom,true);
        }
        ColliderGroup chestColliders=new ColliderGroup();
        for (int i = 0; i < chests.size(); i++) {
            Collider c=new Collider(chests.get(i).getPosition(),new Vec2(0.15f,0.15f));
            chestColliders.addCollider(c);
        }
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        if(isten.getInputHandler().isKeyDown(KeyEvent.VK_E)){
            Vec2 playerPostion = isten.getPlayer().getPlayerCollider().getPosition();
            int index = 0;
            for(var chest : chests){
                Vec2 playerChestVector = Vec2.subtract(playerPostion,chest.getPosition());
                double playerChestDistance = sqrt(Vec2.dot(playerChestVector,playerChestVector));
                if(playerChestDistance <= 0.5 && !chest.isOpened()){
                    chest.open();
                    isten.getSocketClient().sendData(("11"+index).getBytes());
                    break;
                }
                index++;
            }
        }
    }

    @Override
    public void onDestroy() {

    }
    private WallLocation wallInUnitRoomPicker(UnitRoom unitRoom ){

        WallLocation wall;//0=left, 1=top, 2=right, 3=bottom
        ArrayList<WallLocation> walls = new ArrayList<>();
        if(unitRoom.isLeftWall())walls.add(WallLocation.LEFT);
        if(unitRoom.isTopWall())walls.add(WallLocation.TOP);
        if(unitRoom.isRightWall())walls.add(WallLocation.RIGHT);
        if(unitRoom.isBottomWall()) walls.add(WallLocation.BOTTOM);

        Random random = new Random();
        return walls.get(random.nextInt(walls.size()));


    }

    public enum WallLocation {
        LEFT,
        TOP,
        RIGHT,
        BOTTOM
    }

    @Override
    public Vector<Chest> getChests() { return chests; }
}
