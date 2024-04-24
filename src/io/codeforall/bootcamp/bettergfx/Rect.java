package io.codeforall.bootcamp.bettergfx;

import org.academiadecodigo.simplegraphics.graphics.Rectangle;

public class Rect extends Rectangle {

    public Rect(double left, double top, double width, double height) {
        super(left, top, width, height);
    }

    public boolean collidesWith(Rect rect) {
        return (this.getBottom() > rect.getTop() && this.getTop() < rect.getBottom() &&
                this.getRight() > rect.getLeft() && this.getLeft() < rect.getRight());
    }

    public void setTopLeft(double left, double top) {
        int dx = (int) left - getLeft();
        int dy = (int) top - getTop();
        translate(dx, dy);
    }
    public int getLeft() {
        return this.getX();
    }
    public int getRight() {
        return this.getX() + this.getWidth();
    }

    public int getTop() {
        return this.getY();
    }

    public int getBottom() {
        return this.getTop() + this.getHeight();
    }
}
