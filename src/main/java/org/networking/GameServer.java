package main.java.org.networking;

import main.java.org.game.Isten;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import main.java.org.game.Map.Room;
import main.java.org.game.Map.RoomType;
import main.java.org.game.Map.UnitRoom;
import main.java.org.items.ChestManager;
import main.java.org.items.Item;
import main.java.org.items.ItemManager;
import main.java.org.linalg.Vec2;
import main.java.org.networking.Packet.PacketTypes;

public class GameServer extends Thread {
    private ArrayList<ServerSideHandler> serverSideHandlers;
    private VillainHandler villainHandler;
    private MapHandler mapHandler;
    private TimeHandler timeHandler;
    private PlayerStatusHandler playerStatusHandler;
    private ChestGenerationHandler chestGenerationHandler;
    private DatagramSocket socket;

    private ArrayList<byte[]> events = new ArrayList<>();

    Isten isten;
    boolean isInitialized = false;

    //Notification for gamemanager, that the server is initialized, so it can enter the gameloop
    SharedObject InitializationLock;

    //Just values, not references for the actual players
    //When changing something in one of the connectedPlayers it won't change anything on the actual players in updatable
    private List<PlayerMP> connectedPlayers = new ArrayList<>();
    public GameServer(Isten isten) {
        this.isten = isten;
        InitializationLock = new SharedObject();
        try {
            this.socket = new DatagramSocket(1331);
        }
        catch(SocketException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("ITS THE SERVER!");
        startServer();
        while(true) {
            //Get packets from clients
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

    private void startServer() {
        serverSideHandlers = new ArrayList<ServerSideHandler>();
        mapHandler = new MapHandler();
        timeHandler = new TimeHandler();
        villainHandler = new VillainHandler();
        playerStatusHandler = new PlayerStatusHandler();
        chestGenerationHandler = new ChestGenerationHandler();

        serverSideHandlers.add(mapHandler);
        serverSideHandlers.add(timeHandler);
        serverSideHandlers.add(villainHandler);
        serverSideHandlers.add(playerStatusHandler);
        serverSideHandlers.add(chestGenerationHandler);

        for(ServerSideHandler serverSideHandler: serverSideHandlers) {
            serverSideHandler.create(this);
            serverSideHandler.setInitialized(true);
        }
        isInitialized = true;
        //Send notification to game manager, so that it can start the game loop
        InitializationLock.sendNotification();

    }

    public void updateServer(Isten isten, double deltaTime) {
        for(ServerSideHandler serverSideHandler: serverSideHandlers) {
            if(serverSideHandler.isInitialized && isInitialized) serverSideHandler.update(isten, deltaTime);
        }
    }

    //Parse packet to string
    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        PacketTypes type = Packet.lookupPacket(message.substring(0,2));
        Packet packet = null;
        switch(type) {
            default:
                break;
            case INVALID:
                break;
            case LOGIN:
                packet = new Packet00Login(data);
                handleLogin((Packet00Login)packet, address, port);
                break;
            case DISCONNECT:
                break;
            case MOVE:
                packet = new Packet02Move(data);
                handleMove(((Packet02Move)packet));
                break;
            case ANIMATION:
                packet = new Packet03Animation(data);
                handleAnimation((Packet03Animation) packet);
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
            case PLAYERPOSFORDOOROPEN:
                packet = new Packet25PlayerForDoorOpen(data);
                handlePlayerPosForDoorOpen((Packet25PlayerForDoorOpen)packet);
                break;
            case CAMEMBERT:
                packet = new Packet17Camembert(data);
                handleCamembert((Packet17Camembert)packet);
                break;
            case TVSZ:
                packet = new Packet15Tvsz(data);
                handleTvsz((Packet15Tvsz)packet);
                break;
            case SOROSPOHAR:
                packet = new Packet16Sorospohar(data);
                handleSorospohar((Packet16Sorospohar) packet);
                break;
            case REPLACECHEST:
                packet = new Packet40ReplaceChest(data);
                handleReplaceChest((Packet40ReplaceChest) packet);
                break;
            case ISPLAYERINVILLAINROOM:
                packet = new Packet41IsPlayerInVillainRoom(data);
                handleIsPlayerVillainRoom((Packet41IsPlayerInVillainRoom)packet);
                break;
            case ITEMSDROPPED:
                packet = new Packet42ItemsDropped(data);
                handleItemsDropped((Packet42ItemsDropped)packet);
                break;
        }
    }

    private void handleReplaceChest(Packet40ReplaceChest packet) { sendDataToAllClients(packet.getData());}

    private void handleSorospohar(Packet16Sorospohar packet) { sendDataToAllClients(packet.getData()); }

    private void handleItemsDropped(Packet42ItemsDropped packet) {
        sendDataToAllClients(packet.getData());
    }

    private void handleIsPlayerVillainRoom(Packet41IsPlayerInVillainRoom packet) {
        sendDataToAllClients(packet.getData());
    }

    private void handleTvsz(Packet15Tvsz packet) {
        sendDataToAllClients(packet.getData());
    }

    private void handleCamembert(Packet17Camembert packet) {
        int x = (int)packet.getX();
        int y = (int)packet.getY();
        Room ownerRoom = isten.getMap().getUnitRooms()[y][x].getOwnerRoom();
        ownerRoom.setRoomType(RoomType.GAS);

        for(UnitRoom unitRoom: ownerRoom.getUnitRooms()) {
            Packet04UnitRoom packet04UnitRoom = new Packet04UnitRoom(unitRoom.getPosition().x,
                    unitRoom.getPosition().y, RoomType.GAS.ordinal());
            sendDataToAllClients(packet04UnitRoom.getData());
        }
    }

    private void handleGasmask(Packet14Gasmask packet) {
        for(int i = 0; i < isten.getUpdatables().size(); i++) {
            if(isten.getUpdatable(i).getClass() == ItemManager.class) {
                isten.getUpdatables().get(i).getItems().get(packet.getItemIndex()).setCapacity(packet.getCapacity());
                isten.getUpdatables().get(i).getItems().get(packet.getItemIndex()).resizeBar(packet.getCapacity());
            }
        }
        sendDataToAllClients(packet.getData());
    }

    private void handlePlayerPosForDoorOpen(Packet25PlayerForDoorOpen packet) {
        mapHandler.CheckIfPlayerOpenedDoor(packet);
    }

    private void handleItemDropped(Packet13ItemDropped packet) {
        //events.add(packet.getData());
        sendDataToAllClients(packet.getData());
    }

    private void handleItemPickedUp(Packet12ItemPickedUp packet) {
        //events.add(packet.getData());
        sendDataToAllClients(packet.getData());
    }

    private void handleChestOpened(Packet11ChestOpened packet) {
        events.add(packet.getData());
        sendDataToAllClients(packet.getData());
    }

    //handle Animation Packet
    private void handleAnimation(Packet03Animation packet) {
        packet.writeData(this);
    }

    //handle Login Packet
    private void handleLogin(Packet00Login packet, InetAddress address, int port) {
        PlayerMP player = null;
        player = new PlayerMP(isten, packet.getUsername(), address, port);
        player.setSkinID(packet.getSkinID());
        this.addConnection(player, packet);

        //Handle creation of villains - when player joins, the server generated villains are generated on client as well
        handlePlayerJoinedData(player);
    }

    private void handlePlayerJoinedData(PlayerMP player) {
        playerStatusHandler.addClient(player);
        mapHandler.sendDataToClient(player);
        villainHandler.sendDataToClient(player);
        chestGenerationHandler.sendDataToClient(player);
        for (byte[] data : events) sendDataToAllClients(data);
    }

    //handle Move Packet
    private void handleMove(Packet02Move packet) {
        packet.writeData(this);
    }

    public void addConnection(PlayerMP player, Packet00Login packet) {
        boolean alreadyConnected = false;
        for (PlayerMP p : this.connectedPlayers) {
            if(player.getUsername().equalsIgnoreCase(p.getUsername())) {
                if(p.ipAddress == null) {
                    p.ipAddress = player.ipAddress;
                }
                if(p.port == -1) {
                    p.port = player.port;
                }
                alreadyConnected = true;
            }
            else {
                //New player's position is (0,0) -> later: spawnPoints
                //Send data to the already connected players, that the new player exists
                packet = new Packet00Login(player.getUsername(), 0, 0, player.getSkinID());
                sendData(packet.getData(), p.ipAddress, p.port);

                //Send position as well, so that the players spawn at their current position
                //Send data to the new player, that the already connected player exists
                int index = isten.getPlayerMPIndex(p.getUsername());
                Vec2 pos = ((PlayerMP)isten.getUpdatable(index)).getPlayerCollider().getPosition();
                packet = new Packet00Login(p.getUsername(), pos.x, pos.y, p.getSkinID());
                sendData(packet.getData(), player.ipAddress, player.port);

            }
        }
        if(!alreadyConnected) {
            //If the player has not been connected before, then add it to connectedPlayers
            this.connectedPlayers.add(player);
        }
    }

    //Send data to one client
    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Send data to all clients
    public void sendDataToAllClients(byte[] data) {
        for(int i = 0; i < connectedPlayers.size(); i++) {
            PlayerMP p = connectedPlayers.get(i);
            sendData(data, p.ipAddress, p.port);
        }
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public SharedObject getInitializationLock() {
        return InitializationLock;
    }
}
