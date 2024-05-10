package main.java.org.networking;

public class Packet12ItemPickedUp extends Packet{

    private int itemIndex;
    private String username;
    private int selectedSlot;

    public Packet12ItemPickedUp(int itemIndex, String username, int selectedSlot) {
        super(12);
        this.itemIndex = itemIndex;
        this.username = username;
        this.selectedSlot = selectedSlot;
    }

    public Packet12ItemPickedUp(byte[] data) {
        super(12);
        String[] dataArray = readData(data).split(",");
        itemIndex = Integer.parseInt(dataArray[0]);
        username = dataArray[1];
        selectedSlot = Integer.parseInt(dataArray[2]);
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) { server.sendDataToAllClients(getData()); }

    @Override
    public byte[] getData() { return ("12" + itemIndex + "," + username + "," + selectedSlot).getBytes(); }

    public int getItemIndex() { return itemIndex; }
    public String getUsername() {
        return username;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }
}
