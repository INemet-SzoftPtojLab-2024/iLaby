package main.java.org.networking;

import main.java.org.game.Isten;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

public class GameServerTester {

    Isten mockIsten = mock(Isten.class);

    @Test
    public void testRun() {
        GameServer gameServer = new GameServer(mockIsten);

        assertDoesNotThrow(() -> {
            gameServer.start();
            gameServer.interrupt();
        });
    }

    @Test
    public void testParsePacket() throws UnknownHostException {
        GameServer gameServer = new GameServer(mockIsten);
        gameServer.start();

        InetAddress testAddress = InetAddress.getByName("127.0.0.1");
        int testPort = 12345;

        byte[] data00 = ("00" + "username" + "," +1 + "," + 1 + "," + 1).getBytes();
        byte[] data01 = ("01" + "username").getBytes();
        byte[] data02 = ("02" + "username" + "," + 1 + "," + 1).getBytes();
        byte[] data03 = ("03" + "username" + "," + 1).getBytes();
        byte[] data11 = ("11" + 1 + "," + 1 + "," + 1).getBytes();
        byte[] data12 = ("12" + 1 + "," + "username" + "," + 1).getBytes();
        byte[] data13 = ("13" + 1 + "," + 1 + "," + 1 + "," + "username" + "," + 1 + "," + true).getBytes();
        byte[] data14 = ("14" + 1 + "," + 1).getBytes();
        byte[] data15 = ("15" + 1 + "," + 1).getBytes();
        byte[] data16 = ("16" + 1 + "," + 1).getBytes();
        byte[] data17 = ("17" + 1 + "," + 1 + "," + 1 + "," + "username").getBytes();
        byte[] data18 = ("18" + 1 + "," + 1 + "," + 1 + "," + "username" + "," + 1).getBytes();
        byte[] data19 = ("19" + 1 + "," + 1 + "," + 1).getBytes();
        byte[] data25 = ("25" + "username").getBytes();
        byte[] data40 =  ("40" + 1 + "," + 1 + "," + 1 + "," + 1).getBytes();
        byte[] data41 = ("41" + true + "," + "username").getBytes();
        byte[] data42 = ("42" + "username").getBytes();

        gameServer.parsePacket(("-1").getBytes(), testAddress, testPort);
        gameServer.parsePacket(data01, testAddress, testPort);
        gameServer.parsePacket(data02, testAddress, testPort);
        gameServer.parsePacket(data03, testAddress, testPort);
        gameServer.parsePacket(data11, testAddress, testPort);
        gameServer.parsePacket(data12, testAddress, testPort);
        gameServer.parsePacket(data13, testAddress, testPort);
        gameServer.parsePacket(data14, testAddress, testPort);
        gameServer.parsePacket(data41, testAddress, testPort);
        gameServer.parsePacket(data42, testAddress, testPort);

        Packet15Tvsz packet15Tvsz = new Packet15Tvsz(data15);
        gameServer.handleTvsz(packet15Tvsz);
        Packet16Sorospohar packet16Sorospohar = new Packet16Sorospohar(data16);
        gameServer.handleSorospohar(packet16Sorospohar);
        Packet19Transistor packet19Transistor = new Packet19Transistor(data19);
        gameServer.handleTransistor(packet19Transistor);
        Packet40ReplaceChest packet40ReplaceChest = new Packet40ReplaceChest(data40);
        gameServer.handleReplaceChest(packet40ReplaceChest);
    }
}
