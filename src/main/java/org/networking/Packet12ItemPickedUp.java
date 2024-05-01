package main.java.org.networking;

public class Packet12ItemPickedUp extends Packet{

    private int itemIndex;
    public Packet12ItemPickedUp(byte[] data) {
        super(12);
        itemIndex = Integer.parseInt(readData(data));
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) { server.sendDataToAllClients(getData()); }

    @Override
    public byte[] getData() { return ("12" + itemIndex).getBytes(); }

    public int getItemIndex() { return itemIndex; }
}
