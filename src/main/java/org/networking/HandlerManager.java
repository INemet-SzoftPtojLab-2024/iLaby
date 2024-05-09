package main.java.org.networking;

import main.java.org.entities.player.Player;
import main.java.org.entities.villain.Villain;
import main.java.org.game.Graphics.Image;

import java.net.InetAddress;
import java.util.concurrent.locks.Lock;
import main.java.org.game.Graphics.Text;
import main.java.org.game.Isten;
import main.java.org.game.Map.*;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.game.updatable.Updatable;
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

    public static class UnitRoomData extends HandlerData {
        public UnitRoomData(Vec2 position, String path) {
            this.position = position;
            this.path = path;
        }
        public Vec2 position;
        public String path;
    }
    public static class EdgePieceChangeData extends HandlerData {
        public EdgePieceChangeData(float x, float y, boolean isDoor) {
            this.x = x;
            this.y = y;
            this.isDoor = isDoor;
        }
        public float x;
        public float y;
        public boolean isDoor;
    }

    public static class WallDeleteData extends HandlerData {
        public WallDeleteData(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float x;
        public float y;
    }

    public static class VillainData extends HandlerData {
        VillainData(String villainName, Vec2 position, String imgPath) {
            this.villainName = villainName;
            this.position = position;
            this.imgPath = imgPath;
        }

        public String villainName;
        public Vec2 position;
        public String imgPath;
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

    public static class LoginData extends HandlerData {
        public LoginData(String username, InetAddress inetAddress, int port, Vec2 position, int skinID) {
            this.username = username;
            this.inetAddress = inetAddress;
            this.port = port;
            this.position = position;
            this.skinID = skinID;
        }

        public String username;
        public Vec2 position;
        public int port;
        public InetAddress inetAddress;
        public int skinID;
    }

    public static class DoorOpenData extends HandlerData {
        public DoorOpenData(float x, float y, boolean isSolid) {
            this.x = x;
            this.y = y;
            this.isSolid = isSolid;
        }
        public float x;
        public float y;
        public boolean isSolid;
    }

    public static class InGasRoomData extends HandlerData {
        public InGasRoomData(String username, boolean isInGasRoom) {
            this.username = username;
            this.isInGasRoom = isInGasRoom;
        }
        public String username;
        public boolean isInGasRoom;
    }

    public static class VillainInGasRoomData extends HandlerData {
        public VillainInGasRoomData(String villainName, boolean isInGasRoom) {
            this.villainName = villainName;
            this.isInGasRoom = isInGasRoom;
        }
        public String villainName;
        public boolean isInGasRoom;
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
                case Wall: {
                    if (data.getClass() != WallData.class) return;
                    WallData wallData = (WallData) data;
                    wallHandler(wallData);
                    break;
                }
                case Login: {
                    if(data.getClass() != LoginData.class) return;
                    LoginData loginData = (LoginData)data;
                    loginHandler(loginData);
                    break;
                }
                case WallDelete: {
                    if(data.getClass() != WallDeleteData.class) return;
                    WallDeleteData wallDeleteData = (WallDeleteData)data;
                    wallDeleteHandler(wallDeleteData);
                    break;
                }
                case EdgePieceChanged: {
                    if(data.getClass() != EdgePieceChangeData.class) return;
                    EdgePieceChangeData edgePieceChangeData = (EdgePieceChangeData)data;
                    edgePieceChangeHandler(edgePieceChangeData);
                    break;
                }
                case DoorOpen: {
                    if(data.getClass() != DoorOpenData.class) return;
                    DoorOpenData doorOpenData = (DoorOpenData)data;
                    doorOpenHandler(doorOpenData);
                    break;
                }
                case UnitRoom:
                {
                    if(data.getClass() != UnitRoomData.class) return;
                    UnitRoomData unitRoomData = (UnitRoomData)data;
                    unitRoomHandler(unitRoomData);
                    break;
                }
                case InGasRoom: {
                    if(data.getClass() != InGasRoomData.class) return;
                    InGasRoomData inGasRoomData = (InGasRoomData)data;
                    inGasRoomHandler(inGasRoomData);
                    break;
                }
                case VillainInGasRoom: {
                    if(data.getClass() != VillainInGasRoomData.class) return;
                    VillainInGasRoomData villainInGasRoomData = (VillainInGasRoomData)data;
                    VillainInGasRoomHandler(villainInGasRoomData);
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
        Wall,
        Login,
        WallDelete,
        EdgePieceChanged,
        DoorOpen,
        UnitRoom,
        InGasRoom,
        VillainInGasRoom
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
            return synchronizedTasks.remove(0);
        }
        finally {
            lock.unlock();
        }

    }

    public HandlerData getHandlerData() {
        lock.lock();
        try {
            if (handlerDataList.isEmpty()) return null;
            return synchronizedHandlerDataList.remove(0);
        }
        finally {
            lock.unlock();
        }

    }


    //->>>>>>>>>>>>>>>>//
    //HANDLER FUNCTIONS
    //->>>>>>>>>>>>>>>>//

    private void unitRoomHandler(UnitRoomData unitRoomData) {
        Vec2 position = unitRoomData.position;
        String path = unitRoomData.path;

        Map map = isten.getMap();

        for(int i = 0; i < map.getMapRowSize(); i++) {
            for(int j = 0; j < map.getMapColumnSize(); j++) {
                if(map.getUnitRooms()[i][j].getPosition().x == position.x &&
                        map.getUnitRooms()[i][j].getPosition().y == position.y) {
                    map.getUnitRooms()[i][j].setNewImage(path, isten);
                }
            }
        }
    }

    private void doorOpenHandler(DoorOpenData doorOpenData) {
        float x = doorOpenData.x;
        float y = doorOpenData.y;
        boolean isSolid = doorOpenData.isSolid;

        if(isten.getClientMap().getEdgeBetweenRooms() == null) return;

        for(int i = 0; i < isten.getClientMap().getEdgeBetweenRooms().getWalls().size(); i++) {
            EdgePiece edgePiece = isten.getClientMap().getEdgeBetweenRooms().getWalls().get(i);
            if(edgePiece.isDoor() && edgePiece.getPosition().x == x && edgePiece.getPosition().y == y) {
                //ha az isSolid false -> akkor open
                if(edgePiece instanceof  Door){
                    if(!isSolid) ((Door) edgePiece).openOnClient();
                    else ((Door) edgePiece).closeOnClient();
                }

                break;
            }
        }
    }

    private void wallDeleteHandler(WallDeleteData wallDeleteData) {

        float x = wallDeleteData.x;
        float y = wallDeleteData.y;

        if(isten.getClientMap().getEdgeBetweenRooms() == null) return;

        for(int i = 0; i < isten.getClientMap().getEdgeBetweenRooms().getWalls().size(); i++) {
            EdgePiece edgePiece = isten.getClientMap().getEdgeBetweenRooms().getWalls().get(i);
            if(edgePiece.getPosition().x == x
                    && edgePiece.getPosition().y == y) {
                isten.getClientMap().removeEdgePiece(edgePiece);
                break;
            }
        }
    }

    private void wallHandler(WallData wallData) {
        Collider collider = new Collider(wallData.pos, wallData.scale);

        EdgePiece edgePiece;

        if (wallData.isDoor) {
            collider.setSolidity(false);
            edgePiece = new Door(collider, collider.getPosition(), null, null);
            edgePiece.setNewImage("./assets/doors/doors_leaf_closed.png", wallData.scale, isten);

        } else {
            edgePiece = new Wall(collider, collider.getPosition(), null, null);
            edgePiece.setNewImage("./assets/walls/wall_mid.png", wallData.scale, isten);
        }

        isten.getClientMap().addEdgePiece(edgePiece);
        isten.getClientMap().getEdgeBetweenRooms().addCollider(collider);



    }

    private void villainHandler(VillainData villainData) {
        Villain villain = new Villain(villainData.villainName, villainData.position, villainData.imgPath);
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

    private void loginHandler(LoginData loginData) {
        String username = loginData.username;
        InetAddress address = loginData.inetAddress;
        int port = loginData.port;
        Vec2 position = loginData.position;
        int skinID = loginData.skinID;

        PlayerMP player = new PlayerMP(username, address, port, position);
        player.setSkinID(skinID);
        isten.addUpdatable(player);
    }

    private void edgePieceChangeHandler(EdgePieceChangeData edgePieceChangeData) {

        int sizeOfEdges = isten.getClientMap().getEdgeBetweenRooms().getWalls().size();
        ArrayList<EdgePiece> walls = isten.getClientMap().getEdgeBetweenRooms().getWalls();
        for(int i = 0; i < sizeOfEdges; i++) {
            EdgePiece oldEdgePiece = walls.get(i);
            EdgePiece edgePiece;

            if(oldEdgePiece.isDoor() != edgePieceChangeData.isDoor
                    && edgePieceChangeData.x == oldEdgePiece.getPosition().x
                    && edgePieceChangeData.y == oldEdgePiece.getPosition().y) {

                isten.getClientMap().removeEdgePiece(oldEdgePiece);

                Collider collider = new Collider(oldEdgePiece.getCollider().getPosition(), oldEdgePiece.getCollider().getScale());
                isten.getClientMap().getEdgeBetweenRooms().addCollider(collider);

                if (edgePieceChangeData.isDoor) {
                    collider.setSolidity(false);

                    edgePiece = new Door(collider, collider.getPosition(), null, null);
                    edgePiece.setNewImage("./assets/doors/doors_leaf_closed.png", oldEdgePiece.getCollider().getScale(), isten);
                }
                else {
                    collider.setSolidity(true);
                    edgePiece = new Wall(collider, collider.getPosition(), null, null);
                    edgePiece.setNewImage("./assets/walls/wall_mid.png", oldEdgePiece.getCollider().getScale(), isten);
                }

                isten.getClientMap().addEdgePiece(edgePiece);
                break;
            }

        }


    }

    private void inGasRoomHandler(InGasRoomData inGasRoomData) {
        String username = inGasRoomData.username;
        boolean isInGasRoom = inGasRoomData.isInGasRoom;

        for(Updatable updatable: isten.getUpdatables()) {

            if(updatable.getClass() == PlayerMP.class ) {
                PlayerMP player = (PlayerMP)updatable;
                if(player.getUsername().equalsIgnoreCase(username)) player.isInGasRoom(isInGasRoom);
            }
        }
    }

    private void VillainInGasRoomHandler(VillainInGasRoomData villainInGasRoomData) {
        String villainName = villainInGasRoomData.villainName;
        boolean isInGasRoom = villainInGasRoomData.isInGasRoom;

        for(Villain villain: isten.getUpdatablesByType(Villain.class)) {
            if(villain.getVillainName().equalsIgnoreCase(villainName)) {
                villain.isInGasRoom(isInGasRoom);
            }
        }
    }


}