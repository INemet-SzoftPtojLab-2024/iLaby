package main.java.org.game.physics;

import main.java.org.linalg.Vec2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColliderGroupTest
{
    @Test
    public void testAddAndRemoveCollider() {
        ColliderGroup colliderGroup = new ColliderGroup();
        Collider collider1 = new Collider();
        Collider collider2 = new Collider();

        assertTrue(colliderGroup.getColliders().isEmpty());

        colliderGroup.addCollider(collider1);
        assertEquals(1, colliderGroup.getColliders().size());
        assertTrue(colliderGroup.getColliders().contains(collider1));

        colliderGroup.addCollider(collider2);
        assertEquals(2, colliderGroup.getColliders().size());
        assertTrue(colliderGroup.getColliders().contains(collider2));

        colliderGroup.removeCollider(collider1);
        assertEquals(1, colliderGroup.getColliders().size());
        assertFalse(colliderGroup.getColliders().contains(collider1));

        colliderGroup.removeCollider(collider2.getId());
        assertEquals(0, colliderGroup.getColliders().size());
        assertFalse(colliderGroup.getColliders().contains(collider2));
    }
    @Test
    public void testUpdateBounds() {
        ColliderGroup colliderGroup = new ColliderGroup();
        Collider collider1 = new Collider();
        Collider collider2 = new Collider();

        colliderGroup.addCollider(collider1);
        colliderGroup.addCollider(collider2);

        collider1.setPosition(new Vec2(5.0f, 5.0f));
        collider1.setScale(new Vec2(2.0f, 2.0f));
        collider2.setPosition(new Vec2(-5.0f, -5.0f));
        collider2.setScale(new Vec2(3.0f, 3.0f));

        colliderGroup.updateBounds();

        float expectedNewLowerBoundX = Math.min(collider1.getPosition().x - 0.5f * collider1.getScale().x,
                collider2.getPosition().x - 0.5f * collider2.getScale().x);
        float expectedNewLowerBoundY = Math.min(collider1.getPosition().y - 0.5f * collider1.getScale().y,
                collider2.getPosition().y - 0.5f * collider2.getScale().y);
        float expectedNewUpperBoundX = Math.max(collider1.getPosition().x + 0.5f * collider1.getScale().x,
                collider2.getPosition().x + 0.5f * collider2.getScale().x);
        float expectedNewUpperBoundY = Math.max(collider1.getPosition().y + 0.5f * collider1.getScale().y,
                collider2.getPosition().y + 0.5f * collider2.getScale().y);

        assertEquals(expectedNewLowerBoundX, colliderGroup.getLowerBound().x);
        assertEquals(expectedNewLowerBoundY, colliderGroup.getLowerBound().y);
        assertEquals(expectedNewUpperBoundX, colliderGroup.getUpperBound().x);
        assertEquals(expectedNewUpperBoundY, colliderGroup.getUpperBound().y);
    }
    @Test
    public void testIsColliderInBounds() {
        ColliderGroup colliderGroup = new ColliderGroup();
        Collider collider1 = new Collider();
        Collider collider2 = new Collider();

        colliderGroup.addCollider(collider1);
        colliderGroup.addCollider(collider2);

        assertTrue(colliderGroup.isColliderInBounds(collider1));

        assertTrue(colliderGroup.isColliderInBounds(collider2));

        collider1.setPosition(new Vec2(colliderGroup.getUpperBound().x + 1.0f, collider1.getPosition().y));
        assertFalse(colliderGroup.isColliderInBounds(collider1));

        collider1.setPosition(new Vec2(collider1.getPosition().x, colliderGroup.getUpperBound().y + 1.0f));
        assertFalse(colliderGroup.isColliderInBounds(collider1));

        collider2.setPosition(new Vec2(colliderGroup.getLowerBound().x - 1.0f, collider2.getPosition().y));
        assertFalse(colliderGroup.isColliderInBounds(collider2));

        collider2.setPosition(new Vec2(collider2.getPosition().x, colliderGroup.getLowerBound().y - 1.0f));
        assertFalse(colliderGroup.isColliderInBounds(collider2));
    }
}