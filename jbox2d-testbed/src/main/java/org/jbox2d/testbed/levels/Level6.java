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
package org.jbox2d.testbed.levels;

import javafx.scene.Scene;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.testbed.Enemy;
import org.jbox2d.testbed.Hero;
import org.jbox2d.testbed.framework.AbstractTestbedController;
import org.jbox2d.testbed.framework.SettingsIF;
import org.jbox2d.testbed.framework.game.objects.GameObjectFactory;
import org.jbox2d.testbed.framework.game.objects.GeometryBodyFactory;
import org.jbox2d.testbed.framework.game.objects.MovingObject;
import org.jbox2d.testbed.framework.utils.Line;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Murphy
 */
public class Level6 extends CommonLevel {
    private static float width = 80;
    private static float height = 60;

    public Level6(AbstractTestbedController controller, Scene scene) {
        super(controller, scene);
    }

    @Override
    public void initTest(boolean deserialized) {
        super.initTest(false);
        createGameObjects();
        exit = GeometryBodyFactory.createRectangle(getWidth() / 2 - 0.25f,
                -getHeight() / 2 + 4 * commonPersonEdge, 0.25f, 4, BodyType.STATIC, getWorld(), Color3f.GREEN);
        rightBlockedFixtures.add(exit.getFixtureList());

    }

    protected void createGameObjects() {
        Body heroBody = GeometryBodyFactory.createRectangle(-8, 2, commonPersonEdge, commonPersonEdge, BodyType.DYNAMIC, getWorld(), Color3f.BLUE);
        destroyableList.add(heroBody);
        hero = new Hero(heroBody, getWorld());

        Body enemyBody = GeometryBodyFactory.createRectangle(-35, -28, commonPersonEdge, commonPersonEdge, BodyType.DYNAMIC, getWorld(), Color3f.RED);
        enemyBody.setLinearVelocity(new Vec2(1, 0));
        Enemy enemy = new Enemy(enemyBody, getWorld());
        enemyList.add(enemy);
        destroyableList.add(enemyBody);

        enemyBody = GeometryBodyFactory.createRectangle(30, -28, commonPersonEdge, commonPersonEdge, BodyType.DYNAMIC, getWorld(), Color3f.RED);
        enemyBody.setLinearVelocity(new Vec2(-1, 0));
        enemy = new Enemy(enemyBody, getWorld());
        enemyList.add(enemy);
        destroyableList.add(enemyBody);

        enemyBody = GeometryBodyFactory.createRectangle(15, -28, commonPersonEdge, commonPersonEdge, BodyType.DYNAMIC, getWorld(), Color3f.RED);
        enemyBody.setLinearVelocity(new Vec2(-1, 0));
        enemy = new Enemy(enemyBody, getWorld());
        enemyList.add(enemy);
        destroyableList.add(enemyBody);

    }


    protected void createPlatforms() {
        Body p1 = GeometryBodyFactory.createGameBrick(-29, 0, 6f, 0.5f, BodyType.STATIC, getWorld());
        List<Line> lines = GeometryBodyFactory.splitRectangle(-29, 0, 6f, 0.5f);
        linesList.addAll(lines);
        p1.getFixtureList().m_friction = 3;
        objectForJump.add(p1.getFixtureList());

        Body p2 = GeometryBodyFactory.createGameBrick(-10, 0, 6f, 0.5f, BodyType.STATIC, getWorld());
        lines = GeometryBodyFactory.splitRectangle(-10, 0, 6f, 0.5f);
        linesList.addAll(lines);
        p2.getFixtureList().m_friction = 3;
        objectForJump.add(p2.getFixtureList());

        Body p3 = GeometryBodyFactory.createGameBrick(9, 0, 6f, 0.5f, BodyType.STATIC, getWorld());
        lines = GeometryBodyFactory.splitRectangle(9, 0, 6f, 0.5f);
        linesList.addAll(lines);
        p3.getFixtureList().m_friction = 3;
        objectForJump.add(p3.getFixtureList());

        Body p4 = GeometryBodyFactory.createGameBrick(28, 0, 6f, 0.5f, BodyType.STATIC, getWorld());
        lines = GeometryBodyFactory.splitRectangle(28, 0, 6f, 0.5f);
        linesList.addAll(lines);
        p4.getFixtureList().m_friction = 3;
        objectForJump.add(p4.getFixtureList());

        Body p6 = GeometryBodyFactory.createGameBrick(-20, 5, 6f, 0.5f, BodyType.STATIC, getWorld());
        lines = GeometryBodyFactory.splitRectangle(-20, 5, 6f, 0.5f);
        linesList.addAll(lines);
        p6.getFixtureList().m_friction = 3;
        objectForJump.add(p6.getFixtureList());


        Body p7 = GeometryBodyFactory.createGameBrick(0, 5, 6f, 0.5f, BodyType.STATIC, getWorld());
        lines = GeometryBodyFactory.splitRectangle(0, 5, 6f, 0.5f);
        linesList.addAll(lines);
        p7.getFixtureList().m_friction = 3;
        objectForJump.add(p7.getFixtureList());

        Body p8 = GeometryBodyFactory.createGameBrick(20, 5, 6f, 0.5f, BodyType.STATIC, getWorld());
        lines = GeometryBodyFactory.splitRectangle(20, 5, 6f, 0.5f);
        linesList.addAll(lines);
        p8.getFixtureList().m_friction = 3;
        objectForJump.add(p8.getFixtureList());


        Body p5 = GeometryBodyFactory.createGameBrick(38.5f, -20, 1.2f, 0.1f, BodyType.STATIC, getWorld());
        lines = GeometryBodyFactory.splitRectangle(38.5f, -20, 1.2f, 0.1f);
        linesList.addAll(lines);
        p5.getFixtureList().m_friction = 3;
        objectForJump.add(p5.getFixtureList());

        Body platform2 = GeometryBodyFactory.createRectangle(37, -25, 0.2f, 5f, BodyType.KINEMATIC, getWorld());
        platform2.getFixtureList().m_friction = 0;
        rightBlockedFixtures.add(platform2.getFixtureList());
        List<Vec2> coordinatesList = new ArrayList<>();
        coordinatesList.add(new Vec2(37f, -10));
        MovingObject mo1 = GameObjectFactory.createMovingObject(platform2, null, coordinatesList, false, new Vec2(0, 1));
        movingObjectList.add(mo1);
    }

    @Override
    public void step(SettingsIF settings) {
        super.step(settings);
        if (hero.getEnemyKilled() == 3) {
            for (MovingObject movingObject : movingObjectList) {
                if(!movingObject.isActive()) {
                    movingObject.setActive(true);
                }
            }
        }
    }

    @Override
    protected int getLevelIndex() {
        return 5;
    }
    @Override
    protected boolean hasGun() {
        return true;
    }

    @Override
    protected void checkEnemyAction() {
    }

    @Override
    public String getLevelName() {
        return "Level 6";
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
