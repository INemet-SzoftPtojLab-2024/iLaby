package main.java.org.game.Graphics;

import main.java.org.game.Isten;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

public class TimeCounter extends Updatable {
    private double timeRemaining; //Unit:sec
    private ImageUI timerBackgroundImage;
    private TextUI timeText;
    GameRenderer renderer;

    /**
     * @param time the available time, unit:sec
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
        //azért mert az User Interface(UI) dolgok a képernyő megfelelő helyére fixáltak, a szimplák meg nem
        timeText=new TextUI(secondsToMMSS(timeRemaining),new Vec2(renderer.getWidth()/2.0f+5.0f,10),"./assets/Monocraft.ttf",13,255,255,0);
        timerBackgroundImage=new ImageUI(new Vec2((float)renderer.getWidth()/2,25),new Vec2(100,50),"./assets/timer_background.png");

        timeText.setVisibility(true);
        timeText.setSortingLayer(-69);
        timerBackgroundImage.setVisibility(true);
        timerBackgroundImage.setSortingLayer(-69);
        isten.getRenderer().addRenderable(timerBackgroundImage);
        isten.getRenderer().addRenderable(timeText);
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        timeRemaining-=deltaTime;
        if(!timeText.getText().equals(secondsToMMSS(timeRemaining))){
            timeText.setText(secondsToMMSS(timeRemaining));
            timeText.setPosition(new Vec2(renderer.getWidth()/2.0f+5.0f,10));
            timerBackgroundImage.setPosition(new Vec2((float)renderer.getWidth()/2,25));
        }
    }

    @Override
    public void onDestroy() {

    }
}
