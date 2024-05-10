package main.java.org.networking;

public class Packet11ChestOpened extends Packet {

    private int chestIndex;
    private int itemIndex1;
    private int itemIndex2;
    public Packet11ChestOpened(int chestIndex, int itemIndex1, int itemIndex2) {
        super(11);
        this.chestIndex = chestIndex;
        this.itemIndex1 = itemIndex1;
        this.itemIndex2 = itemIndex2;
    }
    public Packet11ChestOpened(byte[] data) {
        super(11);
        String[] dataArray = readData(data).split(",");
        chestIndex = Integer.parseInt(dataArray[0]);
        itemIndex1 = Integer.parseInt(dataArray[1]);
        itemIndex2 = Integer.parseInt(dataArray[2]);

    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) { server.sendDataToAllClients(getData()); }

    @Override
    public byte[] getData() { return ("11" + chestIndex + "," + itemIndex1 + "," + itemIndex2).getBytes(); }

    public int getChestIndex() { return chestIndex; }

    public int getItemIndex1() {
        return itemIndex1;
    }

    public int getItemIndex2() {
        return itemIndex2;
    }
}
