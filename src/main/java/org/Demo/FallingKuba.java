package main.java.org.Demo;

import main.java.org.game.Graphics.Image;
import main.java.org.game.Isten;
import main.java.org.game.physics.Collider;
import main.java.org.game.physics.CollisionInfo;
import main.java.org.game.updatable.Updatable;
import main.java.org.linalg.Vec2;

public class FallingKuba extends Updatable {
    Collider c;
    Image im;


    @Override
    public void onStart(Isten isten) {
        //create collider
        c=new Collider(new Vec2(0,0), new Vec2(1,1));
        c.setMovability(true);
        c.setVelocity(new Vec2(0,-1));
        isten.getPhysicsEngine().addCollider(c);//register collider in the physics engine

        //create the image of the kuba
        im = new Image(new Vec2(), 1, 1, new Vec2(50,50), "./assets/cube.jpg");
        isten.getRenderer().addRenderable(im);//register image in the renderer
    }

    @Override
    public void onUpdate(Isten isten, double deltaTime) {
        var ci=c.getLastCollisionInfo();

        //detect collision and change direction
        if(ci.size()!=0)
        {
            if(ci.get(0).collisionNormal!=null)
                c.setVelocity(ci.get(0).collisionNormal);
            else
                c.setVelocity(new Vec2(0));
        }

        //move image
        //the origin of the image is in its top right corner, therefore the imagePos looks like this: screenSpace(collider position) - 0.5*imageScale
        Vec2 imageScale=Vec2.scale(c.getScale(), 50);
        Vec2 imagePos=isten.convertWorldToScreen(c.getPosition(),new Vec2(), 50);
        imagePos.x-=0.5*imageScale.x;
        imagePos.y-=0.5f*imageScale.y;
        im.setPosition(imagePos);
    }

    @Override
    public void onDestroy() {

    }
}
