package io.codeforall.bootcamp;

import io.codeforall.bootcamp.entities.Player;
import org.academiadecodigo.simplegraphics.keyboard.Keyboard;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEvent;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEventType;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardHandler;

import java.security.Key;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import static org.academiadecodigo.simplegraphics.keyboard.KeyboardEvent.*;

interface Controler {
    void removeAll();
}

public class Controls {
    public final LinkedList<Controler> allControls = new LinkedList<>();
    public MainMenuControls mainMenuControls;
    public VerticalControls verticalControls;
    public HorizontalControls horizontalControls;

    public Controls(Player player) {
        addPlayerControls(player);
    }

    public Controls(Game game) {
        this.mainMenuControls = new MainMenuControls(game);
        allControls.add(mainMenuControls);
    }

    public void addDeathControls() {
        mainMenuControls.addDeathControls();
    }

    public void removeDeathControls() {
        this.mainMenuControls.removeDeathControls();
    }

    public void removePlayerControls() {
        horizontalControls.removeAll();
        verticalControls.removeAll();
    }

    public void removeAll() {
        for (Controler c : allControls) {
            c.removeAll();
        }
    }

    public void addMenuControls(Game game, boolean dead) {
        allControls.add(new MainMenuControls(game));
    }

    public void addPlayerControls(Player player) {
        this.horizontalControls = new HorizontalControls(player);
        this.verticalControls = new VerticalControls(player);
    }

    private class MainMenuControls implements KeyboardHandler, Controler {
        private final LinkedHashSet<KeyboardEvent> allKeys = new LinkedHashSet<>();
        private final LinkedHashSet<KeyboardEvent> deathControls = new LinkedHashSet<>();
        private Keyboard kb = new Keyboard(this);
        private final Game game;

        private MainMenuControls(Game game) {
            this.game = game;
            init();
        }

        public void addDeathControls() {
            KeyboardEvent pP = new KeyboardEvent();
            pP.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
            pP.setKey(KeyboardEvent.KEY_P);
            kb.addEventListener(pP);
            deathControls.add(pP);
        }

        public void removeDeathControls() {
            for (KeyboardEvent k : deathControls) {
                kb.removeEventListener(k);
            }
        }

        public void init() {
            KeyboardEvent pSpace = new KeyboardEvent();
            pSpace.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
            pSpace.setKey(KEY_SPACE);
            kb.addEventListener(pSpace);
        }

        public void removeAll() {
            for (KeyboardEvent k : allKeys) {
                kb.removeEventListener(k);
            }
        }

        @Override
        public void keyPressed(KeyboardEvent keyboardEvent) {
            switch (keyboardEvent.getKey()) {
                case KEY_SPACE -> {
                    removeAll();
                    game.start();
                }
                case KEY_P -> game.startNewLevel();
            }
        }

        @Override
        public void keyReleased(KeyboardEvent keyboardEvent) {

        }
    }

    private static class VerticalControls implements KeyboardHandler, Controler {
        private final LinkedHashSet<KeyboardEvent> allKeys = new LinkedHashSet<>();

        private Player player;
        private Keyboard keyboard;

        public VerticalControls(Player player) {
            this.player = player;
            init();
        }


        public void init() {
            keyboard = new Keyboard(this);

            KeyboardEvent pressedUp = new KeyboardEvent();
            pressedUp.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
            pressedUp.setKey(KEY_SPACE);
            keyboard.addEventListener(pressedUp);

            allKeys.add(pressedUp);
        }

        @Override
        public void keyPressed(KeyboardEvent keyboardEvent) {
            if (keyboardEvent.getKey() == KEY_SPACE) {
                player.jump();
            }
        }

        @Override
        public void keyReleased(KeyboardEvent keyboardEvent) {

        }

        @Override
        public void removeAll() {
            for (KeyboardEvent k : allKeys) {
                keyboard.removeEventListener(k);
            }
        }
    }

    private static class HorizontalControls implements KeyboardHandler, Controler {
        private final LinkedHashSet<KeyboardEvent> allKeys = new LinkedHashSet<>();
        Player player;
        Keyboard keyboard;


        public HorizontalControls(Player player) {
            this.player = player;
            init();
        }


        public void init() {
            keyboard = new Keyboard(this);

            KeyboardEvent pressedRight = new KeyboardEvent();
            pressedRight.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
            pressedRight.setKey(KeyboardEvent.KEY_RIGHT);
            keyboard.addEventListener(pressedRight);

            KeyboardEvent releasedRight = new KeyboardEvent();
            releasedRight.setKeyboardEventType(KeyboardEventType.KEY_RELEASED);
            releasedRight.setKey(KeyboardEvent.KEY_RIGHT);
            keyboard.addEventListener(releasedRight);

            KeyboardEvent pressedLeft = new KeyboardEvent();
            pressedLeft.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
            pressedLeft.setKey(KeyboardEvent.KEY_LEFT);
            keyboard.addEventListener(pressedLeft);

            KeyboardEvent releasedLeft = new KeyboardEvent();
            releasedLeft.setKeyboardEventType(KeyboardEventType.KEY_RELEASED);
            releasedLeft.setKey(KeyboardEvent.KEY_LEFT);
            keyboard.addEventListener(releasedLeft);

            KeyboardEvent pressedT = new KeyboardEvent();
            pressedT.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
            pressedT.setKey(KeyboardEvent.KEY_T);
            keyboard.addEventListener(pressedT);

            KeyboardEvent releasedT = new KeyboardEvent();
            releasedT.setKeyboardEventType(KeyboardEventType.KEY_RELEASED);
            releasedT.setKey(KeyboardEvent.KEY_T);
            keyboard.addEventListener(releasedT);

            allKeys.addAll(Arrays.asList(pressedLeft, pressedRight, pressedT, releasedLeft, releasedRight, releasedT));
/*
            KeyboardEvent pressedP = new KeyboardEvent();
            pressedP.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
            pressedP.setKey(KeyboardEvent.KEY_P);
            keyboard.addEventListener(pressedP);

 */

        }

        @Override
        public void keyPressed(KeyboardEvent keyboardEvent) {
            switch (keyboardEvent.getKey()) {
                case KeyboardEvent.KEY_T -> player.activateTurbo();
                case KeyboardEvent.KEY_P -> player.revive();
                default -> player.moveInput(keyboardEvent.getKey());
            }
        }

        @Override
        public void keyReleased(KeyboardEvent keyboardEvent) {
            switch (keyboardEvent.getKey()) {
                case KeyboardEvent.KEY_T -> player.deactivateTurbo();
                default -> player.stop(keyboardEvent.getKey());
            }
        }

        @Override
        public void removeAll() {
            for (KeyboardEvent k : allKeys) {
                keyboard.removeEventListener(k);
            }
        }
    }
}
