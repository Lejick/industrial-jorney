package org.jbox2d.testbed.framework.game.objects;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Color3f;
import org.jbox2d.dynamics.*;

public class GeometryBodyFactory {

    public static Body createRectangle(float x, float y, float hx, float hy, BodyType bodyType, World world, Color3f color) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(hx, hy);
        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 1.0f;
        fd.friction = 0.3f;
        BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(x, y);
        Body body = world.createBody(bd);
        body.shapeColor = color;
        body.createFixture(fd);
        return body;
    }

    public static Body createRectangle(float x, float y, float hx, float hy, BodyType bodyType, World world) {
        return createRectangle(x, y, hx, hy, bodyType, world, null);
    }

}
