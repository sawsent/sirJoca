package io.codeforall.bootcamp;


import io.codeforall.bootcamp.entities.Player;
import io.codeforall.bootcamp.utilities.math.Vector;
import io.codeforall.bootcamp.sprites.Sprite;
import org.academiadecodigo.simplegraphics.pictures.Picture;

public class Camera {

    private final Player player;

    public Camera(Player player) {
        this.player = player;
    }

    public Vector getRelativePos(Sprite sprite) {
        Vector relativePos = new Vector(sprite.getPos().X(), sprite.getPos().Y());
        relativePos.add(-1 * player.getPos().X(), -1 * player.getPos().Y());
        relativePos.add(player.SCREEN_POS.X(), player.SCREEN_POS.Y());
        return relativePos;
    }

    public Vector getBackGroundPos(int x) {
        Vector relativePos = new Vector(x, 0);
        relativePos.add(-1 * player.getPos().X(), -1 * player.getPos().Y());
        relativePos.add(player.SCREEN_POS.X(), player.SCREEN_POS.Y());
        return relativePos;
    }
}
