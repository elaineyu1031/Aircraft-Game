package cn.tedu.shoot;

import java.util.Random;

/**
 * This is a class for Bee
 * It is a flying object and is related to Award
 */
public class Bee extends FlyingObject implements Award {
    private int xspeed = 1;//initialize the speed of x coordinate
    private int yspeed = 2;//initialize the speed of y coordinate
    private int awardType;//awardType

    /**
     * Write method of Bee
     */
    Bee() {
        image = ShootGame.bee; //get image
        width = image.getWidth();   //the width
        height = image.getHeight(); //the height
        Random rand = new Random();
        x = rand.nextInt(ShootGame.WIDTH - this.width); //x: generate a random number from 0 to (window width- bee width)
  		 //Bee starts off being hidden above the window,
    	 //with lower edge touching the upper bound of window
        y = -this.height;
        awardType = rand.nextInt(2) - 1; //we generate a random award type 0 or 1
    }


    /**
     * we get award type
     * @return type 1 or type 2 award
     */
    public int getType() {
        return awardType;
    }

    /**
     * set x, y coordinate
     */
    void step() {
        x += xspeed;
        y += yspeed;
        if (this.x >= ShootGame.WIDTH - this.width) {
            xspeed = -1;
        }
        if (this.x <= 0) {
            xspeed = 1;
        }
    }

    /**
     * if bee gets out of bounds, we pause
     * @return we pause the game if bee hits the boundary of the window
     */
     boolean outOfBounds() {
        return this.y >= ShootGame.HEIGHT;
    }
}
