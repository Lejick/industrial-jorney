package org.jbox2d.testbed;


import org.jbox2d.dynamics.*;

public class Hero {
    protected Body heroBody;
    protected World world;
    public long stepInAir;

    public Hero(Body heroBody, World world) {
        this.heroBody = heroBody;
        this.world=world;
    }

    public Body getBody() {
        return heroBody;
    }

    public void setBody(Body heroBody) {
        this.heroBody = heroBody;
    }


}
