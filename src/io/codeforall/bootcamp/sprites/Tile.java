package io.codeforall.bootcamp.sprites;

import io.codeforall.bootcamp.bettergfx.Rect;
import io.codeforall.bootcamp.utilities.math.Vector;
import org.academiadecodigo.simplegraphics.pictures.Picture;

import static io.codeforall.bootcamp.utilities.Settings.TILESIZE;

public class Tile extends Sprite {
    private final Vector pos;
    private final Rect rect;
    private final String type;
    private boolean dead;
    public Tile(int col, int row, SpriteGroup[] groups, String type , String filePath) {
        super(groups);
        this.pos = new Vector(col * TILESIZE, row * TILESIZE);
        this.rect = new Rect(pos.X(), pos.Y(), TILESIZE, TILESIZE);
        this.type = type;
        this.image.load(filePath);
    }

    public Tile(int col, int row, SpriteGroup[] groups, String type) {
        super(groups);
        this.pos = new Vector(col * TILESIZE, row * TILESIZE);
        this.rect = new Rect(pos.X(), pos.Y(), TILESIZE, TILESIZE);
        this.type = type;
    }

    public Rect getRect() {
        return this.rect;
    }

    @Override
    public void update(Vector newScreenPos) {
        Vector difference = newScreenPos.minus(image.getX(), image.getY());
        this.image.translate(difference.X(), difference.Y());
        draw();
    }

    @Override
    public void kill() {
        dead = true;
        super.kill();
    }

    @Override
    public void draw() {
        if (dead) {return;}
        image.delete();
        image.draw();
    }


    @Override
    public Vector getPos() {
        return pos;
    }

    @Override
    public String getType() {
        return this.type;
    }
}
