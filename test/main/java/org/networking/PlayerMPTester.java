package main.java.org.networking;

import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.linalg.Vec2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.InetAddress;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerMPTester {

    private Isten isten;
    private PlayerMP player, player2;

    @BeforeEach
    public void setUp() {
        isten = new Isten();
        player = new PlayerMP(isten, "TestPlayer", InetAddress.getLoopbackAddress(), 12345);
        player2 = new PlayerMP(isten, "TestPlayer", InetAddress.getLoopbackAddress(), 12345, new Vec2(0,0));
        player.setPlayerCollider(new Collider(new Vec2(0,0),new Vec2(0.5f, 0.5f)));
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
