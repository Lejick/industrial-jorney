package org.jbox2d.testbed.framework;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

public class Gun {
    private long lastFireStep;
    private long lastStep;
    private long cooldown;
    private Body bullet;
    private Body gunBody;
    private float x;
    private float bulletVel;
    private float y;
    private World world;

    public Gun(World world, float x, float y, long cooldown,float bulletVel) {
        this.cooldown = cooldown;
        this.x = x;
        this.y = y;
        this.world = world;
        this.bulletVel=bulletVel;
    }

    public void fire() {
        if (lastStep - lastFireStep > cooldown) {
            if (bullet != null) {
                world.destroyBody(bullet);
                bullet = null;
            }
            {
                CircleShape shape = new CircleShape();
                shape.m_radius = 0.25f;

                FixtureDef fd = new FixtureDef();
                fd.shape = shape;
                fd.density = 20.0f;
                fd.restitution = 0.05f;

                BodyDef bd = new BodyDef();
                bd.type = BodyType.DYNAMIC;
                bd.bullet = true;
                bd.position.set(x+1, y);

                bullet = world.createBody(bd);
                bullet.createFixture(fd);

                bullet.setLinearVelocity(new Vec2(bulletVel, 0.0f));
                lastFireStep = lastStep;
            }
        }
    }

    public long getLastFireStep() {
        return lastFireStep;
    }

    public void setLastFireStep(long lastFireStep) {
        this.lastFireStep = lastFireStep;
    }

    public long getLastStep() {
        return lastStep;
    }

    public void setLastStep(long lastStep) {
        this.lastStep = lastStep;
    }

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public Body getBullet() {
        return bullet;
    }

    public void setBullet(Body bullet) {
        this.bullet = bullet;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Body getGunBody() {
        return gunBody;
    }

    public void setGunBody(Body gunBody) {
        this.gunBody = gunBody;
    }
}
