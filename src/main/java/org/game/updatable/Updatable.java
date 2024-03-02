package main.java.org.game.updatable;

import main.java.org.game.Isten;

public abstract class Updatable {

    private String tag="";
    private boolean isInitialized=false;

    /** az elso update hivas elott hivodik.*/
    public abstract void onStart(Isten isten);
    public abstract void onUpdate(Isten isten, float deltaTime);
    public abstract void onDestroy();

    public final String getTag(){return tag;}
    public final void setTag(String tag){this.tag=tag;}
    public final boolean isInitialized(){return this.isInitialized;}
    public final void setInitializedTrue(){this.isInitialized=true;}
}
