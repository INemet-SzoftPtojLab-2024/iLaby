package main.java.org.game.physics;

import main.java.org.linalg.Vec2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PhysicsEngineTest {
    @Test
    public void testStep() {
        PhysicsEngine physicsEngine = new PhysicsEngine();

        Collider collider1 = new Collider();
        Collider collider2 = new Collider();
        physicsEngine.addCollider(collider1);
        physicsEngine.addCollider(collider2);

        //assertNotNull(physicsEngine.getCollider(1));
        //assertNotNull(physicsEngine.getCollider(2));
        //assertNull(physicsEngine.getCollider(3));

        ColliderGroup colliderGroup1 = new ColliderGroup();
        colliderGroup1.addCollider(collider1);
        colliderGroup1.addCollider(collider2);
        physicsEngine.addColliderGroup(colliderGroup1);

        CollisionInfo [] collInfo1 = collider1.getLastCollisionInfo();
        CollisionInfo [] collInfo2 = collider2.getLastCollisionInfo();

        physicsEngine.step(0.1);

        assertEquals(0, collInfo1.length);
        assertEquals(0, collInfo2.length);

        Vec2 initialPosition1 = collider1.getPosition();
        Vec2 initialVelocity1 = collider1.getVelocity();

        Vec2 newPosition1 = collider1.getPosition();
        assertEquals(initialPosition1.x + initialVelocity1.x * 0.1, newPosition1.x, 0.0001);


        ArrayList<Collider> simulatedColliders = physicsEngine.getSimulatedColliders();
        ArrayList<ColliderGroup> colliderGroups = physicsEngine.getColliderGroups();

        physicsEngine.reset();

        assertTrue(simulatedColliders.isEmpty());
        assertTrue(colliderGroups.isEmpty());
    }
    @Test
    public void testAddColliderGroup() {
        PhysicsEngine physicsEngine = new PhysicsEngine();
        ColliderGroup colliderGroup = new ColliderGroup();

        physicsEngine.addColliderGroup(colliderGroup);
        assertTrue(physicsEngine.getColliderGroups().contains(colliderGroup));
    }

}