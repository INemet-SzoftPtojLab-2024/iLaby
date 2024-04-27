package main.java.org.networking;

public class Packet06VillainMove extends Packet {

    private String villainName;
    private float x, y;

    public Packet06VillainMove(byte[] data) {
        super(06);
        String[] dataArray = readData(data).split(",");
        this.villainName = dataArray[0];
        this.x = Float.parseFloat(dataArray[1]);
        this.y = Float.parseFloat(dataArray[2]);

    }

    public Packet06VillainMove(String villainName, float x, float y) {
        super(06);
        this.villainName = villainName;
        this.x = x;
        this.y = y;

    }

    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public byte[] getData() {
        return ("06" + this.villainName + "," + this.x + "," + this.y).getBytes();

    }

    public String getVillainName() {
        return villainName;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }


}
