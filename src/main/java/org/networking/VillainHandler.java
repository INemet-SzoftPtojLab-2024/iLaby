package main.java.org.networking;

import main.java.org.entities.villain.Villain;
import main.java.org.game.Isten;
import main.java.org.game.Map.Room;
import main.java.org.game.physics.Collider;
import main.java.org.linalg.Vec2;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Random;

//Handles villain creation and update for Server
public class VillainHandler extends ServerSideHandler {

    ArrayList<Villain> villains = new ArrayList<>();
    ArrayList<Villain> villainSkeletons = new ArrayList<>();

    private double currTime = 0;
    @Override
    public void create(GameServer server) {
        this.server = server;
        this.isten = server.isten;
        villains = new ArrayList<>();
        createVillains();
        isInitialized = true;
        sendDataToWaitingClients();
    }

    public void createVillains() {
        villainSkeletons.add(new Villain("Recski",  "./assets/villain/villain3.png"));
        villainSkeletons.add(new Villain("Csőke",  "./assets/villain/villain3.png"));
        villainSkeletons.add(new Villain("Zeke", "./assets/villain/villain2.png"));
        villainSkeletons.add(new Villain("Kollár",  "./assets/villain/villain2.png"));
        villainSkeletons.add(new Villain("Orsi",  "./assets/villain/villain2.png"));
        villainSkeletons.add(new Villain("Tamás",  "./assets/villain/villain2.png"));
        villainSkeletons.add(new Villain("Nándi",  "./assets/villain/villain2.png"));
        villainSkeletons.add(new Villain("Salyám",  "./assets/villain/villain2.png"));
        villainSkeletons.add(new Villain("Gajdos",  "./assets/villain/villain1.png"));
        villainSkeletons.add(new Villain("Csuka",  "./assets/villain/villain2.png"));

        for(Villain villain: villainSkeletons) {
            villain.setPosition(villain.randomPositions(isten.getMap().getRooms()));
        }

    }

    @Override
    public void sendDataToClient(PlayerMP client) {

        if(!isInitialized) {
            if(!waitingClients.contains(client)) waitingClients.add(client);
            return;
        }

        for(int i = 0; i < villainSkeletons.size(); i++) {
            Packet05Villain packet = new Packet05Villain(villainSkeletons.get(i).getVillainName(),
                    villainSkeletons.get(i).getPosition(),
                    villainSkeletons.get(i).getImagePath());
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

        if(currTime < 10000) {
            currTime += deltaTime;
        }
        else currTime = 0;

        for(Villain skeleton: villainSkeletons) {
            int index = isten.getVillainIndex(skeleton.getVillainName());
            Villain villain = (Villain)isten.getUpdatable(index);

            if(villain == null || !villain.isInitialized()) continue;

            villain.updateVillainOnServer(isten, deltaTime);
            if (villain.isInGasRoom() || villain.getIsFainted()){
                Packet27VillainIsInGasRoom packet = new Packet27VillainIsInGasRoom(villain.getVillainName(), true);
                sendDataToAllClients(packet);
                villain.setVelocity(0.0f);
            }
            else {
                Packet27VillainIsInGasRoom packet = new Packet27VillainIsInGasRoom(villain.getVillainName(), false);
                sendDataToAllClients(packet);
            }


            if((int)(currTime * 100) % 2 == 0) {
                Packet06VillainMove packet = new Packet06VillainMove(villain.getVillainName(),
                        villain.getVillainCollider().getPosition().x,
                        villain.getVillainCollider().getPosition().y);
                sendDataToAllClients(packet);
            }
        }
    }

}
