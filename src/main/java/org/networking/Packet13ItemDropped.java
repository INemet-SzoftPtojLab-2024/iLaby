package main.java.org.networking;

import main.java.org.linalg.Vec2;

public class Packet13ItemDropped extends Packet {

    private int itemIndex;
    private Vec2 pos = new Vec2();
    public Packet13ItemDropped(byte[] data) {
        super(13);
        String[] dataArray = readData(data).split(",");

        this.itemIndex = Integer.parseInt(dataArray[0]);
        this.pos.x = Float.parseFloat(dataArray[1]);
        this.pos.y = Float.parseFloat(dataArray[2]);
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) { server.sendDataToAllClients(getData()); }

    @Override
    public byte[] getData() { return ("13" + itemIndex + "," + pos.x + "," + pos.y).getBytes(); }

    public int getItemIndex() { return itemIndex; }

    public Vec2 getPos() { return pos; }
}
