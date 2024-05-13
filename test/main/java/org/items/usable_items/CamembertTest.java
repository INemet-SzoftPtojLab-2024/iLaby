package main.java.org.items.usable_items;

import main.java.org.entities.player.Player;
import main.java.org.game.Isten;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.RoomType;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.physics.Collider;
import main.java.org.items.Item;
import main.java.org.linalg.Vec2;
import main.java.org.networking.PlayerMP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.swing.text.Position;
import java.net.InetAddress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CamembertTest {
    private Isten isten;
    @Mock
    private Player player;


    @BeforeEach
    public void setUp() {
        isten = new Isten();
        player = Mockito.mock(Player.class);
    }
    @Test
    public void testSetupImage() {
        Isten isten = new Isten();
        Camembert camembert = new Camembert(isten);

        assertFalse(camembert.getImage().getVisibility());
    }

    @Test
    public void testSetupExplosion() {
        Isten isten = new Isten();
        Camembert camembert = new Camembert(isten);

        assertEquals(31, camembert.getExplosion().size());
    }
    @Test
    public void testDropOnGround() {
        Camembert camembert = new Camembert(isten);
        Collider playerCollider = Mockito.mock(Collider.class);
        Vec2 position = new Vec2(5.0f, 5.0f);
        when(player.getPlayerCollider()).thenReturn(playerCollider);
        when(playerCollider.getPosition()).thenReturn(position);

        camembert.use(player, 1.0);

    }
}