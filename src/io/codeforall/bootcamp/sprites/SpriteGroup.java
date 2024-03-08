package io.codeforall.bootcamp.sprites;

import java.util.concurrent.CopyOnWriteArrayList;

public class SpriteGroup extends CopyOnWriteArrayList<Sprite> {

    public boolean add(Sprite sprite) {
        sprite.addTo(this);
        return true;
    }

    public void addElement(Sprite sprite) {
        super.add(sprite);
    }

    public void remove(Sprite sprite) {
        sprite.removeFrom(this);
    }

    public void removeElement(Sprite sprite) {
        super.remove(sprite);
    }
}
