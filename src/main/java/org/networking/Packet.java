package main.java.org.networking;

public abstract class Packet {
    public static enum PacketTypes {
        INVALID(-1),
        LOGIN(00),
        DISCONNECT(01),
        MOVE(02),
        ANIMATION(03),
        UNITROOM(04),
        VILLAIN(05),
        VILLAINMOVE(06),
        TIMER(07),
        CHESTGENERATION(10),
        CHESTOPENED(11),
        ITEMPICKEDUP(12),
        ITEMDROPPED(13),
        WALL(20),
        DEATH(21),
        EDGEPIECECHANGED(22),
        WALLDELETE(23);
        private int packetId;
        private PacketTypes(int packetId) {
            this.packetId = packetId;
        }

        public int getId() {
            return packetId;
        }
    }


    public byte packetId;

    public Packet(int packetId) {
        this.packetId = (byte)packetId;
    }

    public abstract void writeData(GameClient client);
    public abstract void writeData(GameServer server);

    public String readData(byte[] data) {
        String message = new String(data).trim();
        //System.out.println(message);
        return message.substring(2);
    }


    public static PacketTypes lookupPacket(String packetId) {
        try {
            return lookupPacket(Integer.parseInt(packetId));
        }
        catch(NumberFormatException e) {
            return PacketTypes.INVALID;
        }

    }
    public static PacketTypes lookupPacket(int id) {
        for(PacketTypes p: PacketTypes.values()) {
            if(p.getId() == id) {
                return p;
            }
        }
        return PacketTypes.INVALID;
    }


    public abstract byte[] getData();
}
