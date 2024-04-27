package main.java.org.networking;

import main.java.org.entities.villain.Villain;
import main.java.org.game.Graphics.Image;
import java.util.concurrent.locks.Lock;
import main.java.org.game.Graphics.Text;
import main.java.org.game.Isten;
import main.java.org.game.Map.Map;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Handler;

public class HandlerManager {

    Isten isten;

    public HandlerManager(Isten isten) {
        this.isten = isten;
    }

    private List<TaskType> tasks = new ArrayList<>();
    private List<TaskType> synchronizedTasks = Collections.synchronizedList(tasks);
    private List<HandlerData> handlerDataList = new ArrayList<>();
    private List<HandlerData> synchronizedHandlerDataList = Collections.synchronizedList(handlerDataList);
    public Lock lock = new ReentrantLock();

    //->>>>>>>>>>>>>>>>//
    //HANDLER DATA
    //->>>>>>>>>>>>>>>>//
    public static abstract class HandlerData {

    }

    public static class VillainData extends HandlerData {
        VillainData(String villainName, Vec2 position, String imgPath, int random1, int random2) {
            this.villainName = villainName;
            this.position = position;
            this.imgPath = imgPath;
            this.random1 = random1;
            this.random2 = random2;
        }

        public String villainName;
        public Vec2 position;
        public String imgPath;
        public int random1;
        public int random2;
    }

    public static class WallData extends HandlerData {

        WallData(Vec2 pos, Vec2 scale, boolean isDoor) {
            this.pos = pos;
            this.scale = scale;
            this.isDoor = isDoor;
        }

        public Vec2 pos;
        public Vec2 scale;
        public boolean isDoor;
    }

    public static class VillainMoveData extends HandlerData {
        public VillainMoveData(String villainName, Vec2 position) {
            this.villainName = villainName;
            this.position = position;
        }

        public String villainName;
        public Vec2 position;
    }

    //->>>>>>>>>>>>>>>>//
    //EXECUTION
    //->>>>>>>>>>>>>>>>//
    public void executeTasks() {


        if (synchronizedHandlerDataList.size() != synchronizedTasks.size()) return;


        while (!synchronizedTasks.isEmpty()) {

            TaskType task;
            HandlerData data;

            lock.lock();
            try {
                // Critical section
                // Access shared resources here
                task = getTask();
                data = getHandlerData();
            } finally {
                lock.unlock(); // Release the lock
            }

            if (task == null || data == null) return;
            switch (task) {
                case Villain: {
                    if (data.getClass() != VillainData.class) return;
                    VillainData villainData = (VillainData) data;
                    villainHandler(villainData);
                    break;
                }
                case VillainMove: {
                    if (data.getClass() != VillainMoveData.class) return;
                    VillainMoveData villainMoveData = (VillainMoveData) data;
                    villainMoveHandler(villainMoveData);
                    break;
                }
                case WallHandler: {
                    if (data.getClass() != WallData.class) return;
                    WallData wallData = (WallData) data;
                    wallHandler(wallData);
                    break;
                }
            }

        }


    }

    //->>>>>>>>>>>>>>>>//
    //TASKTYPE ENUM
    //->>>>>>>>>>>>>>>>//
    public enum TaskType {
        Villain,
        VillainMove,
        WallHandler,
    }

    synchronized public void addTask(TaskType type) {
        lock.lock();
        try {
            synchronizedTasks.add(type);
        }
        finally {
            lock.unlock();
        }
    }

    synchronized public void addData(HandlerData data) {
        lock.lock();
        try {
            synchronizedHandlerDataList.add(data);
        }
        finally {
            lock.unlock();
        }
    }

    public TaskType getTask() {
        lock.lock();
        try {
            if (tasks.isEmpty()) return null;
            return synchronizedTasks.removeLast();
        }
        finally {
            lock.unlock();
        }

    }

    public HandlerData getHandlerData() {
        lock.lock();
        try {
            if (handlerDataList.isEmpty()) return null;
            return synchronizedHandlerDataList.removeLast();
        }
        finally {
            lock.unlock();
        }

    }


    //->>>>>>>>>>>>>>>>//
    //HANDLER FUNCTIONS
    //->>>>>>>>>>>>>>>>//
    private void wallHandler(WallData wallData) {
        ColliderGroup cg = new ColliderGroup();
        Collider collider = new Collider(wallData.pos, wallData.scale);

        if (wallData.isDoor) {
            isten.getRenderer().addRenderable(new Image(wallData.pos, wallData.scale, "./assets/doors/doors_leaf_closed.png"));
            collider.setSolidity(false);
        } else {
            isten.getRenderer().addRenderable(new Image(wallData.pos, wallData.scale, "./assets/walls/wall_mid.png"));

        }

        cg.addCollider(collider);
        isten.getPhysicsEngine().addColliderGroup(cg);
    }

    private void villainHandler(VillainData villainData) {
        Villain villain = new Villain(villainData.villainName, villainData.position, villainData.imgPath);
        //villain.setRoomForVillain(isten.getMap().getRooms(), villainData.random1, villainData.random2);
        isten.addUpdatable(villain);
    }

    private void villainMoveHandler(VillainMoveData villainMoveData) {
        int index = isten.getVillainIndex(villainMoveData.villainName);
        Villain villain = (Villain) isten.getUpdatable(index);
        if (villain == null) {
            return;
        }
        if (villain.getVillainCollider() != null) {
            Vec2 position = villainMoveData.position;
            villain.getVillainCollider().setPosition(position);
        }
    }
}