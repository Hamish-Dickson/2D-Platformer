package game2D;

import java.awt.*;
import javax.swing.ImageIcon;

/**
    Simple abstract class used for testing. Subclasses should
    implement the draw() method.
*/
public abstract class GameCoreFS {

    protected static final int FONT_SIZE = 24;

    private boolean isRunning;
    protected ScreenManager screen;
    private	long startTime;
    private long currTime;
    private long elapsedTime;

    private long frames;


    /** Signals the game loop that it's time to quit */
    public void stop() { isRunning = false; }


    /** Calls init() and gameLoop() */
    public void run() {
        try {
            init();
            gameLoop();
        }
        finally { screen.restoreScreen(); }
    }


    /** Sets full screen mode and initiates and objects.    */
    public void init() {
        screen = new ScreenManager();
        DisplayMode displayMode = new DisplayMode(1024,768,32,0);
        screen.setFullScreen(displayMode);

        Window window = screen.getFullScreenWindow();
        window.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
        window.setBackground(Color.blue);
        window.setForeground(Color.white);

        isRunning = true;
        frames = 1;
        startTime = 1;
        currTime = 1;
    }

    public Image loadImage(String fileName) { return new ImageIcon(fileName).getImage(); }

    /** Runs through the game loop until stop() is called. */
    public void gameLoop() {
        startTime = System.currentTimeMillis();
        currTime = startTime;
        frames = 1;		// Keep a note of frames for performance measure

        Graphics2D g;
        
        while (isRunning) {
            elapsedTime = System.currentTimeMillis() - currTime;
            currTime += elapsedTime;

            // update
            update(elapsedTime);

            // draw the screen
            g = screen.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            draw(g);
            g.dispose();
            screen.update();
            frames++;

            // take a nap
            try {
                Thread.sleep(20);
            }
            catch (InterruptedException ex) { }
        }
        System.exit(0);
    }

    public float getFPS()
    {
    	if (currTime - startTime <= 0) return 0.0f;
    	return (float)frames/((currTime - startTime)/1000.0f);
    }

    /** Updates the state of the game/animation based on the
        amount of elapsed time that has passed. */
    public void update(long elapsedTime) { /* do nothing  */ }


    /** Draws to the screen. Subclasses must override this method. */
    public abstract void draw(Graphics2D g);
}
