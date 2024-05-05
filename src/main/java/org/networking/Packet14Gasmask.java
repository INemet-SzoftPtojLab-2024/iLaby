package main.java.org.networking;

public class Packet14Gasmask extends Packet {

    private int itemIndex;
    private float capacity;

    public Packet14Gasmask(byte[] data) {
        super(14);
        String[] dataArray = readData(data).split(",");

        this.itemIndex = Integer.parseInt(dataArray[0]);
        this.capacity = Float.parseFloat(dataArray[1]);
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) { server.sendDataToAllClients(getData()); }

    @Override
    public byte[] getData() { return ("14" + itemIndex + "," + capacity).getBytes(); }

    public int getItemIndex() { return itemIndex; }

    public float getCapacity() { return capacity; }
}
