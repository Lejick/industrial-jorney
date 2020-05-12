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
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 * <p>
 * Created at 4:56:29 AM Jan 14, 2011
 */
/**
 * Created at 4:56:29 AM Jan 14, 2011
 */
package org.jbox2d.testbed.tests.level;

import javafx.scene.Scene;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.testbed.framework.AbstractTestbedController;
import org.jbox2d.testbed.framework.Gun;
import org.jbox2d.testbed.framework.MovingObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Murphy
 */
public class Level3 extends CommonLevel {
    private static float width = 80;
    private static float height = 60;
    Body platform1;

    public Level3(AbstractTestbedController controller, Scene scene) {
        super(controller, scene);
    }

    @Override
    public void initTest(boolean deserialized) {
        super.initTest(false);
        createGameObjects();
        createGuns();
        createMovingPlatforms();
        exit = createRectangle(getWidth() / 2 - 1, -getHeight() / 2 + 4 * commonPersonEdge, 1, 4, false, BodyType.STATIC);
        exit.shapeColor = Color3f.GREEN;
    }

    protected void createGameObjects() {
        Body hero = createRectangle(-36, 26, commonPersonEdge, commonPersonEdge, true, BodyType.DYNAMIC);
        destroyableList.add(hero);


        float deltaY = 0;
        for (int j = 0; j < 3; j++) {
            float deltaX = 0;
            for (int i = 0; i < 2; i++) {
                Body simpleBox = createRectangle(31 + deltaX, 21 + deltaY, commonPersonEdge, commonPersonEdge / 1.5f, false, BodyType.DYNAMIC);
                movingObject.add(simpleBox);
                deltaX = deltaX + commonPersonEdge + 6f;
            }
            deltaY = deltaY + commonPersonEdge + 0.7f;
        }
        Body platformBox = createRectangle(34.5f, 25, 5f, 0.1f, false, BodyType.DYNAMIC);

        Body simpleBox = createRectangle(34.5f, 27, commonPersonEdge, commonPersonEdge, false, BodyType.DYNAMIC);
        simpleBox.getFixtureList().m_friction = 5f;
        simpleBox.getFixtureList().m_density = 1f;
        movingObject.add(simpleBox);
    }

    protected void createPlatforms() {
        BodyDef bd = new BodyDef();
        Body ground = getWorld().createBody(bd);
        EdgeShape shape = new EdgeShape();

        shape.set(new Vec2(-40, 20), new Vec2(18, 20));
        Fixture f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);
        shape.set(new Vec2(-40, 19.9f), new Vec2(18, 19.9f));
        ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(30, 20), new Vec2(40, 20));
        f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);
        shape.set(new Vec2(30, 19.9f), new Vec2(40, 19.9f));
        f = ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(-30, 13), new Vec2(40, 13));
        f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);
        shape.set(new Vec2(-30, 12.9f), new Vec2(40, 12.9f));
        f = ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(-40, 5), new Vec2(0, 5));
        f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);
        shape.set(new Vec2(-40, 4.9f), new Vec2(0, 4.9f));
        f = ground.createFixture(shape, 0.0f);


        shape.set(new Vec2(11, 5), new Vec2(40, 5));
        f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);
        shape.set(new Vec2(11, 4.9f), new Vec2(40, 4.9f));
        f = ground.createFixture(shape, 0.0f);

    }


    private void createGuns() {
        Gun gun2 = new Gun(m_world, 32, 14, 100, 1000, 1f);
        gun2.setDetection(true);
        gun2.setDetectY1(13f);
        gun2.setDetectY2(15f);
        for (Body body : destroyableList) {
            gun2.addObjectToAttack(body);
        }
        gun2.setOrientation(new Vec2(-1, 0));
        objectForJump.add(gun2.getGunBodyFixture());
        gunList.add(gun2);
    }

    private void createMovingPlatforms() {
        platform1 = createMovingPlatform(5.5f, 5, 5, 0.2f, false, BodyType.KINEMATIC);
        List<Vec2> coordinatesList = new ArrayList<>();
        coordinatesList.add(new Vec2(5.5f, 5));
        coordinatesList.add(new Vec2(5.5f, -10));
        MovingObject movingObject = new MovingObject(platform1, coordinatesList);
        movingObjectList.add(movingObject);
    }

    @Override
    protected int getLevelIndex() {
        return 2;
    }

    @Override
    protected boolean hasGun() {
        return true;
    }

    @Override
    public String getTestName() {
        return "Level 3";
    }

    @Override
    protected float getWidth() {
        return width;
    }

    @Override
    protected float getHeight() {
        return height;
    }
}
