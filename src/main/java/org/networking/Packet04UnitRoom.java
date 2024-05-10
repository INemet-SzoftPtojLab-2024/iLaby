package main.java.org.networking;

import java.util.Arrays;

public class Packet04UnitRoom extends Packet {

    private float x;
    private float y;
    private int type;


    public Packet04UnitRoom(byte[] data) {
        super(04);
        String[] dataArray = readData(data).split(",");
        this.x = Float.parseFloat(dataArray[0]);;
        this.y = Float.parseFloat(dataArray[1]);
        this.type = Integer.parseInt(dataArray[2]);
    }


    public Packet04UnitRoom(float x, float y, int type) {
        super(04);
        this.x = x;
        this.y = y;
        this.type = type;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return ("04" + this.x + "," + this.y + "," + this.type
                + "," + type + "," + type).getBytes();
    }

    public int getType() {
        return type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}

