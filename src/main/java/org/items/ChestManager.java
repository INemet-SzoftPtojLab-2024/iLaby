package main.java.org.items;

import main.java.org.game.Isten;
import main.java.org.game.Map.Map;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import java.awt.event.KeyEvent;
import java.util.*;

import static java.lang.Math.sqrt;

/**
 * This class generates chests around the map randomly.
 * Rules: There aren't any chests in front of doors.
 * Every chest is next to a wall and headed against the wall.
 */
public class ChestManager extends Updatable {
    private Vector<Chest> chests = new Vector<>();
    private int chestCount;
    private Isten isten;
    private Map map;
    private ColliderGroup colliderGroup;
    //a random unitRoom kiválasztására
    private  final ArrayList<Integer> randomUnitRoom;

    /**
     * @param n How many chests should be generated randomly across the map?
     */
    public ChestManager(int n, Isten isten) {
        this.isten = isten;
        chestCount = n;
        map = null;
        colliderGroup = new  ColliderGroup();
        int mapRowSize = isten.getMap().getMapRowSize();
        int mapColumnSize = isten.getMap().getMapColumnSize();
         randomUnitRoom= new ArrayList<>();
        int listSize = mapRowSize * mapColumnSize;
        for(int i =0;i<listSize;i++){
            randomUnitRoom.add(i);
        }
    }

    @Override
    public void onStart(Isten isten) {

    }

    public void init(Isten isten) {
        isten.getPhysicsEngine().addColliderGroup(colliderGroup);
        Random random = new Random();
        for (int i = 0; i < chestCount; i++) {
            if(!placeChest(random.nextInt(Chest.ChestType.values().length))) break;
        }
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        if (isten.getInputHandler().isKeyDown(KeyEvent.VK_E)) {
            Vec2 playerPostion = isten.getPlayer().getPlayerCollider().getPosition();
            int index = 0;
            for (var chest : chests) {
                Vec2 playerChestVector = Vec2.subtract(playerPostion, chest.getPosition());
                double playerChestDistance = sqrt(Vec2.dot(playerChestVector, playerChestVector));
                if (playerChestDistance <= 0.5 && !chest.isOpened()) {
                    chest.open();
                    isten.getSocketClient().sendData(("11" + index).getBytes());
                    break;
                }
                index++;
            }
        }

        for (int i=0;i<chests.size();i++) {
            if (!chests.get(i).getIsOnRightPlace()) {
                chests.get(i).setUnitRoom(getPlaceForChest().getPosition());
            }
        }
    }
    @Override
    public void onDestroy() {

    }
    private boolean placeChest(int chestType) {
        UnitRoom unitRoom = getPlaceForChest();
        if(unitRoom == null){
            System.out.println("no place for chest");
            return false;
        }

        //CHEST TIPUSOK, a networking miatt sokkal egyszerubb így az itemeket atadni --> Chest.java/fillChest

        Chest chest=new Chest(unitRoom.getPosition(),isten,chestType,chests.size());
        chests.add(chest);
        updateColliderGroup(chest);
        return true;
    }
    private UnitRoom getPlaceForChest(){
        int mapRowSize = isten.getMap().getMapRowSize();
        Collections.shuffle(randomUnitRoom);
        for(Integer number : randomUnitRoom){
            if(isUnitRoomAvalaibleForChest(isten.getMap().getUnitRooms()[(int)(number/mapRowSize)][number%mapRowSize]))
            {
                return isten.getMap().getUnitRooms()[(int)(number/mapRowSize)][number%mapRowSize];
            }
        }
        return null;
    }
    private boolean isUnitRoomAvalaibleForChest(UnitRoom unitRoomTmp){
        if (!unitRoomTmp.hasDoor()//ha egyik fal sem ajtó
                && (unitRoomTmp.isTopWall() || unitRoomTmp.isRightWall() || unitRoomTmp.isBottomWall() || unitRoomTmp.isLeftWall())//ha egyik oldalán legalább fal van
        )
        {
            if(!unitRoomTmp.getHasChest()){
                return true;
            }
        }
        return false;
    }
    @Override
    public Vector<Chest> getChests() {
        return chests;
    }

    public ColliderGroup getColliderGroup() {
        return colliderGroup;
    }

    public void setColliderGroup(ColliderGroup colliderGroup) {
        this.colliderGroup = colliderGroup;
    }
    public void updateColliderGroup(Chest chest){
        if(chest.getCollider() !=null){
            colliderGroup.removeCollider(chest.getCollider());
        }
        chest.setCollider();
        colliderGroup.addCollider(chest.getCollider());
    }
}
