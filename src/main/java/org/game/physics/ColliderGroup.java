package main.java.org.game.physics;

import main.java.org.linalg.Vec2;

import java.util.ArrayList;

public class ColliderGroup
{
    /** an id that is einzigartig for this colliderGroup */
    public final int id;
    /** list of colliders in the collider group */
    private ArrayList<Collider> colliders;

    /**the lower left corner of the box, that contains all of the colliders in the collider group*/
    private Vec2 lowerBound;
    /**the upper right corner of the box, that contains all of the colliders in the collider group*/
    private Vec2 upperBound;

    public ColliderGroup()
    {
        id=nextId++;
        colliders=new ArrayList<>();
        lowerBound=new Vec2();
        upperBound=new Vec2();
    }

    /** returns a reference to the list of the colliders in the group. the return value is NOT safe */
    public ArrayList<Collider> getColliders()//returns a reference to the colliders list
    {
        return colliders;
    }

    /** gets a collider by its id. returns null if the collider was not found */
    public Collider getCollider(int colliderId)
    {
        for(int i=0;i<colliders.size();i++)
            if(colliders.get(i).id==colliderId)
                return colliders.get(i);
        return null;
    }

    /** adds a collider to the colliderGroup */
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

    /** removes a collider */
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

    /** removes a collider based on its id */
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

    /** searches for the lowest and the highest coordinate in which the colliderGroup has colliders */
    public void updateBounds()
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

    /** returns true if the collider is in the bounds of the colliderGroup on which the function was called */
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

    private static int nextId=1;

    /** a function to calculate all the collisions between a collider and the colliders of a collider group */
    public static void resolveCollision(ColliderGroup cg, Collider c, boolean noResolution)
    {
        //filter out the colliders in the colliderGroup that are not touching the collider c
        Collider[] touching=(Collider[])cg.colliders.stream().filter(cgc->(Collider.getDistanceBetweenColliders(c,cgc)<0)).toArray(Collider[]::new);

        //sort the array by distance
        float[] distances=new float[touching.length];
        for(int i=0;i<distances.length;i++)
            distances[i]=Collider.getDistanceBetweenColliders(c,touching[i]);

        for(int i=0;i<touching.length;i++)
        {
            for (int j=i;j<touching.length-1;j++)
            {
                if(distances[j]>distances[j+1])
                {
                    Collider tempC=touching[j];
                    touching[j]=touching[j+1];
                    touching[j+1]=tempC;
                    float tempF=distances[j];
                    distances[j]=distances[j+1];
                    distances[j+1]=tempF;
                }
            }
        }

        //resolve collisions
        for(int i=0;i<touching.length;i++)
            Collider.resolveCollision(c,touching[i],noResolution);
    }

    public Vec2 getLowerBound() {
        return lowerBound;
    }

    public Vec2 getUpperBound() {
        return upperBound;
    }
}
