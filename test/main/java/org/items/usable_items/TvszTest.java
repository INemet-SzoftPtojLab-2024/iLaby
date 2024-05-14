package main.java.org.items.usable_items;

import main.java.org.game.Isten;
import main.java.org.items.Item;
import main.java.org.linalg.Vec2;
import main.java.org.networking.PlayerMP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


import java.net.InetAddress;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TvszTest {
    private Isten isten;
    private PlayerMP player;

    @BeforeEach
    public void setUp() {
        isten = new Isten(); // Instantiate Isten or mock it if necessary
        player = new PlayerMP(isten, "name",mock(InetAddress.class),0); // Instantiate PlayerMP or mock it if necessary
        player.getInventory().onStart(isten);
    }

    @Test
    public void testUseWhenHasCharges() {
        Tvsz tvsz = new Tvsz(isten);
        tvsz.setShouldUseCharge(true); // Set shouldUseCharge to true initially
        tvsz.setCharges(2); // Set initial charges

        tvsz.use(player, 0); // Simulate usage
        assertEquals(1, tvsz.getCharges()); // Check if charges decreased
    }

    @Test
    public void testChargeDecrease() {
        Tvsz tvsz = new Tvsz(isten);
        tvsz.setShouldUseCharge(true); // Set shouldUseCharge to true initially
        tvsz.setCharges(1); // Set initial charges
        tvsz.dropOnGround(new Vec2(0,0));
        tvsz.setDroppedAt(LocalDateTime.now().minusDays(1));
        tvsz.pickUpInInventory(player,1);
        tvsz.use(player, 0); // Simulate usage

        // Ensure item is deleted when no charges left
        boolean contains = false;
        for(Item i : isten.getItemManager().getItems()){
            if(i == tvsz){
                contains = true;
                break;
            }
        }
        assertFalse(contains);
    }


}