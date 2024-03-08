package io.codeforall.bootcamp.entities;

public enum EntityType {
    PLAYER(4, 0.15, -7, 0.1, 10, ".resources/graphics/player", 0.1, "player"),
    ENEMY(1, 0.07, 0, 0.05, 10, ".resources/graphics/enemy", 0.06, "normal"),
    FLAMEPIG(1.6, 0.07, 0, 0.05, 10, ".resources/graphics/flamePig", 0.06, "notouch"),
    SAMURAI(2, 0.2, 0, 0.05, 10, ".resources/graphics/samurai", 0.06, "normal"),

    WOLF(1.6, 0.1, 0, 0.05, 10, ".resources/graphics/wolf", 0.1, "normal"),
    SLIME(0.08, 0.02, 0, 0.05, 10, ".resources/graphics/slime", 0.07, "normal"),
    BAT(1, 1, 0, 0.05, 0, ".resources/graphics/bat", 0.06, "normal");

    public final double MAX_SPEED;
    public final double ACCELERATION;
    public final double JUMP_SPEED;
    public final double WEIGHT;
    public final double TERMINAL_VELOCITY;
    public final double ANIMATION_SPEED;
    public final String TYPE;
    public final String graphicsFolder;

    EntityType(double maxSpeed, double acceleration, double jumpSpeed, double stoppingInercia, double terminalVelocity, String graphicsFolder, double animationSpeed, String type) {
        this.MAX_SPEED = maxSpeed;
        this.ACCELERATION = acceleration;
        this.JUMP_SPEED = jumpSpeed;
        this.WEIGHT = stoppingInercia;
        this.TERMINAL_VELOCITY = terminalVelocity;
        this.ANIMATION_SPEED = animationSpeed;
        this.graphicsFolder = graphicsFolder;
        this.TYPE = type;
    }

}
