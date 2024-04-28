package main.java.org.networking;

import lombok.Getter;
import lombok.Setter;
import main.java.org.items.Chest;
import main.java.org.items.Item;
import main.java.org.linalg.Vec2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class Packet10ChestGeneration extends Packet {
    int heading;
    Vec2 pos = new Vec2();
    int chestType;

    public Packet10ChestGeneration(byte[] data) {
        super(10);
        String[] dataArray = readData(data).split(",");

        this.heading = Integer.parseInt(dataArray[0]);
        this.pos.x = Float.parseFloat(dataArray[1]);
        this.pos.y = Float.parseFloat(dataArray[2]);
        this.chestType = Integer.parseInt(dataArray[3]);
    }

    public Packet10ChestGeneration(int heading, Vec2 pos, int chestType) {
        super(10);
        this.heading = heading;
        this.pos = pos;
        this.chestType = chestType;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) { server.sendDataToAllClients(getData()); }

    @Override
    public byte[] getData() { return ("10" + heading + "," + pos.x + "," + pos.y + "," + chestType).getBytes(); }

}
