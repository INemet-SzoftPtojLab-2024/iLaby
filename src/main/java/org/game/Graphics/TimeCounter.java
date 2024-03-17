package main.java.org.game.Graphics;

import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

public class TimeCounter extends Updatable {
    private double timeRemaining;//mértékegység:sec
    private Image timerBackgroundImage;
    private TextUI timeText;
    GameRenderer renderer;

    /**
     * @param time ennyitol szamol vissza, mertekegyseg:sec
     */
    public TimeCounter(double time){
        timeRemaining=time;
        timerBackgroundImage=null;
        timeText=null;
        renderer=null;
    }
    public String secondsToMMSS(double seconds) {
        long minutes = (long) (seconds / 60);
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, (long) seconds);
    }

    @Override
    public void onStart(Isten isten) {
        renderer=isten.getRenderer();
        //valamiért az Imagenak a koordinatait vilag-koordinatarendszerben, a TextUI koordinatait pedig eszkoz-koordinatarendszerben kell megadni
        timeText=new TextUI(secondsToMMSS(timeRemaining),new Vec2(renderer.getWidth()/2.0f+5.0f,10),"./assets/Monocraft.ttf",13,255,255,0);
        timerBackgroundImage=new Image(new Vec2(0,-renderer.convertScreenToWorld(new Vec2(0,renderer.getHeight()),isten.getCamera()).y-0.5f),new Vec2(2,1),"./assets/timer_background.png");

        timeText.setVisibility(true);
        timerBackgroundImage.setVisibility(true);
        isten.getRenderer().addRenderable(timerBackgroundImage);
        isten.getRenderer().addRenderable(timeText);
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        timeRemaining-=deltaTime;
        if(!timeText.getText().equals(secondsToMMSS(timeRemaining))){
            timeText.setVisibility(false);
            timeText=new TextUI(secondsToMMSS(timeRemaining),new Vec2(renderer.getWidth()/2.0f+5.0f,10),"./assets/Monocraft.ttf",13,255,255,0);
            isten.getRenderer().addRenderable(timeText);
            timeText.setVisibility(true);
        }
    }

    @Override
    public void onDestroy() {

    }
}
