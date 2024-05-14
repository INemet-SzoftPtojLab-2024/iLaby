package main.java.org.networking;

import main.java.org.linalg.Vec2;

public class Packet18Rongy extends Packet {

    private float x;
    private float y;
    private int itemIndex;
    private String username;
    private double impactTime;
    public Packet18Rongy(float x, float y, int itemIndex, String username, double impactTime) {
        super(18);
        this.x = x;
        this.y = y;
        this.itemIndex = itemIndex;
        this.username = username;
        this.impactTime = impactTime;
    }

    public Packet18Rongy(byte[] data) {
        super(18);
        String[] dataArray = readData(data).split(",");

        this.x = Float.parseFloat(dataArray[0]);
        this.y = Float.parseFloat(dataArray[1]);
        this.itemIndex = Integer.parseInt(dataArray[2]);
        this.username = dataArray[3];
        this.impactTime = Double.parseDouble(dataArray[4]);
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) { server.sendDataToAllClients(getData()); }

    @Override
    public byte[] getData() { return ("18" + x + "," + y + "," + itemIndex + "," + username + "," + impactTime).getBytes(); }

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
    public double getImpactTime() {return impactTime; }

}
