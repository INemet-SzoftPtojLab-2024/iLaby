package main.java.org.networking;

import main.java.org.entities.villain.Villain;
import main.java.org.game.Isten;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import main.java.org.linalg.Vec2;
import main.java.org.networking.Packet.PacketTypes;

public class GameServer extends Thread {

    private ArrayList<ServerSideHandler> serverSideHandlers;
    private VillainHandler villainHandler;
    private MapHandler mapHandler;
    private TimeHandler timeHandler;
    private DatagramSocket socket;
    Isten isten;
    boolean isInitialized = false;

    //Just values, not references for the actual players
    //When changing something in one of the connectedPlayers it won't change anything on the actual players in updatable
    private List<PlayerMP> connectedPlayers = new ArrayList<>();
    public GameServer(Isten isten) {
        this.isten = isten;
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
        isInitialized = true;

        serverSideHandlers = new ArrayList<ServerSideHandler>();
        mapHandler = new MapHandler();
        timeHandler = new TimeHandler();
        //villainHandler = new VillainHandler();

        serverSideHandlers.add(mapHandler);
        serverSideHandlers.add(timeHandler);
        //serverSideHandlers.add(villainHandler);

        for(ServerSideHandler serverSideHandler: serverSideHandlers) {
            serverSideHandler.create(this);
            serverSideHandler.setInitialized(true);
        }
    }

    public void updateServer(Isten isten, double deltaTime) {
        for(ServerSideHandler serverSideHandler: serverSideHandlers) {
            serverSideHandler.update(isten, deltaTime);
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
        }
    }

    //handle Animation Packet
    private void handleAnimation(Packet03Animation packet) {
        packet.writeData(this);
    }

    //handle Login Packet
    private void handleLogin(Packet00Login packet, InetAddress address, int port) {
        PlayerMP player = null;
        player = new PlayerMP(packet.getUsername(), address, port);
        this.addConnection(player,((Packet00Login)packet));

        //Handle creation of villains - when player joins, the server generated villains are generated on client as well
        handlePlayerJoinedData(player);
    }

    private void handlePlayerJoinedData(PlayerMP player) {
        mapHandler.sendDataToClient(player);
        //villainHandler.sendDataToClient(player);
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
                packet = new Packet00Login(player.getUsername(), 0, 0);
                sendData(packet.getData(), p.ipAddress, p.port);

                //Send position as well, so that the players spawn at their current posi
                //Send data to the new player, that the already connected player exists
                int index = isten.getPlayerMPIndex(p.getUsername());
                Vec2 pos = ((PlayerMP)isten.getUpdatable(index)).getPlayerCollider().getPosition();
                packet = new Packet00Login(p.getUsername(), pos.x, pos.y);
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
}
