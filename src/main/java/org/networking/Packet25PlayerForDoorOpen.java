package main.java.org.networking;

public class Packet25PlayerForDoorOpen extends Packet {
    private String username;
    public Packet25PlayerForDoorOpen(byte[] data) {
        super(25);
        String dataString = readData(data);
        this.username = dataString;
    }

    public Packet25PlayerForDoorOpen(String username) {
        super(25);
        this.username = username;
    }

    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public byte[] getData() {
        return ("25" + username).getBytes();

    }

    public String getUsername() {
        return username;
    }

}

