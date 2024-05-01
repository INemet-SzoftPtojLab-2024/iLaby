package main.java.org.game.physics;

import main.java.org.linalg.Vec2;

import java.util.ArrayList;

/** a class that handles the collision detection */
public class PhysicsEngine
{
    /** the list of the colliders that are simulated by the physics engine */
    private ArrayList<Collider> simulatedColliders;
    /** the list of the colliderGroups that are simulated by the physics engine */
    private ArrayList<ColliderGroup> colliderGroups;

    public PhysicsEngine()
    {
        simulatedColliders=new ArrayList<>();
        colliderGroups=new ArrayList<>();
    }

    /** steps the physics simulation by deltaTime. the value of the deltaTime is in seconds <br>
     *  the simulation happens so: <br>
     *  - the collision history of the colliders is reset <br>
     *  - the colliders are moved by their velocity <br>
     *  - collisions between simulated colliders and the colliders in the collider group, in which the simulated collider is, will be resolved <br>
     *  - if two simulated colliders overlap, their collision history will be also updated, but collision between them is not resolved*/
    public void step(double deltaTime)
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

            c.setPosition(Vec2.sum(c.getPosition(),new Vec2((float)((double)c.getVelocity().x*deltaTime), (float)((double)c.getVelocity().y*deltaTime))));
        }

        //resolve collisions between colliders in collidergroups and simulated colliders
        for(Collider c:simulatedColliders)
        {
            for(ColliderGroup cg :colliderGroups)
            {
                if(!cg.isColliderInBounds(c))
                    continue;

                ColliderGroup.resolveCollision(cg,c,false);
            }
        }

        //resolve collisions between simulated colliders
        int length=simulatedColliders.size();
        for(int i=0;i<length;i++)
        {
            for(int j=i+1;j<length;j++)
            {
                Collider.resolveCollision(simulatedColliders.get(i), simulatedColliders.get(j),true);
            }
        }
    }

    /** deletes the current state of the physics engine */
    public void reset()
    {
        simulatedColliders.clear();
        colliderGroups.clear();
    }

    /** gets a simulated collider by its id. if the collider is not found, the function returns null */
    public Collider getCollider(int colliderId)
    {
        for(int i=0;i<simulatedColliders.size();i++)
            if(colliderId==simulatedColliders.get(i).id)
                return simulatedColliders.get(i);
        return null;
    }

    /** adds a collider to the list of the simulated colliders */
    public void addCollider(Collider c)
    {
        if(c==null)
            return;
        simulatedColliders.add(c);
    }

    /** removes a collider from the list of the simulated colliders */
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

    /** removes a collider from the list of the simulated colliders */
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

    /** gets a colliderGroup from the list of the colliders groups. if the collider group is not found, returns null */
    public ColliderGroup getColliderGroup(int colliderGroupId)
    {
        for(int i=0;i<colliderGroups.size();i++)
            if(colliderGroupId==colliderGroups.get(i).id)
                return colliderGroups.get(i);
        return null;
    }

    /** adds a colliderGroup to the list of the colliderGroups */
    public void addColliderGroup(ColliderGroup cg)
    {
        if(cg==null)
            return;
        colliderGroups.add(cg);
    }

    /** removes a colliderGroup from the list of the colliderGroups */
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

    /** removes a colliderGroup from the list of the colliderGroups */
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
