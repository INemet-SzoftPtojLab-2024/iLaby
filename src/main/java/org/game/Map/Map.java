package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;


public class Map extends Updatable {
    private ArrayList<Room> rooms;

    @Override
    public void onStart(Isten isten) {
        createTestMap(isten);
    }

    private void createTestMap(Isten isten) {
        /*
        ArrayList<UnitRoom> unitRooms = new ArrayList<>();
        for (int i = 0; i< 13; i++) {
            for (int j = 0; j<10; j++) {
                unitRooms.add(new UnitRoom(new Vec2(i*64,j*64)));
                Image image = new Image(new Vec2(i*64,j*64),64,64,new Vec2(1,1),"./assets/tile.png",true);
                unitRooms.get(unitRooms.size()-1).setImage(image);
                isten.getRenderer().addRenderable(image);
            }
        }*/
        UnitRoom unitRoom = new UnitRoom(new Vec2(200,200));
        Image image = new Image(new Vec2(225,225),50,50,new Vec2(1,1),"./assets/floor.png",true);
        isten.getRenderer().addRenderable(image);
        Wall w1 = new Wall(isten,unitRoom,FramePosition.up);
        Wall w2 = new Wall(isten,unitRoom,FramePosition.down);
        Wall w3 = new Wall(isten,unitRoom,FramePosition.left);
        Wall w4 = new Wall(isten,unitRoom,FramePosition.right);
        unitRoom.setTop(w1);
        unitRoom.setDown(w2);
        unitRoom.setLeft(w3);
        unitRoom.setRight(w4);
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {

    }

    @Override
    public void onDestroy() {

    }

    private void mergeRooms() {

    }

    private void splitRooms() {

    }

    public void drawMap(Graphics2D g2) {
        for (Room r: rooms) {
           for (UnitRoom u: r.unitRooms) {
               u.image.render(g2);
           }
        }
    }
}
