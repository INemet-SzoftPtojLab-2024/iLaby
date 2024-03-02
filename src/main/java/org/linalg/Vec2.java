package main.java.org.linalg;

public class Vec2 implements Cloneable{
    public float x, y;

    public Vec2(float x, float y)
    {
        this.x=x;
        this.y=y;
    }

    public Vec2(float nummah)
    {
        this.x=nummah;
        this.y=nummah;
    }

    public Vec2()
    {
        this.x=0;
        this.y=0;
    }

    public void scale(float factor)
    {
        x*=factor;
        y*=factor;
    }

    public float sqrMagnitude()
    {
        return x*x+y*y;
    }

    public float magnitude()
    {
        return (float)Math.sqrt(x*x+y*y);
    }

    public void normalize()
    {
        float length=magnitude();
        if(length>0.00001f)
        {
            x/=length;
            y/=length;
        }
    }

    @Override
    public Vec2 clone() {
        Vec2 cloned=new Vec2(this.x, this.y);
        return cloned;
    }

    //static stuff
    public static Vec2 up=new Vec2(0,1);
    public static Vec2 down=new Vec2(0, -1);
    public static Vec2 left=new Vec2(-1, 0);
    public static Vec2 right=new Vec2(1,0);

    public static Vec2 sum(Vec2 a, Vec2 b)
    {
        return new Vec2(a.x+b.x, a.y+b.y);
    }

    public static Vec2 subtract(Vec2 a, Vec2 b)
    {
        return new Vec2(a.x-b.x, a.y-b.y);
    }

    public float dot(Vec2 a, Vec2 b)
    {
        return a.x*b.x+a.y*b.y;
    }

    public static Vec2 scale(Vec2 vec, float factor)
    {
        Vec2 temp=vec.clone();
        temp.scale(factor);
        return temp;
    }

    public static Vec2 normalize(Vec2 vec)
    {
        Vec2 temp=vec.clone();
        temp.normalize();
        return temp;
    }

    public static Vec2 lerp(Vec2 a, Vec2 b, float i)
    {
        return new Vec2(a.x+(b.x-a.x)*i, a.y+(b.y-a.y)*i);
    }
}
