package io.codeforall.bootcamp.utilities.math;

public class Vector {

    private double x;
    private double y;

    public Vector() {
        x = 0;
        y = 0;
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vector vector) {
        this.x += vector.X();
        this.y += vector.Y();
    }

    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public Vector minus(Vector vector) {
        return new Vector(x - vector.X(), y - vector.Y());
    }

    public Vector minus(int x, int y) {
        return new Vector(this.x-x, this.y-y);
    }

    public void setEqual(Vector vector) {
        this.x = vector.X();
        this.y = vector.Y();
    }

    public void setEqual(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public double X() {
        return x;
    }

    public double Y() {
        return y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
