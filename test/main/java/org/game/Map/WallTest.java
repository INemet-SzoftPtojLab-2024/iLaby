package main.java.org.game.Map;

import main.java.org.game.Map.Wall;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.physics.Collider;
import main.java.org.linalg.Vec2;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WallTest {
    private Collider mockCollider;
    private Vec2 mockPosition;
    private UnitRoom mockUnitRoom1;
    private UnitRoom mockUnitRoom2;
    private Wall wall;

    @BeforeEach
    void setUp() {
        mockCollider = mock(Collider.class);
        mockPosition = new Vec2(1, 1);
        mockUnitRoom1 = mock(UnitRoom.class);
        mockUnitRoom2 = mock(UnitRoom.class);
    }

    @Test
    void testWallConstructorWithTwoUnitRooms() {
        wall = new Wall(mockCollider, mockPosition, mockUnitRoom1, mockUnitRoom2);

        // Assertions to check if the wall is constructed correctly
        assertNotNull(wall);
        // Add more assertions to verify the initialization of properties if they are accessible
    }

    @Test
    void testWallConstructorWithOneUnitRoom() {
        wall = new Wall(mockCollider, mockPosition, mockUnitRoom1);

        // Assertions to check if the wall is constructed correctly
        assertNotNull(wall);
        // Add more assertions to verify the initialization of properties if they are accessible
    }

    @Test
    void testIsDoor() {
        wall = new Wall(mockCollider, mockPosition, mockUnitRoom1, mockUnitRoom2);
        assertFalse(wall.isDoor(), "Wall should not be a door.");
    }
}
