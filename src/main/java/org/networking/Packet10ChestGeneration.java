package main.java.org.networking;

import main.java.org.items.Chest;
import main.java.org.linalg.Vec2;

public class Packet10ChestGeneration extends Packet {

    private int heading;
    private Vec2 pos = new Vec2();
    private int chestType;
    private int idx;

    public Packet10ChestGeneration(byte[] data) {
        super(10);
        String[] dataArray = readData(data).split(",");

        this.heading = Integer.parseInt(dataArray[0]);
        this.pos.x = Float.parseFloat(dataArray[1]);
        this.pos.y = Float.parseFloat(dataArray[2]);
        this.chestType = Integer.parseInt(dataArray[3]);
        this.idx = Integer.parseInt(dataArray[4]);
    }

    public Packet10ChestGeneration(int heading, Vec2 pos, int chestType) {
        super(10);
        this.heading = heading;
        this.pos = pos;
        this.chestType = chestType;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) { server.sendDataToAllClients(getData()); }

    @Override
    public byte[] getData() { return ("10" + heading + "," + pos.x + "," + pos.y + "," + chestType+ "," + idx).getBytes(); }

    public int getHeading() { return heading; }

    public Vec2 getPos() { return pos; }

    public int getChestType() { return chestType; }
    public int getIdx(){
        return  idx;
    }

}
