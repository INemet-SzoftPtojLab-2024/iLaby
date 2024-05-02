package main.java.org.networking;

public class Packet21Death extends Packet {

    String username;

    public Packet21Death(byte[] data) {
        super(21);
        String dataString = readData(data);
        this.username = dataString;
    }

    public Packet21Death(String username) {
        super(21);
        this.username = username;
    }

    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    public void writeData(GameServer server) {
        if(server != null) server.sendDataToAllClients(getData());
    }

    public byte[] getData() {
        return ("21" + username).getBytes();
    }

   public String getUsername() {
        return username;
   }
}

