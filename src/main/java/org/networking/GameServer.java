package main.java.org.networking;

import main.java.org.game.Isten;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import main.java.org.networking.Packet.PacketTypes;

public class GameServer extends Thread {

    private DatagramSocket socket;
    Isten isten;
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
        PacketTypes type = Packet.lookupPacket(message.substring(0,2));
        Packet packet = null;
        switch(type) {
            default:
                break;
            case INVALID:
                break;
            case LOGIN:
                packet = new Packet00Login(data);
                System.out.println("["+address.getHostAddress() + ":" + port + "] " + ((Packet00Login)packet).getUsername() + " has connected...");
                PlayerMP player = null;
                player = new PlayerMP(((Packet00Login)packet).getUsername(), address, port);
                this.addConnection(player,((Packet00Login)packet));
                if(player != null) {
                    this.connectedPlayers.add(player);
                    isten.addUpdatable(player);
                }
                break;
            case DISCONNECT:
                break;
        }
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
                sendData(packet.getData(),p.ipAddress,p.port);
                packet = new Packet00Login(p.getUsername());
                sendData(packet.getData(),player.ipAddress, player.port);
            }
        }
        if(!alreadyConnected) {
            this.connectedPlayers.add(player);
            //packet.writeData(this);
        }
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendDataToAllClients(byte[] data) {
        for(PlayerMP p: connectedPlayers) {
            sendData(data, p.ipAddress, p.port);
        }
    }
}
