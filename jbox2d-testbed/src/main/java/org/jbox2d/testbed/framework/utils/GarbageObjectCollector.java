package org.jbox2d.testbed.framework.utils;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GarbageObjectCollector {
    Map<Long, List<Body>> objectsMap = new HashMap<Long, List<Body>>();

    public void add(Body body, long stepToLive) {
        List<Body> bodyList = objectsMap.getOrDefault(stepToLive, new ArrayList<>());
        bodyList.add(body);
        objectsMap.put(stepToLive, bodyList);
    }

    public void clear(long step, World world) {
        List<Body> bodyList = objectsMap.getOrDefault(step, new ArrayList<>());
        for (Body body : bodyList) {
            if (body != null && body.getFixtureList() != null) {
                world.destroyBody(body);
                body.setDestroy(true);
            }
        }
    }
}
