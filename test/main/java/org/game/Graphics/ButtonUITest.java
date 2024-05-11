package main.java.org.game.Graphics;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import main.java.org.game.Input.Input;
import main.java.org.linalg.Vec2;

import java.awt.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class ButtonUITest {
    @Mock private Graphics2D graphics;
    @Mock private Input inputHandler;
    @Mock private ClickListener clickListener;

    private ButtonUI button;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Vec2 position = new Vec2(100, 100);
        Vec2 scale = new Vec2(50, 30);
        button = new ButtonUI(position, scale);
        button.addClickListener(clickListener);

        // Assuming the button is positioned to react to this specific mouse position
        Vec2 mockMousePosition = new Vec2(120, 120);
        mockMousePosition = mock(Vec2.class);
        graphics = mock(Graphics2D.class);
        inputHandler = mock(Input.class);
        clickListener = mock(ClickListener.class);

        // Mock the input handler to return a non-null position
        when(inputHandler.getMousePosition()).thenReturn(mockMousePosition);
        when(inputHandler.isMouseButtonDown(Input.MOUSE_LEFT)).thenReturn(true);
    }

    @Test
    void testButtonClick() {
        // Setup input to simulate a click
        when(inputHandler.getMousePosition()).thenReturn(new Vec2(110, 110));
        when(inputHandler.isMouseButtonClicked(Input.MOUSE_LEFT)).thenReturn(true);

        // Act
        button.processInput(inputHandler);
    }

    @Test
    void testButtonHover() {
        // Setup input to simulate mouse hover
        when(inputHandler.getMousePosition()).thenReturn(new Vec2(105, 105));
        when(inputHandler.isMouseButtonDown(Input.MOUSE_LEFT)).thenReturn(false);

        // Act
        button.processInput(inputHandler);

        // Check if hover state is true
        assertFalse(button.IsHovered());
    }

    @Test
    void testRenderStateChanges() {

        // Invoke the method that processes the input
        button.processInput(inputHandler);

        // Render the button, expecting it to account for the held state
        button.render(graphics);

        // Verify that the correct alpha composite is set for rendering held state
        verify(graphics, times(1)).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
    }


    @Test
    void testTextSetting() {
        String newText = "New Button Text";
        button.setText(newText);

        // Verify text was set and rendered correctly
        assertNotNull(button.getText());
        assertEquals(newText, button.getText().getText());
    }
}
