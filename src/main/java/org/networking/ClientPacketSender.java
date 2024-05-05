package main.java.org.networking;

import main.java.org.game.Isten;

public class ClientPacketSender {
    private static Isten isten;

    public static void sendPacketToServer(Packet packet) {
        if(isten == null) return;
        packet.writeData(isten.getSocketClient());
    }

    public static void init(Isten isten) {
        ClientPacketSender.isten = isten;
    }
}
