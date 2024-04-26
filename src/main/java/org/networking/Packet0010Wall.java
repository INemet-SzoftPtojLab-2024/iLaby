package main.java.org.networking;

public class Packet0010Wall extends Packet {

    float posX;
    float posY;
    float scaleX;
    float scaleY;
    String imagePath;

    public Packet0010Wall(byte[] data) {
        super(20);
        String[] dataArray = readData(data).split(",");
        this.posX = Float.parseFloat(dataArray[0]);
        this.posY = Float.parseFloat(dataArray[1]);
        this.scaleX = Float.parseFloat(dataArray[2]);
        this.scaleY = Float.parseFloat(dataArray[3]);
        this.imagePath = dataArray[4];
    }

    public Packet0010Wall(float posX, float posY, float scaleX, float scaleY, String imagePath) {
        super(20);
        this.posX = posX;
        this.posY = posY;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.imagePath = imagePath;
    }

    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    public void writeData(GameServer server) {
        if(server != null) server.sendDataToAllClients(getData());
    }

    public byte[] getData() {
        return ("20" + posX + "," +posY + "," + scaleX + "," + scaleY + "," + imagePath).getBytes();
    }

    public float getPosX () {
        return posX;
    }

    public float getPosY () {
        return posY;
    }
    public float getScaleX () {
        return scaleX;
    }

    public float getScaleY () {
        return scaleY;
    }
    public String getImagePath() {
        return imagePath;
    }



}

