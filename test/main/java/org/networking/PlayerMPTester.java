package main.java.org.networking;

import main.java.org.game.Isten;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.InetAddress;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerMPTester {

    private Isten isten;
    private PlayerMP player;

    @BeforeEach
    public void setUp() {
        isten = new Isten();
        player = new PlayerMP(isten, "TestPlayer", InetAddress.getLoopbackAddress(), 12345);
    }

    @Test
    public void testConstructor() {
        assertNotNull(player);
        assertEquals("TestPlayer", player.getPlayerName().getText());
        assertEquals(InetAddress.getLoopbackAddress(), player.ipAddress);
        assertEquals(12345, player.port);
    }

    @Test
    public void testSendDataToServer() {
        player.sendDataToServer(isten, 0.1);
    }

    @Test
    public void testSendMoveData() {
        player.sendMoveData(isten);
    }

    @Test
    public void testGetUsername() {
        assertEquals("TestPlayer", player.getUsername());
    }
}
