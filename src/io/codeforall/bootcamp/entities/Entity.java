package io.codeforall.bootcamp.entities;

import io.codeforall.bootcamp.bettergfx.Rect;
import io.codeforall.bootcamp.sprites.Animated;
import io.codeforall.bootcamp.sprites.VisualState;
import io.codeforall.bootcamp.utilities.imports.Importer;
import io.codeforall.bootcamp.utilities.math.Vector;
import io.codeforall.bootcamp.sprites.Sprite;
import io.codeforall.bootcamp.sprites.SpriteGroup;
import org.academiadecodigo.simplegraphics.pictures.Picture;

import java.util.Map;

import static io.codeforall.bootcamp.utilities.Settings.*;

public abstract class Entity extends Sprite implements Animated {
    protected Rect rect;
    protected Vector pos;
    protected Vector speed = new Vector();
    protected SpriteGroup obstacleSprites;
    protected boolean grounded = true;
    protected boolean movingLeft;
    protected boolean movingRight;
    protected EntityType entityType;
    protected double maxSpeed;
    protected Map<String, Picture[]> graphics;
    protected VisualState visualState;
    protected double frameCounter;
    protected double ANIMATION_SPEED;
    protected Picture currentFrame = new Picture(TILESIZE,TILESIZE);
    protected boolean dead;
    protected boolean invulnerable;

    public Entity(SpriteGroup[] groups, double x, double y, SpriteGroup obstacleSprites, EntityType entityType) {
        super(groups);
        this.visualState = new VisualState("idleLEFT", Importer.subfolderNames(entityType.graphicsFolder));
        this.obstacleSprites = obstacleSprites;
        this.entityType = entityType;
        this.maxSpeed = entityType.MAX_SPEED;
        this.graphics = Importer.importFolder(entityType.graphicsFolder);
        this.ANIMATION_SPEED = entityType.ANIMATION_SPEED;


        pos = new Vector(x * TILESIZE, y * TILESIZE);

        this.rect = new Rect(pos.X(), pos.Y(), TILESIZE, TILESIZE);
    }

    public void animate(Vector newPos) {

        frameCounter += ANIMATION_SPEED;
        if (frameCounter >= graphics.get(getCurrentState()).length-1) {
            frameCounter = (dead || !grounded) ? graphics.get(getCurrentState()).length-1 : 0;

            if(visualState.actionIs("attack")) {
                visualState.unlock();
            }

        }

        currentFrame.delete();
        currentFrame = graphics.get(getCurrentState())[(int) frameCounter];

        Vector d = newPos.minus(currentFrame.getX(), currentFrame.getY());
        currentFrame.translate(d.X(), d.Y());
        currentFrame.draw();
    }

    public double moveRight() {
        return entityType.ACCELERATION;
    }

    public double moveLeft() {
        return -entityType.ACCELERATION;
    }

    protected void move() {
        double xSpeedToAdd = xSpeedReset();
        if (!dead) {
            if (movingRight) {
                visualState.setDirection("RIGHT");
                xSpeedToAdd += moveRight();
            } else if (movingLeft) {
                visualState.setDirection("LEFT");
                xSpeedToAdd += moveLeft();
            }
        }

        speed.add(xSpeedToAdd, GRAVITY_CONSTANT);
        speedChecks();
        pos.add(speed);
        rect.setTopLeft(pos.X(), pos.Y());
    }

    private void speedChecks() {
        maxSpeedCheck();
        lowSpeedCheck();
    }

    private double xSpeedReset() {
        double v = 0;
        if (speed.X() > 0) {
            visualState.setAction((grounded) ? "move" : "jump");
            v = -entityType.WEIGHT;
        } else if (speed.X() < 0) {
            visualState.setAction((grounded) ? "move" : "jump");
            v = entityType.WEIGHT;
        }
        return v;
    }

    private void maxSpeedCheck() {
        int xDir = (speed.X() >= 0) ? -1 : 1;
        if (Math.abs(speed.X()) > maxSpeed) {
            speed.add(entityType.ACCELERATION * xDir * 2, 0);
        }
        if (speed.Y() >= entityType.TERMINAL_VELOCITY) {
            speed.setEqual(speed.X(), entityType.TERMINAL_VELOCITY);
        }
    }

    private void lowSpeedCheck() {
        if (Math.abs(speed.X()) < entityType.ACCELERATION && stopped()) {
            speed.setEqual(0, speed.Y());
            visualState.setAction((grounded) ? "idle" : "jump");
        }
    }

    protected void checkCollisions() {

        for (Sprite sprite : obstacleSprites) {

            if(sprite.getType().equals("enemyOnly") && (this instanceof Player)) {
                continue;
            }

            if (rect.collidesWith(sprite.getRect())) {

                if (sprite.getType().equals("instakill")) {
                    die();
                }

                if (inTopEdgeOf(sprite)) {
                    this.pos.setEqual(pos.X(), sprite.getRect().getTop()-TILESIZE);
                    this.rect.setTopLeft(pos.X(), pos.Y());
                    this.speed.setEqual(speed.X(), 0);
                    visualState.replace("jump","move");
                    this.grounded = true;

                } else if (inBottomEdgeOf(sprite) && !falling() && !inHorizontalEdgesOf(sprite)) {
                    this.pos.setEqual(pos.X(), sprite.getRect().getBottom());
                    this.rect.setTopLeft(pos.X(), pos.Y());
                    this.speed.setEqual(speed.X(), 0);

                } else if (inLeftEdgeOf(sprite) && !inVerticalEdgesOf(sprite)) {
                    this.pos.setEqual(sprite.getRect().getLeft()-TILESIZE, pos.Y());
                    this.rect.setTopLeft(pos.X(), pos.Y());
                    this.speed.setEqual(0, speed.Y());
                    horizontalCollideBehaviour();

                } else if (inRightEdgeOf(sprite) && !inVerticalEdgesOf(sprite)) {
                    this.pos.setEqual(sprite.getRect().getRight(), pos.Y());
                    this.rect.setTopLeft(pos.X(), pos.Y());
                    this.speed.setEqual(0, speed.Y());
                    horizontalCollideBehaviour();
                }
            }
        }
    }

    protected boolean inHorizontalEdgesOf(Sprite sprite) {
        return inLeftEdgeOf(sprite) || inRightEdgeOf(sprite);
    }

    protected boolean inVerticalEdgesOf(Sprite sprite) {
        return inTopEdgeOf(sprite) || inBottomEdgeOf(sprite);
    }

    protected boolean inTopEdgeOf(Sprite sprite) {
        return rect.getBottom() > sprite.getRect().getTop() && rect.getBottom() <= sprite.getRect().getTop() + entityType.TERMINAL_VELOCITY + 2;
    }

    protected boolean inBottomEdgeOf(Sprite sprite) {
        return rect.getTop() < sprite.getRect().getBottom() && rect.getTop() >= sprite.getRect().getBottom() - entityType.TERMINAL_VELOCITY - 2;
    }

    protected boolean inLeftEdgeOf(Sprite sprite) {
        return rect.getRight() > sprite.getRect().getLeft() && rect.getRight() <= sprite.getRect().getLeft() + maxSpeed + 2;
    }

    protected boolean inRightEdgeOf(Sprite sprite) {
        return rect.getLeft() < sprite.getRect().getRight() && rect.getLeft() >= sprite.getRect().getRight() - maxSpeed - 2;
    }

    protected boolean moving() {
        return movingRight || movingLeft;
    }

    protected boolean stopped() {
        return !movingLeft && !movingRight;
    }

    protected void horizontalCollideBehaviour() {}

    protected boolean falling() {
        return speed.Y()>0;
    }

    protected String getCurrentState() {
        return visualState.getState();
    }

    public void attack() {
        if (visualState.hasAction("attack"));

        visualState.setAction("attack");
        visualState.lock();
    }

    public void die() {
        this.getRect().setTopLeft(Integer.MAX_VALUE,Integer.MAX_VALUE);
        dead = true;
        frameCounter = 0;
        visualState.setAction("dead");
        visualState.lock();
    }

    @Override
    public void update(Vector newScreenPos){
        move();
        checkCollisions();
        animate(newScreenPos);
    }

    @Override
    public void draw() {
        currentFrame.draw();
    }

    @Override
    public void delete() {
        currentFrame.delete();
    }

    @Override
    public Vector getPos() {
        return pos;
    }

    @Override
    public Rect getRect() {
        return rect;
    }

    protected class State {
        private String state;
        private String lockedState = "deadRIGHT";
        private boolean locked = false;
        private final String[] possibleActions = {"idle", "jump", "move", "dead"};
        private final String[] possibleDirections = {"LEFT", "RIGHT"};

        public State(String startState) {
            this.state = startState;
        }

        public void replace(String target, String replacement) {
            state = state.replace(target, replacement);
        }

        public void setAction(String action) {
            for (String s : possibleActions) {
                replace(s, action);
            }
        }

        public void setDirection(String direction) {
            for (String s : possibleDirections) {
                replace(s, direction);
            }
        }

        public String getState() {
            return (locked) ? lockedState : state;
        }

        public void lock() {
            lockedState = state;
            locked = true;
        }

        public void unlock() {
            locked = false;
        }
    }
}
