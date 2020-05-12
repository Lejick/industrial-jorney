package org.jbox2d.testbed.tests.level;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.testbed.framework.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Daniel Murphy
 */
public abstract class CommonLevel extends PlayLevel {
    protected static final Logger log = LoggerFactory.getLogger(Level1.class);
    protected final static float maxSpeedX = 6f;
    protected final static float minSpeedX = -6f;
    protected final static float maxSpeedXAir = 3f;
    protected final static float minSpeedXAir = -3f;
    private int maxLevelIndex = 2;
    protected final static float maxSpeedY = 3f;
    protected static final float commonPersonEdge = 1f;
    long lastDestroy_step = 0;
    long last_step = 0;
    protected List<Fixture> objectForJump = new ArrayList<>();
    protected List<Fixture> contactObjForJump = new ArrayList<>();
    protected Body hero_body;
    protected Body hero_bullet;
    protected Body exit;
    protected Body objectToPush;
    protected AbstractTestbedController controller;
    protected List<Body> movingObject = new ArrayList<>();
    protected boolean canPush = false;
    protected List<Gun> gunList = new ArrayList<>();
    protected List<Body> destroyableList = Collections.synchronizedList(new ArrayList<>());
    protected List<Body> objectToExplode = Collections.synchronizedList(new ArrayList<>());
    protected List<Body> currentToErase = Collections.synchronizedList(new ArrayList<>());
    protected List<Body> nextToErase = Collections.synchronizedList(new ArrayList<>());
    protected Scene scene;
    protected List<Fixture> leftBlockedFixtures = new ArrayList<>();
    protected List<Fixture> rightBlockedFixtures = new ArrayList<>();

    protected boolean blockedFromLeft;
    protected boolean blockedFromRight;

    @Override
    public void processBody(Body argBody, Long argTag) {
        super.processBody(argBody, argTag);
    }

    @Override
    public boolean isSaveLoadEnabled() {
        return true;
    }

    public CommonLevel(AbstractTestbedController controller, Scene scene) {
        this.controller = controller;
        this.scene = scene;
    }

    @Override
    public void initTest(boolean deserialized) {
        contactObjForJump.clear();
        last_step = 0;
        lastDestroy_step = 0;
        objectToPush = null;
        blockedFromRight=false;
        blockedFromLeft=false;
        canPush = false;
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
        rightBlockedFixtures.add(f);
        f.m_friction=0;
        shape.set(new Vec2(-getWidth() / 2, getHeight() / 2), new Vec2(-getWidth() / 2, -getHeight() / 2));
        f = ground.createFixture(shape, 0.0f);
        leftBlockedFixtures.add(f);
        f.m_friction=0;
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

    protected Body createMovingPlatform(float x, float y, float hx, float hy, boolean isHero, BodyType bodyType) {
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
            if (hero_body.getLinearVelocity().x > minSpeedX && !blockedFromLeft) {
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
            if (hero_body.getLinearVelocity().x < maxSpeedX && !blockedFromRight) {
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
        alert.setOnHidden(evt -> {
            Platform.runLater(() -> {
                getModel().getSettings().setPause(false);
                int nextLevelIndex = getLevelIndex() + 1;
                if (getLevelIndex() == maxLevelIndex) {
                    nextLevelIndex = 0;
                }
                controller.playTest(nextLevelIndex);
                controller.reset();
            });
        });
        alert.show();
    }

    @Override
    protected Logger getLogger() {
        return log;
    }

    protected void leftMouseAction() {
        if (hasGun() && cursorInPlayArea() && !hero_body.isDestroy()) {
          Vec2 orientation=new Vec2( getWorldMouse().x-hero_body.getPosition().x,
                  getWorldMouse().y - hero_body.getPosition().y);
            float norm = Math.abs(getWorldMouse().x - hero_body.getPosition().x);
            orientation.x = orientation.x / norm;
            orientation.y = orientation.y / norm;
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
                bd.position.set(hero_body.getPosition().x + 1 * orientation.x, hero_body.getPosition().y + 1 * orientation.y);

                Body  bullet = getWorld().createBody(bd);
                Fixture f = bullet.createFixture(fd);
                bullet.shapeColor = Color3f.RED;
                bullet.setLinearVelocity(new Vec2(orientation.x * 500, orientation.y * 500));
                hero_bullet=bullet;
            }

        }
    }

    protected abstract int getLevelIndex();

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

        if (fixtureA.getBody().isHero()) {
            if (leftBlockedFixtures.contains(fixtureB)) {
                blockedFromLeft = true;
            }
        }
        if (fixtureB.getBody().isHero()) {
            if (leftBlockedFixtures.contains(fixtureA)) {
                blockedFromLeft = true;
            }
        }

        if (fixtureA.getBody().isHero() && (leftBlockedFixtures.contains(fixtureB) || rightBlockedFixtures.contains(fixtureB))) {
            canPush = true;
        }

        if (fixtureB.getBody().isHero() && (leftBlockedFixtures.contains(fixtureA) || rightBlockedFixtures.contains(fixtureA))) {
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
                if (bulletImpulse > 200) {
                    objectToExplode.add(bodyToDestroy);

                    Vec2 bulletVel = gun.getBullet().getLinearVelocity();
                    bulletVel.x = bulletVel.x - 30;
                    gun.getBullet().setLinearVelocity(bulletVel);
                    return;
                }
            }
        }
        if (fixtureA.m_body == hero_bullet) {
            bodyToDestroy = fixtureB.m_body;
        } else if (fixtureB.m_body == hero_bullet) {
            bodyToDestroy = fixtureA.m_body;
        }
        if (bodyToDestroy == hero_body) {
            return;
        }
        if (bodyToDestroy != null  && hero_bullet != null && destroyableList.contains(bodyToDestroy)) {
            float bulletImpulse = hero_bullet.m_mass * hero_bullet.getLinearVelocity().length();
            if (bulletImpulse > 200) {
                objectToExplode.add(bodyToDestroy);
                Vec2 bulletVel = hero_bullet.getLinearVelocity();
                bulletVel.x = bulletVel.x - 30;
                hero_bullet.setLinearVelocity(bulletVel);
                return;
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

        if (fixtureA.getBody().isHero()) {
            if (leftBlockedFixtures.contains(fixtureB)) {
                blockedFromLeft = false;
            }
        }
        if (fixtureB.getBody().isHero()) {
            if (leftBlockedFixtures.contains(fixtureA)) {
                blockedFromLeft = false;
            }
        }

        if (fixtureA.getBody().isHero() && objectToPush == fixtureB.getBody()) {
            objectToPush = null;
        }

        if (fixtureB.getBody().isHero() && objectToPush == fixtureA.getBody()) {
            objectToPush = null;
        }

        if (fixtureA.getBody().isHero() && (leftBlockedFixtures.contains(fixtureB) || rightBlockedFixtures.contains(fixtureB))) {
            canPush = false;
        }

        if (fixtureB.getBody().isHero() && (leftBlockedFixtures.contains(fixtureA) || rightBlockedFixtures.contains(fixtureA))) {
            canPush = false;
        }

    }

    protected abstract boolean hasGun();

    protected boolean cursorInPlayArea() {
        return getWorldMouse().x < getWidth() / 2
                && getWorldMouse().x > -getWidth() / 2
                && getWorldMouse().y > -getHeight() / 2
                && getWorldMouse().y < getHeight() / 2;
    }

    @Override
    public void step(SettingsIF settings) {
        if (hasGun() && cursorInPlayArea()) {
            scene.setCursor(Cursor.CROSSHAIR);
        } else {
            scene.setCursor(Cursor.DEFAULT);
        }
        super.step(settings);
        keyPressed();
        explose();
        for (Gun gun : gunList) {
            gun.checkFire(last_step);
        }
        //  checkToErase();
        movePlatforms();
        last_step++;
    }

    protected abstract void movePlatforms();

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
            body.setDestroy(true);
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
