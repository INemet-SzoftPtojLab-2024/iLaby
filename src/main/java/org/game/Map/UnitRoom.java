package main.java.org.game.Map;

import main.java.org.game.Graphics.Image;
import main.java.org.linalg.Vec2;

public class UnitRoom {
    private Vec2 position;
    private Wall top,left,right,down;
    private Item item;
    public Image image;

    public UnitRoom(Vec2 pos) {
        this.position = pos;
    }

    public Vec2 getPosition() {
        return position;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public void setImage(Image image) {
        this.image = image;
    }


    public void setTop(Wall top) {
        this.top = top;
    }

    public Wall getTop() {
        return top;
    }

    public Wall getLeft() {
        return left;
    }

    public void setLeft(Wall left) {
        this.left = left;
    }

    public Wall getRight() {
        return right;
    }

    public void setRight(Wall right) {
        this.right = right;
    }

    public Wall getDown() {
        return down;
    }

    public void setDown(Wall down) {
        this.down = down;
    }
}
