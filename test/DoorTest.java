import main.java.org.entities.player.Player;
import main.java.org.game.Isten;
import main.java.org.game.Map.Door;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.physics.Collider;
import main.java.org.linalg.Vec2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

public class DoorTest {

    Door testDoor;
    @Mock
    Collider colliderMock;
    @Mock
    Vec2 positionMock;
    @Mock
    UnitRoom unitRoomMock1, unitRoomMock2;
    @Mock
    Room ownerRoomMock1, ownerRoomMock2;
    @Mock
    Isten istenMock;


    @BeforeEach
    public void setUp(){
        colliderMock = Mockito.mock(Collider.class);
        positionMock = Mockito.mock(Vec2.class);
        unitRoomMock1 = Mockito.mock(UnitRoom.class);
        unitRoomMock2 = Mockito.mock(UnitRoom.class);
        ownerRoomMock1 = Mockito.mock(Room.class);
        ownerRoomMock2 = Mockito.mock(Room.class);
        istenMock = Mockito.mock(Isten.class);


        testDoor = new Door(colliderMock, positionMock, unitRoomMock1, unitRoomMock2);

    }

    @Test
    public void testIsOneWay_Successful(){

        when(testDoor.getUnitRoomsBetween().get(0).getOwnerRoom()).thenReturn(ownerRoomMock1);
        when(testDoor.getUnitRoomsBetween().get(1).getOwnerRoom()).thenReturn(ownerRoomMock2);
        when(ownerRoomMock1.getDoorAdjacentRooms().contains(ownerRoomMock2)).thenReturn(true);
        when(ownerRoomMock2.getDoorAdjacentRooms().contains(ownerRoomMock1)).thenReturn(true);

        boolean result = testDoor.isOneWay();

        Assertions.assertTrue(result);
    }

    @Test
    public void testIsPlayerAtDoor_Successful(){

        Vec2 playerPos = new Vec2(0.4f, 0.0f);
        when(testDoor.getMidPosition()).thenReturn(new Vec2(1f, 0.5f));

        boolean result = testDoor.isPlayerAtDoor(istenMock,playerPos);

        Assertions.assertTrue(result);
    }

    @Test
    public void testIsPlayerAtDoor_UnSuccessful(){

        Vec2 playerPos = new Vec2(74.0f, 23.0f);
        when(testDoor.getMidPosition()).thenReturn(new Vec2(1f, 0.5f));

        boolean result = testDoor.isPlayerAtDoor(istenMock,playerPos);

        Assertions.assertFalse(result);
    }
    @Test
    public void testCanBeOpened_Successful(){
        Isten mockIsten = Mockito.mock(Isten.class);
        Player mockPlayer = Mockito.mock(Player.class);
        Room mockRoom = Mockito.mock(Room.class);

        when(mockIsten.getPlayer()).thenReturn(mockPlayer);
        when(mockPlayer.getCurrentRoom()).thenReturn(mockRoom);

        when(mockRoom.getMaxPlayerCount()).thenReturn(5);
        when(mockRoom.getPlayerCount()).thenReturn(2);

        when(unitRoomMock1.getOwnerRoom()).thenReturn(ownerRoomMock1);
        when(unitRoomMock2.getOwnerRoom()).thenReturn(ownerRoomMock2);

        when(mockRoom.getDoorAdjacentRooms().contains(ownerRoomMock1)).thenReturn(true);
        when(mockRoom.getDoorAdjacentRooms().contains(ownerRoomMock2)).thenReturn(true);

        boolean res = testDoor.canBeOpened(istenMock);

        Assertions.assertTrue(res);

    }

    @Test
    public void testCanBeOpened_UnSuccessful(){
        Isten mockIsten = Mockito.mock(Isten.class);
        Player mockPlayer = Mockito.mock(Player.class);
        Room mockRoom = Mockito.mock(Room.class);

        when(mockIsten.getPlayer()).thenReturn(mockPlayer);
        when(mockPlayer.getCurrentRoom()).thenReturn(mockRoom);

        when(mockRoom.getMaxPlayerCount()).thenReturn(2);
        when(mockRoom.getPlayerCount()).thenReturn(2);

        when(unitRoomMock1.getOwnerRoom()).thenReturn(ownerRoomMock1);
        when(unitRoomMock2.getOwnerRoom()).thenReturn(ownerRoomMock2);

        when(mockRoom.getDoorAdjacentRooms().contains(ownerRoomMock1)).thenReturn(false);
        when(mockRoom.getDoorAdjacentRooms().contains(ownerRoomMock2)).thenReturn(false);

        boolean res = testDoor.canBeOpened(istenMock);

        Assertions.assertFalse(res);

    }
    @Test
    public void testIsOpened_SuccessFul(){
        colliderMock.setSolidity(false);

        boolean result = testDoor.isOpened();

        Assertions.assertTrue(result);
    }

    @Test
    public void testIsOpened_UnSuccessFul(){
        colliderMock.setSolidity(true);

        boolean result = testDoor.isOpened();

        Assertions.assertFalse(result);
    }

    @Test
    public void testIsClosed_SuccessFul(){
        colliderMock.setSolidity(true);

        boolean result = testDoor.isClosed();

        Assertions.assertTrue(result);
    }

    @Test
    public void testIsClosed_UnSuccessFul(){
        colliderMock.setSolidity(false);

        boolean result = testDoor.isClosed();

        Assertions.assertFalse(result);
    }

}
