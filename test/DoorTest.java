import main.java.org.entities.player.Player;
import main.java.org.game.Isten;
import main.java.org.game.Map.Door;
import main.java.org.game.Map.EdgePiece;
import main.java.org.game.Map.Room;
import main.java.org.game.Map.UnitRoom;
import main.java.org.game.physics.Collider;
import main.java.org.linalg.Vec2;
import main.java.org.networking.PlayerMP;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

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
    @Mock
    PlayerMP playerMock;


    @BeforeEach
    public void setUp(){
        colliderMock = Mockito.mock(Collider.class);
        positionMock = Mockito.mock(Vec2.class);
        unitRoomMock1 = Mockito.mock(UnitRoom.class);
        unitRoomMock2 = Mockito.mock(UnitRoom.class);
        ownerRoomMock1 = Mockito.mock(Room.class);
        ownerRoomMock2 = Mockito.mock(Room.class);
        playerMock = Mockito.mock(PlayerMP.class);
        istenMock = Mockito.mock(Isten.class);


        testDoor = new Door(colliderMock, positionMock, unitRoomMock1, unitRoomMock2);

        when(ownerRoomMock1.getDoorAdjacentRooms()).thenReturn(Mockito.mock(ArrayList.class));
        when(ownerRoomMock2.getDoorAdjacentRooms()).thenReturn(Mockito.mock(ArrayList.class));
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

/*
    @Test
    public void testIsPlayerAtDoor_Successful(){
        Vec2 playerPos = new Vec2(1f, 0.25f);
        Vec2 doorMidPos = new Vec2(1f, 0.5f);

        //when(testDoor.getMidPosition()).thenReturn(doorMidPos);

        when(Vec2.subtract(testDoor.getMidPosition(),playerPos)).thenReturn(doorMidPos);

        boolean result = testDoor.isPlayerAtDoor(istenMock, playerPos);

        Assertions.assertTrue(result);
    }*/

    @Test
    public void testIsPlayerAtDoor_UnSuccessful(){
        Vec2 playerPos = new Vec2(74.0f, 23.0f);
        Vec2 pp =new Vec2(1f, 0.5f);


        boolean result = testDoor.isPlayerAtDoor(istenMock,playerPos);

        Assertions.assertFalse(result);
    }

    @Test
    public void testCanBeOpened_Successful(){
        PlayerMP player = Mockito.mock(PlayerMP.class);
        when(unitRoomMock1.getOwnerRoom()).thenReturn(ownerRoomMock1);
        when(unitRoomMock2.getOwnerRoom()).thenReturn(ownerRoomMock2);

        when(ownerRoomMock1.getMaxPlayerCount()).thenReturn(5);
        when(ownerRoomMock1.getPlayerCount()).thenReturn(1);

        when(ownerRoomMock2.getMaxPlayerCount()).thenReturn(5);
        when(ownerRoomMock2.getPlayerCount()).thenReturn(0);

        when(player.getCurrentRoom()).thenReturn(ownerRoomMock1);

        when(ownerRoomMock1.getDoorAdjacentRooms().contains(ownerRoomMock2)).thenReturn(true);

        Assertions.assertTrue(testDoor.canBeOpened(player));
    }

    @Test
    public void testCanBeOpened_UnSuccessful(){
        PlayerMP player = Mockito.mock(PlayerMP.class);


        when(ownerRoomMock1.getMaxPlayerCount()).thenReturn(5);
        when(ownerRoomMock1.getPlayerCount()).thenReturn(2);

        when(ownerRoomMock2.getMaxPlayerCount()).thenReturn(5);
        when(ownerRoomMock2.getPlayerCount()).thenReturn(3);

        when(player.getCurrentRoom()).thenReturn(ownerRoomMock1);

        when(unitRoomMock1.getOwnerRoom()).thenReturn(ownerRoomMock1);
        when(unitRoomMock2.getOwnerRoom()).thenReturn(ownerRoomMock2);

        Assertions.assertFalse(testDoor.canBeOpened(player));
    }

    @Test
    public void testIsOpened_SuccessFul(){
        colliderMock.setSolidity(false);

        boolean result = testDoor.isOpened();

        Assertions.assertTrue(result);
    }

    @Test
    public void testIsOpened_UnSuccessFul(){
        when(colliderMock.isSolid()).thenReturn(true);

        boolean result = testDoor.isOpened();

        Assertions.assertFalse(result);
    }

    @Test
    public void testIsClosed_SuccessFul(){
        colliderMock.setSolidity(true);

        boolean result = testDoor.isClosed();

        Assertions.assertFalse(result);
    }

    @Test
    public void testIsClosed_UnSuccessFul(){
        colliderMock.setSolidity(false);

        boolean result = testDoor.isClosed();

        Assertions.assertFalse(result);
    }

}
