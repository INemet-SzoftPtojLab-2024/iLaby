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
        cg.addCollider(new Collider(new Vec2(0,3.5f), new Vec2(5,2)));
        cg.addCollider(new Collider(new Vec2(0,-3.5f), new Vec2(5,2)));
        isten.getPhysicsEngine().addColliderGroup(cg); //register the collider group in the physics engien


        //adding images
        for(Collider c : cg.getColliders())
        {
            Image im = new Image(new Vec2(), 1, 1, Vec2.scale(c.getScale(),50), "./assets/sus.png");
            Vec2 imageScale=Vec2.scale(c.getScale(), 50);
            Vec2 imagePos=isten.convertWorldToScreen(c.getPosition(),new Vec2(), 50);
            imagePos.x-=0.5f*imageScale.x;
            imagePos.y-=0.5f*imageScale.y;
            im.setPosition(imagePos);
            isten.getRenderer().addRenderable(im);//register the image in the renderer
        }
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {

    }

    @Override
    public void onDestroy() {

    }
}
