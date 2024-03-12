package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.linalg.Vec2;

public class UnitRoom {
    private Vec2 position;
    private UnitRoomFrame top,left,right,down;
    private Item item;
    public Image image;

    public UnitRoom(Vec2 pos) {
        this.position = pos;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}
