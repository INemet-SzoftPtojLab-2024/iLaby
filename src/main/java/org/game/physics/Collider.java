package main.java.org.game.physics;

import main.java.org.linalg.Vec2;

import java.util.ArrayList;

public class Collider
{
    public final int id;
    private Vec2 position;
    private Vec2 scale;
    private Vec2 velocity;
    private boolean isMovable;
    private boolean isSolid;
    private String tag;
    private ArrayList<String> lastCollisionTags;

    public Collider()
    {
        id=nextId++;
        position=new Vec2();
        scale=new Vec2(1);
        velocity=new Vec2();
        isSolid=true;
        isMovable=false;
        tag="";
        lastCollisionTags=new ArrayList<>();
    }

    public Collider(Vec2 position, Vec2 scale)
    {
        id=nextId++;
        this.position=position.clone();
        this.scale=scale.clone();
        this.velocity=new Vec2();
        isSolid=true;
        isMovable=false;
        tag="";
        lastCollisionTags=new ArrayList<>();
    }

    public void clearCollisionHistory()
    {
        lastCollisionTags.clear();
    }

    //getters n setters
    public Vec2 getPosition(){return this.position;}//doesn't clone return value
    public Vec2 getScale() {return this.scale;}//doesn't clone return value
    public Vec2 getVelocity(){return this.velocity;}//doesn't clone return value
    public String getTag(){return this.tag;}
    public String[] getLastCollisionTags(){return (String[])this.lastCollisionTags.toArray();}
    public boolean isSolid(){return this.isSolid;}
    public boolean isMovable(){return this.isMovable;}

    public void setPosition(Vec2 position) {this.position=position.clone();}
    public void setScale(Vec2 scale) {this.scale=scale.clone();}
    public void setVelocity(Vec2 velocity) {this.velocity=velocity.clone();}
    public void setTag(String tag){this.tag=tag;}
    public void setSolidity(boolean isSolid){this.isSolid=isSolid;}
    public void setMovability(boolean isMovable){this.isMovable=isMovable;}

    //static
    private static int nextId=0;

    public static void resolveCollision(Collider c1, Collider c2)
    {
        if(c1.isMovable&&!c2.isMovable)
            resolveCollisionMN(c1, c2);
        else if(!c1.isMovable&&c2.isMovable)
            resolveCollisionMN(c2,c1);
        else if(c1.isMovable&&c2.isMovable)
            resolveCollisionMM(c1, c2);
    }

    private static void resolveCollisionMN(Collider c1, Collider c2)//collision resolution where c1 is movable and c2 is not
    {
        boolean shouldResolve=c1.isSolid&&c2.isSolid;

        float minDistanceX=0.5f*c1.scale.x+0.5f*c2.scale.x;
        float minDistanceY=0.5f*c1.scale.y+0.5f*c2.scale.y;

        float distanceX=c2.position.x-c1.position.x;
        float distanceY=c2.position.y-c1.position.y;

        float penetrationX=minDistanceX-Math.abs(distanceX);
        float penetrationY=minDistanceY-Math.abs(distanceY);

        if(penetrationX<=0||penetrationY<=0)//colliders don't touch
            return;

        //resolve in the direction of the greater penetration
        if(penetrationX>penetrationY)
        {
            c1.lastCollisionTags.add(c2.tag);
            c2.lastCollisionTags.add(c1.tag);
            if(shouldResolve)
            {
                c1.velocity.x=0;

                if(c1.position.x<c2.position.x)
                    c1.position.x-=penetrationX;
                else
                    c1.position.x+=penetrationX;
            }
        }
        else
        {
            c1.lastCollisionTags.add(c2.tag);
            c2.lastCollisionTags.add(c1.tag);
            if(shouldResolve)
            {
                c1.velocity.y=0;

                if(c1.position.y<c2.position.y)
                    c1.position.y-=penetrationY;
                else
                    c1.position.y+=penetrationY;
            }
        }
    }

    private static void resolveCollisionMM(Collider c1, Collider c2)//collision resolution where both c1 and c2 are movable
    {

    }
}
