package main.java.org.networking;

import main.java.org.game.Isten;

import javax.xml.crypto.Data;
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
                System.out.println("["+address.getHostAddress() + ":" + port + "] " + ((Packet00Login)packet).getUsername() + " has joined the game...");
                PlayerMP player = null;
                player = new PlayerMP(((Packet00Login)packet).getUsername(), address, port);
                if(player != null) { isten.addUpdatable(player); }
                break;
            case DISCONNECT:
                break;
        }
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
