package main.java.org.game.physics;

import main.java.org.linalg.Vec2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ColliderTest {

    Collider collider1;
    Collider collider2;
    Collider movable;
    Collider immovable;

    @BeforeEach
    public void setUp() {
        collider1 = new Collider(new Vec2(1, 1), new Vec2(4, 4));
        collider2 = new Collider(new Vec2(3, 1), new Vec2(1, 1));
    }

    @Test
    public void testConstructor() {
        assertEquals(1, collider1.getPosition().x);
        assertEquals(1, collider1.getPosition().y);
        assertEquals(4, collider1.getScale().x);
        assertEquals(4, collider1.getScale().y);
        assertTrue(collider1.isSolid());
        assertFalse(collider1.isMovable());
    }

    @Test
    public void testCollision() {
        Collider.resolveCollision(collider1, collider2, false);
        // Assume that collisions modify the position based on some logic; check for that logic here.
        assertNotNull(collider1.getLastCollisionInfo());
    }

    @Test
    public void testSettersAndGetters() {
        collider1.setPosition(new Vec2(5, 5));
        collider1.setScale(new Vec2(2, 2));
        collider1.setVelocity(new Vec2(1, 1));
        collider1.setTag("Player");
        collider1.setSolidity(false);
        collider1.setMovability(true);

        assertEquals(5, collider1.getPosition().x);
        assertEquals(5, collider1.getPosition().y);
        assertEquals(2, collider1.getScale().x);
        assertEquals(2, collider1.getScale().y);
        assertEquals(1, collider1.getVelocity().x);
        assertEquals(1, collider1.getVelocity().y);
        assertEquals("Player", collider1.getTag());
        assertFalse(collider1.isSolid());
        assertTrue(collider1.isMovable());
    }

    @Test
    public void testClearCollisionHistory() {
        collider1.clearCollisionHistory();
        assertEquals(0, collider1.getLastCollisionInfo().length);
    }
    @Test
    public void testCollisionResolutionX() {
        Collider c1 = new Collider(new Vec2(0, 0), new Vec2(2, 2));
        Collider c2 = new Collider(new Vec2(1, 0), new Vec2(2, 2));
        Collider.resolveCollisionMN(c1, c2, false);
        assertEquals(0, c1.getVelocity().x);
        assertTrue(Math.abs(c1.getPosition().x) > 0);  // Position should change
        assertTrue(c1.getLastCollisionInfo().length > 0);
        assertTrue(c2.getLastCollisionInfo().length > 0);
    }


    @Test
    public void testCollisionResolutionY() {
        Collider c1 = new Collider(new Vec2(0, 0), new Vec2(2, 2));
        Collider c2 = new Collider(new Vec2(0, 1), new Vec2(2, 2));
        Collider.resolveCollisionMN(c1, c2, false);
        assertEquals(0, c1.getVelocity().y);
        assertTrue(Math.abs(c1.getPosition().y) > 0);  // Position should change
        assertTrue(c1.getLastCollisionInfo().length >0);
        assertTrue(c2.getLastCollisionInfo().length >0);
    }

    @Test
    public void testNoCollision() {
        Collider c1 = new Collider(new Vec2(0, 0), new Vec2(1, 1));
        Collider c2 = new Collider(new Vec2(5, 5), new Vec2(1, 1));
        Collider.resolveCollisionMN(c1, c2, false);
        assertEquals(0, c1.getLastCollisionInfo().length);
        assertEquals(0, c2.getLastCollisionInfo().length);
    }
    @Test
    public void testCollisionNoResolution() {
        Collider c1 = new Collider(new Vec2(0, 0), new Vec2(2, 2));
        Collider c2 = new Collider(new Vec2(1, 1), new Vec2(2, 2));
        c1.setSolidity(false);
        c2.setSolidity(false);
        Collider.resolveCollisionMN(c1, c2, true);
        assertTrue(c1.getLastCollisionInfo().length > 0);
        assertTrue(c2.getLastCollisionInfo().length > 0);
    }

}
