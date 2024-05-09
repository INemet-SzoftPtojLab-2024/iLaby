package main.java.org.networking;

public class Packet26InGasRoom extends Packet {
    private String username;
    private boolean isInGasRoom;

    public Packet26InGasRoom(byte[] data) {
        super(26);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.isInGasRoom = Boolean.parseBoolean(dataArray[1]);
    }

    public Packet26InGasRoom(String username, boolean isInGasRoom) {
        super(26);
        this.username = username;
        this.isInGasRoom = isInGasRoom;
    }

    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public byte[] getData() {
        return ("26" + username + "," + isInGasRoom).getBytes();

    }

    public String getUsername() {
        return username;
    }

    public boolean isInGasRoom() {
        return isInGasRoom;
    }
}

