package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.game.physics.Collider;

public class UnitRoomFrame {
    protected Collider collider;
    protected Image image;

    public UnitRoomFrame() {}
    public void setCollider(Collider collider) {
        this.collider = collider;
    }
}
