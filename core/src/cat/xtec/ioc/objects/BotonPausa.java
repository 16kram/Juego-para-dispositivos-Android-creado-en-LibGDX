package cat.xtec.ioc.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import cat.xtec.ioc.helpers.AssetManager;

public class BotonPausa extends Actor {

    // Parámetros del botón de pausa
    private Vector2 position;
    private int width, height;

    public BotonPausa(float x, float y, int width, int height) {
        // Inicialitzem els arguments segons la crida del constructor
        position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        // Per a la gestio de hit
        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(AssetManager.pause, position.x, position.y, width, height);
    }

}
