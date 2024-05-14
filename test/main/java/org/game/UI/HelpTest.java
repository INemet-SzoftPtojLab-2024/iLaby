package main.java.org.game.UI;

import main.java.org.game.Graphics.GameRenderer;
import main.java.org.game.Graphics.ImageUI;
import main.java.org.game.Input.Input;
import main.java.org.game.Isten;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class HelpTest {

    @Mock
    private Isten mockIsten;

    @Mock
    private GameRenderer mockRenderer;

    @Mock
    private Input mockInputHandler;

    @Mock
    private ImageUI mockImageUI;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockIsten.getRenderer()).thenReturn(mockRenderer);
        when(mockIsten.getInputHandler()).thenReturn(mockInputHandler);
    }

    @Test
    public void testOnStart() {
        Help help = new Help();
        help.onStart(mockIsten);

        verify(mockRenderer).addRenderable(any(ImageUI.class));
        verify(mockImageUI).setVisibility(false);
    }
}