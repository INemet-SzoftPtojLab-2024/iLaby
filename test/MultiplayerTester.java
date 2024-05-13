import main.java.org.game.Isten;
import main.java.org.linalg.Vec2;
import main.java.org.networking.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class MultiplayerTester {

    @Test
    public void testPacket00Login() {
        byte[] data = ("00" + "username" + "," +1 + "," + 1 + "," + 1).getBytes();
        Packet00Login packet00Login1 = new Packet00Login("username", 1,1,1);
        Packet00Login packet00Login2 = new Packet00Login(data);

        assertEquals(packet00Login1.getUsername(), packet00Login2.getUsername());
        assertEquals(packet00Login1.getX(), packet00Login2.getX());
        assertEquals(packet00Login1.getY(), packet00Login2.getY());
        assertEquals(packet00Login1.getSkinID(), packet00Login2.getSkinID());
        assertArrayEquals(packet00Login1.getData(), packet00Login2.getData());
    }

    @Test
    public void testPacket01Disconnect() {
        byte[] data = ("01" + "username").getBytes();
        Packet01Disconnect packet01Disconnect1 = new Packet01Disconnect("username");
        Packet01Disconnect packet01Disconnect2 = new Packet01Disconnect(data);

        assertEquals(packet01Disconnect1.getUsername(), packet01Disconnect2.getUsername());
        assertArrayEquals(packet01Disconnect1.getData(), packet01Disconnect2.getData());
    }

    @Test
    public void testPacket02Constructor() {
        byte[] data = ("02" + "username" + "," + 1 + "," + 1).getBytes();
        Packet02Move packet02Move1 = new Packet02Move("username", 1, 1);
        Packet02Move packet02Move2 = new Packet02Move(data);

        assertEquals(packet02Move1.getUsername(), packet02Move2.getUsername());
        assertEquals(packet02Move1.getX(), packet02Move2.getX());
        assertEquals(packet02Move1.getY(), packet02Move2.getY());
        assertArrayEquals(packet02Move1.getData(), packet02Move2.getData());
    }

    @Test
    public void testPacket03Constructor() {
        byte[] data = ("03" + "username" + "," + 1).getBytes();
        Packet03Animation packet03Animation1 = new Packet03Animation("username", 1);
        Packet03Animation packet03Animation2 = new Packet03Animation(data);

        assertEquals(packet03Animation1.getUsername(), packet03Animation2.getUsername());
        assertEquals(packet03Animation1.getActiveImage(), packet03Animation2.getActiveImage());
        assertArrayEquals(packet03Animation1.getData(), packet03Animation2.getData());
    }

    @Test
    public void testPacket04Constructor() {
        byte[] data = ("04" + 1 + "," + 1 + "," + 1).getBytes();
        Packet04UnitRoom packet04UnitRoom1 = new Packet04UnitRoom(1,1,1);
        Packet04UnitRoom packet04UnitRoom2 = new Packet04UnitRoom(data);

        assertEquals(packet04UnitRoom1.getX(), packet04UnitRoom2.getX());
        assertEquals(packet04UnitRoom1.getY(), packet04UnitRoom2.getY());
        assertEquals(packet04UnitRoom1.getType(), packet04UnitRoom2.getType());
        assertArrayEquals(packet04UnitRoom1.getData(), packet04UnitRoom2.getData());
    }

    @Test
    public void testPacket05Constructor() {
        byte[] data = ("05" + "villain" + "," + 1 + "," + 1 + "," + "imgpath").getBytes();
        Packet05Villain packet05Villain1 = new Packet05Villain("villain", new Vec2(1,1), "imgpath");
        Packet05Villain packet05Villain2 = new Packet05Villain(data);

        assertEquals(packet05Villain1.getVillainName(), packet05Villain2.getVillainName());
        assertEquals(packet05Villain1.getPosition().x, packet05Villain2.getPosition().x);
        assertEquals(packet05Villain1.getPosition().y, packet05Villain2.getPosition().y);
        assertEquals(packet05Villain1.getImagePath(), packet05Villain2.getImagePath());
        assertArrayEquals(packet05Villain1.getData(), packet05Villain2.getData());
    }

    @Test
    public void testPacket06Constructor() {
        byte[] data = ("06" + "villain" + "," + 1 + "," + 1).getBytes();
        Packet06VillainMove packet06VillainMove1 = new Packet06VillainMove("villain", 1, 1);
        Packet06VillainMove packet06VillainMove2 = new Packet06VillainMove(data);

        assertEquals(packet06VillainMove1.getVillainName(), packet06VillainMove2.getVillainName());
        assertEquals(packet06VillainMove1.getX(), packet06VillainMove2.getX());
        assertEquals(packet06VillainMove1.getY(), packet06VillainMove2.getY());
        assertArrayEquals(packet06VillainMove1.getData(), packet06VillainMove2.getData());
    }

    @Test
    public void testPacket07Timer() {
        byte[] data = ("07" + 1).getBytes();
        Packet07Timer packet07Timer1 = new Packet07Timer(1);
        Packet07Timer packet07Timer2 = new Packet07Timer(data);

        assertEquals(packet07Timer1.getTimeRemaining(), packet07Timer2.getTimeRemaining());
        assertArrayEquals(packet07Timer1.getData(), packet07Timer2.getData());
    }

    @Test
    public void testPacket10ChestGeneration() {
        byte[] data = ("10" + 1 + "," + 1 + "," + 1 + "," + 1 + "," + 1).getBytes();
        Packet10ChestGeneration packet10ChestGeneration1 = new Packet10ChestGeneration(1, new Vec2(1,1), 1, 1);
        Packet10ChestGeneration packet10ChestGeneration2 = new Packet10ChestGeneration(data);

        assertEquals(packet10ChestGeneration1.getWallLocation(), packet10ChestGeneration2.getWallLocation());
        assertEquals(packet10ChestGeneration1.getPos().x, packet10ChestGeneration2.getPos().x);
        assertEquals(packet10ChestGeneration1.getPos().y, packet10ChestGeneration2.getPos().y);
        assertEquals(packet10ChestGeneration1.getChestType(),packet10ChestGeneration2.getChestType());
        assertEquals(packet10ChestGeneration1.getIdx(),packet10ChestGeneration2.getIdx());
        assertArrayEquals(packet10ChestGeneration1.getData(), packet10ChestGeneration2.getData());
    }

    @Test
    public void testPacket11Constructor() {
        byte[] data = ("11" + 1 + "," + 1 + "," + 1).getBytes();
        Packet11ChestOpened packet11ChestOpened1 = new Packet11ChestOpened(1,1,1);
        Packet11ChestOpened packet11ChestOpened2 = new Packet11ChestOpened(data);

        assertEquals(packet11ChestOpened1.getChestIndex(), packet11ChestOpened2.getChestIndex());
        assertEquals(packet11ChestOpened1.getItemIndex1(), packet11ChestOpened2.getItemIndex1());
        assertEquals(packet11ChestOpened1.getItemIndex2(), packet11ChestOpened2.getItemIndex2());
        assertArrayEquals(packet11ChestOpened1.getData(), packet11ChestOpened2.getData());
    }

    @Test
    public void testPacket12ItemPickedUp() {
        byte[] data = ("12" + 1 + "," + "username" + "," + 1).getBytes();
        Packet12ItemPickedUp packet12ItemPickedUp1 = new Packet12ItemPickedUp(1,"username",1);
        Packet12ItemPickedUp packet12ItemPickedUp2 = new Packet12ItemPickedUp(data);

        assertEquals(packet12ItemPickedUp1.getItemIndex(), packet12ItemPickedUp2.getItemIndex());
        assertEquals(packet12ItemPickedUp1.getUsername(), packet12ItemPickedUp2.getUsername());
        assertEquals(packet12ItemPickedUp1.getSelectedSlot(), packet12ItemPickedUp2.getSelectedSlot());
        assertArrayEquals(packet12ItemPickedUp1.getData(), packet12ItemPickedUp2.getData());
    }

    @Test
    public void testPacket13ItemDropped() {
        byte[] data = ("13" + 1 + "," + 1 + "," + 1 + "," + "username" + "," + 1 + "," + true).getBytes();
        Packet13ItemDropped packet13ItemDropped1 = new Packet13ItemDropped(1, new Vec2(1,1), "username", 1, true);
        Packet13ItemDropped packet13ItemDropped2 = new Packet13ItemDropped(data);

        assertEquals(packet13ItemDropped1.getItemIndex(), packet13ItemDropped2.getItemIndex());
        assertEquals(packet13ItemDropped1.getPos().x, packet13ItemDropped2.getPos().x);
        assertEquals(packet13ItemDropped1.getPos().y, packet13ItemDropped2.getPos().y);
        assertEquals(packet13ItemDropped1.getUsername(), packet13ItemDropped2.getUsername());
        assertEquals(packet13ItemDropped1.getSelectedSlot(), packet13ItemDropped2.getSelectedSlot());
        assertArrayEquals(packet13ItemDropped1.getData(), packet13ItemDropped2.getData());
    }

    @Test
    public void testPacket14Gasmask() {
        byte[] data = ("14" + 1 + "," + 1).getBytes();
        Packet14Gasmask packet14Gasmask1 = new Packet14Gasmask(data);
        Packet14Gasmask packet14Gasmask2 = new Packet14Gasmask(data);

        assertEquals(packet14Gasmask1.getItemIndex(), packet14Gasmask2.getItemIndex());
        assertEquals(packet14Gasmask1.getCapacity(), packet14Gasmask2.getCapacity());
        assertArrayEquals(packet14Gasmask1.getData(), packet14Gasmask2.getData());
    }

    @Test
    public void testPacket15Tvsz() {
        byte[] data = ("15" + 1 + "," +1).getBytes();
        Packet15Tvsz packet15Tvsz1 = new Packet15Tvsz(1,1);
        Packet15Tvsz packet15Tvsz2 = new Packet15Tvsz(data);

        assertEquals(packet15Tvsz1.getItemIndex(), packet15Tvsz2.getItemIndex());
        assertEquals(packet15Tvsz1.getCharges(), packet15Tvsz2.getCharges());
        assertArrayEquals(packet15Tvsz1.getData(), packet15Tvsz2.getData());
    }

    @Test
    public void testPacket16Sorospohar() {
        byte[] data = ("16" + 1 + "," +1).getBytes();
        Packet16Sorospohar packet16Sorospohar1 = new Packet16Sorospohar(1,1);
        Packet16Sorospohar packet16Sorospohar2 = new Packet16Sorospohar(data);

        assertEquals(packet16Sorospohar1.getItemIndex(), packet16Sorospohar1.getItemIndex());
        assertEquals(packet16Sorospohar1.getCapacity(), packet16Sorospohar2.getCapacity());
        assertArrayEquals(packet16Sorospohar1.getData(), packet16Sorospohar2.getData());
    }

    @Test
    public void testPacket17Camembert() {
        byte[] data = ("17" + 1 + "," + 1 + "," + 1 + "," + "username").getBytes();
        Packet17Camembert packet17Camembert1 = new Packet17Camembert(1,1,1,"username");
        Packet17Camembert packet17Camembert2 = new Packet17Camembert(data);

        assertEquals(packet17Camembert1.getX(), packet17Camembert2.getX());
        assertEquals(packet17Camembert1.getY(), packet17Camembert2.getY());
        assertEquals(packet17Camembert1.getItemIndex(), packet17Camembert2.getItemIndex());
        assertEquals(packet17Camembert1.getUsername(), packet17Camembert2.getUsername());
        assertArrayEquals(packet17Camembert1.getData(),packet17Camembert2.getData());
        assertArrayEquals(packet17Camembert1.getData(),packet17Camembert2.getData());
    }

    @Test
    public void testPacket18Rongy() {
        byte[] data = ("18" + 1 + "," + 1 + "," + 1 + "," + "username" + "," + 1).getBytes();
        Packet18Rongy packet18Rongy1 = new Packet18Rongy(1,1,1,"username",1);
        Packet18Rongy packet18Rongy2 = new Packet18Rongy(data);

        assertEquals(packet18Rongy1.getX(), packet18Rongy2.getX());
        assertEquals(packet18Rongy1.getY(), packet18Rongy2.getY());
        assertEquals(packet18Rongy1.getItemIndex(), packet18Rongy2.getItemIndex());
        assertEquals(packet18Rongy1.getUsername(), packet18Rongy2.getUsername());
        assertEquals(packet18Rongy1.getImpactTime(), packet18Rongy2.getImpactTime());
        assertArrayEquals(packet18Rongy1.getData(), packet18Rongy2.getData());
        assertArrayEquals(packet18Rongy1.getData(), packet18Rongy2.getData());
    }

    @Test
    public void testPacket19Transistor() {
        byte[] data = ("19" + 1 + "," + 1 + "," + 1).getBytes();
        Packet19Transistor packet19Transistor1 = new Packet19Transistor(new Vec2(1,1),1);
        Packet19Transistor packet19Transistor2 = new Packet19Transistor(data);

        assertEquals(packet19Transistor1.getItemIndex(), packet19Transistor2.getItemIndex());
        assertEquals(packet19Transistor1.getX(), packet19Transistor2.getX());
        assertEquals(packet19Transistor1.getY(), packet19Transistor2.getY());
        assertArrayEquals(packet19Transistor1.getData(), packet19Transistor2.getData());
    }
}
