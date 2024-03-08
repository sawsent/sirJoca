package io.codeforall.bootcamp.entities;

import io.codeforall.bootcamp.Main;
import io.codeforall.bootcamp.SoundManager;
import io.codeforall.bootcamp.sprites.AnimatedObject;
import io.codeforall.bootcamp.sprites.Sprite;
import io.codeforall.bootcamp.utilities.math.Vector;
import io.codeforall.bootcamp.sprites.SpriteGroup;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEvent;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.Key;

import static io.codeforall.bootcamp.utilities.Settings.TILESIZE;

public class Player extends Entity {
    public final Vector SCREEN_POS = new Vector(450, 350);
    private final double turboSpeed = 6;
    private final double turboAnimationSpeed = 0.15;
    private final SpriteGroup enemySprites;
    private final SpriteGroup interactableSprites;
    private int currentLucianos = 0;
    public boolean justTookDamage = false;
    private int revivesLeft = 2;
    private boolean down = false;
    private final SoundManager soundManager;
    private boolean winner = false;

    private Timer reviveTimer;
    private Timer invulnerableTimer;


    public Player(SpriteGroup[] groups, double x, double y, SpriteGroup obstacleSprites, SpriteGroup enemySprites, SpriteGroup interactableSprites) {
        super(groups, x, y, obstacleSprites, EntityType.PLAYER);
        currentFrame.translate(SCREEN_POS.X(), SCREEN_POS.Y());
        this.enemySprites = enemySprites;
        this.interactableSprites = interactableSprites;
        this.soundManager = new SoundManager();

        invulnerable = true;

        reviveTimer = new Timer(2000, arg0 -> standUp());
        invulnerableTimer = new Timer(2000, arg0 -> resetInvulnerable());

        reviveTimer.setRepeats(false);
        invulnerableTimer.setRepeats(false);

        invulnerableTimer.start();
    }

    public int getLucianos() {
        return currentLucianos;
    }

    protected void checkInteractable() {

        for (Sprite sprite : interactableSprites) {
            if (rect.collidesWith(sprite.getRect())) {
                if (sprite.getType().equals("luciano")) {
                    sprite.delete();
                    sprite.kill();
                    currentLucianos++;
                    soundManager.playSound(SoundManager.LUCIANO[(int) (Math.random() * SoundManager.LUCIANO.length)]);
                }
                if (sprite.getType().equals("peanut")) {
                    interactableSprites.remove(sprite);
                    winner = true;
                    soundManager.playSound("papa.wav");
                }
            }
        }
    }

    protected void checkEnemyCollisions() {

        for (Sprite sprite : enemySprites) {
            if (!(sprite instanceof Enemy enemy) || enemy.dead || dead || down) {
                continue;
            }
            if (rect.collidesWith(enemy.getRect())) {
                if (enemy.type.equals("notouch") && !invulnerable) {
                    hitByEnemy();
                    continue;
                }

                if (inTopEdgeOf(enemy) && falling()) {
                    grounded = true;
                    jump();
                    grounded = true;
                    enemy.die();
                    break;
                }
                if (invulnerable) {
                    continue;
                }

                if (inLeftEdgeOf(sprite) && !inVerticalEdgesOf(sprite)) {
                    enemy.visualState.setDirection("LEFT");

                } else if (inRightEdgeOf(sprite) && !inVerticalEdgesOf(sprite)) {
                    enemy.visualState.setDirection("RIGHT");
                }
                enemy.attack();
                hitByEnemy();
            }
        }
    }

    public void jump() {
        if (down) {
            return;
        }
        soundManager.playSound(SoundManager.JUMP_ENEMY[ (int) (SoundManager.JUMP_ENEMY.length * Math.random())]);
        if (grounded && !dead) {
            frameCounter = 0;
            speed.setEqual(speed.X(), EntityType.PLAYER.JUMP_SPEED);
            grounded = false;
            visualState.setAction("jump");
        }
    }

    public void activateTurbo() {
        this.maxSpeed = turboSpeed;
        this.ANIMATION_SPEED = turboAnimationSpeed;
    }

    public void deactivateTurbo() {
        this.maxSpeed = entityType.MAX_SPEED;
        this.ANIMATION_SPEED = entityType.ANIMATION_SPEED;
    }

    public void moveInput(int key) {
        if (down) {
            return;
        }
        switch (key) {
            case KeyboardEvent.KEY_RIGHT -> {
                movingRight = true;
                movingLeft = false;
            }
            case KeyboardEvent.KEY_LEFT -> {
                movingLeft = true;
                movingRight = false;
            }
        }
    }

    public boolean winner() {
        return winner;
    }

    public void hitByEnemy() {
        soundManager.playSound(SoundManager.DEATH[ (int) (SoundManager.DEATH.length * Math.random())]);
        justTookDamage = true;
        if (revivesLeft <= 0) {

            die();
            return;
        }
        fallDown();
    }

    public void revive() {
        if (!dead) {
            return;
        }
        visualState.setAction("idle");
        visualState.unlock();
        dead = false;
    }

    public void fallDown() {
        revivesLeft--;
        visualState.setAction("dead");
        visualState.lock();
        down = true;
        reviveTimer.start();
    }

    public void standUp() {
        visualState.unlock();
        visualState.setAction("invulnerable");
        visualState.lock();
        invulnerable = true;
        down = false;
        stop();
        invulnerableTimer.start();
    }

    private void resetInvulnerable() {
        invulnerable = false;
        visualState.unlock();
    }

    public void stop(int key) {
        switch (key) {
            case KeyboardEvent.KEY_RIGHT -> {
                movingRight = false;
            }
            case KeyboardEvent.KEY_LEFT -> {
                movingLeft = false;
            }
        }
    }
    public void stop() {
        stop(KeyboardEvent.KEY_LEFT);
        stop(KeyboardEvent.KEY_RIGHT);
    }

    public void stopImmediately() {
        stop();
        speed.setEqual(0, 0);
    }

    public boolean isDead() {
        return dead;
    }

    @Override
    public void update(Vector newScreenPos) {
        super.update(newScreenPos);
        checkEnemyCollisions();
        checkInteractable();
    }

}
