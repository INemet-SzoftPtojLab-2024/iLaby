package main.java.org.networking;

import main.java.org.linalg.Vec2;

public class Packet17Camembert extends Packet {

    private float x;
    private float y;
    private int itemIndex;
    private String username;
    public Packet17Camembert(float x, float y, int itemIndex, String username) {
        super(17);
        this.x = x;
        this.y = y;
        this.itemIndex = itemIndex;
        this.username = username;
    }

    public Packet17Camembert(byte[] data) {
        super(17);
        String[] dataArray = readData(data).split(",");

        this.x = Float.parseFloat(dataArray[0]);
        this.y = Float.parseFloat(dataArray[1]);
        this.itemIndex = Integer.parseInt(dataArray[2]);
        this.username = dataArray[3];
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) { server.sendDataToAllClients(getData()); }

    @Override
    public byte[] getData() { return ("17" + x + "," + y + "," + itemIndex + "," + username).getBytes(); }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public int getItemIndex() {
        return itemIndex;
    }

    public String getUsername() {
        return username;
    }

}
