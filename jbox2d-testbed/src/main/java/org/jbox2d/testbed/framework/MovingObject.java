package org.jbox2d.testbed.framework;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.util.ArrayList;
import java.util.List;

public class MovingObject {
    private Body movingBody;
    private Body switcher;
    private Vec2 nextDestination;
    private boolean isActive;
    private Vec2 scalarVelocity;
    private Vec2 currentVelocity;
    private int nextDestinationIndex;
    private int maxDestinationIndex;
    private List<Vec2> coordinatesList = new ArrayList();


    public MovingObject(Body movingBody, Vec2 normVelocity, List<Vec2> coordinatesList) {
        this.movingBody = movingBody;
        nextDestination = coordinatesList.get(0);
        maxDestinationIndex=coordinatesList.size();
        nextDestinationIndex=0;
        float norm=nextDestination.x-movingBody.x
        currentVelocity.x=movingBody.getPosition().x
    }

    public void resetVelocity(){

    }
    public boolean checkPosition() {
        return (movingBody.getPosition().x == nextDestination.x && movingBody.getPosition().y == nextDestination.y);
    }
}
