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
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.testbed.framework.SettingsIF;
import org.jbox2d.testbed.framework.TestbedTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jbox2d.testbed.framework.Gun;

import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Daniel Murphy
 */
public class Level1 extends TestbedTest {
    private static final long BULLET_TAG = 1;

    private static final Logger log = LoggerFactory.getLogger(Level1.class);
    private final static float maxSpeedX = 6f;
    private final static float minSpeedX = -6f;
    private final static float maxSpeedXAir = 3f;
    private final static float minSpeedXAir = -3f;
    private final static float maxSpeedY = 5f;

    private static float width = 60;
    private static float height = 40;
    private static final float commonPersonEdge = 1f;
    long lastDestroy_step = 0;
    long last_step = 0;
    boolean CAN_JUMP = true;
    List<Fixture> objectForJump = new ArrayList<>();
    List<Fixture> contactObjForJump = new ArrayList<>();
    Body action_body;
    Gun gun1;
    List<Body> destroyableList = Collections.synchronizedList(new ArrayList<>());
    List<Body> objectToExplode = Collections.synchronizedList(new ArrayList<>());
    List<Body> currentToErase = Collections.synchronizedList(new ArrayList<>());
    List<Body> nextToErase = Collections.synchronizedList(new ArrayList<>());

    @Override
    public Long getTag(Body argBody) {
        return super.getTag(argBody);
    }

    @Override
    public void processBody(Body argBody, Long argTag) {
        if (argTag == BULLET_TAG) {
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
        createGameBox();
        createPlatforms();
        createGuns();
        Body hero = createRectangle(-25, 15, commonPersonEdge, commonPersonEdge, true);
        Body simpleBox = createRectangle(-20, 15, commonPersonEdge, commonPersonEdge, false);
        destroyableList.add(simpleBox);
        destroyableList.add(hero);
    }


    private void createPlatforms() {
        BodyDef bd = new BodyDef();
        Body ground = getWorld().createBody(bd);
        EdgeShape shape = new EdgeShape();

        shape.set(new Vec2(-width / 2, height / 2 - commonPersonEdge * 6), new Vec2(width / 3, height / 2 - commonPersonEdge * 6));
        Fixture f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);

        shape.set(new Vec2(-width / 7, height / 2 - commonPersonEdge * 12), new Vec2(width / 2, height / 2 - commonPersonEdge * 12));
        f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);

    }

    private void createGuns() {
        gun1 = new Gun(m_world, -width / 2, commonPersonEdge * 12 - 2, 500, 200);
    }

    private void createGameBox() {
        BodyDef bd = new BodyDef();
        Body ground = getWorld().createBody(bd);

        EdgeShape shape = new EdgeShape();
        shape.set(new Vec2(-width / 2, -height / 2), new Vec2(width / 2, -height / 2));
        Fixture f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);

        shape.set(new Vec2(-width / 2, height / 2), new Vec2(width / 2, height / 2));
        ground.createFixture(shape, 0.0f);


        shape.set(new Vec2(width / 2, height / 2), new Vec2(width / 2, -height / 2));
        ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(-width / 2, height / 2), new Vec2(-width / 2, -height / 2));
        ground.createFixture(shape, 0.0f);
    }

    private Body createRectangle(float x, float y, float hx, float hy, boolean isHero) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(hx, hy);
        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 1.0f;
        fd.friction = 0.3f;
        BodyDef bd = new BodyDef();
        if (isHero) {
            bd.shapeColor = Color3f.BLUE;
        }
        bd.type = BodyType.DYNAMIC;
        bd.position.set(x, y);

        Body body = getWorld().createBody(bd);
        Fixture f = body.createFixture(fd);
        f.setUserData(CAN_JUMP);
        if (isHero) {
            action_body = body;
        }
        return body;
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
            if (action_body != null && action_body.getLinearVelocity().y < maxSpeedY && contactObjForJump.size() > 0) {
                Vec2 newVel = new Vec2(action_body.getLinearVelocity().x, action_body.getLinearVelocity().y + 3);
                action_body.setLinearVelocity(newVel);
            }
        }
    }

    public void beginContact(Contact contact) {
        Body bodyToDestroy = null;
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if (fixtureA.m_body == gun1.getBullet()) {
            bodyToDestroy = fixtureB.m_body;
        } else if (fixtureB.m_body == gun1.getBullet()) {
            bodyToDestroy = fixtureA.m_body;
        }
        if (fixtureA.getBody() == action_body) {
            if (objectForJump.contains(fixtureB)) {
                contactObjForJump.add(fixtureB);
            }
        }
        if (fixtureB.getBody() == action_body) {
            if (objectForJump.contains(fixtureA)) {
                contactObjForJump.add(fixtureA);
            }
        }

        if (bodyToDestroy != null && destroyableList.contains(bodyToDestroy)) {
            if (gun1.getBullet().m_linearVelocity.x > 70 || gun1.getBullet().m_linearVelocity.y > 70) {
                objectToExplode.add(bodyToDestroy);
                Vec2 bulletVel = gun1.getBullet().getLinearVelocity();
                bulletVel.x = bulletVel.x - 30;
                gun1.getBullet().setLinearVelocity(bulletVel);
                return;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if (fixtureA.getBody() == action_body) {
            if (objectForJump.contains(fixtureB)) {
                contactObjForJump.remove(fixtureB);
            }
        }
        if (fixtureB.getBody() == action_body) {
            if (objectForJump.contains(fixtureA)) {
                contactObjForJump.remove(fixtureA);
            }
        }
    }

    @Override
    public void step(SettingsIF settings) {
        super.step(settings);
        keyPressed();
        explose();
        gun1.fire();
        gun1.setLastStep(last_step);
        //  checkToErase();
        last_step++;

    }

    private void checkToErase() {
        if (last_step - lastDestroy_step > 1000) {
            for (Body body : currentToErase) {
                m_world.destroyBody(body);
                nextToErase.clear();
            }
            currentToErase = nextToErase;
        }
    }

    private void explose() {
        for (Body body : objectToExplode) {
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
                if (body == action_body) {
                    bd.shapeColor = Color3f.BLUE;
                }
                bd.type = BodyType.DYNAMIC;
                bd.position.set(oldPosition.x, oldPosition.y);
                Body newBody = getWorld().createBody(bd);
                if (newBody != null) {
                    newBody.createFixture(fd);
                }
                nextToErase.add(body);
            }
            lastDestroy_step = last_step;
        }
        objectToExplode.clear();
    }

    @Override
    public String getTestName() {
        return "Level 1";
    }
}
