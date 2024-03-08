package io.codeforall.bootcamp.sprites;

import io.codeforall.bootcamp.Camera;
import io.codeforall.bootcamp.utilities.math.Vector;
import org.academiadecodigo.simplegraphics.pictures.Picture;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class DrawnSpriteGroup extends SpriteGroup {
    public Camera camera;
    private HashMap<Picture, Integer> allBgs;
    private LinkedHashSet<Picture> loadedBGS;

    public void setCamera(Camera camera, HashMap<Picture, Integer> allBgs, LinkedHashSet<Picture> loadedBGS) {
        this.camera = camera;
        this.allBgs = allBgs;
        this.loadedBGS = loadedBGS;
    }

    public void update() {
        for (Sprite s : this) {
            s.update(camera.getRelativePos(s));
        }
    }

    public void updateBgs() {
        for (Picture bg : loadedBGS) {
            Vector newPos = camera.getBackGroundPos(allBgs.get(bg));
            Vector difference = newPos.minus(bg.getX(), bg.getY());
            bg.translate(difference.X(), difference.Y());
        }
    }


}
