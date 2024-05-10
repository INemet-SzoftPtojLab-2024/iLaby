package main.java.org.networking;

public class Packet15Tvsz extends Packet {
    private int itemIndex;
    private int charges;
    private boolean shouldUseCharge;

    public Packet15Tvsz(int itemIndex, int charges) {
        super(15);
        this.itemIndex = itemIndex;
        this.charges = charges;
    }

    public Packet15Tvsz(byte[] data) {
        super(15);
        String[] dataArray = readData(data).split(",");

        this.itemIndex = Integer.parseInt(dataArray[0]);
        this.charges = Integer.parseInt(dataArray[1]);
        //this.shouldUseCharge = Boolean.parseBoolean(dataArray[2]);

        //System.out.println("ch: " + charges);
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
        return ("15" + itemIndex + "," + charges).getBytes();
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public int getCharges() {
        return charges;
    }

    public boolean getShouldUseCharge() {
        return shouldUseCharge;
    }

}
