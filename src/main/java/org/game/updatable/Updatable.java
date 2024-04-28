package main.java.org.game.updatable;

import main.java.org.game.Isten;
import main.java.org.items.Chest;

import java.util.Vector;

public abstract class Updatable {

    private String tag="";
    private boolean isInitialized=false;
    private boolean isDestroyed=false;

    /** az elso update hivas elott hivodik.*/
    public abstract void onStart(Isten isten);
    public abstract void onUpdate(Isten isten, double deltaTime);
    public abstract void onDestroy();

    public final String getTag(){return tag;}
    public final void setTag(String tag){this.tag=tag;}
    public final boolean isInitialized(){return this.isInitialized;}
    public final void setInitializedTrue(){this.isInitialized=true;}
    public final boolean isDestroyed(){return this.isDestroyed;}
    public final void setDestroyedTrue(){this.isDestroyed=true;}

    public Vector<Chest> getChests() { return null; }
}
