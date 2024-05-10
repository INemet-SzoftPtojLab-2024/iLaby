package main.java.org.networking;

public class Packet41IsPlayerInVillainRoom extends Packet {
    private boolean isPlayerInVillainRoom;
    private String username;

    public Packet41IsPlayerInVillainRoom(byte[] data) {
        super(41);
        String[] dataArray = readData(data).split(",");
        this.isPlayerInVillainRoom = Boolean.parseBoolean(dataArray[0]);
        this.username = dataArray[1];
    }

    public Packet41IsPlayerInVillainRoom(String username, boolean isPlayerInVillainRoom) {
        super(41);
        this.username = username;
        this.isPlayerInVillainRoom = isPlayerInVillainRoom;
    }

    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public byte[] getData() {
        return ("41" + isPlayerInVillainRoom + "," + username).getBytes();

    }

    public boolean getIsPlayerInVillainRoom() {
        return isPlayerInVillainRoom;
    }
    public String getUsername() {
        return username;
    }

}

