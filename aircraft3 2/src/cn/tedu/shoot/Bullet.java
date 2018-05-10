package cn.tedu.shoot;

/**
 * This is a class for bullet
 * It is a flying object
 */
public class Bullet extends FlyingObject{
	private int speed = 3;//initialize speed

	/**
	*the x,y coordinates of bullet
	*@param x the x coordinate of bullet
	*@param y the y coordinate of bullet
	*/
	Bullet(int x, int y){   //this is a constructor
		image = ShootGame.bullet; //get image
		width = image.getWidth();   //image's width
		height = image.getHeight(); //image's height
		this.x=x; //x coordinate
		this.y=y; //y coordinate
	}

	/**
	rewrite step method
	*/
	public void step(){
		y-=speed; // y goes up(the bullet goes up)
	}

	/**
	*we pause the game if it hits the boundary of the window
	@return a boolean that whether this.y is less than or equal to -this.height
	*/
	public boolean outOfBounds(){
		return this.y<=-this.height;
	}
}
