package main.java.org.networking;

import main.java.org.linalg.Vec2;

public class Packet17Camembert extends Packet {

    private float x;
    private float y;

    public Packet17Camembert(float x, float y) {
        super(17);
        this.x = x;
        this.y = y;
    }

    public Packet17Camembert(byte[] data) {
        super(17);
        String[] dataArray = readData(data).split(",");

        this.x = Float.parseFloat(dataArray[0]);
        this.y = Float.parseFloat(dataArray[1]);
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) { server.sendDataToAllClients(getData()); }

    @Override
    public byte[] getData() { return ("17" + x + "," + y).getBytes(); }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

}
