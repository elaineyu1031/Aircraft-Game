package cn.tedu.shoot;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Font;


public class ShootGame extends JPanel { //window
    //set window'width and height to fixed values
    static final int WIDTH = 400;
    static final int HEIGHT = 654;
    private static BufferedImage background;//background image
    private static BufferedImage start;     //start image
    private static BufferedImage pause;     //pause image
    private static BufferedImage gameover;  //gameover image
    static BufferedImage airplane;  //enemy image
    static BufferedImage bee;       //bee image
    static BufferedImage bullet;    //bullet image
    static BufferedImage hero0;     //hero0 image
    static BufferedImage hero1;     //hero1 image
    static BufferedImage laser;     //laser image


    static { //initialize static sources(image,video,sound..)
        try {
            background = ImageIO.read(ShootGame.class.getResource("background.png"));
            start = ImageIO.read(ShootGame.class.getResource("start.png"));
            pause = ImageIO.read(ShootGame.class.getResource("pause.png"));
            gameover = ImageIO.read(ShootGame.class.getResource("gameover.png"));
            airplane = ImageIO.read(ShootGame.class.getResource("airplane.png"));
            bee = ImageIO.read(ShootGame.class.getResource("bee.png"));
            bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));
            hero0 = ImageIO.read(ShootGame.class.getResource("hero0.png"));
            hero1 = ImageIO.read(ShootGame.class.getResource("hero1.png"));
            laser = ImageIO.read(ShootGame.class.getResource("laserBlue02.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int START = 0;     //start state
    private static final int RUNNING = 1;   //running state
    private static final int PAUSE = 2;     //pause state
    private static final int GAME_OVER = 3; //gameover state
    private int state = START;               //current state(default start state)


    private Hero hero = new Hero();      //hero object
    private FlyingObject[] flyings = new FlyingObject[]{}; //array of enemies(flying objects+bees)
    private Bullet[] bullets = {};       //array of bullets

    /**
     * generate enemies(flying objects and bees)
     *
     * @return bee or airplane randomly
     */
    private FlyingObject nextOne() {
        Random rand = new Random();
        int type = rand.nextInt(10);
        if (type < 2) {
            return new Bee();
        } else {
            return new Airplane();

        }
    }

    private int enteredIndex = 0;         //number of entrances of enemies

    /**
     * enemies enter the game
     */
    private void enterAction() {   //called every 10ms = 0.01 second
        enteredIndex++;           //add 1 every 10ms = 0.01 second
        if (enteredIndex % 25 == 0) {   //every 0.25 sec appear a flying object
            FlyingObject obj = nextOne();//obtain enemy
            flyings = Arrays.copyOf(flyings, flyings.length + 1);//extend
            flyings[flyings.length - 1] = obj;
        } else if (enteredIndex > 20000) {
            enteredIndex = 0;
        }
    }

    /**
     * generate the flying objects movements
     */
    private void stepAction() {
        hero.step(); //hero move one step
        for (FlyingObject flying : flyings) {
            flying.step();//enemy move one step
        }
        for (Bullet bullet1 : bullets) {
            bullet1.step();//bullet move one step
        }
    }

    private int shootIndex = 0; //number of shooting

    /**
     * bullets enter the game
     */
    private void shootAction() {  //move every 10mm seconds
        shootIndex++;            //add 1 every 10mm seconds
        if (shootIndex % 25 == 0) {    //move every 250(10*25)mm seconds
            Bullet[] bs = hero.shoot();//obtain bullets
            bullets = Arrays.copyOf(bullets, bullets.length + bs.length); //extend capacity(= indexes of bs)
            System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length);//add array

        } else if (shootIndex > 20000) {
            shootIndex = 0;
        }
    }

    /**
     * delete the flying objects(enemies,bees,bullets)
     * that are out of bounds
     */
    private void outOfBoundsAction() { //move every 10mm sec
        int index = 0; //enemies that are not out of bounds
        FlyingObject[] flyingLives = new FlyingObject[flyings.length];
        for (FlyingObject f : flyings) {
            if (!f.outOfBounds()) { //not out of bounds
                flyingLives[index] = f;//move the objects that are not out of bounds to the array
                index++;
            }
        }
        flyings = Arrays.copyOf(flyingLives, index);
        index = 0;
        Bullet[] bulletLives = new Bullet[bullets.length];
        for (Bullet b : bullets) {
            if (!b.outOfBounds()) {
                bulletLives[index] = b;
                index++;

            }
        }
        bullets = Arrays.copyOf(bulletLives, index);
    }

    /**
     * generate the bump between all bullets and all enemies
     */
    private void bangAction() {
        int inx = 0;
//        for (Bullet b : bullets) {
//            if (bang(b)){ //bump of one bullet and all enemies
//                break;
//            }
//            inx++;
//        }
        while (inx < bullets.length) {
            if (bang(bullets[inx])) {
                Bullet b = bullets[inx];
                bullets[inx] = bullets[bullets.length - 1];
                bullets[bullets.length - 1] = b;
                bullets = Arrays.copyOf(bullets, bullets.length - 1);
            } else {
                inx++;
            }
        }
    }

    private int score = 0;

    /**
     * generate the bump between one bullet and all enemies
     */
    private boolean bang(Bullet b) {
        int index = -1; //bumped enemies
        for (int i = 0; i < flyings.length; i++) {
            FlyingObject f = flyings[i];
            if (f.shootBy(b)) { //bump
                index = i;    //record enemies that are bumped
                break;       //do not compare the rest of the enemies
            }
        }
        if (index != -1) {   //bump
            FlyingObject one = flyings[index]; //obtain bumped enemies
            if (one instanceof Enemy) {  //if the bumped is enemy
                Enemy e = (Enemy) one;   //revert the bumped object to enemy
                score += e.getScore();  //player gain score
            }
            if (one instanceof Award) { //if the bumped is award
                Award a = (Award) one;  //revert the bumped object to award
                int type = a.getType();//player gain reward
                switch (type) {
                    case Award.DOUBLE_FIRE://when award is firepower
                        hero.addDoubleFire();//hero add firepower
                        break;
                    case Award.LIFE:        //when award is life
                        hero.addLife();     //hero add life
                        break;
                }
            }
            //switch the bumped object to the last index of flyings
            FlyingObject t = flyings[index];
            flyings[index] = flyings[flyings.length - 1];
            flyings[flyings.length - 1] = t;
            //reduce the last index(the bumped object)
            flyings = Arrays.copyOf(flyings, flyings.length - 1);
            return true;
        }
        return false;
    }

    /**
     * generate the bump between hero and enemies
     */
    private void hitAction() { //move every 10mm sec
        for (int i = 0; i < flyings.length; i++) {
            FlyingObject f = flyings[i];
            if (hero.hit(f)) {//bump
                hero.subtractLife();
                hero.clearDoubleFire();
                //same code as above
                FlyingObject t = flyings[i];
                flyings[i] = flyings[flyings.length - 1];
                flyings[flyings.length - 1] = t;
                flyings = Arrays.copyOf(flyings, flyings.length - 1);
                break;
            }
        }
    }

    /**
     * check if the game ends
     */
    private void checkGameOverAction() {
        if (hero.getLife() <= 0) { //gameover
            state = GAME_OVER;//change the state to gameover state
        }
    }


    /**
     * generate the start of program
     */
    private void action() {
        //create listener object
        MouseAdapter l = new MouseAdapter() {
            //rewrite mouseMoved incident
            public void mouseMoved(MouseEvent e) {
                if (state == RUNNING) {
                    int x = e.getX();  //obtain mouse's x-coord
                    int y = e.getY();  //obtain mouse's y-coord
                    hero.moveTowards(x, y);
                }
            }

            /**
             rewrite mouseClicked incident
             */
            public void mouseClicked(MouseEvent e) {
                switch (state) {
                    case START:         //when start
                        state = RUNNING;  //change state to running state
                        break;
                    case GAME_OVER:    //when over
                        score = 0;       //clear all data to 0
                        hero = new Hero();
                        flyings = new FlyingObject[0];
                        bullets = new Bullet[0];
                        state = START;    //change state to start
                        break;
                }
            }

            /**
             generate the state when mouse exists
             @param e mouse event
             */
            public void mouseExited(MouseEvent e) {
                if (state == RUNNING) {
                    state = PAUSE;
                }
            }

            /**
             generate the state when mouse enters
             @param e mouse event
             */
            public void mouseEntered(MouseEvent e) {
                if (state == PAUSE) {
                    state = RUNNING;
                }
            }
        };
        this.addMouseListener(l);       //solve mouse operating event
        this.addMouseMotionListener(l); //solve mouse moving event


        Timer timer = new Timer(); //create timer object
        int interval = 10;         //set the interval(mm sec)
        timer.schedule(new TimerTask() {
            /**
             generate the movements
             that happens every 10mm sec
             */
            public void run() {
                if (state == RUNNING) {
                    enterAction(); //enemy enter
                    stepAction();
                    shootAction(); //bullet enter(hero start shooting)
                    outOfBoundsAction(); //delete the objects that out of bounds
                    bangAction();  //bump between bullets and enemies
                    hitAction();
                    checkGameOverAction(); //check if game ends
                }
                repaint();     //repaint
            }
        }, interval, interval);//interval plan
    }


    /**
     * generate the whole paintings
     *
     * @param g graphs
     */
    public void paint(Graphics g) {
      g.drawImage(background, 0, 0, null);//default (0,0) top left
      paintHero(g);
      paintFlyings(g);
      paintBullets(g);
      paintScoreAndLife(g);
      paintState(g);
    }

    /**
     * geneate the hero painting
     *
     * @param g graph
     */
    private void paintHero(Graphics g) {
        g.drawImage(hero.image, hero.x, hero.y, null);//object of hero
    }

    /**
     * generate the enemy painting
     *
     * @param g graph
     */
    private void paintFlyings(Graphics g) {
        for (FlyingObject f : flyings) {  //loop through all enemies
            g.drawImage(f.image, f.x, f.y, null);

        }
    }

    /**
     * generate the bullet painting
     *
     * @param g graph
     */
    private void paintBullets(Graphics g) {
        for (Bullet b : bullets) {
            g.drawImage(b.image, b.x, b.y, null);
        }
    }

    /**
     * generate the score and life painting
     */
    private void paintScoreAndLife(Graphics g) {
        g.setColor(new Color(0xFF0000));   //set color to red
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        g.drawString("SCORE:" + score, 10, 25);//1.character 2.x-coord 3.y-coord
        g.drawString("LIFE:" + hero.getLife(), 10, 45);
    }

    /**
     * generate the state painting
     *
     * @param g graph
     */
    private void paintState(Graphics g) {
        switch (state) {
            case START:
                g.drawImage(start, 0, 0, null);
                break;
            case PAUSE:
                g.drawImage(pause, 0, 0, null);
                break;
            case GAME_OVER:
                g.drawImage(gameover, 0, 0, null);
        }
    }

    /**
     * carry on an interaction with the user
     *
     * @param args an array of Strings which we ignore
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();        //create window
        ShootGame game = new ShootGame();   //create panel
        frame.add(game);                    //add the panel to window
        frame.setSize(WIDTH, HEIGHT);      //set the size of window
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.action();
    }

}
