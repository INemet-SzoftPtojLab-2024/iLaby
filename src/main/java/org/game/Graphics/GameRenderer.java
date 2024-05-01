package main.java.org.game.Graphics;

import main.java.org.game.Camera.Camera;
import main.java.org.game.Input.Input;
import main.java.org.linalg.Vec2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.WritableRaster;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * GameRenderer class handles rendering of game elements.
 */
public class GameRenderer extends JPanel implements ActionListener {

    private ArrayList<Renderable> renderables;
    private Camera camera;

    /**
     * Constructor for GameRenderer.
     */
    public GameRenderer(Camera camera, Input input) {
        this.setPreferredSize(new Dimension(800, 800));
        setFocusable(true);

        //input
        input.setTargetComponent(this);
        this.addKeyListener(input);
        this.addMouseListener(input);

        setDoubleBuffered(true);
        renderables = new ArrayList<>();

        this.camera=camera;
    }

    /**
     * Method to add a renderable object to the list.
     *
     * @param r The renderable object to add
     */
    public void addRenderable(Renderable r) {
        renderables.add(r);
    }

    /** this function calculates the rendered positions and scales of the Renderables */
    public void calculateRenderedPositions()
    {
        //calculate renderable positions on screen
        Vec2 screenSize=new Vec2(this.getWidth(),this.getHeight());
        for(int i=0;i<renderables.size();i++)
        {
            if(renderables.get(i).isUIElement())
            {
                renderables.get(i).setRenderedPosition(Renderable.convertUIPositionToTopLeft(renderables.get(i),screenSize));
                renderables.get(i).setRenderedScale(renderables.get(i).getScale());
                continue;
            }

            Vec2 temp=convertWorldToScreen(renderables.get(i).getPosition(), camera);
            renderables.get(i).setRenderedPosition(temp);
            renderables.get(i).setRenderedScale(Vec2.scale(renderables.get(i).getScale(),camera.getPixelsPerUnit()));
        }
    }

    public void processUIInputs(Input input)
    {
        Vec2 mousePos=input.getMousePosition();
        boolean mousePressed=input.isMouseButtonPressed(Input.MOUSE_LEFT);
        boolean mouseHeld=input.isMouseButtonDown(Input.MOUSE_LEFT);
        boolean mouseReleased=input.isMouseButtonReleased(Input.MOUSE_LEFT);
        boolean mouseClicked=input.isMouseButtonClicked(Input.MOUSE_LEFT);

        for(int i=0;i<renderables.size();i++)
        {
            Renderable roblox=renderables.get(i);
            if(roblox.isUIElement()&&roblox.getVisibility())
                roblox.processInput(mousePos,mousePressed,mouseHeld,mouseReleased,mouseClicked);
        }
    }

    /**
     * Method to paint/render the graphics.
     *
     * @param graphics The Graphics object to render on
     */
    public void paint(Graphics graphics) {

        sortRenderables();//TODO: optimize

        //actual render
        Vec2 screenSize=new Vec2(this.getWidth(),this.getHeight());

        setBackground(new Color(50,50,50));

        graphics.fillRect(0,0,this.getWidth(), this.getHeight());

        if(renderables.isEmpty()) return;
        for(int i = 0; i < renderables.size(); i++) {
            Renderable renderable = renderables.get(i);
            //if it is not visible, yeet
            if(!renderable.getVisibility())
                continue;

            //if it is not on the screen, yeet
            Vec2 tempPos=renderable.getRenderedPosition();
            Vec2 tempScale=renderable.getRenderedScale();
            if(tempPos.x-tempScale.x>screenSize.x||tempPos.y-tempScale.y>screenSize.y)
                continue;
            if(tempPos.x+tempScale.x<0||tempPos.y+tempScale.y<0)
                continue;

            //if not yet yeeten, render
            renderable.render(graphics);
        }
    }

    /**
     * Method to convert world coordinates to screen coordinates.
     *
     * @param worldSpaceCoords The world space coordinates to convert
     * @param cam camera
     * @return The screen space coordinates
     */
    public Vec2 convertWorldToScreen(Vec2 worldSpaceCoords, Camera cam)
    {
        Vec2 coords=Vec2.subtract(worldSpaceCoords,cam.getPosition());
        coords.scale(cam.getPixelsPerUnit());
        coords.x+=0.5f*this.getWidth();
        coords.y+=0.5f*this.getHeight();
        coords.y=this.getHeight()-coords.y;
        return coords;
    }
    public Vec2 convertScreenToWorld(Vec2 screenSpaceCoords, Camera cam)
    {
        Vec2 coords = new Vec2();

        coords.x = (screenSpaceCoords.x - 0.5f * this.getWidth()) / cam.getPixelsPerUnit() + cam.getPosition().x;
        coords.y = (this.getHeight() - screenSpaceCoords.y - 0.5f * this.getHeight()) / cam.getPixelsPerUnit() + cam.getPosition().y;

        return coords;
    }


    private void sortRenderables()
    {
        ArrayList<Renderable> renderablesCopy = new ArrayList<>(renderables);
        renderablesCopy.sort(new Renderable.SortingLayerComparator());
        renderables = renderablesCopy;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Needed for implementation
    }
    public void deleteRenderable(Renderable r){
        for(int i=0;i<renderables.size();i++)
        {
            if(renderables.get(i).equals(r))
            {
                renderables.remove(i);
                break;
            }
        }
    }
}