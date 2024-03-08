package io.codeforall.bootcamp;

import io.codeforall.bootcamp.entities.Enemy;
import io.codeforall.bootcamp.entities.Player;
import io.codeforall.bootcamp.sprites.*;
import io.codeforall.bootcamp.entities.EntityType;
import io.codeforall.bootcamp.utilities.imports.CSVReader;
import io.codeforall.bootcamp.utilities.imports.Importer;
import io.codeforall.bootcamp.utilities.math.Vector;
import org.academiadecodigo.simplegraphics.graphics.Text;
import org.academiadecodigo.simplegraphics.pictures.Picture;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import static io.codeforall.bootcamp.utilities.Settings.*;

public class Level {
    private final int LOAD_OFFSET = 2000;
    private final int BG_STRIPE_SIZE = 1000;
    private final LinkedList<Picture> hearts = new LinkedList<>();
    private final LinkedList<Text> texts = new LinkedList<>();
    private final LinkedList<Picture> staticUIImages = new LinkedList<>();
    private final SpriteGroup obstacleSprites = new SpriteGroup();
    private final SpriteGroup enemySprites = new SpriteGroup();
    private final SpriteGroup unloadedSprites = new SpriteGroup();
    private final SpriteGroup interactableSprites = new SpriteGroup();
    private final DrawnSpriteGroup drawnSprites = new DrawnSpriteGroup();
    private final HashMap<Picture, Integer> allBgs;
    private final LinkedHashSet<Picture> loadedBGs = new LinkedHashSet<>();
    private Player player;
    private Controls controls;
    private int loadCheckTimer = 0;
    private int maxLucianos = 0;
    private Text lucianoText;
    private final SoundManager soundManager;


    public Level(Controls controls, SoundManager soundManager) {
        this.soundManager = soundManager;
        this.createLevel(".resources/maps/final.csv");
        allBgs = getBackgroundOffsets(Importer.importOneFolder(".resources/maps/bg", ".png"));
        this.drawnSprites.setCamera(new Camera(player), allBgs, loadedBGs);
        this.controls = controls;
        this.controls.addPlayerControls(player);


        addUIElements();

    }

    public void addUIElements() {
        lucianoText = new Text(110, 110, getLucianos());
        lucianoText.grow(1.5 * lucianoText.getWidth(), 1.5 * lucianoText.getHeight());
        staticUIImages.add(new Picture(5, 90, ".resources/graphics/staticImages/lucianoSmall.png"));

        populateHeartList(3);

        texts.add(lucianoText);
    }


    public void createLevel(String fileName) {
        String[][] levelData = (new CSVReader(fileName)).importCsv();

        for (int row = 0; row < levelData.length; row++) {
            for (int col = 0; col < levelData[row].length; col++) {
                String value = levelData[row][col];

                if (value.equals("-1")) {
                    continue;
                }
                switch (value) {
                    case "0" ->
                            new Tile(col, row, new SpriteGroup[]{obstacleSprites}, "normal", ".resources/graphics/staticImages/testEnemy.png");
                    case "1" ->
                            new Tile(col, row, new SpriteGroup[]{obstacleSprites}, "enemyOnly", ".resources/graphics/staticImages/testEnemy.png");
                    case "2" ->
                            new Tile(col, row, new SpriteGroup[]{obstacleSprites}, "instakill", ".resources/graphics/staticImages/testEnemy.png");
                    case "3" ->
                            new Enemy(new SpriteGroup[]{unloadedSprites, enemySprites}, col, row, EntityType.WOLF, obstacleSprites);
                    case "4" ->
                            new Enemy(new SpriteGroup[]{unloadedSprites, enemySprites}, col, row, EntityType.ENEMY, obstacleSprites);
                    case "5" ->
                            new Enemy(new SpriteGroup[]{unloadedSprites, enemySprites}, col, row, EntityType.SAMURAI, obstacleSprites);
                    case "6" ->
                            new Enemy(new SpriteGroup[]{unloadedSprites, enemySprites}, col, row, EntityType.WOLF, obstacleSprites);
                    case "7" ->
                            new Enemy(new SpriteGroup[]{unloadedSprites, enemySprites}, col, row, EntityType.FLAMEPIG, obstacleSprites);
                    case "8" ->
                            this.player = new Player(new SpriteGroup[]{unloadedSprites}, col, row, obstacleSprites, enemySprites, interactableSprites);
                    case "9" ->
                            new Enemy(new SpriteGroup[]{unloadedSprites, enemySprites}, col, row, EntityType.SLIME, obstacleSprites);
                    case "10" ->
                            new Enemy(new SpriteGroup[]{unloadedSprites, enemySprites}, col, row, EntityType.BAT, obstacleSprites);
                    case "11" -> {
                        new Tile(col, row, new SpriteGroup[]{unloadedSprites, interactableSprites}, "luciano", ".resources/graphics/staticImages/luciano.png");
                        maxLucianos++;
                    }
                    case "12" ->
                            new Tile(col, row, new SpriteGroup[]{interactableSprites, drawnSprites}, "peanut", ".resources/graphics/staticImages/peanutButter.png");

                }

            }
        }
    }

    private void checkLoadedBGs() {
        LinkedList<Picture> toRemove = new LinkedList<>();
        for (Picture key : allBgs.keySet()) {
            if (allBgs.get(key) < player.getPos().X() + LOAD_OFFSET && allBgs.get(key) > player.getPos().X() - LOAD_OFFSET) {
                loadedBGs.add(key);
                key.draw();
            }
        }
        for (Picture bg : loadedBGs) {
            if (!(allBgs.get(bg) < player.getPos().X() + LOAD_OFFSET && allBgs.get(bg) > player.getPos().X() - LOAD_OFFSET)) {
                toRemove.add(bg);
                bg.delete();
            }
        }
        toRemove.forEach(loadedBGs::remove);

    }


    public void checkToLoad() {
        loadCheckTimer--;
        if (loadCheckTimer > 0) {
            return;
        }
        resetLoadCheckTimer();

        checkLoadedBGs();

        LinkedList<Sprite> spritesToLoad = new LinkedList<>();
        for (Sprite sprite : unloadedSprites) {
            if (sprite.getPos().X() < player.getPos().X() + LOAD_OFFSET && sprite.getPos().X() > player.getPos().X() - LOAD_OFFSET) {
                drawnSprites.add(sprite);
                spritesToLoad.add(sprite);
                sprite.draw();
            }
        }
        SpriteGroup spritesToUnload = new SpriteGroup();
        for (Sprite sprite : drawnSprites) {
            if (!(sprite.getPos().X() < player.getPos().X() + LOAD_OFFSET && sprite.getPos().X() > player.getPos().X() - LOAD_OFFSET)) {
                unloadedSprites.add(sprite);
                spritesToUnload.add(sprite);
                sprite.delete();
            }
        }

        unloadedSprites.removeAll(spritesToLoad);
        drawnSprites.removeAll(spritesToUnload);

    }

    private String getLucianos() {
        return "" + player.getLucianos() + "/" + maxLucianos;
    }

    private boolean alreadyAddedDeadBanner = false;
    private boolean alreadyAddedWinBanner = false;
    private void playerChecks() {
        if (player.isDead()) {
            controls.removePlayerControls();
            controls.addDeathControls();
            if (!alreadyAddedDeadBanner) {
                staticUIImages.add(new Picture(340, 285, ".resources/graphics/staticImages/deadbanner.png"));
                alreadyAddedDeadBanner = true;
            }

        }
        if (player.justTookDamage) {
            player.justTookDamage = false;

            Picture toDelete = hearts.pollLast();

            staticUIImages.remove(toDelete);
            assert toDelete != null;
            toDelete.delete();

        }
        if (player.winner()) {
            player.stopImmediately();
            controls.removePlayerControls();
            controls.addDeathControls();
            if (!alreadyAddedWinBanner) {
            staticUIImages.add(new Picture(350, 0, ".resources/graphics/staticImages/win.png"));
            alreadyAddedWinBanner = true;
            }
        }
    }

    private void updateUI() {
        lucianoText.setText(getLucianos());
        for (Text t : texts) {
            t.delete();
            t.draw();
        }
        for (Picture p : staticUIImages) {
            p.delete();
            p.draw();
        }
    }

    public void run() {
        checkToLoad();
        drawnSprites.updateBgs();
        drawnSprites.update();
        updateUI();
        playerChecks();
    }

    private void populateHeartList(int lives) {
        Picture heartBG = new Picture(10, 10, ".resources/graphics/staticImages/heartsBG.png");
        staticUIImages.add(heartBG);
        Vector offset = new Vector(10 - 64, 10);
        for (int i = 0; i < lives; i++) {
            offset.add(64, 0);
            Picture newPicture = new Picture(offset.X(), offset.Y(), ".resources/graphics/staticImages/betterHeart.png");

            hearts.add(newPicture);

            staticUIImages.add(newPicture);
        }
    }

    private HashMap<Picture, Integer> getBackgroundOffsets(Picture[] bgs) {
        HashMap<Picture, Integer> out = new HashMap<>();
        int i = 0;
        for (Picture bg : bgs) {
            out.put(bg, i);
            i += BG_STRIPE_SIZE;
        }
        return out;
    }

    public void deleteEverything() {
        for (Picture p : allBgs.keySet()) {
            p.delete();
        }
        for (Sprite s : drawnSprites) {
            s.delete();
            s.kill();
        }
        for (Sprite s : obstacleSprites) {
            s.kill();
            s.delete();
        }
        for (Text t : texts) {
            t.delete();
        }
        for (Picture p : staticUIImages) {
            p.delete();
        }
        player.kill();
    }

    private void resetLoadCheckTimer() {
        loadCheckTimer = FPS;
    }

}
