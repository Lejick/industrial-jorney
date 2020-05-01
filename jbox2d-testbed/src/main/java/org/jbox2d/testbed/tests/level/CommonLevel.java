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
package org.jbox2d.testbed.tests.level;

import javafx.application.Platform;
import javafx.scene.control.Alert;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Daniel Murphy
 */
public abstract class CommonLevel extends TestbedTest {
    protected static final Logger log = LoggerFactory.getLogger(Level1.class);
    protected final static float maxSpeedX = 6f;
    protected final static float minSpeedX = -6f;
    protected final static float maxSpeedXAir = 3f;
    protected final static float minSpeedXAir = -3f;
    protected final static float maxSpeedY = 5f;

    protected static final float commonPersonEdge = 1f;
    long lastDestroy_step = 0;
    long last_step = 0;
    protected List<Fixture> objectForJump = new ArrayList<>();
    protected List<Fixture> contactObjForJump = new ArrayList<>();
    protected Body hero_body;
    protected Body exit;
    protected Body objectToPush;
    protected List<Body> movingObject = new ArrayList<>();
    protected boolean canPush = false;
    protected List<Fixture> contactObjForPush = new ArrayList<>();
    protected List<Gun> gunList = new ArrayList<>();
    protected List<Body> destroyableList = Collections.synchronizedList(new ArrayList<>());
    protected List<Body> objectToExplode = Collections.synchronizedList(new ArrayList<>());
    protected List<Body> currentToErase = Collections.synchronizedList(new ArrayList<>());
    protected List<Body> nextToErase = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void processBody(Body argBody, Long argTag) {
        super.processBody(argBody, argTag);
    }

    @Override
    public boolean isSaveLoadEnabled() {
        return true;
    }

    @Override
    public void initTest(boolean deserialized) {
        contactObjForJump.clear();
        last_step = 0;
        lastDestroy_step = 0;
        createGameBox();
        createPlatforms();
    }


    protected abstract void createPlatforms();

    private void createGuns() {
        Gun gun1 = new Gun(m_world, -getWidth() / 2, commonPersonEdge * 12 - 2, 200, 100, 0.5f);
        gun1.setOrientation(new Vec2(1, 0));
        objectForJump.add(gun1.getGunBodyFixture());
        gunList.add(gun1);

        Gun gun2 = new Gun(m_world, getWidth() / 2 - 2, commonPersonEdge - 2, 400, 400, 0.5f);
        gun2.setDetection(true);
        gun2.setDetectX1(6);
        gun2.setDetectX2(16);
        gun2.setDetectY1(-0.6f);
        gun2.setDetectY2(-0.4f);
        for (Body body : destroyableList) {
            gun2.addObjectToAttack(body);
        }
        gun2.setOrientation(new Vec2(-1, 0));
        objectForJump.add(gun2.getGunBodyFixture());
        gunList.add(gun2);
    }

    protected void createGameBox() {
        BodyDef bd = new BodyDef();
        Body ground = getWorld().createBody(bd);

        EdgeShape shape = new EdgeShape();
        shape.set(new Vec2(-getWidth() / 2, -getHeight() / 2), new Vec2(getWidth() / 2, -getHeight() / 2));
        Fixture f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);

        shape.set(new Vec2(-getWidth() / 2, getHeight() / 2), new Vec2(getWidth() / 2, getHeight() / 2));
        ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(getWidth() / 2, getHeight() / 2), new Vec2(getWidth() / 2, -getHeight() / 2));
        f = ground.createFixture(shape, 0.0f);
        contactObjForPush.add(f);
        shape.set(new Vec2(-getWidth() / 2, getHeight() / 2), new Vec2(-getWidth() / 2, -getHeight() / 2));
        f = ground.createFixture(shape, 0.0f);
        contactObjForPush.add(f);
    }

    protected Body createRectangle(float x, float y, float hx, float hy, boolean isHero, BodyType bodyType) {
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
        bd.type = bodyType;
        bd.position.set(x, y);

        Body body = getWorld().createBody(bd);
        Fixture f = body.createFixture(fd);
        objectForJump.add(f);
        body.setHero(isHero);
        if (isHero) {
            hero_body = body;
        }
        return body;
    }

    public void keyPressed() {
        if (hero_body == null) {
            return;
        }
        boolean hasContact = hero_body.m_contactList != null;
        if (getModel().getKeys()['a'] || getModel().getKeys()[1092]) {
            if (hero_body.getLinearVelocity().x > minSpeedX && contactObjForJump.size() > 0) {
                Vec2 newVel = new Vec2(hero_body.getLinearVelocity().x - 1, hero_body.getLinearVelocity().y);
                hero_body.setLinearVelocity(newVel);
            }
            if (hero_body.getLinearVelocity().x > minSpeedXAir && !hasContact) {
                Vec2 newVel = new Vec2(hero_body.getLinearVelocity().x - 1, hero_body.getLinearVelocity().y);
                hero_body.setLinearVelocity(newVel);
            }
            if (objectToPush != null && canPush) {
                Vec2 newVel = new Vec2(objectToPush.m_linearVelocity.x + 0.5f, 0);
                objectToPush.setLinearVelocity(newVel);
            }
        }
        if (getModel().getKeys()['d'] || getModel().getKeys()[1074]) {
            if (hero_body.getLinearVelocity().x < maxSpeedX && contactObjForJump.size() > 0) {
                Vec2 newVel = new Vec2(hero_body.getLinearVelocity().x + 1, hero_body.getLinearVelocity().y);
                hero_body.setLinearVelocity(newVel);
            }
            if (hero_body.getLinearVelocity().x < maxSpeedXAir && !hasContact) {
                Vec2 newVel = new Vec2(hero_body.getLinearVelocity().x + 1, hero_body.getLinearVelocity().y);
                hero_body.setLinearVelocity(newVel);
            }
            if (objectToPush != null && canPush) {
                Vec2 newVel = new Vec2(objectToPush.m_linearVelocity.x - 0.5f, 0);
                objectToPush.setLinearVelocity(newVel);
            }
        }
        if (getModel().getKeys()[' ']) {
            if (hero_body.getLinearVelocity().y < maxSpeedY && contactObjForJump.size() > 0) {
                Vec2 newVel = new Vec2(hero_body.getLinearVelocity().x, hero_body.getLinearVelocity().y + 7);
                hero_body.setLinearVelocity(newVel);
            }
        }
    }

    protected void endLevel() {
        getModel().getSettings().setPause(true);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText("Good game. You won! Click OK to exit.");
        alert.setOnHidden(evt -> Platform.exit());
        alert.show();
    }

    public void beginContact(Contact contact) {
        Body bodyToDestroy = null;
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if (fixtureA.getBody().isHero() && fixtureB.getBody() == exit ||
                fixtureB.getBody().isHero() && fixtureA.getBody() == exit) {
            endLevel();
        }

        if (fixtureA.getBody().isHero()) {
            if (objectForJump.contains(fixtureB)) {
                contactObjForJump.add(fixtureB);
            }
        }
        if (fixtureB.getBody().isHero()) {
            if (objectForJump.contains(fixtureA)) {
                contactObjForJump.add(fixtureA);
            }
        }

        if (fixtureA.getBody().isHero()) {
            if (movingObject.contains(fixtureB.getBody())) {
                objectToPush = fixtureB.getBody();
            }
        }
        if (fixtureB.getBody().isHero()) {
            if (movingObject.contains(fixtureA.getBody())) {
                objectToPush = fixtureA.getBody();
            }
        }

        if (fixtureA.getBody().isHero() && contactObjForPush.contains(fixtureB)) {
            log.info("BEGIN Contact for push!");
            canPush = true;
        }

        if (fixtureB.getBody().isHero() && contactObjForPush.contains(fixtureA)) {
            log.info("BEGIN Contact for push!");
            canPush = true;
        }

        for (Gun gun : gunList) {
            if (fixtureA.m_body == gun.getBullet()) {
                bodyToDestroy = fixtureB.m_body;
            } else if (fixtureB.m_body == gun.getBullet()) {
                bodyToDestroy = fixtureA.m_body;
            }

            if (bodyToDestroy != null && gun.getBullet() != null && destroyableList.contains(bodyToDestroy)) {
                float bulletImpulse = gun.getBullet().m_mass * gun.getBullet().getLinearVelocity().length();
                if (bulletImpulse > 400) {
                    objectToExplode.add(bodyToDestroy);
                    Vec2 bulletVel = gun.getBullet().getLinearVelocity();
                    bulletVel.x = bulletVel.x - 30;
                    gun.getBullet().setLinearVelocity(bulletVel);
                    return;
                }
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if (fixtureA.getBody().isHero()) {
            if (objectForJump.contains(fixtureB)) {
                contactObjForJump.remove(fixtureB);
            }
        }
        if (fixtureB.getBody().isHero()) {
            if (objectForJump.contains(fixtureA)) {
                contactObjForJump.remove(fixtureA);
            }
        }

        if (fixtureA.getBody().isHero() && objectToPush == fixtureB.getBody()) {
            objectToPush = null;
        }

        if (fixtureB.getBody().isHero() && objectToPush == fixtureA.getBody()) {
            objectToPush = null;
        }

        if (fixtureA.getBody().isHero() && contactObjForPush.contains(fixtureB.getBody())) {
            canPush = false;
        }

        if (fixtureB.getBody().isHero() && contactObjForPush.contains(fixtureA.getBody())) {
            canPush = false;
        }

    }

    @Override
    public void step(SettingsIF settings) {
        super.step(settings);
        keyPressed();
        explose();
        for (Gun gun : gunList) {
            gun.checkFire(last_step);
        }
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
                if (body.isHero()) {
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

    protected abstract float getWidth();

    protected abstract float getHeight();
}
