package main.java.org.game.physics;

import main.java.org.linalg.Vec2;

import java.util.ArrayList;

public class Collider
{
    /** an id that is einzigartig for this collider */
    public final int id;
    /**the position of the center of the collider */
    private Vec2 position;
    /** the scale of the sides of the collider */
    private Vec2 scale;
    /** the velocity of the collider (in units/sec) */
    private Vec2 velocity;
    /** true if the collider can be moved by the physics engine */
    private boolean isMovable;
    /** true if the collider can collide with other colliders */
    private boolean isSolid;
    /** the tag of the collider */
    private String tag;
    /** the list of the collisions in which the collider was involved in the last physics update */
    private ArrayList<CollisionInfo> lastCollisionInfo;

    public Collider()
    {
        id=nextId++;
        position=new Vec2();
        scale=new Vec2(1);
        velocity=new Vec2();
        isSolid=true;
        isMovable=false;
        tag="";
        lastCollisionInfo=new ArrayList<>();
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
        lastCollisionInfo=new ArrayList<>();
    }

    /** clear the collision history of the collider. only the physics engine should call this */
    public void clearCollisionHistory()
    {
        lastCollisionInfo.clear();
    }

    //getters n setters
    /** returns a reference to the position of the collider */
    public Vec2 getPosition(){return this.position;}//doesn't clone return value
    /** returns a reference to the scale of the collider */
    public Vec2 getScale() {return this.scale;}//doesn't clone return value
    /** returns a reference to the velocity of the collider */
    public Vec2 getVelocity(){return this.velocity;}//doesn't clone return value
    public String getTag(){return this.tag;}
    /** returns an array of the infos about the collisions in which the collider partake in the last phyiscs update. it is safe to modify */
    public String[] getLastCollisionTags(){return (String[])this.lastCollisionInfo.toArray();}
    public boolean isSolid(){return this.isSolid;}
    public boolean isMovable(){return this.isMovable;}

    public void setPosition(Vec2 position) {this.position=position.clone();}
    public void setScale(Vec2 scale) {this.scale=scale.clone();}
    public void setVelocity(Vec2 velocity) {this.velocity=velocity.clone();}
    public void setTag(String tag){this.tag=tag;}
    public void setSolidity(boolean isSolid){this.isSolid=isSolid;}
    public void setMovability(boolean isMovable){this.isMovable=isMovable;}

    //static
    private static int nextId=1;

    /**resolves collisions between two colliders
     * @param noResolution true if the collision should not be resolved, only detected*/
    public static void resolveCollision(Collider c1, Collider c2, boolean noResolution)
    {
        if(c1.isMovable&&!c2.isMovable)
            resolveCollisionMN(c1, c2, noResolution);
        else if(!c1.isMovable&&c2.isMovable)
            resolveCollisionMN(c2,c1,noResolution);
        else if(c1.isMovable&&c2.isMovable)
            resolveCollisionMM(c1, c2, noResolution);
    }

    /**resolves the collision between a movable and an immovable collider. */
    private static void resolveCollisionMN(Collider c1, Collider c2, boolean noResolution)//collision resolution where c1 is movable and c2 is not
    {
        boolean shouldResolve=c1.isSolid&&c2.isSolid&&!noResolution;

        float minDistanceX=0.5f*c1.scale.x+0.5f*c2.scale.x;
        float minDistanceY=0.5f*c1.scale.y+0.5f*c2.scale.y;

        float distanceX=c2.position.x-c1.position.x;
        float distanceY=c2.position.y-c1.position.y;

        float penetrationX=minDistanceX-Math.abs(distanceX);
        float penetrationY=minDistanceY-Math.abs(distanceY);

        if(penetrationX<=0||penetrationY<=0)//colliders don't touch
            return;

        //resolve in the direction of the smaller penetration
        if(penetrationX<penetrationY)
        {
            if(shouldResolve)
            {
                c1.velocity.x=0;

                if(c1.position.x<c2.position.x)
                {
                    c1.position.x-=penetrationX;

                    c1.lastCollisionInfo.add(new CollisionInfo(c2.tag, c2.id, new Vec2(-1,0)));
                    c2.lastCollisionInfo.add(new CollisionInfo(c1.tag, c1.id, new Vec2(1,0)));
                }
                else
                {
                    c1.position.x+=penetrationX;

                    c1.lastCollisionInfo.add(new CollisionInfo(c2.tag, c2.id, new Vec2(1,0)));
                    c2.lastCollisionInfo.add(new CollisionInfo(c1.tag, c1.id, new Vec2(-1,0)));
                }
            }
            else
            {
                c1.lastCollisionInfo.add(new CollisionInfo(c2.tag,c2.id));
                c2.lastCollisionInfo.add(new CollisionInfo(c1.tag,c1.id));
            }
        }
        else
        {
            if(shouldResolve)
            {
                c1.velocity.y=0;

                if(c1.position.y<c2.position.y)
                {
                    c1.position.y-=penetrationY;

                    c1.lastCollisionInfo.add(new CollisionInfo(c2.tag, c2.id, new Vec2(0,-1)));
                    c2.lastCollisionInfo.add(new CollisionInfo(c1.tag, c1.id, new Vec2(0,1)));
                }
                else
                {
                    c1.position.y+=penetrationY;

                    c1.lastCollisionInfo.add(new CollisionInfo(c2.tag, c2.id, new Vec2(0,1)));
                    c2.lastCollisionInfo.add(new CollisionInfo(c1.tag, c1.id, new Vec2(0,-1)));
                }
            }
            else
            {
                c1.lastCollisionInfo.add(new CollisionInfo(c2.tag,c2.id));
                c2.lastCollisionInfo.add(new CollisionInfo(c1.tag,c1.id));
            }
        }
    }

    /** the collision happens between two movable colliders. if resolution happens, the colliders will be moved equally */
    private static void resolveCollisionMM(Collider c1, Collider c2, boolean noResolution)//collision resolution where both c1 and c2 are movable
    {
        boolean shouldResolve=c1.isSolid&&c2.isSolid&&!noResolution;

        float minDistanceX=0.5f*c1.scale.x+0.5f*c2.scale.x;
        float minDistanceY=0.5f*c1.scale.y+0.5f*c2.scale.y;

        float distanceX=c2.position.x-c1.position.x;
        float distanceY=c2.position.y-c1.position.y;

        float penetrationX=minDistanceX-Math.abs(distanceX);
        float penetrationY=minDistanceY-Math.abs(distanceY);

        if(penetrationX<=0||penetrationY<=0)//colliders don't touch
            return;

        //resolve in the direction of the smaller penetration
        if(penetrationX<penetrationY)
        {
            if(shouldResolve)
            {
                c1.velocity.x=0;

                if(c1.position.x<c2.position.x)
                {
                    c1.position.x-=0.5f*penetrationX;
                    c2.position.x+=0.5f*penetrationX;

                    c1.lastCollisionInfo.add(new CollisionInfo(c2.tag, c2.id, new Vec2(-1,0)));
                    c2.lastCollisionInfo.add(new CollisionInfo(c1.tag, c1.id, new Vec2(1,0)));
                }
                else
                {
                    c1.position.x+=0.5f*penetrationX;
                    c2.position.x-=0.5f*penetrationX;

                    c1.lastCollisionInfo.add(new CollisionInfo(c2.tag, c2.id, new Vec2(1,0)));
                    c2.lastCollisionInfo.add(new CollisionInfo(c1.tag, c1.id, new Vec2(-1,0)));
                }
            }
            else
            {
                c1.lastCollisionInfo.add(new CollisionInfo(c2.tag,c2.id));
                c2.lastCollisionInfo.add(new CollisionInfo(c1.tag,c1.id));
            }
        }
        else
        {
            if(shouldResolve)
            {
                c1.velocity.y=0;

                if(c1.position.y<c2.position.y)
                {
                    c1.position.y-=0.5f*penetrationY;
                    c2.position.y+=0.5f*penetrationY;

                    c1.lastCollisionInfo.add(new CollisionInfo(c2.tag, c2.id, new Vec2(0,-1)));
                    c2.lastCollisionInfo.add(new CollisionInfo(c1.tag, c1.id, new Vec2(0,1)));
                }
                else
                {
                    c1.position.y+=0.5f*penetrationY;
                    c2.position.y-=0.5f*penetrationY;

                    c1.lastCollisionInfo.add(new CollisionInfo(c2.tag, c2.id, new Vec2(0,1)));
                    c2.lastCollisionInfo.add(new CollisionInfo(c1.tag, c1.id, new Vec2(0,-1)));
                }
            }
            else
            {
                c1.lastCollisionInfo.add(new CollisionInfo(c2.tag,c2.id));
                c2.lastCollisionInfo.add(new CollisionInfo(c1.tag,c1.id));
            }
        }
    }
}
