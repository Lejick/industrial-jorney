package org.jbox2d.testbed.framework.game.objects;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import java.util.List;

public class GameObjectFactory {

    public static MovingObject createMovingObject(Body movingBody,Body switcher, List<Vec2> coordinatesList, boolean alreadyActive) {
        MovingObject movingObject = new MovingObject(movingBody, coordinatesList);
        if(alreadyActive) {
            movingBody.setLinearVelocity(new Vec2(0, -1));
        }
        movingObject.setSwitcher(switcher);
        return movingObject;
    }

}
