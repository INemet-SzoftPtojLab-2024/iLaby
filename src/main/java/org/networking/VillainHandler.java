package main.java.org.networking;

import main.java.org.entities.villain.Villain;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.linalg.Vec2;

import java.net.DatagramPacket;
import java.util.ArrayList;

//Handles villain creation and update for Server
public class VillainHandler {

    Isten isten;
    GameServer server;
    ArrayList<Villain> villains;

    public VillainHandler(GameServer server) {
        this.server = server;
        this.isten = server.isten;
        villains = new ArrayList<>();
    }

    public void createVillains() {
        villains.add(new Villain("Gonosz1", new Vec2(8,7), "./assets/villain/villain1.png"));
        villains.add(new Villain("Gonosz2", new Vec2(5,5), "./assets/villain/villain2.png"));
        villains.add(new Villain("Gonosz3", new Vec2(3,3), "./assets/villain/villain3.png"));

        for(Villain villain: villains) {
            Collider villainCollider;
            Vec2 playerScale = new Vec2(0.6f, 0.6f);
            villainCollider = new Collider(villain.getPosition(), playerScale);
            villainCollider.setMovability(true);
            villain.setVillainCollider(isten, villainCollider);
        }

    }

    public void sendVillainsToNewClient(PlayerMP client) {
        for(int i = 0; i < villains.size(); i++) {
            Packet05Villain packet = new Packet05Villain(villains.get(i).getVillainName(),
                    villains.get(i).getPosition(),
                    villains.get(i).getImagePath());
            server.sendData(packet.getData(), client.ipAddress, client.port);
        }
    }

    public void updateVillains() {

        for(Villain villain: villains) {
            if(villain.getVillainCollider() == null) continue;
            villain.move();
            Packet06VillainMove packet = new Packet06VillainMove(villain.getVillainName(),
                    villain.getVillainCollider().getPosition().x,
                    villain.getVillainCollider().getPosition().y);
            //send to every client
            packet.writeData(server);
        }
    }

}
