package main.java.org.networking;

import main.java.org.game.Isten;
import main.java.org.game.Map.EdgePiece;
import main.java.org.game.Map.Map;
import main.java.org.game.UI.TimeCounter;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.items.Chest;
import main.java.org.items.ChestManager;
import main.java.org.items.Item;
import main.java.org.items.ItemManager;
import main.java.org.linalg.Vec2;

import java.io.IOException;
import java.net.*;

public class GameClient extends Thread {
    private InetAddress ipAddress;
    private DatagramSocket socket;
    Isten isten;

    public GameClient(Isten isten, String ipAddress) {
        this.isten = isten;
        try {
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);
        }
        catch(SocketException e) {
            e.printStackTrace();
        }
        catch(UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0,2));
        Packet packet = null;
        switch(type) {
            default:
                break;
            case INVALID:
                break;
            case LOGIN:
                packet = new Packet00Login(data);
                handleLogin((Packet00Login) packet, address, port);
                break;
            case DISCONNECT:
                break;
            case MOVE:
                packet = new Packet02Move(data);
                handleMove((Packet02Move) packet);
                break;
            case ANIMATION:
                packet = new Packet03Animation(data);
                handleAnimation((Packet03Animation)packet);
                break;
            case UNITROOM:
                packet = new Packet04UnitRoom(data);
                handleUnitRoom((Packet04UnitRoom)packet);
                break;
            case VILLAIN:
                packet = new Packet05Villain(data);
                handleVillain((Packet05Villain)packet);
                break;
            case VILLAINMOVE:
                packet = new Packet06VillainMove(data);
                handleVillainMove((Packet06VillainMove) packet);
                break;
            case TIMER:
                packet = new Packet07Timer(data);
                handleTimer((Packet07Timer)packet);
                break;
            case CHESTGENERATION:
                packet = new Packet10ChestGeneration(data);
                handleChestGeneration((Packet10ChestGeneration) packet);
                break;
            case CHESTOPENED:
                packet = new Packet11ChestOpened(data);
                handleChestOpened((Packet11ChestOpened) packet);
                break;
            case ITEMPICKEDUP:
                packet = new Packet12ItemPickedUp(data);
                handleItemPickedUp((Packet12ItemPickedUp) packet);
                break;
            case ITEMDROPPED:
                packet = new Packet13ItemDropped(data);
                handleItemDropped((Packet13ItemDropped) packet);
                break;
            case WALL:
                //System.out.println("GOT WALL PACKET");
                packet = new Packet20Wall(data);
                handleWall((Packet20Wall) packet);
                break;
            case DEATH:
                packet = new Packet21Death(data);
                handleDeath((Packet21Death)packet);
                break;
            case EDGEPIECECHANGED:
                packet = new Packet22EdgePieceChanged(data);
                handleEdgePieceChanged((Packet22EdgePieceChanged)packet);
                break;
            case WALLDELETE:
                packet = new Packet23WallDelete(data);
                handleWallDelete((Packet23WallDelete)packet);
                break;
        }
    }

    private void handleEdgePieceChanged(Packet22EdgePieceChanged packet) {

        float x = packet.getX();
        float y = packet.getY();
        boolean isDoor = packet.isDoor();

        HandlerManager hm = isten.getHandlerManager();
        hm.lock.lock();
        try {
            // Critical section
            // Access shared resources here
            hm.addTask(HandlerManager.TaskType.EdgePieceChanged);
            hm.addData(new HandlerManager.EdgePieceChangeData(x,y, isDoor));
        } finally {
            hm.lock.unlock(); // Release the lock
        }

    }

    private void handleWallDelete(Packet23WallDelete packet) {

        float x = packet.getX();
        float y = packet.getY();

        HandlerManager hm = isten.getHandlerManager();
        hm.lock.lock();
        try {
            // Critical section
            // Access shared resources here
            hm.addTask(HandlerManager.TaskType.WallDelete);
            hm.addData(new HandlerManager.WallDeleteData(x,y));
        } finally {
            hm.lock.unlock(); // Release the lock
        }

    }

    private void handleItemDropped(Packet13ItemDropped packet) {
        for(int i = 0; i < isten.getUpdatables().size(); i++) {
            if(isten.getUpdatable(i).getClass() == ItemManager.class) {
                isten.getUpdatables().get(i).getItems().get(packet.getItemIndex()).setLocation(Item.Location.GROUND);
                isten.getUpdatables().get(i).getItems().get(packet.getItemIndex()).getImage().setVisibility(true);
                isten.getUpdatables().get(i).getItems().get(packet.getItemIndex()).getImage().setPosition(packet.getPos());
                isten.getUpdatables().get(i).getItems().get(packet.getItemIndex()).setPosition(packet.getPos());
            }
        }
    }

    private void handleItemPickedUp(Packet12ItemPickedUp packet) {
        for(int i = 0; i < isten.getUpdatables().size(); i++) {
            if(isten.getUpdatable(i).getClass() == ItemManager.class) {
                isten.getUpdatables().get(i).getItems().get(packet.getItemIndex()).setLocation(Item.Location.INVENTORY);
                isten.getUpdatables().get(i).getItems().get(packet.getItemIndex()).getImage().setVisibility(false);
            }
        }
    }

    private void handleChestOpened(Packet11ChestOpened packet) {
        for(int i = 0; i < isten.getUpdatables().size(); i++) {
            if(isten.getUpdatable(i).getClass() == ChestManager.class) {
                isten.getUpdatables().get(i).getChests().get(packet.getChestIndex()).open();
            }
        }
    }

    private int chestGenCount = 0;
    private void handleChestGeneration(Packet10ChestGeneration packet) {
        if(isten.getSocketServer() != null) return;

        int chestIndex = 0;
        for(int i = 0; i < isten.getUpdatables().size(); i++) {
            if(isten.getUpdatable(i).getClass() == ChestManager.class) {
                chestIndex = i;
                isten.getUpdatables().get(i).getChests().add(new Chest(packet.getPos(),isten,packet.getHeading(), packet.getChestType()));
                chestGenCount++;
            }
        }

        ColliderGroup chestColliders=new ColliderGroup();
        for (int i = 0; i < isten.getUpdatables().get(chestIndex).getChests().size(); i++) {
            Collider c=new Collider( isten.getUpdatables().get(chestIndex).getChests().get(i).getPosition(),new Vec2(0.15f,0.15f));
            chestColliders.addCollider(c);
        }
        isten.getPhysicsEngine().addColliderGroup(chestColliders);
    }

    private void handleDeath(Packet21Death packet) {
        String username = packet.getUsername();

        for(int i = 0; i < isten.getUpdatables().size(); i++) {
            if(isten.getUpdatable(i).getClass() == PlayerMP.class) {
                PlayerMP playerMP = (PlayerMP)isten.getUpdatable(i);
                if(playerMP.getUsername().equalsIgnoreCase(username)) {
                    playerMP.setAlive(false);
                }
            }
        }
    }

    private void handleWall(Packet20Wall packet) {

        //if(isten.getSocketServer() != null) return;

        Vec2 pos = new Vec2(packet.getPosX(), packet.getPosY());
        Vec2 scale = new Vec2(packet.getScaleX(), packet.getScaleY());
        boolean isDoor = packet.isDoor();

        HandlerManager hm = isten.getHandlerManager();
        hm.lock.lock();
            try {
                // Critical section
                // Access shared resources here
                hm.addTask(HandlerManager.TaskType.Wall);
                hm.addData(new HandlerManager.WallData(pos, scale, isDoor));
            } finally {
                hm.lock.unlock(); // Release the lock
            }
    }

    private void handleUnitRoom(Packet04UnitRoom packet) {
        Vec2 position = new Vec2(packet.getX(), packet.getY());
        int type = packet.getType();
        String path = "./assets/floors/floor" + (type+1) + ".png";
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

    private void handleTimer(Packet07Timer packet) {
        double timeRemaining = packet.getTimeRemaining();
        TimeCounter.setTimeRemaining(timeRemaining);
    }

    private void handleVillainMove(Packet06VillainMove packet) {
        String villainName = packet.getVillainName();
        Vec2 position = new Vec2(packet.getX(), packet.getY());

        HandlerManager hm = isten.getHandlerManager();
        hm.lock.lock();
            try {
                // Critical section
                // Access shared resources here
                hm.addTask(HandlerManager.TaskType.VillainMove);
                hm.addData(new HandlerManager.VillainMoveData(villainName, position));
            } finally {
                hm.lock.unlock(); // Release the lock
            }
    }

    private void handleVillain(Packet05Villain packet) {
        String villainName = packet.getVillainName();
        Vec2 position = packet.getPosition();
        String imagePath = packet.getImagePath();
        int random1 = packet.getRandom1();
        int random2 = packet.getRandom2();

        HandlerManager hm = isten.getHandlerManager();
        hm.lock.lock();
            try {
                // Critical section
                // Access shared resources here
                hm.addTask(HandlerManager.TaskType.Villain);
                hm.addData(new HandlerManager.VillainData(villainName, position, imagePath, random1, random2));
            } finally {
                hm.lock.unlock(); // Release the lock
            }
    }

    private void handleAnimation(Packet03Animation packet) {
        int index = isten.getPlayerMPIndex(packet.getUsername());
        PlayerMP player = (PlayerMP)isten.getUpdatable(index);
        if(player == null || player.localPlayer || player.getPlayerImage() == null) return;
        for(int i = 0; i < player.getPlayerImage().size(); i++) {
            player.getPlayerImage().get(i).setVisibility(false);
        }
        player.setActiveImage(packet.getActiveImage());
        if(!player.getPlayerImage().isEmpty() && packet.getActiveImage() < player.getPlayerImage().size()) player.getPlayerImage().get(packet.getActiveImage()).setVisibility(true);
    }

    private void handleLogin(Packet00Login packet, InetAddress address, int port) {
        PlayerMP player = null;
        Vec2 position = new Vec2(packet.getX(), packet.getY());
        String username = packet.getUsername();
        int skinID = packet.getSkinID();

        HandlerManager hm = isten.getHandlerManager();
        hm.lock.lock();
        try {
            hm.addTask(HandlerManager.TaskType.Login);
            hm.addData(new HandlerManager.LoginData(username, address, port, position, skinID));
        }
        finally {
            hm.lock.unlock();
        }
    }

    private void handleMove(Packet02Move packet) {
        String username = packet.getUsername();

        int index = isten.getPlayerMPIndex(username);
        PlayerMP player = (PlayerMP)isten.getUpdatable(index);
        if(player == null) return;
        if(player.getPlayerCollider() != null) player.getPlayerCollider().setPosition(new Vec2(packet.getX(), packet.getY()));
    }

    public void sendData(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
        try {
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
