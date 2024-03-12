package main.java.org.game.Map;

import main.java.org.game.physics.Collider;

public class Wall extends UnitRoomFrame {
    public Wall() {
        collider = new Collider();
        image = null;
    }
}
