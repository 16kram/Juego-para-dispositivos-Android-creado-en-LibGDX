package cat.xtec.ioc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.Viewport;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.helpers.InputHandler;
import cat.xtec.ioc.objects.Bonus100;
import cat.xtec.ioc.objects.BotonPausa;
import cat.xtec.ioc.objects.Disparo;
import cat.xtec.ioc.objects.Robot;
import cat.xtec.ioc.objects.ScrollHandler;
import cat.xtec.ioc.utils.Settings;


public class GameScreen implements Screen {

    // Els estats del joc

    public enum GameState {

        READY, RUNNING, GAMEOVER, PAUSE

    }

    private GameState currentState;

    // Objectes necessaris
    private Stage stage;
    private Robot robot;
    private ScrollHandler scrollHandler;
    private Disparo disparo;
    private BotonPausa botonPausa;
    private Label.LabelStyle textStyle;
    private Label puntuacion;
    private Bonus100 bonus100;
    Preferences prefs;//Preferencias para hacer persistente la puntuación máxima

    // Encarregats de dibuixar elements per pantalla
    private ShapeRenderer shapeRenderer;
    private Batch batch;

    // Per controlar l'animació de l'explosió
    private float explosionTime = 0;

    // Preparem el textLayout per escriure text
    private GlyphLayout textLayout;

    //Para controlar la animación de la explosión del bug
    private boolean bugTocado = false;
    private float explosionTimeBug = 0;

    //Puntuación
    public static int puntos = 0;


    public GameScreen(Batch prevBatch, Viewport prevViewport) {

        // Iniciem la música
        AssetManager.music.play();

        // Creem el ShapeRenderer
        shapeRenderer = new ShapeRenderer();

        // Creem l'stage i assginem el viewport
        stage = new Stage(prevViewport, prevBatch);

        batch = stage.getBatch();

        // Creem el robot i la resta d'objectes
        robot = new Robot(Settings.ROBOT_STARTX, Settings.ROBOT_STARTY, Settings.ROBOT_WIDTH, Settings.ROBOT_HEIGHT);
        disparo = new Disparo(Settings.DISPARO_STARTX, Settings.DISPARO_STARTY, Settings.DISPARO_WIDTH, Settings.DISPARO_HEIGHT);
        bonus100 = new Bonus100(Settings.GAME_WIDTH, Settings.GAME_HEIGHT / 3, Settings.BONUS100_WIDTH, Settings.BONUS100_HEIGHT, 2.0f);
        botonPausa = new BotonPausa(Settings.PAUSA_STARTX, Settings.PAUSA_STARTY, Settings.PAUSA_WIDTH, Settings.PAUSA_HEIGHT);
        textStyle = new Label.LabelStyle(AssetManager.font, null);
        puntuacion = new Label(Settings.SCORE + puntos, textStyle);
        puntuacion.setPosition(Settings.SCORE_STARTX, Settings.SCORE_STARTY);
        scrollHandler = new ScrollHandler();

        // Afegim els actors a l'stage
        stage.addActor(scrollHandler);
        stage.addActor(robot);
        stage.addActor(botonPausa);
        stage.addActor(puntuacion);
        stage.addActor(bonus100);

        // Donem nom a l'Actor
        robot.setName("robot");

        //Damos nombre al botón de pausa
        botonPausa.setName("botonPausa");

        //Damos nombre a la puntuacion
        puntuacion.setName("puntuacion");

        // Iniciem el GlyphLayout
        textLayout = new GlyphLayout();
        textLayout.setText(AssetManager.font, "Are you\nready?");

        currentState = GameState.READY;

        // Assignem com a gestor d'entrada la classe InputHandler
        Gdx.input.setInputProcessor(new InputHandler(this));

        //Creamos un objeto Preferences para guardar la máxima puntuación del juego
        prefs = Gdx.app.getPreferences("prefs");

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        // Dibuixem tots els actors de l'stage
        stage.draw();

        // Depenent de l'estat del joc farem unes accions o unes altres
        switch (currentState) {

            case GAMEOVER:
                botonPausa.remove();
                //puntuacion.remove();
                updateGameOver(delta);
                break;
            case RUNNING:
                stage.addActor(botonPausa);
                stage.addActor(bonus100);
                stage.addActor(puntuacion);
                updateRunning(delta);
                break;
            case READY:
                botonPausa.remove();
                puntuacion.remove();
                updateReady();
                break;


        }
    }

    private void updateReady() {

        // Dibuixem el text al centre de la pantalla
        batch.begin();
        AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH / 2) - textLayout.width / 2, (Settings.GAME_HEIGHT / 2) - textLayout.height / 2);
        //Mostramos la puntuación en pantalla
        puntuacion.setText(Settings.SCORE + puntos);
        batch.end();

    }

    private void updateRunning(float delta) {
        stage.act(delta);

        if (scrollHandler.collides(robot)) {
            // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
            AssetManager.explosionSound.play();
            //Eliminamos el disparo
            //stage.getRoot().findActor("disparo").remove();
            //Eliminamos el robot
            stage.getRoot().findActor("robot").remove();
            disparo.remove();//Eliminamos el disparo
            if (puntos < 100) {
                textLayout.setText(AssetManager.font, "Game Over :'(");
            } else if (puntos >= 100 && puntos < 150) {
                textLayout.setText(AssetManager.font, "Necesita entrenar mas :'(");
            } else if (puntos > 150) {
                textLayout.setText(AssetManager.font, "Buen trabajo!  :)");
            }
            //Guardamos en las Preferencias la puntuación máxima
            int puntMaxGuardada = prefs.getInteger("puntMax");
            if (puntos > puntMaxGuardada) {
                prefs.putInteger("puntMax", puntos);
                prefs.flush();//Hacemos persistente la puntuación máxima
            }
            currentState = GameState.GAMEOVER;
        }

        //Si hay colisión entre el disparo y un bug
        if (scrollHandler.collides(disparo)) {
            bugTocado = true;
            AssetManager.explosionSound.play();
            Gdx.app.log("COLISION", "Colisión entre bala y bug");
            puntos += Settings.SCORE_INCREASE_BUG;
            Gdx.app.log("SCORE", "SCORE " + puntos);
        }

        //Activa la animación de la explosión del bug
        if (bugTocado) {
            explosionBug(delta);
        }

        //Si hay colisión entre el robot y el bonus100
        if (Intersector.overlaps(bonus100.getCollisionRect(), robot.getCollisionRect())) {
            if (bonus100.getPuntosBonusActivos()) {
                AssetManager.bonusSound.play();
                puntos += bonus100.getPuntuacion();
                bonus100.setPuntosBonusActivos(false);
            }
            robot.robotBombilla();//Si coge el bonus pone el robot con cara de bombilla
            bonus100.bonusCogido();
            //bonus100.reset();
            Gdx.app.log("COLISION", "Colision BONUS");
        }


        //Mostramos la puntuación en pantalla
        batch.begin();
        puntuacion.setText(Settings.SCORE + puntos);
        batch.end();
    }

    private void updateGameOver(float delta) {
        stage.act(delta);
        batch.begin();
        AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH - textLayout.width) / 2, (Settings.GAME_HEIGHT - textLayout.height) / 2);
        // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
        batch.draw((TextureRegion) AssetManager.explosionAnim.getKeyFrame(explosionTime, false), (robot.getX() + robot.getWidth() / 2) - 32, robot.getY() + robot.getHeight() / 2 - 32, 64, 64);
        batch.end();
        explosionTime += delta;
    }

    //Explosión del bug
    private void explosionBug(float delta) {
        //stage.act(delta);
        batch.begin();
        // Si hi ha hagut col·lisió:
        batch.draw((TextureRegion) AssetManager.explosionAnim.getKeyFrame(explosionTimeBug, false), scrollHandler.leeposX(), scrollHandler.leeposY() - Settings.ROBOT_HEIGHT / 2, 64, 64);
        batch.end();
        explosionTimeBug += delta;
        //Si ha finalizado la animación
        if (AssetManager.explosionAnim.isAnimationFinished(explosionTimeBug)) {
            bugTocado = false;
            explosionTimeBug = 0;
        }
    }

    public void reset() {

        // Posem el text d'inici
        textLayout.setText(AssetManager.font, "Are you\nready?");
        // Cridem als restart dels elements.
        robot.reset();
        disparo.reset();
        bonus100.reset();
        scrollHandler.reset();

        // Posem l'estat a 'Ready'
        currentState = GameState.READY;

        // Afegim el robot a l'stage
        stage.addActor(robot);

        // Posem a 0 les variables per controlar el temps jugat i l'animació de l'explosió
        explosionTime = 0.0f;

        //Ponemos lo puntuación a cero
        puntos = 0;

    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        shapeRenderer.dispose();
    }

    public Robot getRobot() {
        return robot;
    }

    public Disparo getDisparo() {
        return disparo;
    }

    public Stage getStage() {
        return stage;
    }

    public ScrollHandler getScrollHandler() {
        return scrollHandler;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    public BotonPausa getBotonPausa() {
        return botonPausa;
    }
}
