package main.java.org.networking;

public class Packet27VillainIsInGasRoom extends Packet {
    private String villainName;
    private boolean isInGasRoom;

    public Packet27VillainIsInGasRoom(byte[] data) {
        super(27);
        String[] dataArray = readData(data).split(",");
        this.villainName = dataArray[0];
        this.isInGasRoom = Boolean.parseBoolean(dataArray[1]);
    }

    public Packet27VillainIsInGasRoom(String villainName, boolean isInGasRoom) {
        super(27);
        this.villainName = villainName;
        this.isInGasRoom = isInGasRoom;
    }

    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public byte[] getData() {
        return ("27" + villainName + "," + isInGasRoom).getBytes();

    }

    public String getVillainName() {
        return villainName;
    }

    public boolean isInGasRoom() {
        return isInGasRoom;
    }
}