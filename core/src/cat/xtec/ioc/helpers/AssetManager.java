package cat.xtec.ioc.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import cat.xtec.ioc.utils.Settings;

public class AssetManager {

    // Sprite Sheet
    public static Texture sheet;

    // robot y fondo
    public static TextureRegion robot, robotDown, robotUp, robotBocaAbierta, robotBombilla, background;

    // bugs
    public static TextureRegion[] bug;
    public static Animation bugAnim;

    // Explosión
    public static TextureRegion[] explosion;
    public static Animation explosionAnim;

    //Disparo
    public static TextureRegion disparo;

    //Bonus
    public static TextureRegion bonus100;
    public static TextureRegion bonus100Cogido;
    public static TextureRegion bonus1000;
    public static TextureRegion bonus1000Cogido;

    // Sonidos
    public static Sound explosionSound;
    public static Sound bonusSound;
    public static Sound disparoSound;
    public static Music music;


    // Fuentes
    public static BitmapFont font;

    //Icono pausa
    public static TextureRegion pause;


    public static void load() {
        // Carreguem les textures i li apliquem el mètode d'escalat 'nearest'
        sheet = new Texture(Gdx.files.internal("spritesheet_juego.png"));
        sheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        // Sprites del robot
        robot = new TextureRegion(sheet, 0, 0, 46, 58);
        robot.flip(false, true);

        robotUp = new TextureRegion(sheet, 46, 0, 46, 58);
        robotUp.flip(false, true);

        robotDown = new TextureRegion(sheet, 92, 0, 46, 58);
        robotDown.flip(false, true);

        robotBocaAbierta = new TextureRegion(sheet, 141, 0, 52, 58);
        robotBocaAbierta.flip(false, true);

        robotBombilla = new TextureRegion(sheet, 196, 0, 52, 58);
        robotBombilla.flip(false, true);


        // Carreguem els 16 estats de l'bug
        bug = new TextureRegion[6];
        for (int i = 0; i < bug.length; i++) {
            bug[i] = new TextureRegion(sheet, i * 70, 58, 70, 90);
            bug[i].flip(false, true);
        }


        // Creem l'animació de l'bug i fem que s'executi contínuament
        bugAnim = new Animation(0.05f, bug);
        bugAnim.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        // Creem els 16 estats de l'explosió
        explosion = new TextureRegion[20];//Se añaden 4 frames más para que desaparezca la explosión

        // Carreguem els 16 estats de l'explosió
        int index = 0;
        for (int k = 0; k < 5; k++) {
            for (int j = 0; j < 4; j++) {
                explosion[index++] = new TextureRegion(sheet, 0 + (k * 62), 160 + (j * 64), 62, 64);
                //  explosion[index-1].flip(false, true);
            }
        }

        // Creamos la animación de la explosión
        explosionAnim = new Animation(0.04f, explosion);

        // Fondo de pantalla
        background = new TextureRegion(sheet, 0, 520, 3020, 370);
        background.flip(false, true);

        //Sprites del disparo
        disparo = new TextureRegion(sheet, 3, 926, 24, 24);
        disparo.flip(false, true);

        //Botón pause
        pause = new TextureRegion(sheet, 2, 956, 24, 24);
        pause.flip(false, true);

        //Sprites del bonus 100
        bonus100 = new TextureRegion(sheet, 0, 992, 26, 26);
        bonus100.flip(false, true);
        bonus100Cogido = new TextureRegion(sheet, 32, 992, 26, 26);
        bonus100Cogido.flip(false, true);

        //Sprites del bonus 1000
        bonus1000 = new TextureRegion(sheet, 130, 992, 32, 26);
        bonus1000.flip(false, true);
        bonus1000Cogido = new TextureRegion(sheet, 168, 992, 32, 26);
        bonus1000Cogido.flip(false, true);

        /******************************* Sounds *************************************/
        // Explosió
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));

        // Música del joc
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/ComputerLove.mp3"));
        music.setVolume(Settings.VOL_MUSIC_HIGH);
        music.setLooping(true);

        //Sonido de bonus
        bonusSound = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.wav"));

        //Sonido del disparo
        disparoSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser-shot.wav"));


        /******************************* Text *************************************/
        // Fuente de texto
        FileHandle fontFile = Gdx.files.internal("fonts/spray.fnt");
        font = new BitmapFont(fontFile, true);
        font.getData().setScale(0.4f);


    }


    public static void dispose() {
        // Alliberem els recursos gràfics i de audio
        sheet.dispose();
        explosionSound.dispose();
        bonusSound.dispose();
        music.dispose();
    }


}
