package org.jbox2d.testbed;


import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

public class Hero {
    protected Body heroBody;
    public Body activeBullet;
    protected World world;
    public long stepInAir;

    public Hero(Body heroBody, World world) {
        this.heroBody = heroBody;
        this.world=world;
    }

    public Body fireWeapon1(Vec2 targetPosition){
        Vec2 orientation = new Vec2(targetPosition.x - heroBody.getPosition().x,
                targetPosition.y - heroBody.getPosition().y);

        float norm = Math.abs(targetPosition.x - heroBody.getPosition().x);
        orientation.x = orientation.x / norm;
        orientation.y = orientation.y / norm;
            CircleShape shape = new CircleShape();
            shape.m_radius = 0.25f;

            FixtureDef fd = new FixtureDef();
            fd.shape = shape;
            fd.density = 20.0f;
            fd.restitution = 0.05f;

            BodyDef bd = new BodyDef();
            bd.type = BodyType.DYNAMIC;
            bd.bullet = true;
            bd.position.set(heroBody.getPosition().x + 1 * orientation.x, heroBody.getPosition().y + 1 * orientation.y);

            Body hero_bullet = world.createBody(bd);
             hero_bullet.createFixture(fd);
            hero_bullet.setLinearVelocity(new Vec2(orientation.x * 300, orientation.y * 300));
            activeBullet=hero_bullet;
            return hero_bullet;
    }
    public Body getBody() {
        return heroBody;
    }

    public void setBody(Body heroBody) {
        this.heroBody = heroBody;
    }


}
