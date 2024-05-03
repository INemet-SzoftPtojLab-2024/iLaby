package main.java.org.networking;


import main.java.org.linalg.Vec2;

public class Packet22EdgePieceChanged extends Packet {

    private float x, y;
    private boolean isDoor;

    public Packet22EdgePieceChanged(byte[] data) {
        super(22);
        String[] dataArray = readData(data).split(",");
        this.x = Float.parseFloat(dataArray[0]);
        this.y = Float.parseFloat(dataArray[1]);
        this.isDoor = Boolean.parseBoolean(dataArray[2]);
    }

    public Packet22EdgePieceChanged(float x, float y, boolean isDoor) {
        super(22);
        this.x = x;
        this.y = y;
        this.isDoor = isDoor;
    }

    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public byte[] getData() {
        return ("22" + x + "," + y + "," + isDoor).getBytes();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    public boolean isDoor() {
        return isDoor;
    }

}

