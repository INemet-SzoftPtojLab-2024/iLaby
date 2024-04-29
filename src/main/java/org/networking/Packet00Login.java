package main.java.org.networking;

public class Packet00Login extends Packet {

    private String username;
    private float x,y;
    private int skinID;
    public Packet00Login(byte[] data) {
        super(00);
        String[] dataArray = readData(data).split(",");
        System.out.println(readData(data));
        this.username = dataArray[0];
        this.x = Float.parseFloat(dataArray[1]);
        this.y = Float.parseFloat(dataArray[2]);
        this.skinID = Integer.parseInt(dataArray[3]);
    }

    public Packet00Login(String username, float x, float y, int skinID) {
        super(00);
        this.username = username;
        this.x = x;
        this.y = y;
        this.skinID = skinID;

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
        return ("00" + this.username + "," + this.x + "," + this.y + "," + this.skinID).getBytes();
    }

    public String getUsername() {
        return username;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    public int getSkinID() {
        return skinID;
    }
}
