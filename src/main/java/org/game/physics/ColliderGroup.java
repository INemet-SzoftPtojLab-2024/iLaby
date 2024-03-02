package main.java.org.game.physics;

import main.java.org.linalg.Vec2;

import java.util.ArrayList;

public class ColliderGroup
{
    public final int id;
    private ArrayList<Collider> colliders;
    Vec2 lowerBound, upperBound;//the lower left and upper right corner of the box, that contains all of the colliders in the collider group

    public ColliderGroup()
    {
        id=nextId++;
        colliders=new ArrayList<>();
        lowerBound=new Vec2();
        upperBound=new Vec2();
    }

    public ArrayList<Collider> getColliders()//returns a reference to the colliders list
    {
        return colliders;
    }

    public Collider getCollider(int colliderId)
    {
        for(int i=0;i<colliders.size();i++)
            if(colliders.get(i).id==colliderId)
                return colliders.get(i);
        return null;
    }

    public void addCollider(Collider c)
    {
        if(c==null)
            return;

        colliders.add(c);

        Vec2 pos=c.getPosition().clone();
        Vec2 scale=c.getScale().clone();

        Vec2 wannabeLowerBound=new Vec2(pos.x-0.5f*scale.x, pos.y-0.5f*scale.y);
        Vec2 wannabeUpperBound=new Vec2(pos.x+0.5f*scale.x, pos.y+0.5f*scale.y);

        //updating bounding box if necessary
        if(colliders.size()==1)
        {
            lowerBound=wannabeLowerBound;
            upperBound=wannabeUpperBound;
        }
        else
        {
            if(wannabeLowerBound.x<lowerBound.x)
                lowerBound.x=wannabeLowerBound.x;
            if(wannabeLowerBound.y<lowerBound.y)
                lowerBound.y=wannabeLowerBound.y;
            if(wannabeUpperBound.x>upperBound.x)
                upperBound.x=wannabeUpperBound.x;
            if(wannabeUpperBound.y>upperBound.y)
                upperBound.y= wannabeUpperBound.y;
        }
    }

    public void removeCollider(Collider c)
    {
        boolean found=false;
        for(int i=0;i<colliders.size();i++)
        {
            if(colliders.get(i)==c)
            {
                colliders.remove(i);
                found=true;
                break;
            }
        }

        if(found)
            updateBounds();
    }

    public void removeCollider(int colliderId)
    {
        boolean found=false;
        for(int i=0;i<colliders.size();i++)
        {
            if(colliders.get(i).id==colliderId)
            {
                colliders.remove(i);
                found=true;
                break;
            }
        }

        if(found)
            updateBounds();
    }

    private void updateBounds()
    {
        if(colliders.isEmpty())
        {
            lowerBound=new Vec2();
            upperBound=new Vec2();
            return;
        }

        Vec2 lower=Vec2.subtract(colliders.get(0).getPosition(),Vec2.scale(colliders.get(0).getScale(),0.5f));
        Vec2 upper=Vec2.sum(colliders.get(0).getPosition(),Vec2.scale(colliders.get(0).getScale(),0.5f));

        for(int i=1;i<colliders.size();i++)
        {
            Vec2 tempLower=Vec2.subtract(colliders.get(i).getPosition(),Vec2.scale(colliders.get(i).getScale(),0.5f));
            Vec2 tempUpper=Vec2.sum(colliders.get(i).getPosition(),Vec2.scale(colliders.get(i).getScale(),0.5f));

            if(tempLower.x<lower.x)
                lower.x=tempLower.x;
            if(tempLower.y<lower.y)
                lower.y=tempLower.y;
            if(tempUpper.x>upper.x)
                upper.x=tempUpper.x;
            if(tempUpper.y>upper.y)
                upper.y=tempUpper.y;
        }

        lowerBound=lower;
        upperBound=upper;
    }

    public boolean isColliderInBounds(Collider c)//is the collider in the bounds of the colliderGroup
    {
        Vec2 cLower=Vec2.subtract(c.getPosition(),Vec2.scale(c.getScale(),0.5f));
        Vec2 cUpper=Vec2.sum(c.getPosition(),Vec2.scale(c.getScale(),0.5f));

        if(cLower.x>upperBound.x)
            return false;
        if(cUpper.x<lowerBound.x)
            return false;
        if(cLower.y>upperBound.y)
            return false;
        if(cUpper.y<lowerBound.y)
            return false;
        return true;
    }

    private static int nextId=0;
}
