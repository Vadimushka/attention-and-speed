package team6.g13it18k.gameworld;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import team6.g13it18k.asHelpers.AssetLoader;
import team6.g13it18k.gameobjects.Bird;
import team6.g13it18k.gameobjects.ScrollHandler;

public class GameWorld {

    private Bird bird;
    private ScrollHandler scroller;
    private Rectangle ground;
    private int score = 0;
    private float runTime = 0;
    private int midPointY;

    private GameState currentState;

    public enum GameState {
        MENU, READY, RUNNING, GAMEOVER, HIGHSCORE
    }

    public GameWorld(int midPointY) {
        currentState = GameState.MENU;
        this.midPointY = midPointY;
        bird = new Bird(33, midPointY - 5, 17, 22);
        scroller = new ScrollHandler(this, midPointY + 66);
        ground = new Rectangle(0, midPointY + 66, 136, 11);

    }

    public void update(float delta) {
        runTime += delta;

        switch (currentState) {
            case READY:
            case MENU:
                updateReady(delta);
                break;

            case RUNNING:
                updateRunning(delta);
                break;
            default:
                break;
        }

    }

    private void updateReady(float delta) {
        bird.updateReady(runTime);
        scroller.updateReady(delta);
    }

    private void updateRunning(float delta) {
        if (delta > .15f) {
            delta = .15f;
        }

        bird.update(delta);
        scroller.update(delta);

        if (scroller.collides(bird) && bird.isAlive()) {
            scroller.stop();
            bird.die();
            AssetLoader.dead.play();
        }

        if (Intersector.overlaps(bird.getBoundingCircle(), ground)) {
            scroller.stop();
            bird.die();
            bird.decelerate();
            currentState = GameState.GAMEOVER;

            if (score > AssetLoader.getHighScore()) {
                AssetLoader.setHighScore(score);
                currentState = GameState.HIGHSCORE;
            }
        }
    }

    public Bird getBird() {
        return bird;
    }

    public int getMidPointY() {
        return midPointY;
    }

    ScrollHandler getScroller() {
        return scroller;
    }

    int getScore() {
        return score;
    }

    public void addScore() {
        score += 1;
    }

    public void start() {
        currentState = GameState.RUNNING;
    }

    public void ready() {
        currentState = GameState.READY;
    }

    public void restart() {
        currentState = GameState.READY;
        score = 0;
        bird.onRestart(midPointY - 5);
        scroller.onRestart();
        currentState = GameState.READY;
    }

    public boolean isReady() {
        return currentState == GameState.READY;
    }

    public boolean isGameOver() {
        return currentState == GameState.GAMEOVER;
    }

    public boolean isHighScore() {
        return currentState == GameState.HIGHSCORE;
    }

    public boolean isMenu() {
        return currentState == GameState.MENU;
    }

    boolean isRunning() {
        return currentState == GameState.RUNNING;
    }
}