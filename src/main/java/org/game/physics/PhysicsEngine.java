package main.java.org.game.physics;

import main.java.org.linalg.Vec2;

import java.util.ArrayList;

public class PhysicsEngine
{
    private ArrayList<Collider> simulatedColliders;
    private ArrayList<ColliderGroup> colliderGroups;

    public PhysicsEngine()
    {
        simulatedColliders=new ArrayList<>();
        colliderGroups=new ArrayList<>();
    }

    public void step(float deltaTime)
    {
        //clear collision tags
        for(Collider c : simulatedColliders)
            c.clearCollisionHistory();
        for(ColliderGroup cg: colliderGroups)
            for(Collider c: cg.getColliders())
                c.clearCollisionHistory();

        //move simulated colliders based on their velocity
        for(Collider c : simulatedColliders)
        {
            if(!c.isMovable())
                continue;

            c.setPosition(Vec2.scale(c.getVelocity(),deltaTime));
        }

        //resolve collisions between colliders in collidergroups and simulated colliders
        for(Collider c:simulatedColliders)
        {
            for(ColliderGroup cg :colliderGroups)
            {
                if(!cg.isColliderInBounds(c))
                    continue;
                for(Collider ck : cg.getColliders())
                    Collider.resolveCollision(c,ck);
            }
        }

        //resolve collisions between simulated colliders
        //TODO if necessary
    }

    public void reset()
    {
        simulatedColliders.clear();
        colliderGroups.clear();
    }

    public Collider getCollider(int colliderId)
    {
        for(int i=0;i<simulatedColliders.size();i++)
            if(colliderId==simulatedColliders.get(i).id)
                return simulatedColliders.get(i);
        return null;
    }

    public void addCollider(Collider c)
    {
        if(c==null)
            return;
        simulatedColliders.add(c);
    }

    public void removeCollider(Collider c)
    {
        for(int i=0;i<simulatedColliders.size();i++)
        {
            if(c==simulatedColliders.get(i))
            {
                simulatedColliders.remove(i);
                break;
            }
        }
    }

    public void removeCollider(int colliderId)
    {
        for(int i=0;i<simulatedColliders.size();i++)
        {
            if(colliderId==simulatedColliders.get(i).id)
            {
                simulatedColliders.remove(i);
                break;
            }
        }
    }


    public ColliderGroup getColliderGroup(int colliderGroupId)
    {
        for(int i=0;i<colliderGroups.size();i++)
            if(colliderGroupId==colliderGroups.get(i).id)
                return colliderGroups.get(i);
        return null;
    }

    public void addColliderGroup(ColliderGroup cg)
    {
        if(cg==null)
            return;
        colliderGroups.add(cg);
    }

    public void removeColliderGroup(ColliderGroup cg)
    {
        for(int i=0;i<colliderGroups.size();i++)
        {
            if(cg==colliderGroups.get(i))
            {
                colliderGroups.remove(i);
                break;
            }
        }
    }

    public void removeColliderGroup(int colliderGroupId)
    {
        for(int i=0;i<colliderGroups.size();i++)
        {
            if(colliderGroupId==colliderGroups.get(i).id)
            {
                colliderGroups.remove(i);
                break;
            }
        }
    }
}
