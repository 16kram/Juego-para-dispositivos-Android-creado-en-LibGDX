package cat.xtec.ioc.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import cat.xtec.ioc.objects.BotonPausa;
import cat.xtec.ioc.objects.Disparo;
import cat.xtec.ioc.objects.Robot;
import cat.xtec.ioc.screens.GameScreen;
import cat.xtec.ioc.utils.Settings;

public class InputHandler implements InputProcessor {

    // Enter per a la gesitó del moviment d'arrastrar
    int previousY = 0;
    // Objectes necessaris
    private Robot robot;
    private Disparo disparo;
    private GameScreen screen;
    private Vector2 stageCoord;
    private BotonPausa botonPausa;
    private Stage stage;

    public InputHandler(GameScreen screen) {

        // Obtenim tots els elements necessaris
        this.screen = screen;
        robot = screen.getRobot();
        disparo = screen.getDisparo();
        botonPausa = screen.getBotonPausa();
        stage = screen.getStage();

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        switch (screen.getCurrentState()) {
            case READY:

                // Si fem clic comencem el joc
                screen.setCurrentState(GameScreen.GameState.RUNNING);
                break;
            case RUNNING:
                previousY = screenY;

                stageCoord = stage.screenToStageCoordinates(new Vector2(screenX, screenY));
                Actor actorHit = stage.hit(stageCoord.x, stageCoord.y, true);
                if (actorHit != null) {
                    Gdx.app.log("HIT", actorHit.getName());
                    //Si pulsamos el robot dispara
                    if (actorHit.getName() == "robot") {
                        //Obtenemos la posición Y del robot y la pasamos al disparo
                        float posY = stage.getRoot().findActor("robot").getY();
                        disparo.setPositionY(posY);
                        //Añadimos la bala al stage y la activamos para que se dispare
                        stage.addActor(disparo);
                        disparo.setName("disparo");
                        if (!disparo.getBalaDisparada()) {//Sólo suena cada vez que sale una bala
                            AssetManager.disparoSound.play();
                        }
                        disparo.setBalaDisparada(true);
                        robot.bocaAbierta();
                    }
                    //Si pulsamos el botón pausa
                    if (actorHit.getName() == "botonPausa") {
                        screen.setCurrentState(GameScreen.GameState.PAUSE);
                        botonPausa.remove();//Quitamos el botón de pausa
                        stage.getRoot().findActor("puntuacion").remove();
                        AssetManager.music.setVolume(Settings.VOL_MUSIC_LOW);//Bajamos el volúmen de la música
                    }
                }
                break;
            // Si l'estat és GameOver tornem a iniciar el joc
            case GAMEOVER:
                screen.reset();
                break;

            //Hacemos una pausa y si tocamos en la pantalla se vuelve a poner en marcha el juego
            case PAUSE:
                stage.addActor(botonPausa);//Añadimos el botón de pausa
                screen.setCurrentState(GameScreen.GameState.RUNNING);
                AssetManager.music.setVolume(Settings.VOL_MUSIC_HIGH);//Subimos el volúmen de la música
                break;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        // Quan deixem anar el dit acabem un moviment
        // i posem la nau en l'estat normal
        robot.goStraight();
        return true;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Posem un umbral per evitar gestionar events quan el dit està quiet
        if (Math.abs(previousY - screenY) > 2)

            // Si la Y és major que la que tenim
            // guardada és que va cap avall
            if (previousY < screenY) {
                robot.goDown();
            } else {
                // En cas contrari cap a dalt
                robot.goUp();
            }
        // Guardem la posició de la Y
        previousY = screenY;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
