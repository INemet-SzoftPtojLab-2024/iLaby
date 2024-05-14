package main.java.org.items.usable_items;

import main.java.org.game.Isten;
import main.java.org.game.Map.Room;
import main.java.org.game.physics.Collider;
import main.java.org.items.Item;
import main.java.org.linalg.Vec2;
import main.java.org.networking.PlayerMP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransistorTest {

    private Isten isten;
    private PlayerMP player;

    @BeforeEach
    public void setUp() {
        isten = new Isten(); // Instantiate Isten or mock it if necessary
        player = new PlayerMP(isten, "name",mock(InetAddress.class),0); // Instantiate PlayerMP or mock it if necessary
        player.getInventory().onStart(isten);
        player.localPlayer = true;
    }

    @Test
    public void testCountTextInvisibleAfterDropOnGround(){
        Transistor t = new Transistor(isten);
        t.dropOnGround(new Vec2(0,0));
        assertFalse(t.getCountText().getVisibility());
    }
/*
    @Test
    public void testActivatedVisibleAfterUse(){
        Transistor t = new Transistor(isten);
        PlayerMP pmp = mock(PlayerMP.class);
        Collider collider = mock(Collider.class);
        when(collider.getPosition()).thenReturn(new Vec2(1,1));
        when(pmp.getPlayerCollider()).thenReturn(collider);
        t.use(pmp,0);
        assertTrue(t.getActivatedImage().getVisibility());
    }
    @Test
    public void testActivatedImagePosition(){
        Transistor t = new Transistor(isten);
        PlayerMP pmp = mock(PlayerMP.class);
        Collider collider = mock(Collider.class);
        when(collider.getPosition()).thenReturn(new Vec2(1,1));
        when(pmp.getPlayerCollider()).thenReturn(collider);
        t.use(pmp,0);
        assertEquals(t.getActivatedImage().getPosition().x,1);
        assertEquals(t.getActivatedImage().getPosition().y,1);
    }*/

    @Test
    public void testChargeDecrease() {
        Transistor t = new Transistor(isten);
        t.dropOnGround(new Vec2(0,0));
        t.setDroppedAt(LocalDateTime.now().minusDays(1));
        t.pickUpInInventory(player,1);

        // Ensure item is deleted when no charges left
        boolean contains = false;
        for(Item i : player.getInventory().getStoredItems()){
            if(i == t){
                contains = true;
                break;
            }
        }
        assertTrue(contains);
    }
}