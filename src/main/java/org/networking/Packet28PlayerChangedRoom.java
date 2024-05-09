package main.java.org.networking;

public class Packet28PlayerChangedRoom extends Packet {
    private String username;

    public Packet28PlayerChangedRoom(byte[] data) {
        super(28);
        String dataString = readData(data);
        this.username = dataString;
    }

    public Packet28PlayerChangedRoom(String username) {
        super(28);
        this.username = username;
    }

    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public byte[] getData() {
        return ("28" + username).getBytes();

    }

    public String getUsername() {
        return username;
    }

}

