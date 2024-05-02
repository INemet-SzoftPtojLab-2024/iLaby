package main.java.org.networking;

public class Packet03Animation extends Packet {

    private String username;
    //private int prevImage;
    private int activeImage;
    public Packet03Animation(byte[] data) {
        super(03);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.activeImage = Integer.parseInt(dataArray[1]);
        //this.prevImage = Integer.parseInt(dataArray[2]);

    }

    public Packet03Animation(String username, int activeImage) {
        super(03);
        this.activeImage = activeImage;
        this.username = username;
        //this.prevImage = prevImage;
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
        return ("03" + this.username + "," + this.activeImage).getBytes();
    }

    public String getUsername() {
        return username;
    }
    public int getActiveImage() {
        return activeImage;
    }
}
