package main.java.org.entities.player;

import main.java.org.game.Camera.Camera;
import main.java.org.game.Graphics.GameRenderer;
import main.java.org.game.Graphics.Image;
import main.java.org.game.Graphics.Text;
import main.java.org.game.Input.Input;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.PhysicsEngine;
import main.java.org.linalg.Vec2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerTest {
    @Mock
    Isten mockIsten;
    @Mock
    Collider mockCollider;
    @Mock
    GameRenderer mockRenderer;
    @Mock
    Camera mockCamera;
    @Mock
    PhysicsEngine mockPhysicsEngine;
    @Mock
    Input mockInputHandler;
    @Mock
    Text mockPlayerName;
    Player testPlayer;

    @BeforeEach
    public void setUp() {
        mockIsten = mock(Isten.class);
        mockCollider = mock(Collider.class);
        mockRenderer = mock(GameRenderer.class);
        mockCamera = mock(Camera.class);
        mockPhysicsEngine = mock(PhysicsEngine.class);
        mockInputHandler = mock(Input.class);
        mockPlayerName = mock(Text.class);

        testPlayer = new Player(mockIsten, "Name");

        when(mockIsten.getPhysicsEngine()).thenReturn(mockPhysicsEngine);
        when(mockIsten.getRenderer()).thenReturn((mockRenderer));
        when(mockIsten.getCamera()).thenReturn(mockCamera);
        when(mockIsten.getInputHandler()).thenReturn(mockInputHandler);
        when(mockCollider.getPosition()).thenReturn(new Vec2(0, 0));

        //testPlayer.setPlayerName(mockPlayerName);
        testPlayer.setPlayerCollider(mockCollider);
    }

    @Test
    public void testConstructor() {
        // Verify that player name is set correctly
        assertEquals("Name", testPlayer.getPlayerName().getText());

        // Verify that player collider is set correctly
        assertEquals(mockCollider, testPlayer.getPlayerCollider());

        // Verify that other attributes are initialized properly
        assertNull(testPlayer.getPlayerImage());
        assertNull(testPlayer.getDeath());
        assertNull(testPlayer.getWinBgn());
        assertEquals(0, testPlayer.getActiveImage());
        assertNull(testPlayer.getMotivational());
        assertNull(testPlayer.getSieg());
        assertEquals(0.0f, testPlayer.getTime());
        assertTrue(testPlayer.isAlive());
        assertFalse(testPlayer.isWon());
        assertEquals(0, testPlayer.getFaintingTime());
        assertFalse(testPlayer.isFainted());
        assertEquals(2, testPlayer.getSpeed());
        assertNotNull(testPlayer.getInventory());
    }

    @Test
    public void testParameterizedConstructor() {
        String name = "TestPlayer";
        Player player = new Player(mockIsten, name);

        assertNull(player.getPlayerCollider());
        assertNull(player.getPlayerImage());
        assertEquals(0, player.getActiveImage());
        assertEquals(0.0f, player.getTime(), 0.001);
        assertNotNull(player.getPlayerName());
        assertEquals(name, player.getPlayerName().getText());
        assertEquals(0, player.getPlayerName().getColor().getRed());
        assertEquals(0, player.getPlayerName().getColor().getGreen());
        assertEquals(255, player.getPlayerName().getColor().getBlue());
    }
    @Test
    public void testOnStart() {
        testPlayer.onStart(mockIsten);

        verify(mockIsten.getPhysicsEngine()).addCollider(any(Collider.class));
        verify(mockCamera).setPixelsPerUnit(100);
    }
/*
    @Test
    public void testMovementAndAnimationW() {
        Player player = Mockito.mock(Player.class);
        player.onStart(mockIsten);
        when(mockInputHandler.isKeyDown(KeyEvent.VK_W)).thenReturn(true);
        when(mockInputHandler.isKeyDown(KeyEvent.VK_A)).thenReturn(false);
        when(mockInputHandler.isKeyDown(KeyEvent.VK_S)).thenReturn(false);
        when(mockInputHandler.isKeyDown(KeyEvent.VK_D)).thenReturn(false);
        when(mockInputHandler.isKeyDown(anyInt())).thenReturn(false);

        player.onUpdate(mockIsten, 0.1);

        assertEquals(0, player.getPlayerCollider().getVelocity().x);
        assertEquals(2, player.getPlayerCollider().getVelocity().y);
    }
    @Test
    public void testMovementAndAnimationA() {

        testPlayer.onStart(mockIsten);
        when(mockInputHandler.isKeyDown('W')).thenReturn(false);
        when(mockInputHandler.isKeyDown('A')).thenReturn(true);
        when(mockInputHandler.isKeyDown('S')).thenReturn(false);
        when(mockInputHandler.isKeyDown('D')).thenReturn(false);
        when(mockInputHandler.isKeyDown(16)).thenReturn(false);

        testPlayer.onUpdate(mockIsten, 0.1);

        assertEquals(-2, testPlayer.getPlayerCollider().getVelocity().x);
        assertEquals(0, testPlayer.getPlayerCollider().getVelocity().y);
    }
    @Test
    public void testMovementAndAnimationS() {

        testPlayer.onStart(mockIsten);
        when(mockInputHandler.isKeyDown('W')).thenReturn(false);
        when(mockInputHandler.isKeyDown('A')).thenReturn(false);
        when(mockInputHandler.isKeyDown('S')).thenReturn(true);
        when(mockInputHandler.isKeyDown('D')).thenReturn(false);
        when(mockInputHandler.isKeyDown(16)).thenReturn(false);

        testPlayer.onUpdate(mockIsten, 0.1);

        assertEquals(0, testPlayer.getPlayerCollider().getVelocity().x);
        assertEquals(-2, testPlayer.getPlayerCollider().getVelocity().y);

    }
    @Test
    public void testMovementAndAnimationD() {

        testPlayer.onStart(mockIsten);
        when(mockInputHandler.isKeyDown('W')).thenReturn(false);
        when(mockInputHandler.isKeyDown('A')).thenReturn(false);
        when(mockInputHandler.isKeyDown('S')).thenReturn(false);
        when(mockInputHandler.isKeyDown('D')).thenReturn(true);
        when(mockInputHandler.isKeyDown(16)).thenReturn(false);

        testPlayer.onUpdate(mockIsten, 0.1);

        assertEquals(2, testPlayer.getPlayerCollider().getVelocity().x);
        assertEquals(0, testPlayer.getPlayerCollider().getVelocity().y);

    }
    @Test
    public void testAnimationChange(){
        testPlayer.setPlayerImage(new ArrayList<>());
        for (int i = 0; i < 4; i++) {
            testPlayer.getPlayerImage().add(mock(Image.class));
            testPlayer.getPlayerImage().get(i).setVisibility(false);
        }

        testPlayer.setActiveImage(0);
        testPlayer.setTime(0.0f);

        when(mockCollider.getVelocity()).thenReturn(new Vec2(2,0));
        testPlayer.onUpdate(mockIsten, 0.3);
        assertEquals(1, testPlayer.getActiveImage());

        when(mockCollider.getVelocity()).thenReturn(new Vec2(-2,0));
        testPlayer.onUpdate(mockIsten, 0.3);
        assertEquals(1, testPlayer.getActiveImage());
    }*/
}