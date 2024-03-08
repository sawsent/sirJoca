package io.codeforall.bootcamp.sprites;

import io.codeforall.bootcamp.bettergfx.Rect;
import io.codeforall.bootcamp.utilities.math.Vector;
import org.academiadecodigo.simplegraphics.pictures.Picture;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Sprite {
    protected Picture image = new Picture();
    private final CopyOnWriteArrayList<SpriteGroup> groups = new CopyOnWriteArrayList<>();
    public Sprite(SpriteGroup[] groups) {
        addToAll(groups);
    }
    public void addToAll(SpriteGroup[] groups) {
        for (SpriteGroup group : groups) {
            addTo(group);
        }
    }
    public void addTo(SpriteGroup group) {
        group.addElement(this);
        groups.add(group);
    }

    public void removeFrom(SpriteGroup group) {
        group.removeElement(this);
        groups.remove(group);
    }

    public void kill() {
        for (SpriteGroup group : groups) {
            this.groups.remove(group);
            group.removeElement(this);
        }
        image.delete();
    }

    public abstract void draw();
    public void delete() {
        image.delete();
    }
    public abstract void update(Vector newScreenPos);

    public abstract Vector getPos();

    public abstract Rect getRect();

    public String getType() {
        return "normal";
    }
}
