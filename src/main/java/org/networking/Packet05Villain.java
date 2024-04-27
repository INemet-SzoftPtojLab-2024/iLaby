package main.java.org.networking;

import main.java.org.linalg.Vec2;

public class Packet05Villain extends Packet {

    private String villainName;
    private Vec2 position;
    private String imagePath;

    private int random1, random2;
    public Packet05Villain(byte[] data) {
        super(05);
        String[] dataArray = readData(data).split(",");
        this.villainName = dataArray[0];
        this.position = new Vec2(Float.parseFloat(dataArray[1]), Float.parseFloat(dataArray[2]));
        this.imagePath = dataArray[3];
        this.random1 = Integer.parseInt(dataArray[4]);
        this.random2 = Integer.parseInt(dataArray[5]);
    }

    public Packet05Villain(String villainName, Vec2 position, String imagePath, int random1, int random2) {
        super(05);
        this.villainName = villainName;
        this.position = position;
        this.imagePath = imagePath;
        this.random1 = random1;
        this.random2 = random2;
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
        return ("05" + this.villainName + "," + this.position.x + "," + this.position.y + "," + this.imagePath
        + "," + this.random1 + "," + this.random2).getBytes();
    }

    public String getVillainName() {
        return villainName;
    }
    public Vec2 getPosition() {
        return position;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getRandom1() {
        return random1;
    }
    public int getRandom2() {
        return random2;
    }
}
