package cn.tedu.shoot;

import java.awt.image.BufferedImage;

public class Hero extends FlyingObject {
    private int life;//number of life
    private int doubleFire;//range of firepower
    private BufferedImage[] images;//array of images
    private int index;//help changing images

    private int vx = 0;
    private int vy = 0;
    /**
     * generate a hero
     * and set the default values
     */
    Hero() {
        image = ShootGame.hero0; //picture
        width = image.getWidth();   //width of image
        height = image.getHeight(); //height of image
        x = 150; //fixed x coordinate
        y = 400; //fixed y coordinate
        life = 3;//default three lives
        doubleFire = 0; //default zero firepower
        images = new BufferedImage[]{ShootGame.hero0, ShootGame.hero1}; //switch two images
        index = 0; //help switching images
    }

    /**
     * set the hero's move
     */
    void step() { //move every 10mm seconds
        image = images[index++ / 10 % images.length];
    }

    /**
     * create the bullets of hero
     *
     * @return the number of bullets
     */

    Bullet[] shoot() {
        int xStep = this.width / 4;    //1/4 width of hero
        int yStep = 20;              //fixed value
        if (doubleFire > 0) { //double firepower
            Bullet[] bs = new Bullet[2]; //two bullets
            bs[0] = new Laser(this.x + xStep, this.y - yStep);
            bs[1] = new Laser(this.x + 3 * xStep, this.y - yStep);
            doubleFire -= 2; //if shoot a doubleFire, fire power reduced two
            return bs;
        } else { //one firepower
            Bullet[] bs = new Bullet[1];
            bs[0] = new Bullet(this.x + 2 * xStep, this.y - yStep);
            return bs;
        }
    }

    /**
     * hero moves towards the mouse
     * x,y are the mouse's x,y coordinated
     */
    int moveInx = 0;
    void moveTowards(int x, int y) {
        if (moveInx % 3 == 0) {
            x = x - this.width / 2;
            y = y - this.height / 2;
            vx = (x - this.x) / 10;
            vy = (y - this.y) / 10;
            this.x +=  vx;
            this.y +=  vy;
        } else if (moveInx > 198) {
            moveInx = 0;
        }
        moveInx++;
    }

    /**
     * rewrite outOfBounds
     * check if it out of bounds
     */
    boolean outOfBounds() {
        return false; //never out of bounds
    }

    /**
     * add one life to hero
     */
    void addLife() {
        life++;
    }

    /**
     * get the lives of hero
     *
     * @return the lives of hero
     */
    int getLife() {
        return life;
    }

    /**
     * reduce one life of hero
     */
    void subtractLife() {
        life--;
    }

    /**
     * add 40 firepower of the hero
     */
    void addDoubleFire() {
        doubleFire += 40;
    }

    /**
     * clear the firepower
     */
    void clearDoubleFire() {
        doubleFire = 0; //firepower becomes zero
    }

    /**
     * hero hit enemy
     *
     * @return x>=x1 && x<=x2 && y>=y1 && y<=y2
     */
    boolean hit(FlyingObject other) {
        int x1 = other.x - this.width / 2;
        int x2 = other.x + other.width + this.width / 2;
        int y1 = other.y - this.height / 2;
        int y2 = other.y + other.height + this.height / 2;
        int x = this.x + this.width / 2;
        int y = this.y + this.height / 2;

        return x >= x1 && x <= x2
                &&
                y >= y1 && y <= y2;
    }


}
