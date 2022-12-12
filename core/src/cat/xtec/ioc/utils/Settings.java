package cat.xtec.ioc.utils;

public class Settings {

    // Mida del joc, s'escalarà segons la necessitat
    public static final int GAME_WIDTH = 240;
    public static final int GAME_HEIGHT = 135;

    // Propietats del robot
    public static final float ROBOT_VELOCITY = 75;
    public static final int ROBOT_WIDTH = 16;
    public static final int ROBOT_HEIGHT = 32;
    public static final float ROBOT_STARTX = 20;
    public static final float ROBOT_STARTY = GAME_HEIGHT / 2 - ROBOT_HEIGHT / 2;

    // Rang de valors per canviar la mida del bug.
    public static final float MAX_BUG = 1.5f;
    public static final float MIN_BUG = 0.5f;

    // Propiedades del disparo
    public static final int DISPARO_WIDTH = 12;
    public static final int DISPARO_HEIGHT = 12;
    public static final float DISPARO_STARTX = 30;
    public static final float DISPARO_STARTY = (GAME_HEIGHT / 2 - DISPARO_HEIGHT / 2) - 10;

    // Configuració Scrollable
    public static final int BUG_SPEED = -150;
    public static final int BUG_GAP = 75;
    public static final int BG_SPEED = -100;

    // Propiedades del bonus
    public static final int SCORE_INCREASE = 100; // s'incrementa en 100 cada cop que toca una bonus de 100
    public static final int SCORE_INCREASE_1000 = 1000; // s'incrementa en 1000 cada cop que toca una bonus de 1000
    public static final int SCORE_SPEED = -175;
    public static final int SCORE_INCREASE_BUG = 25;// s'incrementa en 25 cada cop que toca un bug

    //Propiedades del botón pausa
    public static final int PAUSA_WIDTH = 12;
    public static final int PAUSA_HEIGHT = 12;
    public static final float PAUSA_STARTX = (GAME_WIDTH - PAUSA_WIDTH) - 8;
    public static final float PAUSA_STARTY = 8;

    //Propiedades de la puntuación
    public static final float SCORE_STARTX = 4;
    public static final float SCORE_STARTY = 4;

    //Propiedades del volúmen de la música
    public static final float VOL_MUSIC_HIGH = 1.0f;
    public static final float VOL_MUSIC_LOW = 0.1f;
    public static final String SCORE = "SCORE: ";

    //Propiedades de Bonus100
    public static final int BONUS100_WIDTH = 24;
    public static final int BONUS100_HEIGHT = 24;


}
