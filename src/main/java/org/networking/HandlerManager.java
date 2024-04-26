package main.java.org.networking;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandlerManager {

    Isten isten;

    public HandlerManager(Isten isten) {
        this.isten = isten;
    }
    private List<TaskType> tasks = new ArrayList<>();
    private List<TaskType> synchronizedTasks = Collections.synchronizedList(tasks);
    private List<HandlerData> handlerDataList = new ArrayList<>();
    private List<HandlerData> synchronizedHandlerDataList = Collections.synchronizedList(handlerDataList);

    public static abstract class HandlerData {

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

    public void executeTasks() {

        if(synchronizedHandlerDataList.size() != synchronizedTasks.size()) return;

        while (!synchronizedTasks.isEmpty()) {
            TaskType task = getTask();
            HandlerData data = getHandlerData();
            System.out.println("EXECUTE TASKS");
            switch (task) {
                case WallHandler: {
                    System.out.println("HANDLE WALL DATA");
                    WallData wallData = (WallData)data;
                    wallHandler(wallData);
                }
            }
        }
    }

    public enum TaskType {
        WallHandler
    }

    public void addTask(TaskType type) {
        synchronizedTasks.add(type);
    }

    public void addData(HandlerData data) {
        synchronizedHandlerDataList.add(data);
    }

    public TaskType getTask() {
        if(tasks.isEmpty()) return null;
        TaskType task = tasks.getLast();
        synchronizedTasks.remove(task);
        return task;
    }

    public HandlerData getHandlerData() {
        if(handlerDataList.isEmpty()) return null;
        HandlerData data = handlerDataList.getLast();
        synchronizedHandlerDataList.remove(data);
        return data;
    }

    private void wallHandler(WallData wallData) {
        ColliderGroup cg = new ColliderGroup();
        Collider collider = new Collider(wallData.pos, wallData.scale);

        if(wallData.isDoor) {
            isten.getRenderer().addRenderable(new Image(wallData.pos, wallData.scale, "./assets/doors/doors_leaf_closed.png"));
            collider.setSolidity(false);
        }
        else {
            isten.getRenderer().addRenderable(new Image(wallData.pos, wallData.scale, "./assets/walls/wall_mid.png"));

        }

        cg.addCollider(collider);
        isten.getPhysicsEngine().addColliderGroup(cg);
    }



}
