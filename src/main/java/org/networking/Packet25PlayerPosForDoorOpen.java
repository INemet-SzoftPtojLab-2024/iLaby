package main.java.org.networking;

public class Packet25PlayerPosForDoorOpen extends Packet {
    private float x,y;

    public Packet25PlayerPosForDoorOpen(byte[] data) {
        super(25);
        String[] dataArray = readData(data).split(",");
        this.x = Float.parseFloat(dataArray[0]);
        this.y = Float.parseFloat(dataArray[1]);
    }

    public Packet25PlayerPosForDoorOpen(float x, float y) {
        super(25);
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
        return ("25" + this.x + "," + this.y).getBytes();

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}

