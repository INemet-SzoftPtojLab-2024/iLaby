package main.java.org.networking;

public class Packet24DoorOpen extends Packet {
    private float x,y;
    private boolean isSolid;
    public Packet24DoorOpen(byte[] data) {
        super(24);
        String[] dataArray = readData(data).split(",");
        this.x = Float.parseFloat(dataArray[0]);
        this.y = Float.parseFloat(dataArray[1]);
        this.isSolid = Boolean.parseBoolean(dataArray[2]);
    }

    public Packet24DoorOpen(float x, float y, boolean isSolid) {
        super(24);
        this.x = x;
        this.y = y;
        this.isSolid = isSolid;
    }

    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public byte[] getData() {
        return ("24" + this.x + "," + this.y + "," + this.isSolid).getBytes();

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    public boolean isSolid() {
        return isSolid;
    }

}
