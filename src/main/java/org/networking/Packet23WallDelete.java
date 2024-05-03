package main.java.org.networking;

public class Packet23WallDelete extends Packet {
    private float x,y;

    public Packet23WallDelete(byte[] data) {
        super(23);
        String[] dataArray = readData(data).split(",");
        this.x = Float.parseFloat(dataArray[0]);
        this.y = Float.parseFloat(dataArray[1]);
    }

    public Packet23WallDelete(float x, float y) {
        super(23);
        this.x = x;
        this.y = y;
    }

    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public byte[] getData() {
        return ("23" + this.x + "," + this.y).getBytes();

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}
