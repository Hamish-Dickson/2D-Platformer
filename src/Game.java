import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import game2D.*;

// Game demonstrates how we can override the GameCore class
// to create our own 'game'. We usually need to implement at
// least 'draw' and 'update' (not including any local event handling)
// to begin the process. You should also add code to the 'init'
// method that will initialise event handlers etc. By default GameCore
// will handle the 'Escape' key to quit the game but you should
// override this with your own event handler.

/**
 * @author David Cairns
 */
@SuppressWarnings("serial")

public class Game extends GameCore {
    // Useful game constants
    static int screenWidth = 562;
    static int screenHeight = 384;

    int jumpsDone = 0;//keeps track of jumps before landing so as to only allow 2 before landing on solid surface.

    float lift = 0.005f;
    float gravity = 0.0005f;

    // Game state flags
    boolean up = false;
    boolean left;
    boolean right;
    boolean falling = true;

    boolean gameOver = false;
    // Game resources
    Animation playerAnim;

    Sprite player = null;
    Sprite enemy1 = null;
    Sprite enemy2 = null;
    Sprite enemy3 = null;


    TileMap tmap = new TileMap();    // Our tile map, note that we load it in init()


    private int level = 1; //keep track of current level
    private String status = "Alive :D";

    private int animDuration = 500;


    /**
     * The obligatory main method that creates
     * an instance of our class and starts it running
     *
     * @param args The list of parameters this program might use (ignored)
     */
    public static void main(String[] args) {
        Sound s = new Sound("sounds/song.wav");

        s.start();
        Game gct = new Game();
        gct.init("map1.txt");
        // Start in windowed mode with the given screen height and width
        gct.run(false, screenWidth, screenHeight);


    }

    /**
     * Initialise the class, e.g. set up variables, load images,
     * create animations, register event handlers
     */
    public void init(String mapFile) {

        // Load the tile map and print it out so we can check it is valid
        tmap.loadMap("maps", mapFile);


        playerAnim = new Animation();
        playerAnim.addFrame(loadImage("images/sprites/player_pause1.png"), animDuration);
        playerAnim.addFrame(loadImage("images/sprites/player_pause2.png"), animDuration);

        Animation enemyAnim = new Animation();
        enemyAnim.addFrame(loadImage("images/sprites/enemy_0.png"), animDuration);
        enemyAnim.addFrame(loadImage("images/sprites/enemy_1.png"), animDuration);
        enemyAnim.play();

        // Initialise the player with an animation
        player = new Sprite(playerAnim);


        //Initialise the enemies with animation
        enemy1 = new Sprite(enemyAnim);
        enemy2 = new Sprite(enemyAnim);
        enemy3 = new Sprite(enemyAnim);


        initialiseGame();

        System.out.println(tmap);
    }

    /**
     * You will probably want to put code to restart a game in
     * a separate method so that you can call it to restart
     * the game.
     */
    public void initialiseGame() {
        player.setX(20);
        player.setY(100);
        player.setVelocityX(0);
        player.setVelocityY(0);
        player.show();

        setupEnemies();

        enemy1.show();
        enemy2.show();
        enemy3.show();
    }

    private void setupEnemies() {
        switch (level) {
            case 1:
                enemy1.setX(230);
                enemy2.setX(400);
                enemy3.setX(800);

                enemy1.setY(150);
                enemy2.setY(185);
                enemy3.setY(100);
                break;
            case 2:
                enemy1.setX(270);
                enemy2.setX(450);
                enemy3.setX(800);

                enemy1.setY(170);
                enemy2.setY(200);
                enemy3.setY(250);
                break;
            default:
                enemy1.setX(230);
                enemy2.setX(400);
                enemy3.setX(800);

                enemy1.setY(150);
                enemy2.setY(180);
                enemy3.setY(150);
        }
    }

    /**
     * Draw the current state of the game
     */
    public void draw(Graphics2D g) {
        // Be careful about the order in which you draw objects - you
        // should draw the background first, then work your way 'forward'

        // First work out how much we need to shift the view
        // in order to see where the player is.
        int xo = (int) -player.getX() + 20;
        int yo = 0;


        // If relative, adjust the offset so that
        // it is relative to the player

        // ...?

        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());


        // Apply offsets to sprites and draw
        player.setOffsets(xo, yo);
        enemy1.setOffsets(xo, yo);
        enemy2.setOffsets(xo, yo);
        enemy3.setOffsets(xo, yo);

        enemy1.draw(g);
        enemy2.draw(g);
        enemy3.draw(g);

        player.draw(g);
        // Apply offsets to tile map and draw  it
        tmap.draw(g, xo, yo);

        // Show score and status information
        String msg = "Status: " + status;
        g.setColor(Color.darkGray);
        g.drawString(msg, getWidth() - 150, 50);
    }

    /**
     * Update any sprites and check for collisions
     *
     * @param elapsed The elapsed time between this call and the previous call of elapsed
     */
    public void update(long elapsed) {
        if (!gameOver) { //if the game is running
            // Make adjustments to the speed of the sprite due to gravity
            if (falling) {
                player.setVelocityY(player.getVelocityY() + (gravity * elapsed));
            }
            player.setAnimationSpeed(1.0f);

            if (up) {
                if (jumpsDone < 2) {//only allows for 2 jumps per landing i.e the player must land before they jump again
                    if (player.getVelocityY() >= 0) {//only allow jump when trajectory is downwards
                        player.setVelocityY(-0.20f);
                        player.shiftY(-0.01f);
                        up = false;
                        updateAnim("up");
                        Sound s = new Sound("sounds/jump.wav");
                        s.start();
                        jumpsDone++;
                    }
                }
            }

            if (left) {
                if (player.getVelocityX() > 0.04f) {//slow down dramatically if trying to move in other direction
                    player.setVelocityX(0.04f);
                    left = false;
                } else {
                    player.setVelocityX(player.getVelocityX() - 0.04f);
                    left = false;
                    updateAnim("left");
                }
            }

            if (right) {
                if (player.getVelocityX() < -0.04f) {//slow down dramatically if trying to move in other direction
                    player.setVelocityX(-0.04f);
                    right = false;
                } else {
                    player.setVelocityX(player.getVelocityX() + 0.04f);
                    right = false;
                    updateAnim("right");
                }
            }

            ArrayList<Sprite> enemies = new ArrayList<Sprite>();
            enemies.add(enemy1);
            enemies.add(enemy2);
            enemies.add(enemy3);

            for (Sprite enemy : enemies) {
                if ((enemy.getX() >850 && enemy.isDirection()) || (enemy.getX()<250 && !enemy.isDirection())){
                    enemy.setDirection(!enemy.isDirection());
                }

                if(enemy.isDirection()){
                    enemy.setVelocityX(0.04f);
                }
                else{
                    enemy.setVelocityX(-0.04f);
                }
                enemy.update(elapsed);
            }
            // Now update the sprites animation and position
            player.update(elapsed);

            // Then check for any collisions that may have occurred
            handleTileMapCollisions(player, elapsed);
            
            //check for sprite collisions
            handleSpriteCollisions();
        } else {//if the game is over

        }

    }



    private void updateAnim(String direction) {
        System.out.println(direction);
        switch (direction) {
            case "left":
                playerAnim = new Animation();
                playerAnim.addFrame(loadImage("images/sprites/player_left1.png"), animDuration);
                playerAnim.addFrame(loadImage("images/sprites/player_left2.png"), animDuration);
                break;

            case "right":
                playerAnim = new Animation();
                playerAnim.addFrame(loadImage("images/sprites/player_right1.png"), animDuration);
                playerAnim.addFrame(loadImage("images/sprites/player_right2.png"), animDuration);
                break;
            case "up":
                playerAnim = new Animation();
                playerAnim.addFrame(loadImage("images/sprites/player_up1.png"), animDuration);
                playerAnim.addFrame(loadImage("images/sprites/player_up2.png"), animDuration);
                break;
            case "dead":
                playerAnim = new Animation();
                playerAnim.addFrame(loadImage("images/sprites/player_dead.png"), animDuration);
                break;
            case "pause":
                playerAnim = new Animation();
                playerAnim.addFrame(loadImage("images/sprites/player_pause1.png"), animDuration);
                playerAnim.addFrame(loadImage("images/sprites/player_pause2.png"), animDuration);
                break;
            default:
                playerAnim = new Animation();
                playerAnim.addFrame(loadImage("images/sprites/player_pause1.png"), animDuration);
                playerAnim.addFrame(loadImage("images/sprites/player_pause2.png"), animDuration);
                break;
        }
        player.setAnimation(playerAnim);
    }

    private void handleSpriteCollisions() {
        ArrayList<Sprite> enemies = new ArrayList<Sprite>();
        enemies.add(enemy1);
        enemies.add(enemy2);
        enemies.add(enemy3);

        boolean collided = false;

        for (Sprite enemy: enemies){//for each enemy
            if(boundingBoxCollision(enemy, player)){
                collided = true;
            }
        }

        if(collided){
            endGame();
        }

    }

    /**
     * function to check if 2 sprites are colliding with each other
     * @param s1 first sprite to be checked
     * @param s2 second sprite to be checked
     * @return true if collision, else false
     */
    public boolean boundingBoxCollision(Sprite s1, Sprite s2) {
        return ((s1.getX() + s1.getImage().getWidth(null) >s2.getX()) && s1.getX() <=s2.getX()) &&
                ((s1.getY() + s1.getImage().getHeight(null) > s2.getY()) && s1.getY() < s2.getY());
    }

    /**
     * Checks and handles collisions with the tile map for the
     * given sprite 's'. Initial functionality is limited...
     *
     * @param s       The Sprite to check collisions for
     * @param elapsed How time has gone by
     */
    public void handleTileMapCollisions(Sprite s, long elapsed) {
        // This method should check actual tile map collisions. For
        // now it just checks if the player has gone off the bottom
        // of the tile map.

        if (s.getY() + s.getHeight() > tmap.getPixelHeight()) {
            // Put the player back on the map
            s.setY(tmap.getPixelHeight() - s.getHeight());
        }

        int tileCoordX = (int) (s.getX() / tmap.getTileWidth());
        int tileCoordY = (int) ((s.getY() + s.getHeight()) / tmap.getTileHeight());//offset by 1 so the player sits on top of the block

        if (tmap.getTileChar(tileCoordX, tileCoordY) == 'p' || tmap.getTileChar(tileCoordX, tileCoordY) == 'b') {//if grass or dirt block touched
            if (s.getVelocityY() > 0) {
                s.setVelocityY(0);
            }

            s.setY((float) (tileCoordY * tmap.getTileHeight()) - s.getHeight());
            falling = false;
            jumpsDone = 0;
        } else {
            falling = true;
        }
        if (tmap.getTileChar(tileCoordX, tileCoordY) == 't') {
            endGame();
        }
        if (tmap.getTileChar(tileCoordX, tileCoordY) == 'f') {
            nextLevel();
        }

    }

    private void nextLevel() {
        if (level == 1) {
            init("map2.txt");
            level = 2;
        } else {
            level = 1;
            init("map1.txt");
        }
    }

    private void endGame() {
        updateAnim("dead");
        player.setVelocityX(0);
        player.setVelocityY(0);
        gameOver = true;
        status = "Dead X.X";
        Sound s = new Sound("sounds/death.wav");
        s.start();
    }

    private void resetGame() {//reset the player character to original state
        gameOver = false;
        player.setX(20);
        player.setY(100);
        player.setVelocityY(0);
        player.setVelocityX(0);
        updateAnim("pause");
        status = "Alive :D";
        right = false;
        left = false;
    }


    /**
     * Override of the keyPressed event defined in GameCore to catch our
     * own events
     *
     * @param e The event that has been generated
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_ESCAPE) stop();

        if (key == KeyEvent.VK_UP) up = true;

        if (key == KeyEvent.VK_LEFT) left = true;

        if (key == KeyEvent.VK_RIGHT) right = true;

        if (key == KeyEvent.VK_F5) resetGame();

        if (key == KeyEvent.VK_M) {
            level = 2;
            init("map2.txt");

        }
        if (key == KeyEvent.VK_N) {
            level = 1;
            init("map1.txt");

        }

    }



    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        // Switch statement instead of lots of ifs...
        // Need to use break to prevent fall through.
        switch (key) {
            case KeyEvent.VK_ESCAPE:
                stop();
                break;
            default:
                break;
        }
    }
}
