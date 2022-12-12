package cat.xtec.ioc.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.Random;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Settings;

public class Robot extends Actor {

    // Distintes posicions del robot, recta, pujant i baixant
    public static final int ROBOT_STRAIGHT = 0;
    public static final int ROBOT_UP = 1;
    public static final int ROBOT_DOWN = 2;
    public static final int ROBOT_BOCA_ABIERTA = 3;
    public static final int ROBOT_BOMBILLA = 4;


    // Paràmetres del robot
    private Vector2 position;
    private int width, height;
    private int direction;
    Random movimientoCabeza;

    private Rectangle collisionRect;


    public Robot(float x, float y, int width, int height) {

        // Inicialitzem els arguments segons la crida del constructor
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);

        // Inicialitzem la robot a l'estat normal
        direction = ROBOT_STRAIGHT;

        // Creem el rectangle de col·lisions
        collisionRect = new Rectangle();

        // Per a la gestio de hit
        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);

        //Probabilidad de que la cabeza esté en una de sus tres posiciones (arriba, centro, abajo)
        movimientoCabeza = new Random();


    }


    public void act(float delta) {
        super.act(delta);

        // Movem el robot depenent de la direcció controlant que no surti de la pantalla
        switch (direction) {
            case ROBOT_UP:
                if (this.position.y - Settings.ROBOT_VELOCITY * delta >= 0) {
                    this.position.y -= Settings.ROBOT_VELOCITY * delta;
                }
                break;
            case ROBOT_DOWN:
                if (this.position.y + height + Settings.ROBOT_VELOCITY * delta <= Settings.GAME_HEIGHT) {
                    this.position.y += Settings.ROBOT_VELOCITY * delta;
                }
                break;
            case ROBOT_STRAIGHT:
                break;
        }

        collisionRect.set(position.x, position.y, width, height);
        setBounds(position.x, position.y, width, height);

    }


    // Getters dels atributs principals
    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Robot getRobot() {
        return this;
    }

    // Canviem la direcció de la robot: Puja
    public void goUp() {
        direction = ROBOT_UP;
    }

    // Canviem la direcció de la robot: Baixa
    public void goDown() {
        direction = ROBOT_DOWN;
    }

    // Posem la robot al seu estat original
    public void goStraight() {
        direction = ROBOT_STRAIGHT;
    }

    //Ponemos el robot con la boca abierta para disparar
    public void bocaAbierta() {
        direction = ROBOT_BOCA_ABIERTA;
    }

    //Pone en la cara del robot una bombilla
    public void robotBombilla() {
        direction = ROBOT_BOMBILLA;
    }

    // Obtenim el TextureRegion depenent de la posició del robot
    public TextureRegion getRobotTexture() {

        switch (direction) {

            case ROBOT_STRAIGHT:
                return AssetManager.robot;
            case ROBOT_UP:
                return AssetManager.robotUp;
            case ROBOT_DOWN:
                return AssetManager.robotDown;
            case ROBOT_BOCA_ABIERTA:
                return AssetManager.robotBocaAbierta;
            case ROBOT_BOMBILLA:
                return AssetManager.robotBombilla;
            default:
                return AssetManager.robot;
        }
    }

    public void reset() {

        // La posem a la posició inicial i a l'estat normal
        position.x = Settings.ROBOT_STARTX;
        position.y = Settings.ROBOT_STARTY;
        direction = ROBOT_STRAIGHT;
        collisionRect = new Rectangle();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(getRobotTexture(), position.x, position.y, width, height);
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }
}
