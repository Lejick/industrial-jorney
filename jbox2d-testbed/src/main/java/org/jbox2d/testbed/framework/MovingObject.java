package org.jbox2d.testbed.framework;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.util.ArrayList;
import java.util.List;

public class MovingObject {
    private Body movingBody;
    private Body switcher;
    private Vec2 nextDestination;
    private Vec2 prevDestination;
    private boolean isActive = false;
    private Vec2 scalarVelocity = new Vec2(1, 1);
    private int nextDestinationIndex;
    private int maxDestinationIndex;
    private List<Vec2> coordinatesList = new ArrayList();

    public MovingObject(Body movingBody, List<Vec2> coordinatesList) {
        nextDestination = coordinatesList.get(1);
        prevDestination = coordinatesList.get(0);
        maxDestinationIndex = coordinatesList.size() - 1;
        nextDestinationIndex = 1;
        this.coordinatesList = coordinatesList;
        this.movingBody = movingBody;
    }

    public void calculateVelocity() {
        if (movingBody == null) return;
        if (isActive) {
            nextDestinationIndex++;
            if (nextDestinationIndex > maxDestinationIndex) {
                nextDestinationIndex = 0;
            }
            prevDestination = nextDestination;
            nextDestination = coordinatesList.get(nextDestinationIndex);
            float norm = (float) Math.sqrt
                    (Math.pow(prevDestination.x - nextDestination.x, 2) +
                            Math.pow(prevDestination.y - nextDestination.y, 2));
            Vec2 normVector = new Vec2((nextDestination.x - prevDestination.x) / norm,
                    (nextDestination.y - prevDestination.y) / norm);
            Vec2 currentVelocity = new Vec2(normVector.x * scalarVelocity.x, normVector.y * scalarVelocity.y);
            movingBody.setLinearVelocity(currentVelocity);
        } else {
            movingBody.setLinearVelocity(new Vec2(0, 0));
        }

    }

    public void calculateStep() {
        if (checkPosition()) {
            calculateVelocity();
        }
    }

    public boolean checkPosition() {
        float dx = Math.abs(movingBody.getPosition().x - nextDestination.x);
        float dy = Math.abs(movingBody.getPosition().y - nextDestination.y);
        return dx < 0.1 && dy < 0.1;
    }

    public Body getSwitcher() {
        return switcher;
    }

    public void setSwitcher(Body switcher) {
        this.switcher = switcher;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        if(active==true){
            calculateVelocity();
        } else {
            movingBody.setLinearVelocity(new Vec2(0,0));
        }
    }
}
