package main.java.org.networking;

public class Packet04Map extends Packet {
    private int[] mapData;
    public Packet04Map(byte[] data) {
        super(04);
        String[] dataArray = readData(data).split(",");
    }

    public Packet04Map(int[] mapData) {
        super(04);
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return ("04").getBytes();
    }
}

