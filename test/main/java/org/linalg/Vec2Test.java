package main.java.org.linalg;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class Vec2Test {

    private static final float EPSILON = 0.00001f;

    @Test
    public void testConstructors() {
        Vec2 vec = new Vec2(3, 4);
        assertEquals(3, vec.x, EPSILON);
        assertEquals(4, vec.y, EPSILON);

        vec = new Vec2(5);
        assertEquals(5, vec.x, EPSILON);
        assertEquals(5, vec.y, EPSILON);

        vec = new Vec2();
        assertEquals(0, vec.x, EPSILON);
        assertEquals(0, vec.y, EPSILON);
    }

    @Test
    public void testScale() {
        Vec2 vec = new Vec2(3, 4);
        vec.scale(2);
        assertEquals(6, vec.x, EPSILON);
        assertEquals(8, vec.y, EPSILON);
    }

    @Test
    public void testSqrMagnitude() {
        Vec2 vec = new Vec2(3, 4);
        assertEquals(25, vec.sqrMagnitude(), EPSILON);
    }

    @Test
    public void testMagnitude() {
        Vec2 vec = new Vec2(3, 4);
        assertEquals(5, vec.magnitude(), EPSILON);
    }

    @Test
    public void testNormalize() {
        Vec2 vec = new Vec2(3, 4);
        vec.normalize();
        assertEquals(0.6, vec.x, EPSILON);
        assertEquals(0.8, vec.y, EPSILON);
    }

    @Test
    public void testClone() {
        Vec2 original = new Vec2(1, 2);
        Vec2 clone = original.clone();
        assertEquals(original.x, clone.x, EPSILON);
        assertEquals(original.y, clone.y, EPSILON);
        assertNotSame(original, clone);
    }

    @Test
    public void testStaticSum() {
        Vec2 a = new Vec2(1, 2);
        Vec2 b = new Vec2(3, 4);
        Vec2 result = Vec2.sum(a, b);
        assertEquals(4, result.x, EPSILON);
        assertEquals(6, result.y, EPSILON);
    }

    @Test
    public void testStaticSubtract() {
        Vec2 a = new Vec2(5, 6);
        Vec2 b = new Vec2(3, 4);
        Vec2 result = Vec2.subtract(a, b);
        assertEquals(2, result.x, EPSILON);
        assertEquals(2, result.y, EPSILON);
    }

    @Test
    public void testStaticDot() {
        Vec2 a = new Vec2(1, 3);
        Vec2 b = new Vec2(2, 4);
        float result = Vec2.dot(a, b);
        assertEquals(14, result, EPSILON);
    }

    @Test
    public void testStaticScale() {
        Vec2 vec = new Vec2(1, 2);
        Vec2 result = Vec2.scale(vec, 2);
        assertEquals(2, result.x, EPSILON);
        assertEquals(4, result.y, EPSILON);
    }

    @Test
    public void testStaticNormalize() {
        Vec2 vec = new Vec2(3, 4);
        Vec2 result = Vec2.normalize(vec);
        assertEquals(0.6, result.x, EPSILON);
        assertEquals(0.8, result.y, EPSILON);
    }

    @Test
    public void testStaticLerp() {
        Vec2 a = new Vec2(0, 0);
        Vec2 b = new Vec2(10, 10);
        Vec2 result = Vec2.lerp(a, b, 0.5f);
        assertEquals(5, result.x, EPSILON);
        assertEquals(5, result.y, EPSILON);
    }
}
