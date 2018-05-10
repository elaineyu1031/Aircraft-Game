package cn.tedu.shoot;
import java.awt.image.BufferedImage;

abstract public class FlyingObject {
	BufferedImage image;
	int width;
	int height;
	int x;
	int y;

	/**
	call abstract method step for the flying objects
	*/
	abstract void step();

	/**
	To see if the flying objects reach the boundary
	we use abstract method because each flying objects perform differently
	@return this abstract methods returns the value that depends on subclass
	*/
	abstract boolean outOfBounds();



	/**
	Too see if the bullet hits the target (this: refers to enemy plane)
	@param bullet
	@return a boolean that whether x>=x1 && x<=x2 && y>=y1 && y<=y2 (if the bullet hits the enemy)
	*/
	boolean shootBy(Bullet bullet){
		int x1 = this.x;
		int x2 = this.x+this.width;
		int y1 = this.y;
		int y2 = this.y+this.height;
		int x = bullet.x;
		int y = bullet.y;

		return x>=x1 && x<=x2 && y>=y1 && y<=y2; //x is between x1 and x2 and y is between y1 and y2
	}
}
