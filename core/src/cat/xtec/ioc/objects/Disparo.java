package cat.xtec.ioc.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Settings;

public class Disparo extends Actor {
    // Parámetros del disparo
    private Vector2 position;
    private int width, height;
    private int velocidad = 4;


    //Si la bala se dispara se pone a true
    private boolean balaDisparada = false;

    private Rectangle collisionRect;


    public Disparo(float x, float y, int width, int height) {
        // Inicialitzem els arguments segons la crida del constructor
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);

        // Creem el rectangle de col·lisions
        collisionRect = new Rectangle();
    }

    public void act(float delta) {
        if (balaDisparada) {
            position.x = position.x + velocidad;
            collisionRect.set(position.x, position.y, width, height);
            if (position.x > Settings.GAME_WIDTH) {
                balaDisparada = false;
                position.x = Settings.DISPARO_STARTX;
                this.remove();//Elimina la bala
            }
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(AssetManager.disparo, position.x, position.y, width, height);
    }

    //Selecciona si la bala se dispara o no
    public void setBalaDisparada(boolean balaDisparada) {
        this.balaDisparada = balaDisparada;
    }

    //Retorna si la bala ha sido disparada
    public boolean getBalaDisparada() {
        return balaDisparada;
    }

    //Inicializamos las coordenadas del disparo
    public void reset() {
        position.x = Settings.DISPARO_STARTX;
        position.y = Settings.DISPARO_STARTY;
        balaDisparada = false;
        collisionRect = new Rectangle();
        this.remove();
    }

    //Añadimos la posición Y del robot al Disparo para que estén a la misma altura
    public void setPositionY(float y) {
        //Este IF evita que la bala pueda ser cambiada de posición vertical mientras está en movimiento
        if (!balaDisparada) {
            position.y = y + 8;
        }
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }


}
