package main.java.org.networking;

import main.java.org.linalg.Vec2;

public class Packet13ItemDropped extends Packet {
    private int itemIndex;
    private Vec2 pos = new Vec2();
    private String username;
    private int selectedSlot;
    private boolean replaced;
    public Packet13ItemDropped(int itemIndex, Vec2 pos, String username, int selectedSlot, boolean replaced) {
        super(13);
        this.itemIndex = itemIndex;
        this.pos = pos;
        this.username = username;
        this.selectedSlot = selectedSlot;
        this.replaced = replaced;
    }
    public Packet13ItemDropped(byte[] data) {
        super(13);
        String[] dataArray = readData(data).split(",");

        this.itemIndex = Integer.parseInt(dataArray[0]);
        this.pos.x = Float.parseFloat(dataArray[1]);
        this.pos.y = Float.parseFloat(dataArray[2]);
        this.username = dataArray[3];
        this.selectedSlot = Integer.parseInt(dataArray[4]);
        this.replaced = Boolean.parseBoolean(dataArray[5]);
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) { server.sendDataToAllClients(getData()); }

    @Override
    public byte[] getData() { return ("13" + itemIndex + "," + pos.x + "," + pos.y
    + "," + username + "," + selectedSlot + "," + replaced).getBytes(); }

    public int getItemIndex() { return itemIndex; }

    public Vec2 getPos() { return pos; }
    public String getUsername() {
        return username;
    }
    public int getSelectedSlot() {
        return selectedSlot;
    }

    public boolean getReplaced() {
        return replaced;
    }
}
