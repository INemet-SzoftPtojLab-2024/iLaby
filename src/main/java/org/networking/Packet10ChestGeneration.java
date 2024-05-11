package main.java.org.networking;

import main.java.org.linalg.Vec2;

public class Packet10ChestGeneration extends Packet {

    private int wallLocation;
    private Vec2 pos = new Vec2();
    private int chestType;
    private int idx;

    public Packet10ChestGeneration(byte[] data) {
        super(10);
        String[] dataArray = readData(data).split(",");

        this.pos.x = Float.parseFloat(dataArray[1]);
        this.pos.y = Float.parseFloat(dataArray[2]);
        this.chestType = Integer.parseInt(dataArray[3]);
        this.idx = Integer.parseInt(dataArray[4]);
    }

    public Packet10ChestGeneration(int wallLocation, Vec2 pos, int chestType, int index) {
        super(10);
        this.wallLocation = wallLocation;
        this.pos = pos;
        this.chestType = chestType;
        this.idx = index;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) { server.sendDataToAllClients(getData()); }

    @Override
    public byte[] getData() { return ("10" + wallLocation + "," + pos.x + "," + pos.y + "," + chestType+ "," + idx).getBytes(); }

    public int getWallLocation() { return wallLocation; }

    public Vec2 getPos() { return pos; }

    public int getChestType() { return chestType; }
    public int getIdx(){
        return  idx;
    }

}
