package main.java.org.networking;

import java.util.ArrayList;
import java.util.List;

public class Packet50Fog extends Packet{

    private int[] data;//fogOfWarHelper indices and values interleaved

    public Packet50Fog(int[] data)
    {
        super(50);
        this.data=new int[data.length];
        for(int i=0;i<data.length;i++)
            this.data[i]=data[i];
    }

    public Packet50Fog(byte[] bytes)
    {
        super(50);
        String[] dataArray=readData(bytes).split(",");
        int count=Integer.parseInt(dataArray[0]);
        data=new int[count];
        for(int i=0;i<count;i++)
            data[i]=Integer.parseInt(dataArray[i+1]);
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
        StringBuilder sb=new StringBuilder();
        sb.append(50);
        sb.append(data.length).append(",");
        for(int i=0;i<data.length;i++)
            sb.append(data[i]).append(",");

        return sb.substring(0,sb.length()-1).getBytes();
    }

    public int[] getValues()
    {
        return data;
    }
}
