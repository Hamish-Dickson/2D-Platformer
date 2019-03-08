package game2D;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
    The Animation class manages a series of images (frames) and
    the amount of time to display each frame.
    
    @author David Cairns
*/
public class Animation {

    private ArrayList<AnimFrame> frames;	// The set of animation frames
    private int currFrameIndex;				// Current frame animation is on
    private long animTime;					// Current animation time
    private long totalDuration;				// Total animation time
    private float animSpeed = 1.0f;			// Animation speed, e.g. 2 will be twice as fast
    
    private boolean loop = true;			// True if the animation should continue looping
    private boolean looped = false;			// True if 1 animation loop has been completed
    private boolean play = true;			//	True if the animation should animate
    private int stopFrame = -1;				// A frame to stop on, if < 0 it is ignored

    /**
     * Creates a new, empty Animation.
     */
    public Animation() {
        frames = new ArrayList<AnimFrame>();
        totalDuration = 0;
        looped = false;
        start();
    }
    
    /**
     * Adds an image to the animation with the specified
     * duration (time to display the image).
     *   
     * @param image		The image to add
     * @param duration	The time it should be displayed for
     * 
     */
    public synchronized void addFrame(Image image, long duration)
    {
        totalDuration += duration;
        frames.add(new AnimFrame(image, totalDuration));
    }


    /**
     *  Starts this animation over from the beginning.
     */
    public synchronized void start() {
        animTime = 0;
        currFrameIndex = 0;
        looped = false;
    }

    /**
     * Updates this animation's current image (frame) based
     * on how much time has elapsed.
     * 
     * @param elapsedTime	Time that has elapsed since last call
     */
    public synchronized void update(long elapsedTime) {
    	
    	// If we are paused, don't update the animation
    	if (!play) return; 
    	
    	elapsedTime = (long)(elapsedTime * animSpeed);
    	
        if (frames.size() > 1) 
        {
            animTime += elapsedTime;

            if (animTime >= totalDuration) 
            {
            	if (loop)
            	{
	                animTime = animTime % totalDuration;
	                currFrameIndex = 0;
            	}
            	else
            	{
            		animTime = totalDuration;
            	}
            	looped = true;
            }

            while (animTime > getFrame(currFrameIndex).endTime) {
                currFrameIndex++;
            }
            
            // If we hit a stopFrame, pause the animation
            // It will be -1 if we should not stop at this point
            if (currFrameIndex == stopFrame)
            {
            	play = false;
            	stopFrame = -1;
            }
        }
    }


    /**
     * Gets this Animation's current image. Returns null if this
     * animation has no images.
     * 
     * @return The current image that should be displayed
     */
    public synchronized Image getImage() {
        if (frames.size() == 0) {
            return null;
        }
        else {
            return getFrame(currFrameIndex).image;
        }
    }

    /**
     * Works out which frame to display, incorporating
     * the offset.
     * 
     * @param i	The frame index to get
     * 
     * @return The animation frame corresponding to the index
     */
    private AnimFrame getFrame(int i) {
        return (AnimFrame)frames.get(i);
    }

    /**
     * Gets the image associated with frame 'i'. This may be
     * useful if you have loaded a set of images from a sprite
     * sheet and wish to inspect the images that have been loaded.
     * 
     * @param i	The index of the frame to request
     * @return	A reference to the image at index 'i'
     */
    public Image getFrameImage(int i) {
    	
    	if (i < 0 || i >= frames.size()) return null;
    	
    	AnimFrame frame = frames.get(i);
    	return frame.image;
    }
    
    /**
     * Tell an animation to loop continuously or not.
     * 
     * @param shouldLoop True if it should loop continuously.
     */
    public void setLoop(boolean shouldLoop)
    {
    	loop = shouldLoop;
    }
    
    /**
     * Has this animation looped once?
     * 
     * @return	True if it has looped once.
     */
    public boolean hasLooped() { return looped; }
    
    /**
     * Loads a complete animation from an animation sheet and adds each
     * frame in the sheet to the animation with the given frameDuration.
     * 
     * @param fileName	The path to the file to load the animations from
     * @param rows		How many rows there are in the sheet
     * @param columns	How many columns there are in the sheet
     * @param frameDuration	The duration of each frame
     */
    public void loadAnimationFromSheet(String fileName, int columns, int rows, int frameDuration)
    {
    	Image sheet = new ImageIcon(fileName).getImage();
    	Image[] images = getImagesFromSheet(sheet, columns, rows);
    	
    	for (int i=0; i<images.length; i++)
    	{
    		addFrame(images[i], frameDuration);
    	}
    }
    

    
    /**
     * Loads a set of images from a sprite sheet so that they can be added to an animation.
     * Courtesy of Donald Robertson.
     * 
     * @param sheet
     * @param rows
     * @param columns
     * @return
     */
    private Image[] getImagesFromSheet(Image sheet, int columns, int rows) {

        // basic method to achieve split of sprite sheet
        // overloading could be used to achieve more complex things 
    	// such as sheets where all images are not the same dimensions
        // deliberately 'overcommented' for clarity when integrating with
    	// main engine

        // initialise image array to return
        Image[] split = new Image[rows*columns];

        // easiest way to count as going through sprite sheet as though it is a 2d array
        int count = 0;

        // initialise width & height of split up images
        int width = sheet.getWidth(null)/columns;
        int height = sheet.getHeight(null)/rows;

        // for each column in each row
        for(int i = 0; i < rows; i++) 
        {
            for(int j = 0; j < columns; j++) 
            {
            	// create an image filter
            	// top left (x) = j*width, (y) = i*height
            	// extract rectangular region of width and height from origin x,y
            	ImageFilter cropper = new CropImageFilter(j*width,i*height, width, height);
            	
                // create image source based on original sprite sheet with filter applied
                // results in image source for cropped image being generated
                FilteredImageSource cropped = new FilteredImageSource(sheet.getSource(), cropper);
                
                // create a new image using generated image source and store in appropriate array element
                split[count] = Toolkit.getDefaultToolkit().createImage(cropped);
                        
                // increment count to prevent elements being overwritten
                count++;
            }
        }

        // return array
        return split;
    }

    /**
     * Pause the animation.
     */
    public void pause()
    {
    	play = false;
    }
    
    /**
     * Pause the animation at given 'frame'
     * @param frame
     */
    public void pauseAt(int frame)
    {
    	if ((frame < 0) || (frame >= frames.size())) 
    		stopFrame = 0;
    	else
    		stopFrame = frame; 
    }
    
    /**
     * Play the animation
     */
    public void play()
    {
    	play = true;
    }
    
    /**
     * Change the animation 'rate'. E.g. 2 would be twice as fast.
     * 
     * @param rate	The rate to animate at.
     */
    public void setAnimationSpeed(float rate)
    {
    	animSpeed = rate;
    }
    
    /**
     * Set the animation to frame 'f'.
     * 
     * @param f	The frame to shift to.
     */
    public void setAnimationFrame(int f)
    {
    	if (f < 0 || f >= frames.size()) return;
    	currFrameIndex = f;
    }
    
    /**
     * Private class to hold information about a given
     * animation frame.
     * 
     */
    private class AnimFrame {

        Image image;	// The image for a frame.
        long endTime;	// The time at which this frame ends.

        /**
         * Create a new frame with the given image and end time.
         * 
         * @param image		The image to use
         * @param endTime	The associated end time
         */
        public AnimFrame(Image image, long endTime) {
            this.image = image;
            this.endTime = endTime;
        }
    }
}
