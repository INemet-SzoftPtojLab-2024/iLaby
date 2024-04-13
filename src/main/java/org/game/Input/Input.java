package main.java.org.game.Input;

import main.java.org.linalg.Vec2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

public class Input implements KeyListener, MouseListener {

    private Component targetComponent=null;

    private final boolean[] keyDown;//the key states in the current update
    private final boolean[] previousKeyDown;//the key states in the previous update
    private final boolean[] nextKeyDown;//the key states in the next updates (if a keyevent happens, it will be stored here until the next update)

    private Vec2 mousePosition;
    private Vec2 previousMousePosition;
    private Vec2 mouseDelta;

    private final boolean[] mouseButtonDown;
    private final boolean[] previousMouseButtonDown;
    private final boolean[] nextMouseButtonDown;
    private final long[] mouseButtonPressStart;//to help determine whether it was a click or the button has been held down
    private final long MOUSE_CLICK_THRESHOLD_NANO=200000000;
    public final static int MOUSE_LEFT=0, MOUSE_MIDDLE=1, MOUSE_RIGHT=2;

    public Input()
    {
        targetComponent=null;

        //256 for the first 256 ascii characters
        keyDown=new boolean[256];
        previousKeyDown=new boolean[256];
        nextKeyDown=new boolean[256];

        Arrays.fill(keyDown, false);
        Arrays.fill(previousKeyDown, false);
        Arrays.fill(nextKeyDown, false);

        //mouse things
        mousePosition= new Vec2();
        previousMousePosition= new Vec2();
        mouseDelta=new Vec2();


        mouseButtonDown=new boolean[3];
        previousMouseButtonDown=new boolean[3];
        nextMouseButtonDown=new boolean[3];
        mouseButtonPressStart=new long[3];

        Arrays.fill(mouseButtonDown, false);
        Arrays.fill(previousMouseButtonDown, false);
        Arrays.fill(nextMouseButtonDown, false);
    }

    /** do not ever call this function. */
    public void update()
    {
        //keys
        for(int i=0;i<256;i++)
        {
            previousKeyDown[i]=keyDown[i];
            keyDown[i]=nextKeyDown[i];
        }

        //mouse movement
        previousMousePosition.x= mousePosition.x;
        previousMousePosition.y= mousePosition.y;
        Point tempMousePosition=MouseInfo.getPointerInfo().getLocation();
        if(targetComponent!=null)
            SwingUtilities.convertPointFromScreen(tempMousePosition,targetComponent);
        mousePosition.x=tempMousePosition.x;
        mousePosition.y= tempMousePosition.y;
        mouseDelta.x= mousePosition.x-previousMousePosition.x;
        mouseDelta.y= mousePosition.y-previousMousePosition.y;

        //mouse buttons
        for(int i=0;i<3;i++)
        {
            previousMouseButtonDown[i]=mouseButtonDown[i];
            mouseButtonDown[i]=nextMouseButtonDown[i];
            if(isMouseButtonPressed(MOUSE_LEFT+i))
                mouseButtonPressStart[i]=System.nanoTime();
        }
    }

    /**sets the target component of the input handler <br>
     * target component is used to calculate mouse positions relative to the component <br>
     * when the target component is null, the mouse positions are in screen position
     */
    public void setTargetComponent(Component targetComponent)
    {
        this.targetComponent=targetComponent;
    }

    //key functions
    /** is the key being held down
     * @param keyCode is the keycode of the key (for example KeyEvent.VK_A for the letter A)*/
    public boolean isKeyDown(int keyCode)
    {
        if(keyCode>=256)
            return false;
        return keyDown[keyCode];
    }

    /** has the key just been pressed down
     * @param keyCode is the keycode of the key (for example KeyEvent.VK_A for the letter A)*/
    public boolean isKeyPressed(int keyCode)
    {
        if(keyCode>=256)
            return false;
        if(keyDown[keyCode]&&!previousKeyDown[keyCode])
            return true;
        return false;
    }

    /** has the key just been released
     * @param keyCode is the keycode of the key (for example KeyEvent.VK_A for the letter A)*/
    public boolean isKeyReleased(int keyCode)
    {
        if(keyCode>=256)
            return false;

        if(!keyDown[keyCode]&&previousKeyDown[keyCode])
            return true;
        return false;
    }

    //mouse functions
    public Vec2 getMousePosition()
    {
        return mousePosition.clone();
    }

    public float getMousePositionX()
    {
        return mousePosition.x;
    }

    public float getMousePositionY()
    {
        return mousePosition.y;
    }

    public Vec2 getMouseDelta()
    {
        return mouseDelta.clone();
    }

    public float getMouseDeltaX()
    {
        return mouseDelta.x;
    }

    public float getMouseDeltaY()
    {
        return mouseDelta.y;
    }

    //mouse button functions

    /** is the mouse button being held down
     * @param mouseButton either Input.MOUSE_LEFT, Input.MOUSE_MIDDLE or Input.MOUSE_RIGHT
     */
    public boolean isMouseButtonDown(int mouseButton)
    {
        if(mouseButton!=MOUSE_LEFT&&mouseButton!=MOUSE_MIDDLE&&mouseButton!=MOUSE_RIGHT)
            return false;

        return mouseButtonDown[mouseButton];
    }

    /** has the mouse button been just pressed down
     * @param mouseButton either Input.MOUSE_LEFT, Input.MOUSE_MIDDLE or Input.MOUSE_RIGHT
     */
    public boolean isMouseButtonPressed(int mouseButton)
    {
        if(mouseButton!=MOUSE_LEFT&&mouseButton!=MOUSE_MIDDLE&&mouseButton!=MOUSE_RIGHT)
            return false;

        if(mouseButtonDown[mouseButton]&&!previousMouseButtonDown[mouseButton])
            return true;
        return false;
    }

    /** has the mouse button been just released
     * @param mouseButton either Input.MOUSE_LEFT, Input.MOUSE_MIDDLE or Input.MOUSE_RIGHT
     */
    public boolean isMouseButtonReleased(int mouseButton)
    {
        if(mouseButton!=MOUSE_LEFT&&mouseButton!=MOUSE_MIDDLE&&mouseButton!=MOUSE_RIGHT)
            return false;

        if(!mouseButtonDown[mouseButton]&&previousMouseButtonDown[mouseButton])
            return true;
        return false;
    }

    /** true if the mouse button has been clicked <br>
     * (pressed and released in a short time)
     * @param mouseButton either Input.MOUSE_LEFT, Input.MOUSE_MIDDLE or Input.MOUSE_RIGHT
     */
    public boolean isMouseButtonClicked(int mouseButton)
    {
        if(mouseButton!=MOUSE_LEFT&&mouseButton!=MOUSE_MIDDLE&&mouseButton!=MOUSE_RIGHT)
            return false;

        if(!mouseButtonDown[mouseButton]&&previousMouseButtonDown[mouseButton]&&System.nanoTime()-mouseButtonPressStart[mouseButton]<MOUSE_CLICK_THRESHOLD_NANO)
            return true;

        return false;
    }



    //keylistener functions
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()>=256)
            return;
        nextKeyDown[e.getKeyCode()]=true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()>=256)
            return;
        nextKeyDown[e.getKeyCode()]=false;
    }


    //mouselistener functions
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int index=-1;


        if(SwingUtilities.isLeftMouseButton(e))
            index=MOUSE_LEFT;
        if(SwingUtilities.isMiddleMouseButton(e))
            index=MOUSE_MIDDLE;
        if(SwingUtilities.isRightMouseButton(e))
            index=MOUSE_RIGHT;


        if(index==-1)
            return;


        nextMouseButtonDown[index]=true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int index=-1;


        if(SwingUtilities.isLeftMouseButton(e))
            index=MOUSE_LEFT;
        if(SwingUtilities.isMiddleMouseButton(e))
            index=MOUSE_MIDDLE;
        if(SwingUtilities.isRightMouseButton(e))
            index=MOUSE_RIGHT;


        if(index==-1)
            return;


        nextMouseButtonDown[index]=false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
