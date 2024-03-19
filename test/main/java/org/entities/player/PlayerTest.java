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
    Player player;

    @BeforeEach
    public void setUp() {
        mockIsten = mock(Isten.class);
        mockCollider = mock(Collider.class);
        mockRenderer = mock(GameRenderer.class);
        mockCamera = mock(Camera.class);
        mockPhysicsEngine = mock(PhysicsEngine.class);
        mockInputHandler = mock(Input.class);
        mockPlayerName = mock(Text.class);

        player = new Player();

        when(mockIsten.getPhysicsEngine()).thenReturn(mockPhysicsEngine);
        when(mockIsten.getRenderer()).thenReturn((mockRenderer));
        when(mockIsten.getCamera()).thenReturn(mockCamera);
        when(mockIsten.getInputHandler()).thenReturn(mockInputHandler);
        when(mockCollider.getPosition()).thenReturn(new Vec2(0, 0));
        player.setPlayerName(mockPlayerName);
        player.setPlayerCollider(mockCollider);
    }

    @Test
    public void testConstructor() {
        Player player = new Player();
        assertNull(player.getPlayerCollider());
        assertNull(player.getPlayerImage());
        assertEquals(0, player.getActiveImage());
        assertEquals(0.0f, player.getTime(), 0.001);
        assertNull(player.getPlayerName());
    }

    @Test
    public void testParameterizedConstructor() {
        String name = "TestPlayer";
        Player player = new Player(name);

        assertNull(player.getPlayerCollider());
        assertNull(player.getPlayerImage());
        assertEquals(0, player.getActiveImage());
        assertEquals(0.0f, player.getTime(), 0.001);
        assertNotNull(player.getPlayerName());
        assertEquals(name, player.getPlayerName().getText());
        assertEquals(255, player.getPlayerName().getColor().getRed());
        assertEquals(255, player.getPlayerName().getColor().getGreen());
        assertEquals(255, player.getPlayerName().getColor().getBlue());
    }
}