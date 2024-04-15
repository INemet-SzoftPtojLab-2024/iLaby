package main.java.org.networking;

import main.java.org.game.Isten;
import main.java.org.linalg.Vec2;

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

        //System.out.println("Parse packet client");
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
        }
    }

    private void handleAnimation(Packet03Animation packet) {
        int index = isten.getPlayerMPIndex(packet.getUsername());
        PlayerMP player = (PlayerMP)isten.getUpdatable(index);
        if(player == null || player.localPlayer) return;
        for(int i = 0; i < player.getPlayerImage().size(); i++) {
            player.getPlayerImage().get(i).setVisibility(false);
        }
        player.setActiveImage(packet.getActiveImage());
        player.getPlayerImage().get(packet.getActiveImage()).setVisibility(true);
    }

    private void handleLogin(Packet00Login packet, InetAddress address, int port) {
        PlayerMP player = null;
        Vec2 player_pos = new Vec2(packet.getX(), packet.getY());
        player = new PlayerMP(packet.getUsername(), address, port, player_pos);
        if(player != null) { isten.addUpdatable(player); }
    }
    private void handleMove(Packet02Move packet) {
        isten.movePlayer((packet.getUsername()), packet.getX(), packet.getY());
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
