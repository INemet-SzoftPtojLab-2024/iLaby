package main.java.org.game.Map;

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
