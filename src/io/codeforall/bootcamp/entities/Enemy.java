package io.codeforall.bootcamp.entities;

import io.codeforall.bootcamp.SoundManager;
import io.codeforall.bootcamp.utilities.math.Vector;
import io.codeforall.bootcamp.sprites.SpriteGroup;

import static io.codeforall.bootcamp.utilities.Settings.*;

public class Enemy extends Entity {

    private int framesTillDespawn = 5 * FPS;
    public final String type;

    public Enemy(SpriteGroup[] groups, double x, double y, EntityType  entityType, SpriteGroup obstacleSprites) {
        super(groups, x, y, obstacleSprites, entityType);
        this.type = entityType.TYPE;
        spawnBehaviour();
    }
    private void despawn() {
        if (dead && --framesTillDespawn < 0) {
            kill();
            currentFrame.delete();
        }
    }

    public void changeDirection() {
        movingLeft = !movingLeft;
        movingRight = !movingRight;
    }

    public void randomDirection() {
        if (Math.random() < 0.5) {
            movingRight = true;
            return;
        }
        movingLeft = true;
    }

    private void spawnBehaviour() {
        randomDirection();

        if (entityType.equals(EntityType.FLAMEPIG)) {
            movingLeft = false;
            movingRight = false;
        }
    }

    @Override
    public void die() {
        (new SoundManager()).playSound(SoundManager.BOUNCE_ENEMY[(int) (SoundManager.BOUNCE_ENEMY.length * Math.random())]);
        super.die();
    }

    @Override
    public void horizontalCollideBehaviour() {
        changeDirection();
    }

    @Override
    public void update(Vector newScreenPos) {
        move();
        checkCollisions();
        animate(newScreenPos);
        despawn();
    }

}


