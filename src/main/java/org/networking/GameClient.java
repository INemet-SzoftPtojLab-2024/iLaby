package main.java.org.networking;

import main.java.org.game.Isten;
import main.java.org.game.UI.TimeCounter;
import main.java.org.items.Chest;
import main.java.org.items.ChestManager;
import main.java.org.items.Item;
import main.java.org.items.ItemManager;
import main.java.org.items.usable_items.*;
import main.java.org.linalg.Vec2;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class GameClient extends Thread {
    private InetAddress ipAddress;
    private DatagramSocket socket;
    Isten isten;
    int unitRoomPacketCount = 0;

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

    public void parsePacket(byte[] data, InetAddress address, int port) {
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
                unitRoomPacketCount++;
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
            case GASMASK:
                packet = new Packet14Gasmask(data);
                handleGasmask((Packet14Gasmask) packet);
                break;
            case TVSZ:
                packet =new Packet15Tvsz(data);
                handleTvsz((Packet15Tvsz)packet);
                break;
            case SOROSPOHAR:
                packet = new Packet16Sorospohar(data);
                handleSorospohar((Packet16Sorospohar) packet);
                break;
            case CAMEMBERT:
                packet = new Packet17Camembert(data);
                handleCamembert((Packet17Camembert)packet);
                break;
            case RONGY:
                packet = new Packet18Rongy(data);
                handleRongy((Packet18Rongy) packet);
                break;
            case TRANSISTOR:
                packet = new Packet19Transistor(data);
                handleTransistor((Packet19Transistor) packet);
                break;
            case WALL:
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
            case DOOROPEN:
                packet = new Packet24DoorOpen(data);
                handleDoorOpen((Packet24DoorOpen)packet);
                break;
            case INGASROOM:
                packet = new Packet26InGasRoom(data);
                handleInGasRoom((Packet26InGasRoom)packet);
                break;
            case VILLAININGASROOM:
                packet = new Packet27VillainIsInGasRoom(data);
                handleVillainInGasRoom((Packet27VillainIsInGasRoom)packet);
                break;
            case PLAYERROOMCHANGED:
                packet = new Packet28PlayerChangedRoom(data);
                handlePlayerChangedRoom((Packet28PlayerChangedRoom)packet);
                break;
            case REPLACECHEST:
                packet = new Packet40ReplaceChest(data);
                handleReplaceChest((Packet40ReplaceChest)packet);
                break;
            case ISPLAYERINVILLAINROOM:
                packet =new Packet41IsPlayerInVillainRoom(data);
                handleIsPlayerInVillainRoom((Packet41IsPlayerInVillainRoom)packet);
                break;
            case ITEMSDROPPED:
                packet = new Packet42ItemsDropped(data);
                handleItemsDropped((Packet42ItemsDropped)packet);
                break;
            case FOG:
                packet=new Packet50Fog(data);
                handleFog((Packet50Fog)packet);
                break;
        }
    }

    private void handleRongy(Packet18Rongy packet) {
        ((Rongy) isten.getItemManager().getItems().get(packet.getItemIndex())).stinkAnimation();
        isten.getItemManager().getItems().get(packet.getItemIndex()).setUsed(true);
    }

    private void handleTransistor(Packet19Transistor packet) {
        Vec2 position = new Vec2(packet.getX(), packet.getY());

        ((Transistor) isten.getItemManager().getItems().get(packet.getItemIndex())).getActivatedImage().setPosition(position);
        ((Transistor) isten.getItemManager().getItems().get(packet.getItemIndex())).getCountText().setVisibility(false);
        ((Transistor) isten.getItemManager().getItems().get(packet.getItemIndex())).setActivated(true);
    }

    private void handleCamembert(Packet17Camembert packet) {
        int itemIndex = packet.getItemIndex();
        String username = packet.getUsername();

        for(PlayerMP player: isten.getUpdatablesByType(PlayerMP.class)) {
            if(player.getPlayerName().getText().equalsIgnoreCase(username) && !player.localPlayer) {
                player.getInventory().setCamembert((Camembert)isten.getItemManager().getItems().get(itemIndex));
                player.getInventory().setCamembertTriggered(true);
            }
        }
    }

    private void handleReplaceChest(Packet40ReplaceChest packet) {
        if(isten.getSocketServer() != null) return;

        Vec2 newPos = packet.getPos();
        int wallLocation = packet.getWallLocation();
        int index = packet.getIdx();
        //megkeressunk a chestet, majd meghivjuk ra a replace fv-t
        Chest chest = isten.getChestManager().getChests().get(index);
        chest.replaceChest(newPos, wallLocation);
    }

    private void handleItemsDropped(Packet42ItemsDropped packet) {
        String username = packet.getUsername();

        for(PlayerMP player: isten.getUpdatablesByType(PlayerMP.class)) {
            if(player.getUsername().equalsIgnoreCase(username)) {
                for(int i = 0; i < player.getInventory().getStoredItems().size(); i++) {
                    Item item = player.getInventory().getStoredItems().get(i);
                    if(item != null) item.dropOnGround(new Vec2(player.getPlayerCollider().getPosition().x,
                            player.getPlayerCollider().getPosition().y));
                }
                for (int i = 0; i < 5; i++) {
                    player.getInventory().getStoredItems().set(i, null);
                }
            }
        }
    }

    private void handleSorospohar(Packet16Sorospohar packet) {
        ((Sorospohar) isten.getItemManager().getItems().get(packet.getItemIndex())).setCapacity(packet.getCapacity());
        ((Sorospohar) isten.getItemManager().getItems().get(packet.getItemIndex())).resizeBar(packet.getCapacity());
    }

    private void handleIsPlayerInVillainRoom(Packet41IsPlayerInVillainRoom packet) {
        String username = packet.getUsername();
        boolean isPlayerInVillainRoom = packet.getIsPlayerInVillainRoom();

        for(PlayerMP player: isten.getUpdatablesByType(PlayerMP.class)) {
            if(player.getUsername().equalsIgnoreCase(username)) {
                player.setPlayerInVillainRoom(isPlayerInVillainRoom);
            }

        }
    }

    private void handleTvsz(Packet15Tvsz packet) {
        int charges = packet.getCharges();
        ((Tvsz)isten.getItemManager().getItems().get(packet.getItemIndex())).setCharges(charges);
    }


    private void handlePlayerChangedRoom(Packet28PlayerChangedRoom packet) {
        for(PlayerMP player: isten.getUpdatablesByType(PlayerMP.class)) {
            if(player.getUsername().equalsIgnoreCase(packet.getUsername())) {
                player.changedRoom(true);
            }
        }
    }

    private void handleVillainInGasRoom(Packet27VillainIsInGasRoom packet) {
        String villainName = packet.getVillainName();
        boolean isInGasRoom = packet.isInGasRoom();

        HandlerManager hm = isten.getHandlerManager();
        hm.lock.lock();
        try {
            // Critical section
            // Access shared resources here
            hm.addTask(HandlerManager.TaskType.VillainInGasRoom);
            hm.addData(new HandlerManager.VillainInGasRoomData(villainName, isInGasRoom));
        } finally {
            hm.lock.unlock(); // Release the lock
        }
    }

    private void handleInGasRoom(Packet26InGasRoom packet) {
        String username = packet.getUsername();
        boolean isInGasRoom = packet.isInGasRoom();

        HandlerManager hm = isten.getHandlerManager();
        hm.lock.lock();
        try {
            // Critical section
            // Access shared resources here
            hm.addTask(HandlerManager.TaskType.InGasRoom);
            hm.addData(new HandlerManager.InGasRoomData(username, isInGasRoom));
        } finally {
            hm.lock.unlock(); // Release the lock
        }
    }

    private void handleGasmask(Packet14Gasmask packet) {
        for(int i = 0; i < isten.getUpdatables().size(); i++) {
            if(isten.getUpdatable(i).getClass() == ItemManager.class) {
                ((Gasmask) isten.getUpdatables().get(i).getItems().get(packet.getItemIndex())).setCapacity(packet.getCapacity());
                ((Gasmask) isten.getUpdatables().get(i).getItems().get(packet.getItemIndex())).resizeBar(packet.getCapacity());
            }
        }
    }

    private void handleDoorOpen(Packet24DoorOpen packet) {
        float x = packet.getX();
        float y = packet.getY();
        boolean isSolid = packet.isSolid();

        HandlerManager hm = isten.getHandlerManager();
        hm.lock.lock();
        try {
            // Critical section
            // Access shared resources here
            hm.addTask(HandlerManager.TaskType.DoorOpen);
            hm.addData(new HandlerManager.DoorOpenData(x,y, isSolid));
        } finally {
            hm.lock.unlock(); // Release the lock
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
        Vec2 pos = packet.getPos();
        String username = packet.getUsername();
        int selectedSlot = packet.getSelectedSlot();
        int itemIndex = packet.getItemIndex();
        boolean replaced = packet.getReplaced();

        for(int i = 0; i < isten.getUpdatables().size(); i++) {
            if(isten.getUpdatable(i).getClass() == ItemManager.class) {

                for(PlayerMP player: isten.getUpdatablesByType(PlayerMP.class)) {
                    if(player.getUsername().equalsIgnoreCase(username)) {
                        Item item = isten.getItemManager().getItems().get(itemIndex);

                        player.getInventory().dropItem(item, pos, selectedSlot, replaced);
                        break;
                    }
                }
            }
        }
    }

    private void handleItemPickedUp(Packet12ItemPickedUp packet) {
        int itemIndex = packet.getItemIndex();
        String username = packet.getUsername();
        int selectedSlot = packet.getSelectedSlot();

        for(int i = 0; i < isten.getUpdatables().size(); i++) {
            if(isten.getUpdatable(i).getClass() == ItemManager.class) {

                for(PlayerMP player: isten.getUpdatablesByType(PlayerMP.class)) {
                    if(player.getUsername().equalsIgnoreCase(username)) {
                        Item item = isten.getItemManager().getItems().get(itemIndex);
                        item.pickUpInInventory(player, selectedSlot);
                        break;
                    }
                }
            }
        }
    }

    private void handleChestOpened(Packet11ChestOpened packet) {
        int chestIndex = packet.getChestIndex();
        int index1 = packet.getItemIndex1();
        int index2 = packet.getItemIndex2();

        for(int i = 0; i < isten.getUpdatables().size(); i++) {
            if(isten.getUpdatable(i).getClass() == ChestManager.class) {
                Chest chest = isten.getUpdatables().get(i).getChests().get(chestIndex);
                Item item1 = null, item2 = null;
                if(index1 == -1 && index2 == -1) {

                }
                else if(index1 != -1 && index2 == -1) {
                    item1 = isten.getItemManager().getItems().get(index1);
                }
                else if(index1 != -1) {
                    item1 = isten.getItemManager().getItems().get(index1);
                    item2 = isten.getItemManager().getItems().get(index2);
                }
                chest.open(item1, item2);
                break;
            }
        }
    }

    private void handleChestGeneration(Packet10ChestGeneration packet) {
        if(isten.getSocketServer() != null) return;

        for(int i = 0; i < isten.getUpdatables().size(); i++) {
            if(isten.getUpdatable(i).getClass() == ChestManager.class) {
                Chest chest =  new Chest(packet.getPos(),isten, packet.getChestType(), packet.getWallLocation(), packet.getIdx());
                ChestManager chestManager = (ChestManager) isten.getUpdatables().get(i); // = isten.getChestManager()...
                chestManager.addChest(chest);
            }
        }
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

        HandlerManager hm = isten.getHandlerManager();
        hm.lock.lock();
        try {
            // Critical section
            // Access shared resources here
            hm.addTask(HandlerManager.TaskType.UnitRoom);
            hm.addData(new HandlerManager.UnitRoomData(position, path));
        } finally {
            hm.lock.unlock(); // Release the lock
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

        HandlerManager hm = isten.getHandlerManager();
        hm.lock.lock();
            try {
                // Critical section
                // Access shared resources here
                hm.addTask(HandlerManager.TaskType.Villain);
                hm.addData(new HandlerManager.VillainData(villainName, position, imagePath));
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

    private void handleFog(Packet50Fog packet)
    {
        ArrayList<PlayerMP> players=isten.getUpdatablesByType(PlayerMP.class);
        for(int i=0;i<players.size();i++)
            players.get(i).appendFogSyncInfo(packet.getValues());
    }

    public void sendData(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
        try {
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }
}
