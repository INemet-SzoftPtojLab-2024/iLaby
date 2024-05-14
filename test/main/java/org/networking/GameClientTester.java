package main.java.org.networking;

import main.java.org.game.Isten;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class GameClientTester {

    @Test
    public void testConstructor() {
        Isten isten = new Isten();
        String ipAddress = "127.0.0.1";

        GameClient gameClient = new GameClient(isten, ipAddress);
        assertNotNull(gameClient);
    }

    @Test
    public void testRun() {
        Isten isten = new Isten();
        String ipAddress = "127.0.0.1";
        GameClient gameClient = new GameClient(isten, ipAddress);

        assertDoesNotThrow(() -> {
            gameClient.start();
            gameClient.interrupt();
        });
    }

    @Test
    public void testParsePacket() throws UnknownHostException {
        Isten isten = new Isten();
        String ipAddress = "127.0.0.1";
        GameClient gameClient = new GameClient(isten, ipAddress);
        InetAddress testAddress = InetAddress.getByName("127.0.0.1");
        int testPort = 12345;

        byte[] data00 = ("00" + "username" + "," +1 + "," + 1 + "," + 1).getBytes();
        byte[] data01 = ("01" + "username").getBytes();
        byte[] data02 = ("02" + "username" + "," + 1 + "," + 1).getBytes();
        byte[] data03 = ("03" + "username" + "," + 1).getBytes();
        byte[] data04 = ("04" + 1 + "," + 1 + "," + 1).getBytes();
        byte[] data05 = ("05" + "villain" + "," + 1 + "," + 1 + "," + "imgpath").getBytes();
        byte[] data06 = ("06" + "villain" + "," + 1 + "," + 1).getBytes();
        byte[] data07 = ("07" + 1).getBytes();
        byte[] data10 = ("10" + 1 + "," + 1 + "," + 1 + "," + 1 + "," + 1).getBytes();
        byte[] data11 = ("11" + 1 + "," + 1 + "," + 1).getBytes();
        byte[] data12 = ("12" + 1 + "," + "username" + "," + 1).getBytes();
        byte[] data13 = ("13" + 1 + "," + 1 + "," + 1 + "," + "username" + "," + 1 + "," + true).getBytes();
        byte[] data14 = ("14" + 1 + "," + 1).getBytes();
        byte[] data15 = ("15" + 1 + "," + 1).getBytes();
        byte[] data16 = ("16" + 1 + "," + 1).getBytes();
        byte[] data17 = ("17" + 1 + "," + 1 + "," + 1 + "," + "username").getBytes();
        byte[] data18 = ("18" + 1 + "," + 1 + "," + 1 + "," + "username" + "," + 1).getBytes();
        byte[] data19 = ("19" + 1 + "," + 1 + "," + 1).getBytes();
        byte[] data20 = ("20" + 1 + "," + 1 + "," + 1 + "," + 1 + "," + true).getBytes();
        byte[] data21 = ("21" + "username").getBytes();
        byte[] data22 = ("22" + 1 + "," + 1 + "," + true).getBytes();
        byte[] data23 = ("23" + 1 + "," + 1).getBytes();
        byte[] data24 = ("24" + 1 + "," + 1 + "," + true).getBytes();
        byte[] data25 = ("25" + "username").getBytes();
        byte[] data26 = ("26" + "username" + "," + true).getBytes();
        byte[] data27 = ("27" + "villain" + "," + true).getBytes();
        byte[] data28 = ("28" + "username").getBytes();
        byte[] data40 =  ("40" + 1 + "," + 1 + "," + 1 + "," + 1).getBytes();
        byte[] data41 = ("41" + true + "," + "username").getBytes();
        byte[] data42 = ("42" + "username").getBytes();

        gameClient.parsePacket(("-1").getBytes(), testAddress, testPort);
        gameClient.parsePacket(data00, testAddress, testPort);
        gameClient.parsePacket(data01, testAddress, testPort);
        gameClient.parsePacket(data02, testAddress, testPort);
        gameClient.parsePacket(data03, testAddress, testPort);
        gameClient.parsePacket(data04, testAddress, testPort);
        gameClient.parsePacket(data05, testAddress, testPort);
        gameClient.parsePacket(data06, testAddress, testPort);
        gameClient.parsePacket(data07, testAddress, testPort);
        gameClient.parsePacket(data10, testAddress, testPort);
        gameClient.parsePacket(data11, testAddress, testPort);
        gameClient.parsePacket(data12, testAddress, testPort);
        gameClient.parsePacket(data13, testAddress, testPort);
        gameClient.parsePacket(data14, testAddress, testPort);
        gameClient.parsePacket(data17, testAddress, testPort);
        gameClient.parsePacket(data20, testAddress, testPort);
        gameClient.parsePacket(data21, testAddress, testPort);
        gameClient.parsePacket(data22, testAddress, testPort);
        gameClient.parsePacket(data23, testAddress, testPort);
        gameClient.parsePacket(data24, testAddress, testPort);
        gameClient.parsePacket(data25, testAddress, testPort);
        gameClient.parsePacket(data26, testAddress, testPort);
        gameClient.parsePacket(data27, testAddress, testPort);
        gameClient.parsePacket(data28, testAddress, testPort);
        gameClient.parsePacket(data41, testAddress, testPort);
        gameClient.parsePacket(data42, testAddress, testPort);
    }
}
