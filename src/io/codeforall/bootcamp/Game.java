package io.codeforall.bootcamp;

import org.academiadecodigo.simplegraphics.graphics.Canvas;
import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;
import org.academiadecodigo.simplegraphics.pictures.Picture;

import static io.codeforall.bootcamp.utilities.Settings.*;

public class Game {
    private SoundManager soundManager = new SoundManager();
    private Level level;
    private final Controls controls = new Controls(this);
    private String gameState = "startScreen";
    private final Picture startBanner = new Picture(0, 0, ".resources/graphics/banners/menuScreen.jpg");
    Rectangle OUTLINE_RECT = new Rectangle(0, 0, WIDTH + 20, HEIGHT + 20);

    public Game() {
        Canvas.limitCanvasHeight(HEIGHT-10);
        Canvas.limitCanvasWidth(WIDTH-10);
        OUTLINE_RECT.delete();
        OUTLINE_RECT.fill();
        OUTLINE_RECT.setColor(Color.CYAN);
        startBanner.draw();

        this.level = new Level(controls, soundManager);
        this.soundManager.playBgMusic();
    }

    public void startNewLevel() {
        level.deleteEverything();
        OUTLINE_RECT.delete();
        OUTLINE_RECT.fill();
        OUTLINE_RECT.setColor(Color.CYAN);
        controls.removePlayerControls();
        this.level = new Level(controls, soundManager);
        controls.removeDeathControls();
    }

    public void start() {
        gameState = "level";
    }

    public void run() {

        try {
            while (true) {
                switch (gameState) {
                    case "level" -> level.run();
                } ;


                Thread.sleep(1000 / FPS);
            }
        } catch (InterruptedException e) {
            System.out.println("Something went very wrong: " + e.getMessage());
        }
    }

}
