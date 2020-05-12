package org.jbox2d.testbed.framework;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.util.ArrayList;
import java.util.List;

public class MovingObject {
    private Body movingBody;
    private Body switcher;
    private Vec2 nextDestination;
    private boolean isActive=true;
    private Vec2 scalarVelocity=new Vec2(1,1);
    private Vec2 currentVelocity;
    private int nextDestinationIndex;
    private int maxDestinationIndex;
    private List<Vec2> coordinatesList = new ArrayList();


    public MovingObject(Body movingBody, List<Vec2> coordinatesList) {
        nextDestination = coordinatesList.get(1);
        maxDestinationIndex = coordinatesList.size() - 1;
        nextDestinationIndex = 1;
        this.coordinatesList=coordinatesList;
        this.movingBody = movingBody;
        calculateVelocity();

    }

    public void calculateVelocity() {
        if(movingBody==null) return;
        if (isActive) {
            float norm = (float) Math.sqrt
                    (Math.pow(movingBody.getPosition().x - nextDestination.x,2)+
                            Math.pow(movingBody.getPosition().y - nextDestination.y,2));
            Vec2 normVector = new Vec2(( nextDestination.x-movingBody.getPosition().x) / norm,
                    (nextDestination.y-movingBody.getPosition().y) / norm);
            currentVelocity = new Vec2(normVector.x * scalarVelocity.x, normVector.y * scalarVelocity.y);
            movingBody.setLinearVelocity(currentVelocity);
            nextDestinationIndex++;
            if (nextDestinationIndex > maxDestinationIndex) {
                nextDestinationIndex = 0;
            }
         movingBody.setLinearVelocity(currentVelocity);
        } else {
            movingBody.setLinearVelocity(new Vec2(0, 1));
        }
        nextDestination = coordinatesList.get(nextDestinationIndex);
    }

    public void calculateStep() {
        if (checkPosition()) {
            calculateVelocity();
        }
    }

    public boolean checkPosition() {
        return (movingBody.getPosition().x == nextDestination.x && movingBody.getPosition().y == nextDestination.y);
    }
}
