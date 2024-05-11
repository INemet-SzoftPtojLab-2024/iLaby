package main.java.org.networking;

public class Packet16Sorospohar extends Packet {

    private int itemIndex;
    private float capacity;

    public Packet16Sorospohar(byte[] data) {
        super(16);
        String[] dataArray = readData(data).split(",");

        this.itemIndex = Integer.parseInt(dataArray[0]);
        this.capacity = Float.parseFloat(dataArray[1]);
    }

    public Packet16Sorospohar(int itemIndex, float capacity) {
        super(16);

        this.itemIndex = itemIndex;
        this.capacity = capacity;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) { server.sendDataToAllClients(getData()); }

    @Override
    public byte[] getData() { return ("16" + itemIndex + "," + capacity).getBytes(); }

    public int getItemIndex() { return itemIndex; }

    public float getCapacity() { return capacity; }
}