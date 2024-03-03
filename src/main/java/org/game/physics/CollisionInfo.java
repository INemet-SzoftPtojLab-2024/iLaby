package main.java.org.game.physics;

import main.java.org.linalg.Vec2;

/** infos about a collision between two colliders*/
public class CollisionInfo {
    /**the tag of the other collider. null if the tag is unknown*/
    public final String tag;
    /** the id of the other collider. -1 if the id is unknown*/
    public final int id;
    /** the normal of the surface womit the collider collided.
     * it's null, if the collision wasn't resolved (zBsp. one of the colliders is not solid)*/
    public final Vec2 collisionNormal;

    public CollisionInfo(String tag, int id, Vec2 collisionNormal)
    {
        this.tag=tag;
        this.id=id;
        this.collisionNormal=collisionNormal;
    }

    public CollisionInfo(String tag, int id)
    {
        this.tag=tag;
        this.id=id;
        this.collisionNormal=null;
    }

    public CollisionInfo(String tag, Vec2 collisionNormal)
    {
        this.tag=tag;
        this.id=-1;
        this.collisionNormal=collisionNormal;
    }

    public CollisionInfo(int id, Vec2 collisionNormal)
    {
        this.tag=null;
        this.id=id;
        this.collisionNormal=collisionNormal;
    }

    public CollisionInfo(String tag)
    {
        this.tag=tag;
        this.id=-1;
        this.collisionNormal=null;
    }

    public CollisionInfo(int id)
    {
        this.tag=null;
        this.id=id;
        this.collisionNormal=null;
    }
}
