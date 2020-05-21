package org.jbox2d.testbed;

import org.jbox2d.dynamics.Body;

public class Hero {
    protected Body heroBody;
    public long stepInAir;

    public Hero(Body heroBody) {
        this.heroBody = heroBody;
    }

    public Body getBody() {
        return heroBody;
    }

    public void setBody(Body heroBody) {
        this.heroBody = heroBody;
    }


}
