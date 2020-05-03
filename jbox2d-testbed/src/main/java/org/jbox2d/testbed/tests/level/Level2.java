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
 */
/**
 * Created at 4:56:29 AM Jan 14, 2011
 */
package org.jbox2d.testbed.tests.level;

import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.testbed.framework.Gun;

/**
 * @author Daniel Murphy
 */
public class Level2 extends CommonLevel {
    private static float width = 60;
    private static float height = 40;

    @Override
    public void initTest(boolean deserialized) {
        super.initTest(false);
        createGameObjects();
        createGuns();
        exit = createRectangle(getWidth() / 2 - 1, getHeight() / 2 - commonPersonEdge * 4, 1, 4, false, BodyType.STATIC);
        exit.shapeColor = Color3f.GREEN;
    }

    protected void createGameObjects() {
        Body hero = createRectangle(-getWidth() / 2 + 2, getHeight() / 2 - 2 * commonPersonEdge, commonPersonEdge, commonPersonEdge, true, BodyType.DYNAMIC);
        destroyableList.add(hero);
        Body jumplatform = createRectangle(0, commonPersonEdge, commonPersonEdge * 5, commonPersonEdge / 1.5f, false, BodyType.DYNAMIC);
        movingObject.add(jumplatform);

        //  Body simpleBox2 = createRectangle(0, getHeight() / 2 - commonPersonEdge * 29, commonPersonEdge, commonPersonEdge, false, BodyType.DYNAMIC);
        // movingObject.add(simpleBox2);
        // destroyableList.add(simpleBox2);

    }

    protected void createPlatforms() {
        BodyDef bd = new BodyDef();
        Body ground = getWorld().createBody(bd);
        EdgeShape shape = new EdgeShape();
        shape.set(new Vec2(-getWidth() / 2 + commonPersonEdge * 6, getHeight() / 2), new Vec2(-getWidth() / 2 + commonPersonEdge * 6, commonPersonEdge * 3));
        ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(-getWidth() / 2 + commonPersonEdge * 5, 0), new Vec2(getWidth() / 8, 0));
        Fixture f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);

        shape.set(new Vec2(getWidth() / 8, 0), new Vec2(getWidth() / 8, -getHeight() / 2));
        f = ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(-getWidth() / 2 + commonPersonEdge * 5, 0), new Vec2(-getWidth() / 2 + commonPersonEdge * 5, -getHeight() / 2));
        f = ground.createFixture(shape, 0.0f);

        shape.set(new Vec2(getWidth() / 8, -getHeight() / 2 + 3), new Vec2(getWidth() / 8 + 2, -getHeight() / 2 + 3));
        f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);
        shape.set(new Vec2(getWidth() / 4 + 3.5f * commonPersonEdge-2, -getHeight() / 2 + 3), new Vec2(getWidth() / 4 + 3.5f * commonPersonEdge, -getHeight() / 2 + 3));
        f = ground.createFixture(shape, 0.0f);
        objectForJump.add(f);

        shape.set(new Vec2(getWidth() / 4 + 3.5f * commonPersonEdge, 0), new Vec2(getWidth() / 4 + 3.5f * commonPersonEdge, -getHeight() / 2));
        f = ground.createFixture(shape, 0.0f);

    }

    private void createGuns() {
        Gun gun1 = new Gun(m_world, -getWidth() / 2 + 2, -getHeight() / 2 + commonPersonEdge, 70, 400, 0.5f);
        gun1.setOrientation(new Vec2(0, 1));
        objectForJump.add(gun1.getGunBodyFixture());
        gunList.add(gun1);

        Gun gun2 = new Gun(m_world, 9*commonPersonEdge, -getHeight() / 2 + commonPersonEdge, 500, 400, 0.5f);
        gun2.setOrientation(new Vec2(0, 1));
        objectForJump.add(gun2.getGunBodyFixture());
        gunList.add(gun2);

        Gun gun3 = new Gun(m_world, 14*commonPersonEdge, -getHeight() / 2 + commonPersonEdge, 500, 300, 0.5f);
        gun3.setOrientation(new Vec2(0, 1));
        objectForJump.add(gun3.getGunBodyFixture());
        gunList.add(gun3);
    }

    @Override
    public String getTestName() {
        return "Level 2";
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
