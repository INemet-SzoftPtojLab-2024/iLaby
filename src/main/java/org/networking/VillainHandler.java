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


        villainSkeletons.add(new Villain("Villain1", "3"));
        villainSkeletons.add(new Villain("Villain2",  "3"));
        villainSkeletons.add(new Villain("Villain3",  "3"));
        villainSkeletons.add(new Villain("Villain4",  "3"));
        villainSkeletons.add(new Villain("Villain5",  "3"));
        villainSkeletons.add(new Villain("Villain6",  "3"));
        villainSkeletons.add(new Villain("Villain7",  "3"));
        villainSkeletons.add(new Villain("Villain8",  "3"));
        villainSkeletons.add(new Villain("Gajdos",  "1"));
        villainSkeletons.add(new Villain("Csuka",  "2"));

        for(Villain villain: villainSkeletons) {
            float[] data = villain.randomPositions(isten.getMap().getRooms());
            int random1 = (int)data[0];
            int random2 = (int)data[1];
            Vec2 pos = new Vec2(data[2], data[3]);
            villain.setPosition(pos);
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
                    villainSkeletons.get(i).getImagePath(),
                    villainSkeletons.get(i).getRandom1(),
                    villainSkeletons.get(i).getRandom2());
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


            if(villain.getRoom() == null) {
                villain.setRoomForVillain(isten.getMap().getRooms(), skeleton.getRandom1(), skeleton.getRandom2());
            }


            villain.move(isten, deltaTime);

            if((int)(currTime * 100) % 2 == 0) {
                Packet06VillainMove packet = new Packet06VillainMove(villain.getVillainName(),
                        villain.getVillainCollider().getPosition().x,
                        villain.getVillainCollider().getPosition().y);
                sendDataToAllClients(packet);
            }
        }
    }

}
