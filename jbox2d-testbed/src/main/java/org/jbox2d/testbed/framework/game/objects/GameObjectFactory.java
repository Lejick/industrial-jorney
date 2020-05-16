package org.jbox2d.testbed.framework.game.objects;

import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import java.util.List;

public class GameObjectFactory {

    public static MovingObject createMovingObject(Body movingBody, Body switcher, List<Vec2> coordinatesList, boolean alreadyActive) {
        MovingObject movingObject = new MovingObject(movingBody, coordinatesList);
        switcher.shapeColor = Color3f.ORANGE;
        if (alreadyActive) {
            movingBody.setLinearVelocity(new Vec2(0, -1));
            switcher.shapeColor = Color3f.GREEN;
        }
        movingObject.setSwitcher(switcher);
        return movingObject;
    }

}
