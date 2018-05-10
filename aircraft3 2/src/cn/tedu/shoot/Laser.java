package cn.tedu.shoot;

/**
 * This is a class for Laser
 * It is related to Bullet
 */
class Laser extends Bullet{
	private int speed = 3;//the speed

	/**
	the x,y coordinate of laser
	@param x
	@param y
	*/
	Laser(int x, int y){
		super(x,y);
		image = ShootGame.laser; //get image
		width = image.getWidth();   //image's width
		height = image.getHeight(); //image's height
		this.x=x; //x coordinate
		this.y=y; //y coordinate
	}
}
