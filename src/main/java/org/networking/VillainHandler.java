package main.java.org.networking;

import main.java.org.entities.villain.Villain;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.linalg.Vec2;

import java.net.DatagramPacket;
import java.util.ArrayList;

//Handles villain creation and update for Server
public class VillainHandler extends ServerSideHandler {

    ArrayList<Villain> villains;

    @Override
    public void create(GameServer server) {
        this.server = server;
        this.isten = server.isten;
        villains = new ArrayList<>();
        createVillains();
    }

    public void createVillains() {
        villains.add(new Villain("Gajdos",  "./assets/villain/villain1.png"));
        villains.add(new Villain("Csuka",  "./assets/villain/villain2.png"));
        villains.add(new Villain("Villain",  "./assets/villain/villain3.png"));
        villains.add(new Villain("Villain",  "./assets/villain/villain3.png"));
        villains.add(new Villain("Villain",  "./assets/villain/villain3.png"));
        villains.add(new Villain("Villain",  "./assets/villain/villain3.png"));
        villains.add(new Villain("Villain",  "./assets/villain/villain3.png"));
        villains.add(new Villain("Villain",  "./assets/villain/villain3.png"));
        villains.add(new Villain("Villain",  "./assets/villain/villain3.png"));
        villains.add(new Villain("Villain",  "./assets/villain/villain3.png"));

        for(Villain villain: villains) {
            Collider villainCollider;
            Vec2 playerScale = new Vec2(0.6f, 0.6f);
            villainCollider = new Collider(villain.getPosition(), playerScale);
            villainCollider.setMovability(true);
            villain.setVillainCollider(isten, villainCollider);
            villain.setPosition(new Vec2(5,2));
        }

    }

    @Override
    public void sendDataToClient(PlayerMP client) {
        for(int i = 0; i < villains.size(); i++) {
            Packet05Villain packet = new Packet05Villain(villains.get(i).getVillainName(),
                    villains.get(i).getPosition(),
                    villains.get(i).getImagePath());
            server.sendData(packet.getData(), client.ipAddress, client.port);
        }
    }

    @Override
    public void sendDataToAllClients(Packet packet) {
        //send to every client
        packet.writeData(server);
    }

    @Override
    public void update(Isten isten, double deltaTime) {

        for(Villain villain: villains) {
            if(villain.getVillainCollider() == null) continue;
            villain.move(isten, deltaTime);
            Packet06VillainMove packet = new Packet06VillainMove(villain.getVillainName(),
                    villain.getVillainCollider().getPosition().x,
                    villain.getVillainCollider().getPosition().y);
            sendDataToAllClients(packet);
        }
    }

}
