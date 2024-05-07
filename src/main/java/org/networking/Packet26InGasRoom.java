package main.java.org.networking;

public class Packet26InGasRoom extends Packet {
    private float x,y;
    private boolean isInGasRoom;

    public Packet26InGasRoom(byte[] data) {
        super(26);
        String[] dataArray = readData(data).split(",");
        this.x = Float.parseFloat(dataArray[0]);
        this.y = Float.parseFloat(dataArray[1]);
        this.isInGasRoom = Boolean.parseBoolean(dataArray[2]);
    }

    public Packet26InGasRoom(float x, float y, boolean isInGasRoom) {
        super(26);
        this.x = x;
        this.y = y;
        this.isInGasRoom = isInGasRoom;
    }

    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public byte[] getData() {
        return ("26" + this.x + "," + this.y + "," + isInGasRoom).getBytes();

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isInGasRoom() {
        return isInGasRoom;
    }
}

