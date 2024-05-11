package main.java.org.game.Map;

import static org.junit.jupiter.api.Assertions.*;

import main.java.org.game.Graphics.GameRenderer;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.linalg.Vec2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UnitRoomTest {
    private UnitRoom unitRoom;
    private Room mockRoom;

    @BeforeEach
    void setUp() {
        unitRoom = new UnitRoom(new Vec2(0, 0));
        mockRoom = Mockito.mock(Room.class);
        Mockito.when(mockRoom.getRoomType()).thenReturn(RoomType.BASIC);
        unitRoom.setOwnerRoom(mockRoom);
    }

    @Test
    void testNeighborManagement() {
        UnitRoom topNeighbor = new UnitRoom(new Vec2(0, 1));
        UnitRoom bottomNeighbor = new UnitRoom(new Vec2(0, -1));

        unitRoom.setTopNeighbor(topNeighbor);
        unitRoom.setBottomNeighbor(bottomNeighbor);

        assertEquals(topNeighbor, unitRoom.getTopNeighbor());
        assertEquals(bottomNeighbor, unitRoom.getBottomNeighbor());
    }

    @Test
    void testEdgeDetection() {
        unitRoom.setTopNeighbor(null); // No neighbor on top
        assertTrue(unitRoom.isTopEdge());
    }

    @Test
    void testDoorManagement() {
        unitRoom.setHasDoor(true);
        assertTrue(unitRoom.hasDoor());

        UnitRoom doorNeighbor = new UnitRoom(new Vec2(0, 1));
        doorNeighbor.setHasDoor(true);
        unitRoom.setRightNeighbor(doorNeighbor);

        assertTrue(unitRoom.isRightDoor());
    }

    @Test
    void testImageSetting() {
        Isten mockIsten = Mockito.mock(Isten.class);
        GameRenderer mockRenderer = Mockito.mock(GameRenderer.class);
        Mockito.when(mockIsten.getRenderer()).thenReturn(mockRenderer);

        unitRoom.setNewImage("./assets/floors/floor1.png", mockIsten);

        Mockito.verify(mockRenderer).addRenderable(Mockito.any(Image.class));
        assertNotNull(unitRoom.image);
    }
}
