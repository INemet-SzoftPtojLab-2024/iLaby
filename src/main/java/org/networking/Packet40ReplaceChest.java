
package main.java.org.networking;

import main.java.org.linalg.Vec2;

public class Packet40ReplaceChest extends Packet {

    private int wallLocation;
    private Vec2 pos = new Vec2();
    private int idx;

    public Packet40ReplaceChest(byte[] data) {
        super(40);
        String[] dataArray = readData(data).split(",");
        this.wallLocation = Integer.parseInt(dataArray[0]);
        this.pos.x = Float.parseFloat(dataArray[1]);
        this.pos.y = Float.parseFloat(dataArray[2]);
        this.idx = Integer.parseInt(dataArray[3]);
    }

    public Packet40ReplaceChest(int wallLocation, Vec2 pos, int index) {
        super(40);
        this.wallLocation = wallLocation;
        this.pos = pos;
        this.idx = index;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) { server.sendDataToAllClients(getData()); }

    @Override
    public byte[] getData() { return ("40" + wallLocation + "," + pos.x + "," + pos.y + "," + idx).getBytes(); }

    public int getWallLocation() { return wallLocation; }

    public Vec2 getPos() { return pos; }

    public int getIdx(){
        return  idx;
    }

}
