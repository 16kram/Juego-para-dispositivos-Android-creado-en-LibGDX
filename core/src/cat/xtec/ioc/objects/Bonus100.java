package cat.xtec.ioc.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.screens.GameScreen;
import cat.xtec.ioc.utils.Settings;

public class Bonus100 extends Actor {
    // Parámetros del bonus100
    private Vector2 position;
    private float width, height;
    private float velocidad;
    private Rectangle collisionRect;
    private boolean bonusActivo = false;
    private Random probabilidad;
    private TextureRegion spriteBonus;
    private boolean puntosBonusActivos = true;
    private boolean activaBonus100 = true;
    private Random probabilidadTipoBonus;
    private int devolverPuntuacion = Settings.SCORE_INCREASE_1000;

    public Bonus100(float x, float y, int width, int height, float velocidad) {
        position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.velocidad = velocidad;
        collisionRect = new Rectangle();
        probabilidad = new Random();
        probabilidadTipoBonus = new Random();
        spriteBonus = AssetManager.bonus100;
    }

    public void act(float delta) {
        //Probabilidad de que aparezca el bonus por pantalla
        //Conforme se van incrementando los puntos es más dificil que salgan los bonus
        if (probabilidad.nextInt(GameScreen.puntos + 1000) < 10 && !bonusActivo) {
            bonusActivo = true;
            //Probabilidad de que aparezca un bonus de 100 o de 1000 por pantalla
            if (probabilidadTipoBonus.nextInt(100) > 30) {//Hay un 70% de que salga el bonus de 100 y un 30% de que salga el bonus de 1000
                spriteBonus = AssetManager.bonus100;
                activaBonus100 = true;
                velocidad = 2.0f;//Disminuimos la velocidad si el bonus es de 100
                devolverPuntuacion = Settings.SCORE_INCREASE;
            } else {
                spriteBonus = AssetManager.bonus1000;
                activaBonus100 = false;
                velocidad = 8.0f;//Aumentamos la velocidad si el bonus es de 1000
                devolverPuntuacion = Settings.SCORE_INCREASE_1000;
            }
        }

        //Si el bonus está activo se muestra por pantalla
        if (bonusActivo) {
            position.x = position.x - velocidad;
            collisionRect.set(position.x, position.y, width, height);
            if (position.x < -Settings.BONUS100_WIDTH) {
                position.x = Settings.GAME_WIDTH;
                position.y = Settings.GAME_HEIGHT / 3;
                bonusActivo = false;
                puntosBonusActivos = true;//Cuando el bonus toca el lado izquierdo se vuelve a activar la puntuación
            }
            position.y += Math.sin(position.x / 30) * 4;
        }
    }

    //Inicializamos las coordenadas del reset
    public void reset() {
        position.x = Settings.GAME_WIDTH;
        position.y = Settings.GAME_HEIGHT / 3;
        bonusActivo = false;
        activaBonus100 = true;
        collisionRect = new Rectangle();
        this.remove();
    }

    //Si cogemos el bonus cambia el sprite según sea de 100 o de 1000
    public void bonusCogido() {
        if (activaBonus100) {
            spriteBonus = AssetManager.bonus100Cogido;
        } else {
            spriteBonus = AssetManager.bonus1000Cogido;
        }
    }

    //Establece si la puntuación del bonus está activa o no
    public void setPuntosBonusActivos(boolean puntos) {
        puntosBonusActivos = puntos;
    }

    //Se mira si se puede activar la puntuación del bonus para que sólo cuente una vez
    public boolean getPuntosBonusActivos() {
        return puntosBonusActivos;
    }

    //Devuelve la puntuación
    public int getPuntuacion() {
        return devolverPuntuacion;
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(spriteBonus, position.x, position.y, width, height);
    }
}
