package main.java.org.game.Map;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

class RoomTypeTest {

    @Test
    void testGetRandomRoomTypeAlwaysReturnsValidValue() {
        // Test multiple times to ensure reliability
        for (int i = 0; i < 1000; i++) {
            assertNotNull(RoomType.getRandomRoomtype(), "getRandomRoomtype() must never return null");
            assertTrue(EnumSet.of(RoomType.GAS, RoomType.BASIC, RoomType.CURSED, RoomType.SHADOW).contains(RoomType.getRandomRoomtype()), "getRandomRoomtype() returned an unexpected RoomType");
        }
    }

    @Test
    void testGetRandomRoomTypeDistribution() {
        Map<RoomType, Integer> counts = new HashMap<>();
        int total = 10000;
        for (int i = 0; i < total; i++) {
            RoomType type = RoomType.getRandomRoomtype();
            counts.put(type, counts.getOrDefault(type, 0) + 1);
        }

        // Assuming uniform distribution, each RoomType should appear roughly equally often
        // This check allows for a simple distribution test without strict statistical assertion
        int expectedCount = total / RoomType.values().length;
        int tolerance = total / 20; // 5% tolerance
        for (RoomType type : RoomType.values()) {
            assertTrue(
                    Math.abs(counts.get(type) - expectedCount) < tolerance,
                    "Distribution of " + type + " is outside of the expected range"
            );
        }
    }
}
