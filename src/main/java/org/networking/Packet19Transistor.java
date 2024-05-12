package main.java.org.networking;

import main.java.org.linalg.Vec2;

public class Packet19Transistor extends Packet {
    private float x;
    private float y;
    private int itemIndex;

    public Packet19Transistor(byte[] data) {
        super(17);
        String[] dataArray = readData(data).split(",");

        this.x = Float.parseFloat(dataArray[0]);
        this.y = Float.parseFloat(dataArray[1]);
        this.itemIndex = Integer.parseInt(dataArray[2]);
    }

    public Packet19Transistor(Vec2 position, int itemIndex) {
        super(17);
        this.x = position.x;
        this.y = position.y;
        this.itemIndex = itemIndex;
    }

    @Override
    public void writeData(GameClient client) { client.sendData(getData()); }

    @Override
    public void writeData(GameServer server) { server.sendDataToAllClients(getData()); }

    @Override
    public byte[] getData() { return ("19" + x + "," + y + "," + itemIndex).getBytes(); }

    public float getX() { return x; }

    public float getY() { return y; }

    public int getItemIndex() { return itemIndex; }
}
