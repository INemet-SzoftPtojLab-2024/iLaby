package main.java.org.Demo;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.ColliderGroup;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

public class Chamber extends Updatable {
    ColliderGroup cg;

    @Override
    public void onStart(Isten isten) {
        //adding colliders
        cg=new ColliderGroup();
        cg.addCollider(new Collider(new Vec2(0,4), new Vec2(5,0.5f)));
        cg.addCollider(new Collider(new Vec2(0,-4), new Vec2(5,0.5f)));
        isten.getPhysicsEngine().addColliderGroup(cg); //register the collider group in the physics engien


        //adding images
        for(Collider c : cg.getColliders())
        {
            Image im = new Image(new Vec2(), 1, 1, new Vec2(250,25), "./assets/cube.jpg");
            Vec2 imageScale=Vec2.scale(c.getScale(), 50);
            Vec2 imagePos=isten.convertWorldToScreen(c.getPosition(),new Vec2(), 50);
            imagePos.x-=0.5f*imageScale.x;
            imagePos.y-=0.5f*imageScale.y;
            im.setPosition(imagePos);
            isten.getRenderer().addRenderable(im);
        }
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {

    }

    @Override
    public void onDestroy() {

    }
}
