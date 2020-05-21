package org.jbox2d.testbed.levels;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.testbed.Hero;
import org.jbox2d.testbed.framework.*;
import org.jbox2d.testbed.framework.game.objects.Gun;
import org.jbox2d.testbed.framework.game.objects.MovingObject;
import org.jbox2d.testbed.framework.game.objects.SwitchType;
import org.jbox2d.testbed.framework.utils.GarbageObjectCollector;
import org.jbox2d.testbed.framework.utils.Line;
import org.jbox2d.testbed.framework.utils.LineIntersectChecker;
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
    protected List<Line> linesList = new ArrayList<>();
    private int maxLevelIndex = 3;
    protected final static float maxSpeedY = 3f;
    protected static final float commonPersonEdge = 1f;
    long lastDestroy_step = 0;
    long last_step = 0;
    long enemyFireCD = 50;
    long lastEnemyFire = 0;
    protected List<Fixture> objectForJump = new ArrayList<>();
    protected List<Fixture> contactObjForJump = new ArrayList<>();
    protected Hero hero;
    protected Body hero_bullet;
    protected Body enemy_bullet;
    protected Body exit;
    protected Body objectToPush;
    protected AbstractTestbedController controller;
    protected List<Body> movingObject = new ArrayList<>();
    protected boolean canPush = false;
    protected List<Gun> gunList = new ArrayList<>();
    protected List<Body> destroyableList = Collections.synchronizedList(new ArrayList<>());
    protected List<Body> objectToExplode = Collections.synchronizedList(new ArrayList<>());
    protected Scene scene;
    protected List<Fixture> leftBlockedFixtures = new ArrayList<>();
    protected List<Fixture> rightBlockedFixtures = new ArrayList<>();
    protected List<MovingObject> movingObjectList = new ArrayList<>();
    protected Body enemyBody;
    protected float constantEnemyVelocity = 6;
    GarbageObjectCollector garbageObjectCollector = new GarbageObjectCollector();

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
        garbageObjectCollector = new GarbageObjectCollector();
        last_step = 0;
        lastEnemyFire = 0;
        lastDestroy_step = 0;
        objectToPush = null;
        blockedFromRight = false;
        blockedFromLeft = false;
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
        f.m_friction = 0;
        shape.set(new Vec2(-getWidth() / 2, getHeight() / 2), new Vec2(-getWidth() / 2, -getHeight() / 2));
        f = ground.createFixture(shape, 0.0f);
        leftBlockedFixtures.add(f);
        f.m_friction = 0;
    }

    public void keyPressed() {
        if (hero == null) {
            return;
        }
        boolean hasContact = hero.getBody().m_contactList != null;
        if(hasContact){
            hero.stepInAir=0;
        } else {
            hero.stepInAir++;
        }
        if (getModel().getKeys()['a'] || getModel().getKeys()[1092]) {
            if (hero.getBody().getLinearVelocity().x > minSpeedX && !blockedFromLeft) {
                Vec2 newVel = new Vec2(hero.getBody().getLinearVelocity().x - 1, hero.getBody().getLinearVelocity().y);
                hero.getBody().setLinearVelocity(newVel);
            }
            if (hero.getBody().getLinearVelocity().x > minSpeedXAir && !hasContact) {
                Vec2 newVel = new Vec2(hero.getBody().getLinearVelocity().x - 1, hero.getBody().getLinearVelocity().y);
                hero.getBody().setLinearVelocity(newVel);
            }
            if (objectToPush != null && canPush) {
                Vec2 newVel = new Vec2(objectToPush.m_linearVelocity.x + 0.5f, 0);
                objectToPush.setLinearVelocity(newVel);
            }
        }
        if (getModel().getKeys()['d'] || getModel().getKeys()[1074]) {
            if (hero.getBody().getLinearVelocity().x < maxSpeedX && !blockedFromRight) {
                Vec2 newVel = new Vec2(hero.getBody().getLinearVelocity().x + 1, hero.getBody().getLinearVelocity().y);
                hero.getBody().setLinearVelocity(newVel);
            }
            if (hero.getBody().getLinearVelocity().x < maxSpeedXAir && !hasContact) {
                Vec2 newVel = new Vec2(hero.getBody().getLinearVelocity().x + 1, hero.getBody().getLinearVelocity().y);
                hero.getBody().setLinearVelocity(newVel);
            }
            if (objectToPush != null && canPush) {
                Vec2 newVel = new Vec2(objectToPush.m_linearVelocity.x - 0.5f, 0);
                objectToPush.setLinearVelocity(newVel);
            }
        }
        if (getModel().getKeys()[' ']) {
            if (hero.getBody().getLinearVelocity().y < maxSpeedY && contactObjForJump.size() > 0) {
                Vec2 newVel = new Vec2(hero.getBody().getLinearVelocity().x, hero.getBody().getLinearVelocity().y + 7);
                hero.getBody().setLinearVelocity(newVel);
            } else if(!hasContact && hero.stepInAir<8 && hero.stepInAir>3){
                Vec2 newVel = new Vec2(hero.getBody().getLinearVelocity().x, hero.getBody().getLinearVelocity().y +1.2f);
                hero.getBody().setLinearVelocity(newVel);
            }
        }
    }

    protected void endLevel() {
        getModel().getSettings().setPause(true);
        getModel().resetKeys();
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

    protected void enemyAction() {
        if (enemyBody != null) {
            Vec2 currentVel = enemyBody.getLinearVelocity();
            currentVel.x = constantEnemyVelocity;
            enemyBody.setLinearVelocity(currentVel);
        }
    }

    protected void enemyFireAction() {
        if (enemyBody != null && hero.getBody() != null) {
            Line fireLine = new Line(hero.getBody().getPosition(), enemyBody.getPosition());
            boolean isVisible = true;
            for (Line line : linesList) {
                if (LineIntersectChecker.doIntersect(fireLine, line)) {
                    isVisible = false;
                    break;
                }
            }

            if (!enemyBody.isDestroy() && !hero.getBody().isDestroy() && isVisible && lastEnemyFire < last_step - enemyFireCD) {
                Vec2 orientation = new Vec2(hero.getBody().getPosition().x - enemyBody.getPosition().x,
                        hero.getBody().getPosition().y - enemyBody.getPosition().y);

                float norm = Math.abs(enemyBody.getPosition().x - hero.getBody().getPosition().x);
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
                    bd.position.set(enemyBody.getPosition().x + 1 * orientation.x, enemyBody.getPosition().y + 1 * orientation.y);
                    enemy_bullet  = getWorld().createBody(bd);
                    enemy_bullet.createFixture(fd);
                    enemy_bullet.setLinearVelocity(new Vec2(orientation.x * 300, orientation.y * 300));
                    lastEnemyFire = last_step;
                }
            }
        }
    }

    protected void leftMouseAction() {
        if (hasGun() && cursorInFireArea() && !hero.getBody().isDestroy()) {
            Vec2 orientation = new Vec2(getWorldMouse().x - hero.getBody().getPosition().x,
                    getWorldMouse().y - hero.getBody().getPosition().y);

            float norm = Math.abs(getWorldMouse().x - hero.getBody().getPosition().x);
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
                bd.position.set(hero.getBody().getPosition().x + 1 * orientation.x, hero.getBody().getPosition().y + 1 * orientation.y);

                hero_bullet = getWorld().createBody(bd);
                Fixture f = hero_bullet.createFixture(fd);
                hero_bullet.setLinearVelocity(new Vec2(orientation.x * 300, orientation.y * 300));
                garbageObjectCollector.add(hero_bullet, last_step + 400);
            }
        }
    }

    protected abstract int getLevelIndex();

    public void beginContact(Contact contact) {
        Body bodyToDestroy = null;
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if (isHero(fixtureA.getBody()) && fixtureB.getBody() == exit ||
                isHero(fixtureB.getBody()) && fixtureA.getBody() == exit) {
            endLevel();
        }

        if (isHero(fixtureA.getBody())) {
            if (objectForJump.contains(fixtureB)) {
                contactObjForJump.add(fixtureB);
            }
        }
        if (isHero(fixtureB.getBody())) {
            if (objectForJump.contains(fixtureA)) {
                contactObjForJump.add(fixtureA);
            }
        }

        if (isHero(fixtureA.getBody())) {
            if (movingObject.contains(fixtureB.getBody())) {
                objectToPush = fixtureB.getBody();
            }
        }
        if (isHero(fixtureB.getBody())) {
            if (movingObject.contains(fixtureA.getBody())) {
                objectToPush = fixtureA.getBody();
            }
        }

        if (isHero(fixtureA.getBody())) {
            if (leftBlockedFixtures.contains(fixtureB)) {
                blockedFromLeft = true;
            }
        }
        if (isHero(fixtureB.getBody())) {
            if (leftBlockedFixtures.contains(fixtureA)) {
                blockedFromLeft = true;
            }
        }
        if (fixtureA.getBody() == enemyBody && leftBlockedFixtures.contains(fixtureB) ||
                fixtureA.getBody() == enemyBody && rightBlockedFixtures.contains(fixtureB) ||
                fixtureB.getBody() == enemyBody && leftBlockedFixtures.contains(fixtureA) ||
                fixtureB.getBody() == enemyBody && rightBlockedFixtures.contains(fixtureA)

        ) {
            constantEnemyVelocity = -constantEnemyVelocity;
        }
        if (isHero(fixtureA.getBody()) && (leftBlockedFixtures.contains(fixtureB) || rightBlockedFixtures.contains(fixtureB))) {
            canPush = true;
        }

        if (isHero(fixtureB.getBody()) && (leftBlockedFixtures.contains(fixtureA) || rightBlockedFixtures.contains(fixtureA))) {
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
                if (bulletImpulse > 100) {
                    objectToExplode.add(bodyToDestroy);

                    Vec2 bulletVel = gun.getBullet().getLinearVelocity();
                    bulletVel.x = bulletVel.x - 30;
                    gun.getBullet().setLinearVelocity(bulletVel);
                    return;
                }
            }
        }

        for (MovingObject movingObject : movingObjectList) {
            if (movingObject.getSwitcher() == fixtureA.getBody() || movingObject.getSwitcher() == fixtureB.getBody()) {
                movingObject.setActive(true);
            }
        }
        if (fixtureA.getBody() == hero.getBody() && fixtureB.getBody() == enemy_bullet ||
                fixtureB.getBody() == hero.getBody() && fixtureA.getBody() == enemy_bullet) {
            float bulletImpulse = enemy_bullet.m_mass * enemy_bullet.getLinearVelocity().length();
            if (bulletImpulse > 50) {
                objectToExplode.add(hero.getBody());
            }
        }

        if (fixtureA.m_body == hero_bullet) {
            bodyToDestroy = fixtureB.m_body;
        } else if (fixtureB.m_body == hero_bullet) {
            bodyToDestroy = fixtureA.m_body;
        }
        if (bodyToDestroy == hero.getBody()) {
            return;
        }
        if (bodyToDestroy != null && hero_bullet != null && destroyableList.contains(bodyToDestroy)) {
            float bulletImpulse = hero_bullet.m_mass * hero_bullet.getLinearVelocity().length();
            if (bulletImpulse > 50) {
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
        if (isHero(fixtureA.getBody())) {
            if (objectForJump.contains(fixtureB)) {
                contactObjForJump.remove(fixtureB);
            }
        }
        if (isHero(fixtureB.getBody())) {
            if (objectForJump.contains(fixtureA)) {
                contactObjForJump.remove(fixtureA);
            }
        }

        if (isHero(fixtureA.getBody())) {
            if (leftBlockedFixtures.contains(fixtureB)) {
                blockedFromLeft = false;
            }
        }
        if (isHero(fixtureB.getBody())) {
            if (leftBlockedFixtures.contains(fixtureA)) {
                blockedFromLeft = false;
            }
        }

        if (isHero(fixtureA.getBody()) && objectToPush == fixtureB.getBody()) {
            objectToPush = null;
        }

        if (isHero(fixtureB.getBody()) && objectToPush == fixtureA.getBody()) {
            objectToPush = null;
        }

        if (isHero(fixtureA.getBody()) && (leftBlockedFixtures.contains(fixtureB) || rightBlockedFixtures.contains(fixtureB))) {
            canPush = false;
        }

        if (isHero(fixtureB.getBody()) && (leftBlockedFixtures.contains(fixtureA) || rightBlockedFixtures.contains(fixtureA))) {
            canPush = false;
        }

        for (MovingObject movingObject : movingObjectList) {
            if ((isHero(fixtureA.getBody()) || isHero(fixtureB.getBody())) &&
                    movingObject.getSwitcher() == fixtureA.getBody() || movingObject.getSwitcher() == fixtureB.getBody()) {
                if (movingObject.getSwitchType() == SwitchType.HOLDING) {
                    movingObject.setActive(false);
                }
            }
        }

    }

    protected abstract boolean hasGun();

    protected abstract void checkEnemyAction();

    protected boolean cursorInFireArea() {
        Line fireLine = new Line(hero.getBody().getPosition(), getWorldMouse());
        for (Line line : linesList) {
            if (LineIntersectChecker.doIntersect(fireLine, line)) {
                return false;
            }
        }
        return getWorldMouse().x < getWidth() / 2
                && getWorldMouse().x > -getWidth() / 2
                && getWorldMouse().y > -getHeight() / 2
                && getWorldMouse().y < getHeight() / 2;
    }

    @Override
    public void step(SettingsIF settings) {
        if (hasGun() && cursorInFireArea()) {
            scene.setCursor(Cursor.CROSSHAIR);
        } else {
            scene.setCursor(Cursor.DEFAULT);
        }
        super.step(settings);
        keyPressed();
        explose();
        enemyAction();
        enemyFireAction();
        for (Gun gun : gunList) {
            gun.checkFire(last_step);
        }
        for (MovingObject movingObject : movingObjectList) {
            movingObject.calculateStep();
        }
        if (last_step % 20 == 0) {
          garbageObjectCollector.clear(last_step, getWorld());
        }
        last_step++;
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
                bd.shapeColor = body.shapeColor;
                bd.type = BodyType.DYNAMIC;
                bd.position.set(oldPosition.x, oldPosition.y);
                Body newBody = getWorld().createBody(bd);
                if (newBody != null) {
                    newBody.createFixture(fd);
                }
            }
            lastDestroy_step = last_step;
        }
        objectToExplode.clear();
    }

    protected boolean isHero(Body body) {
        return body == hero.getBody();
    }

    protected abstract float getWidth();

    protected abstract float getHeight();
}
