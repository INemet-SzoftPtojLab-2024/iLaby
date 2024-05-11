package main.java.org.networking;

public class Packet42ItemsDropped extends Packet {
    private String username;

    public Packet42ItemsDropped(byte[] data) {
        super(42);
        String dataArray = readData(data);
        this.username = dataArray;
    }

    public Packet42ItemsDropped(String username) {
        super(42);
        this.username = username;
    }

    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public byte[] getData() {
        return ("42" + username).getBytes();

    }
    public String getUsername() {
        return username;
    }

}
