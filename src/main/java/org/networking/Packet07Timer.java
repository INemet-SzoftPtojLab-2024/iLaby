package main.java.org.networking;

public class Packet07Timer extends Packet {

    double timeRemaining;
    public Packet07Timer(byte[] data) {
        super(07);
        String dataArray = readData(data);
        this.timeRemaining = Double.parseDouble(dataArray);
    }

    public Packet07Timer(double timeRemaining) {
        super(07);
        this.timeRemaining = timeRemaining;
    }

    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    public void writeData(GameServer server) {
        if(server != null) server.sendDataToAllClients(getData());
    }

    public byte[] getData() {
        return ("07" + this.timeRemaining).getBytes();
    }

   public double getTimeRemaining() {
        return timeRemaining;
   }

}
