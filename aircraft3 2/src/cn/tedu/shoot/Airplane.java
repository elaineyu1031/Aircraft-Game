package cn.tedu.shoot;
import java.util.Random;

/**
This is the class of airplane
The airplane of enemy is both flying object and enemy
*/
public class Airplane extends FlyingObject implements Enemy{
	//Airplane Method
	Airplane(){
		image = ShootGame.airplane; //get the image of airplane
		width = image.getWidth();   //the width of image
		height = image.getHeight(); //the height of image
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH-this.width);//x: generate a random number from 0 to (window width- airplane width)
		y = -this.height;////-1*the height of enemy plane
	}

	/**
	@return 5
	getting Score when hit the enemy plane
	*/
	public int getScore(){
		return 5;//if we hit the enemy plane, we get 5 points
	}

	/**
	Set y coordinate
	*/
	void step(){
		int speed = 2;
		y+= speed; //y goes downward
	}
	/**
	we pause the game if it hits the boundary of the window
	@return this.y>=ShootGame.HEIGHT
	*/
	boolean outOfBounds(){
		return this.y>=ShootGame.HEIGHT;//this refers to enemy plane
	}
}
