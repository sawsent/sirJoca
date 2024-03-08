package io.codeforall.bootcamp.sprites;

import io.codeforall.bootcamp.utilities.imports.Importer;
import io.codeforall.bootcamp.utilities.math.Vector;
import org.academiadecodigo.simplegraphics.pictures.Picture;

import static io.codeforall.bootcamp.utilities.Settings.TILESIZE;

public class AnimatedObject extends Tile implements Animated {
    private double frameCounter = 0;
    private final double ANIMATION_SPEED = 0.06;
    protected Picture currentFrame = new Picture(TILESIZE,TILESIZE);
    protected Picture[] graphics;
    public AnimatedObject(int col, int row, SpriteGroup[] groups, String folderPath) {
        super(col, row, groups, "normal");
        graphics = Importer.importOneFolder(folderPath);
    }

    public AnimatedObject(int col, int row, SpriteGroup[] groups, String type, String folderPath) {
        super(col, row, groups, type);
        graphics = Importer.importOneFolder(folderPath);
    }

    public void animate(Vector newPos) {
        frameCounter += ANIMATION_SPEED;
        if (frameCounter >= graphics.length-1) {
            frameCounter = 0;
        }

        currentFrame.delete();
        currentFrame = graphics[(int) frameCounter];

        Vector delta = newPos.minus(currentFrame.getX(), currentFrame.getY());
        currentFrame.translate(delta.X(), delta.Y());
        currentFrame.draw();
    }

    @Override
    public void update(Vector newScreenPos) {
        animate(newScreenPos);
    }
}
