import main.java.org.game.Isten;
import main.java.org.linalg.Vec2;
import main.java.org.networking.*;
import org.junit.Assert;
import org.junit.Test;

public class MultiplayerTester {

    @Test
    public void testPacket02Constructor() {
        Packet02Move packet02Move = new Packet02Move("username", 1, 1);
        Assert.assertEquals(packet02Move, new Packet02Move("username", 1, 1));
    }

    @Test
    public void testPacket03Constructor() {
        Packet03Animation packet03Animation = new Packet03Animation("username", 1);
        Assert.assertEquals(packet03Animation, new Packet03Animation("username", 1));
    }

    @Test
    public void testPacket04Constructor() {
        Packet04UnitRoom packet04UnitRoom = new Packet04UnitRoom(1,1,1);
        Assert.assertEquals(packet04UnitRoom, new Packet04UnitRoom(1,1,1));
    }

    @Test
    public void testPacket05Constructor() {
        Packet05Villain packet05Villain = new Packet05Villain("villain", new Vec2(1,1), "imgpath");
        Assert.assertEquals(packet05Villain, new Packet05Villain("villain", new Vec2(1,1), "imgpath"));
    }

    @Test
    public void testPacket06Constructor() {
        Packet06VillainMove packet06VillainMove = new Packet06VillainMove("villain1", 1, 1);
        Assert.assertEquals(packet06VillainMove, new Packet06VillainMove("villain1", 1, 1));
    }

    @Test
    public void testPacket07Constructor() {
        Packet07Timer packet07Timer = new Packet07Timer(2.0f);
        Assert.assertEquals(packet07Timer, new Packet07Timer(2.0f));
    }

    @Test
    public void testPacket21Constructor() {
        Packet21Death packet21Death = new Packet21Death("player1");
        Assert.assertEquals(packet21Death, new Packet21Death("player1"));
    }

    @Test
    public void testSentPacketEqualsArrivedPacket02() {
        Packet02Move packet = new Packet02Move("player1", 2.0f, 2.0f);
        byte[] data = "02player1,2.0,2.0".getBytes();
        Assert.assertEquals(new Packet02Move(data), packet);
    }

    @Test
    public void testSentPacketEqualsArrivedPacket03() {
        Packet03Animation packet = new Packet03Animation("player1", 1);
        byte[] data = "03player1,1".getBytes();
        Assert.assertEquals(new Packet03Animation(data), packet);
    }

    @Test
    public void testSentPacketEqualsArrivedPacket04() {
        Packet04UnitRoom packet = new Packet04UnitRoom(1,1,1);
        byte[] data = "041.0,1.0,1".getBytes();
        Assert.assertEquals(new Packet04UnitRoom(data), packet);
    }

    @Test
    public void testSentPacketEqualsArrivedPacket05() {
        Packet05Villain packet = new Packet05Villain("villain1", new Vec2(1,1), "imagePath");
        byte[] data = "05villain1,1.0,1.0,imagePath".getBytes();
        Assert.assertEquals(new Packet05Villain(data), packet);
    }

    @Test
    public void testSentPacketEqualsArrivedPacket06() {
        Packet06VillainMove packet = new Packet06VillainMove("villain1", 1.0f, 1.0f);
        byte[] data = "06villain1,1.0,1.0".getBytes();
        Assert.assertEquals(new Packet06VillainMove(data), packet);
    }

    @Test
    public void testSentPacketEqualsArrivedPacket07() {
        Packet07Timer packet = new Packet07Timer(3.0);
        byte[] data = "073.0".getBytes();
        Assert.assertEquals(new Packet07Timer(data), packet);
    }

    @Test
    public void testSentPacketEqualsArrivedPacket20() {
        Packet20Wall packet = new Packet20Wall(1.0f, 1.0f, 2.0f, 2.0f, true);
        byte[] data = "201.0,1.0,2.0,2.0,1".getBytes();
        Assert.assertEquals(new Packet20Wall(data), packet);
    }

    @Test
    public void testSentPacketEqualsArrivedPacket21() {
        Packet21Death packet = new Packet21Death("player1");
        byte[] data = "21player1".getBytes();
        Assert.assertEquals(new Packet21Death(data), packet);
    }

}
