package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
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
        ArrayList<UnitRoom> unitRooms = new ArrayList<>();
        for (int i = 0; i< 13; i++) {
            for (int j = 0; j<10; j++) {
                unitRooms.add(new UnitRoom(new Vec2(i*64,j*64)));
                Image image = new Image(new Vec2(i*64,j*64),64,64,new Vec2(1,1),"./assets/tile.png");
                unitRooms.get(unitRooms.size()-1).setImage(image);
                isten.getRenderer().addRenderable(image);
            }
        }

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
