/**
 * Copyright (c) 2013, Daniel Murphy
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 */
/**
 * Created at 4:56:29 AM Jan 14, 2011
 */
package org.jbox2d.testbed.tests;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.testbed.framework.SettingsIF;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Daniel Murphy
 */
public class Level1 extends TestbedTest {
    private static final long BULLET_TAG = 1;

    private static final Logger log = LoggerFactory.getLogger(Level1.class);

    public static final int e_columnCount = 1;
    public static final int e_rowCount = 10;
    private final float maxSpeedX = 6f;
    private final float minSpeedX = -6f;
    private final float maxSpeedXAir = 3f;
    private final float minSpeedXAir = -3f;
    private final float maxSpeedY = 5f;
    AtomicBoolean isExplose = new AtomicBoolean(false);
    private long lastFire = 0;
    Body m_bullet;
    Body action_body;
    List<Body> destroyableList = new ArrayList<>();
    List<Body> objectToExplode = new ArrayList<>();

    @Override
    public Long getTag(Body argBody) {
        if (argBody == m_bullet) {
            return BULLET_TAG;
        }
        return super.getTag(argBody);
    }

    @Override
    public void processBody(Body argBody, Long argTag) {
        if (argTag == BULLET_TAG) {
            m_bullet = argBody;
            return;
        }
        super.processBody(argBody, argTag);
    }

    @Override
    public boolean isSaveLoadEnabled() {
        return true;
    }

    @Override
    public void initTest(boolean deserialized) {
        if (deserialized) {
            return;
        }

        {
            BodyDef bd = new BodyDef();
            Body ground = getWorld().createBody(bd);

            EdgeShape shape = new EdgeShape();
            shape.set(new Vec2(-50.0f, -20.0f), new Vec2(50.0f, -20.0f));
            ground.createFixture(shape, 0.0f);

            shape.set(new Vec2(-50.0f, 30.0f), new Vec2(50.0f, 30.0f));
            ground.createFixture(shape, 0.0f);

            shape.set(new Vec2(50.0f, 30.0f), new Vec2(50.0f, -20.0f));
            ground.createFixture(shape, 0.0f);

            shape.set(new Vec2(-50.0f, 30.0f), new Vec2(-50.0f, -20.0f));
            ground.createFixture(shape, 0.0f);



            shape.set(new Vec2(-50.0f, 6.0f), new Vec2(20.0f, 6.0f));
            ground.createFixture(shape, 0.0f);

            shape.set(new Vec2(-15.0f, 3.0f), new Vec2(50.0f, 3.0f));
            ground.createFixture(shape, 0.0f);

        }

        float xs[] = new float[]{-10.0f, -10.0f, -5.0f, 5.0f, 10.0f};

        for (int j = 0; j < e_columnCount; ++j) {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.5f, 0.5f);

            FixtureDef fd = new FixtureDef();
            fd.shape = shape;
            fd.density = 1.0f;
            fd.friction = 0.3f;

            for (int i = 0; i < e_rowCount; ++i) {
                BodyDef bd = new BodyDef();
                bd.type = BodyType.DYNAMIC;

                int n = j * e_rowCount + i;
                assert (n < e_rowCount * e_columnCount);

                float x = 0.0f;
                bd.position.set(xs[j] + x, 10 + 0.752f + 1.54f * i);
                Body body = getWorld().createBody(bd);
                body.createFixture(fd);
                destroyableList.add(body);
            }
        }
        createActionBody();
        m_bullet = null;
    }

    private void fireBullet() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastFire > 3000) {
            if (m_bullet != null) {
                getWorld().destroyBody(m_bullet);
                m_bullet = null;
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
                bd.position.set(-50.0f, 5.0f);

                m_bullet = getWorld().createBody(bd);
                m_bullet.createFixture(fd);

                m_bullet.setLinearVelocity(new Vec2(400.0f, 0.0f));
                lastFire = currentTime;
            }

        }
    }

    private void createActionBody() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 1.0f;
        fd.friction = 0.3f;

        BodyDef bd = new BodyDef();
        bd.actionBody = true;
        bd.type = BodyType.DYNAMIC;


        float x = 0.0f;
        // float x = RandomFloat(-0.02f, 0.02f);
        // float x = i % 2 == 0 ? -0.025f : 0.025f;
        bd.position.set(-15, 15);
        action_body = getWorld().createBody(bd);
        action_body.createFixture(fd);
    }

    public void keyPressed() {
        boolean hasContact = action_body.m_contactList != null;
        if (getModel().getKeys()['a']) {
            if (action_body != null && action_body.getLinearVelocity().x > minSpeedX && hasContact) {
                Vec2 newVel = new Vec2(action_body.getLinearVelocity().x - 1, action_body.getLinearVelocity().y);
                action_body.setLinearVelocity(newVel);
            }
            if (action_body != null && action_body.getLinearVelocity().x > minSpeedXAir && !hasContact) {
                Vec2 newVel = new Vec2(action_body.getLinearVelocity().x - 1, action_body.getLinearVelocity().y);
                action_body.setLinearVelocity(newVel);
            }
        }
        if (getModel().getKeys()['d']) {
            if (action_body != null && action_body.getLinearVelocity().x < maxSpeedX && hasContact) {
                Vec2 newVel = new Vec2(action_body.getLinearVelocity().x + 1, action_body.getLinearVelocity().y);
                action_body.setLinearVelocity(newVel);
            }
            if (action_body != null && action_body.getLinearVelocity().x < maxSpeedXAir && !hasContact) {
                Vec2 newVel = new Vec2(action_body.getLinearVelocity().x + 1, action_body.getLinearVelocity().y);
                action_body.setLinearVelocity(newVel);
            }
        }
        if (getModel().getKeys()[' ']) {
            if (action_body != null && action_body.getLinearVelocity().y < maxSpeedY && hasContact) {
                Vec2 newVel = new Vec2(action_body.getLinearVelocity().x, action_body.getLinearVelocity().y + 3);
                action_body.setLinearVelocity(newVel);
            }
        }
    }

    public void beginContact(Contact contact) {
        Body bodyToDestroy;
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if(fixtureA.m_body == m_bullet){
            bodyToDestroy=fixtureB.m_body;
        } else if(fixtureB.m_body == m_bullet){
            bodyToDestroy=fixtureA.m_body;
        } else {
            return;
        }
        if (bodyToDestroy == action_body) {
            log.info("bullet speed=" + m_bullet.m_linearVelocity.x);
            if (m_bullet.m_linearVelocity.x > 50 || m_bullet.m_linearVelocity.y > 50) {
                isExplose.set(true);
                return;
            }
        }
        if (destroyableList.contains(bodyToDestroy) ) {
            if (m_bullet.m_linearVelocity.x > 50 || m_bullet.m_linearVelocity.y > 50) {
                objectToExplode.add(bodyToDestroy);
                Vec2 bulletVel = m_bullet.getLinearVelocity();
                bulletVel.x = bulletVel.x - 30;
                m_bullet.setLinearVelocity(bulletVel);
                return;
            }
        }
    }

    public void endContact(Contact contact) {

    }

    @Override
    public void step(SettingsIF settings) {
        super.step(settings);
        keyPressed();
        fireBullet();
        explose();
    }

    private void explose() {
        if (isExplose.get()) {
            Vec2 oldPosition = action_body.getPosition();
            m_world.destroyBody(action_body);
            for (int i = 0; i < 20; i++) {
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(0.1f, 0.1f);

                FixtureDef fd = new FixtureDef();
                fd.shape = shape;
                fd.density = 1.0f;
                fd.friction = 0.3f;
                BodyDef bd = new BodyDef();
                bd.actionBody = true;
                bd.type = BodyType.DYNAMIC;
                bd.position.set(oldPosition.x, oldPosition.y);
                Body body = getWorld().createBody(bd);
                if (body != null) {
                    body.createFixture(fd);
                }
            }
        }
        isExplose.set(false);
        for(Body body:objectToExplode) {
            Vec2 oldPosition = body.getPosition();
            m_world.destroyBody(body);
            for (int i = 0; i < 10; i++) {
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(0.1f, 0.1f);

                FixtureDef fd = new FixtureDef();
                fd.shape = shape;
                fd.density = 1.0f;
                fd.friction = 0.3f;
                BodyDef bd = new BodyDef();
                bd.type = BodyType.DYNAMIC;
                bd.position.set(oldPosition.x, oldPosition.y);
                Body newBody = getWorld().createBody(bd);
                if (newBody != null) {
                    newBody.createFixture(fd);
                }
            }
        }
        objectToExplode.clear();
    }

    @Override
    public String getTestName() {
        return "Level 1";
    }
}
