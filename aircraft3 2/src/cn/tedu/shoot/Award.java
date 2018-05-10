package cn.tedu.shoot;
/**
This writes the method for Award
*/
public interface Award {
	public int DOUBLE_FIRE = 0;//initialize fire power
	public int LIFE = 1;//initialize number of life
	//get award(type 1 or 2)
	public int getType();
}
