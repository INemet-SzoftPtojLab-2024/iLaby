package main.java.org.networking;

public class Packet11ChestOpened extends Packet {
    int chestIndex;
    public Packet11ChestOpened(byte[] data) {
        super(11);
        chestIndex = Integer.parseInt(readData(data));
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) { server.sendDataToAllClients(getData()); }

    @Override
    public byte[] getData() { return ("11" + chestIndex).getBytes(); }
}
