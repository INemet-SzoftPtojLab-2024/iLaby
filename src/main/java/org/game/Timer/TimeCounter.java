package main.java.org.game.Timer;

import main.java.org.game.Graphics.*;
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
        timeText=new TextUI(secondsToMMSS(timeRemaining),new Vec2(5,10),"./assets/Monocraft.ttf",13,255,255,0);
        timerBackgroundImage=new ImageUI(new Vec2(0,25),new Vec2(100,50),"./assets/timer_background.png");

        timeText.setVisibility(true);
        timeText.setSortingLayer(-69);
        timeText.setAlignment(Renderable.CENTER, Renderable.TOP);
        timerBackgroundImage.setVisibility(true);
        timerBackgroundImage.setSortingLayer(-69);
        timerBackgroundImage.setAlignment(Renderable.CENTER, Renderable.TOP);
        isten.getRenderer().addRenderable(timerBackgroundImage);
        isten.getRenderer().addRenderable(timeText);
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        timeRemaining-=deltaTime;
        if(!timeText.getText().equals(secondsToMMSS(timeRemaining))){
            timeText.setText(secondsToMMSS(timeRemaining));
        }
    }

    @Override
    public void onDestroy() {

    }
}
