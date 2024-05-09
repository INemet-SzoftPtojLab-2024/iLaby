package main.java.org.game.Camera;

import main.java.org.linalg.Vec2;

public class Camera {
    private Vec2 position;
    private float pixelsPerUnit;

    public Camera()
    {
        position=new Vec2();
        pixelsPerUnit=50;
    }

    public Vec2 getPosition()
    {
        return this.position.clone();
    }

    public void setPosition(Vec2 position)
    {
        this.position.x=position.x;
        this.position.y=position.y;
    }

    public float getPixelsPerUnit()
    {
        return this.pixelsPerUnit;
    }

    public void setPixelsPerUnit(float pixelsPerUnit)
    {
        this.pixelsPerUnit=pixelsPerUnit;
    }
}
