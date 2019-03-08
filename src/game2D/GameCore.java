package game2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.*;


/**
 * @author David Cairns
 * 
 * Core Game class that implements default game loop. Subclasses should
 * implement the draw() method and override the update method.
*/
public abstract class GameCore extends JFrame implements KeyListener {

	private static final long serialVersionUID = 1L;

	protected static final int FONT_SIZE = 12;
    
    private ScreenManager screen;		// A screen manager to use
    private DisplayMode displayMode;	// The required display mode

    private boolean isRunning;			// true if the game loop should continue
    private boolean fullScreen;			// true if the game is in full screen mode
    private	long startTime;				// The time the game started
    private long currTime;				// The current time
    private long elapsedTime;			// Elapsed time since previous check
    
    private long frames;				// Used to calculate frames per second (FPS)
    private Window win;					// Window object used to handle the display
    
    private BufferedImage buffer=null;	// buffer is used as a buffered image for drawing offscreen
    private Graphics2D bg=null;    		// The virtual Graphics2D device associated with the above image
    
    
    /**
     * Default constructor for GameCore
     *
     */
    public GameCore()
    {
    	isRunning = false;
    	fullScreen = false;
    	
        frames = 1;
        startTime = 1;
        currTime = 1;
    }



    /** 
     * Signals the game loop that it's time to quit 
     * 
     */
    public void stop() { isRunning = false; }


    /** 
     * Starts the game by first initialising the game via init()
     * and then calling the gameLoop()
     *
     * @param full True to set to fullscreen mode, false otherwise
     * @param x Width of screen in pixels
     * @param y Height of screen in pixels
     */
    public void run(boolean full, int x, int y) {
        try 
        {
            init(full,x,y);
            gameLoop();
        }
        finally 
		{ 
        	
        }
    }


    /**
     * Internal initialisation method.
     * 
     * @param full	True to start the game in full screen mode
     * @param xres	Width in pixels of game screen
     * @param yres	Height in pixels of game screen
     */
    private void init(boolean full, int xres, int yres) {
    	
    	fullScreen = full;
    	
    	if (fullScreen)
    	{
    		screen = new ScreenManager();
    		displayMode = new DisplayMode(xres,yres,32,0);
    		win = screen.getFullScreenWindow();
    	}
    	else
    	{
    		win = this;
            win.setSize(xres,yres);
    	}

        setVisible(true);
        
        win.addKeyListener(this);
        win.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
    }
    
    /**
     * Shows and hides the main game window
     * 
     * @param true to show the game window, false to hide
     * 
     */
    public void setVisible(boolean show)
    {
    	if (!fullScreen) 
    	{
    		super.setVisible(show);
    		return;
    	}

    	// Full screen only from here on
    	if (show)
    	{
            screen.setFullScreen(displayMode);
    		win = screen.getFullScreenWindow();
    	}
    	else
    	{
    		screen.restoreScreen();
    	}
    }

    /**
     * Loads an image with the given 'fileName'
     * 
     * @param fileName The file path to the image file that should be loaded 
     * @return A reference to the Image object that was loaded
     */
    public Image loadImage(String fileName) 
    { 
    	return new ImageIcon(fileName).getImage(); 
    }

    /**
     *  Runs through the game loop until stop() is called. 
     *  
     *  This method will call your update() method followed by your draw()
     *  method to display the updated game state. It implements double buffering
     *  for both full screen and windowed mode.
     */
    public void gameLoop() {
        startTime = System.currentTimeMillis();
        currTime = startTime;
        frames = 1;		// Keep a note of frames for performance measure

        Graphics2D g;
        isRunning = true;
        
        // Create our own buffer
        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        bg = (Graphics2D)buffer.createGraphics();
        bg.setClip(0, 0, getWidth(), getHeight());
        
        while (isRunning) {
            elapsedTime = System.currentTimeMillis() - currTime;
            currTime += elapsedTime;

            // Call the overridden update method
            update(elapsedTime);

	        // Get the current graphics device 	            
            g = (Graphics2D)win.getGraphics();
            
            
	        if (g != null)
	        {
 	            if (fullScreen)
	            {
	            	// Set the clipping (drawable) region to be the screen bounds
	            	g.setClip(0, 0, getWidth(), getHeight());
		            draw(g);
	            	screen.update();
	            	g.dispose();
	            }
	            else
	            {
	            	draw(bg);
	            	g.drawImage(buffer,null,0,0);
 	            }
            }
            else
            {
            	System.err.println("Null reference for graphics");
            	break;
            }
            
            frames++;

            // take a nap
            try {
                Thread.sleep(10);
            }
            catch (InterruptedException ex) { }
        }
        System.exit(0);
    }

    /**
     * @return The screen width in pixels
     */
    public int getWidth()
    {
    	if (fullScreen) 
    		return screen.getWidth();
    	else
    		return super.getWidth();
    }
    
    /**
     * @return The screen height in pixels
     */
    public int getHeight()
    {
    	if (fullScreen) 
    		return screen.getHeight();
    	else
    		return super.getHeight();
    }
    
    /**
     * @return The current frames per second (FPS)
     */
    public float getFPS()
    {
    	if (currTime - startTime <= 0) return 0.0f;
    	return (float)frames/((currTime - startTime)/1000.0f);
    }

    /**
     * Handles the keyReleased event to check for the 'Escape' key being
     * pressed. If you override this method, make sure you allow the user 
     * to stop the game.
     */
	public void keyReleased(KeyEvent e) 
	{ 
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) stop(); 
	}

	/**
	 * Handler for the keyPressed event (empty)
	 */
	public void keyPressed(KeyEvent e) { }
	
	/**
	 * Handler for the keyTyped event (empty)
	 */
	public void keyTyped(KeyEvent e) {	}
		
    /** 
     * Updates the state of the game/animation based on the
     * amount of elapsed time that has passed. You should
     * override this in your game class to do something useful.
     */
    public void update(long elapsedTime) { /* do nothing  */ }


    /** 
     * Subclasses must override this method to draw output to
     * the screen via the Graphics2D object 'g'.
     * 
     * @param g The Graphics2D object to draw with.
     */
    public abstract void draw(Graphics2D g);
}
