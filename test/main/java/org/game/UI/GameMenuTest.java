package main.java.org.game.UI;

import static org.junit.jupiter.api.Assertions.*;

import main.java.org.game.Graphics.*;
import main.java.org.game.Input.Input;
import main.java.org.game.Isten;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.event.KeyEvent;

import static org.mockito.Mockito.*;

public class GameMenuTest {

    @Mock
    private Isten mockIsten;

    @Mock
    private GameRenderer mockRenderer;

    @Mock
    private Input mockInputHandler;

    @Mock
    private TextUI mockText;

    @Mock
    private ButtonUI mockButtonContinue;

    @Mock
    private ButtonUI mockButtonEscape;

    @Mock
    private ImageUI mockImageBackground;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockIsten.getRenderer()).thenReturn(mockRenderer);
        when(mockIsten.getInputHandler()).thenReturn(mockInputHandler);
    }

    @Test
    public void testOnStart() {
        GameMenu gameMenu = new GameMenu();
        gameMenu.onStart(mockIsten);

        verify(mockRenderer, times(4)).addRenderable(any(Renderable.class));
        verify(mockInputHandler, never()).isKeyPressed(KeyEvent.VK_ESCAPE);
    }

    @Test
    public void testOnUpdateEscapeKeyPressed() {
        GameMenu gameMenu = new GameMenu();
        gameMenu.onStart(mockIsten);

        when(mockInputHandler.isKeyPressed(KeyEvent.VK_ESCAPE)).thenReturn(true);
        gameMenu.onUpdate(mockIsten, 0.1);

        verify(mockIsten.getInputHandler()).isKeyPressed(KeyEvent.VK_ESCAPE);
        verify(mockButtonEscape).setVisibility(false);
    }
}
