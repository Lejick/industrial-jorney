package org.jbox2d.testbed;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

public class Enemy {
    protected Body enemyBody;
    public Body activeBullet;
    protected World world;
    public long weapon1CoolDown = 50;
    public long lastFireWeapon1 = 0;

    public Enemy(Body enemyBody, World world) {
        this.enemyBody = enemyBody;
        this.world = world;
    }

    public Body getBody() {
        return enemyBody;
    }

    public Body fireWeapon1(Vec2 targetPosition) {
        Vec2 orientation = new Vec2(targetPosition.x - enemyBody.getPosition().x,
                targetPosition.y - enemyBody.getPosition().y);

        float norm = Math.abs(enemyBody.getPosition().x - targetPosition.x);
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
        bd.position.set(enemyBody.getPosition().x + 1 * orientation.x, enemyBody.getPosition().y + 1 * orientation.y);
        Body enemy_bullet = world.createBody(bd);
        enemy_bullet.createFixture(fd);
        enemy_bullet.setLinearVelocity(new Vec2(orientation.x * 300, orientation.y * 300));
        activeBullet = enemy_bullet;
        return enemy_bullet;
    }

}
