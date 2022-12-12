package cat.xtec.ioc.objects;

import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.Random;

import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;

public class ScrollHandler extends Group {

    // Fons de pantalla
    Background bg, bg_back;

    // Bugs
    int numBugs;
    private ArrayList<Bug> bugs;

    // Objecte Random
    Random r;

    //Posición del bug en el momento que explota
    //Para que GameScreen pueda poner la explosión donde se encuentra ubicado el bug que ha colisionado con la bala
    private float posX, posY;

    public ScrollHandler() {

        // Creem els dos fons
        bg = new Background(0, 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);
        bg_back = new Background(bg.getTailX(), 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);

        // Afegim els fons al grup
        addActor(bg);
        addActor(bg_back);

        // Creem l'objecte random
        r = new Random();

        // Comencem amb 3 bugs
        numBugs = 3;

        // Creem l'ArrayList
        bugs = new ArrayList<Bug>();

        // Definim una mida aleatòria entre el mínim i el màxim
        float newSize = Methods.randomFloat(Settings.MIN_BUG, Settings.MAX_BUG) * 34;

        // Afegim el primer bug a l'Array i al grup
        Bug robot = new Bug(Settings.GAME_WIDTH, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.BUG_SPEED);
        bugs.add(robot);
        addActor(robot);

        // Des del segon fins l'últim bug
        for (int i = 1; i < numBugs; i++) {
            // Creem la mida al·leatòria
            newSize = Methods.randomFloat(Settings.MIN_BUG, Settings.MAX_BUG) * 34;
            // Afegim l'bug.
            robot = new Bug(bugs.get(bugs.size() - 1).getTailX() + Settings.BUG_GAP, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.BUG_SPEED);
            // Afegim l'asteroide a l'ArrayList
            bugs.add(robot);
            // Afegim l'asteroide al grup d'actors
            addActor(robot);
        }

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Si algun element està fora de la pantalla, fem un reset de l'element.
        if (bg.isLeftOfScreen()) {
            bg.reset(bg_back.getTailX());

        } else if (bg_back.isLeftOfScreen()) {
            bg_back.reset(bg.getTailX());

        }

        for (int i = 0; i < bugs.size(); i++) {
            Bug bug = bugs.get(i);
            if (bug.isLeftOfScreen()) {
                if (i == 0) {
                    bug.reset(bugs.get(bugs.size() - 1).getTailX() + Settings.BUG_GAP);
                } else {
                    bug.reset(bugs.get(i - 1).getTailX() + Settings.BUG_GAP);
                }
            }
        }
    }

    public boolean collides(Robot robot) {

        // Comprovem les col·lisions entre cada bug y el robot
        for (Bug bug : bugs) {
            if (bug.collides(robot)) {
                return true;
            }
        }
        return false;
    }

    //Comprobamos la colisión entre el bug y el disparo
    public boolean collides(Disparo disparo) {
        for (int n = 0; n < bugs.size(); n++) {
            if (bugs.get(n).collides(disparo)) {
                posX = bugs.get(n).getX();
                posY = bugs.get(n).getY();
                bugs.get(n).reset(Settings.GAME_WIDTH + 40);//El bug vuelve a aparecer por la izquierda
                disparo.reset();
                return true;
            }
        }
        return false;
    }

    //Devuelve la posición X del bug que ha hecho colisión con la bala
    public float leeposX() {
        return posX;
    }

    // //Devuelve la posición Y del bug que ha hecho colisión con la bala
    public float leeposY() {
        return posY;
    }


    public void reset() {

        // Posem el primer bug fora de la pantalla per la dreta
        bugs.get(0).reset(Settings.GAME_WIDTH);
        // Calculem les noves posicions de la resta d'bugs.
        for (int i = 1; i < bugs.size(); i++) {

            bugs.get(i).reset(bugs.get(i - 1).getTailX() + Settings.BUG_GAP);

        }
    }

    public ArrayList<Bug> getBugs() {
        return bugs;
    }
}