package game2D;

import java.awt.Image;
import java.awt.*;
import java.awt.geom.*;

/**
 * This class provides the functionality for a moving animated image or Sprite.
 * 
 * @author David Cairns
 *
 */
public class Sprite {

	// The current Animation to use for this sprite
    private Animation anim;		

    // Position (pixels)
    private float x;
    private float y;

    // Velocity (pixels per millisecond)
    private float dx;
    private float dy;

    // Dimensions of the sprite
    private float height;
    private float width;
    private float radius;

    // The scale to draw the sprite at where 1 equals normal size
    private double scale;
    // The rotation to apply to the sprite image
    private double rotation;

    // If render is 'true', the sprite will be drawn when requested
    private boolean render;
    
    // The draw offset associated with this sprite. Used to draw it
    // relative to specific on screen position (usually the player)
    private int xoff=0;
    private int yoff=0;

    /**
     *  Creates a new Sprite object with the specified Animation.
     *  
     * @param a The animation to use for the sprite.
     * 
     */
    public Sprite(Animation anim) {
        this.anim = anim;
        render = false;
        scale = 1.0f;
        rotation = 0.0f;
    }

    /**
     * Change the animation for the sprite to 'a'.
     *
     * @param a The animation to use for the sprite.
     */
    public void setAnimation(Animation a)
    {
    		anim = a;
    }
    
    /**
     * Set the current animation to the given 'frame'
     * 
     * @param frame The frame to set the animation to
     */
    public void setAnimationFrame(int frame)
    {
    	anim.setAnimationFrame(frame);
    }
    
    /**
     * Pauses the animation at its current frame. Note that the 
     * sprite will continue to move, it just won't animate
     */
    public void pauseAnimation()
    {
    	anim.pause();
    }
    
    /**
     * Pause the animation when it reaches frame 'f'. 
     * 
     * @param f The frame to stop the animation at
     */
    public void pauseAnimationAtFrame(int f)
    {
    	anim.pauseAt(f);
    }
    
    /**
     * Change the speed at which the current animation runs. A
     * speed of 1 will result in a normal animation,
     * 0.5 will be half the normal rate and 2 will double it.
     * 
     * Note that if you change animation, it will run at whatever
     * speed it was previously set to.
     * 
     * @param speed	The speed to set the current animation to.
     */
    public void setAnimationSpeed(float speed)
    {
    	anim.setAnimationSpeed(speed);
    }
    
    /**
     * Starts an animation playing if it has been paused.
     */
    public void playAnimation()
    {
    	anim.play();
    }
    
    /**
     * Returns a reference to the current animation
     * assigned to this sprite.
     * 
     * @return A reference to the current animation
     */
    public Animation getAnimation()
    {
    	return anim;
    }

    /**
        Updates this Sprite's Animation and its position based
        on the elapsedTime.
        
        @param The time that has elapsed since the last call to update
    */
    public void update(long elapsedTime) {
    	if (!render) return;
        x += dx * elapsedTime;
        y += dy * elapsedTime;
        anim.update(elapsedTime);
        width = anim.getImage().getWidth(null);
        height = anim.getImage().getHeight(null);
        if (width > height)
        	radius = width / 2.0f;
        else
        	radius = height / 2.0f;
    }

    /**
        Gets this Sprite's current x position.
    */
    public float getX() {
        return x;
    }

    /**
        Gets this Sprite's current y position.
    */
    public float getY() {
        return y;
    }

    /**
        Sets this Sprite's current x position.
    */
    public void setX(float x) {
        this.x = x;
    }

    /**
        Sets this Sprite's current y position.
    */
    public void setY(float y) {
        this.y = y;
    }

    public void shiftX(float shift)
    {
    	this.x += shift;
    }
    
    public void shiftY(float shift)
    {
    	this.y += shift;
    }
    
    /**
        Gets this Sprite's width, based on the size of the
        current image.
    */
    public int getWidth() {
        return anim.getImage().getWidth(null);
    }

    /**
        Gets this Sprite's height, based on the size of the
        current image.
    */
    public int getHeight() {
        return anim.getImage().getHeight(null);
    }

    /**
    	Gets the sprites radius in pixels
    */
    public float getRadius()
    {
    	return radius;
    }

    /**
        Gets the horizontal velocity of this Sprite in pixels
        per millisecond.
    */
    public float getVelocityX() {
        return dx;
    }

    /**
        Gets the vertical velocity of this Sprite in pixels
        per millisecond.
    */
    public float getVelocityY() {
        return dy;
    }

    /**
        Sets the horizontal velocity of this Sprite in pixels
        per millisecond.
    */
    public void setVelocityX(float dx) {
        this.dx = dx;
    }

    /**
        Sets the vertical velocity of this Sprite in pixels
        per millisecond.
    */
    public void setVelocityY(float dy) {
        this.dy = dy;
    }

	/**
		Set the scale of the sprite to 's'. If s is 1
		the sprite will be drawn at normal size. If 's'
		is 0.5 it will be drawn at half size. Note that
		scaling and rotation are only applied when
		using the drawTransformed method.
	*/
    public void setScale(float s)
    {
    	scale = s;
    }

	/**
		Get the current value of the scaling attribute.
		See 'setScale' for more information.
	*/
    public double getScale()
    {
    	return scale;
    }

	/**
		Set the rotation angle for the sprite in degrees.
		Note that scaling and rotation are only applied when
		using the drawTransformed method.
	*/
    public void setRotation(double r)
    {
    	rotation = Math.toRadians(r);
    }

	/**
		Get the current value of the rotation attribute.
		in degrees. See 'setRotation' for more information.
	*/
    public double getRotation()
    {
    	return Math.toDegrees(rotation);
    }

    /**
     	Stops the sprites movement at the current position
    */
    public void stop()
    {
    	dx = 0;
    	dy = 0;
    }

    /**
        Gets this Sprite's current image.
    */
    public Image getImage() {
        return anim.getImage();
    }

	/**
		Draws the sprite with the graphics object 'g' at
		the current x and y co-ordinates. Scaling and rotation
		transforms are NOT applied.
	*/
    public void draw(Graphics2D g)
    {
    	if (!render) return;

    	g.drawImage(getImage(),(int)x+xoff,(int)y+yoff,null);
    }

	/**
		Draws the sprite with the graphics object 'g' at
		the current x and y co-ordinates with the current scaling
		and rotation transforms applied.
		
		@param g The graphics object to draw to,
	*/
    public void drawTransformed(Graphics2D g)
    {
    	if (!render) return;

		AffineTransform transform = new AffineTransform();
		transform.translate(Math.round(x)+xoff,Math.round(y)+yoff);
		transform.scale(scale,scale);
		transform.rotate(rotation,getImage().getWidth(null)/2,getImage().getHeight(null)/2);
		// Apply transform to the image and draw it
		g.drawImage(getImage(),transform,null);
    }


	/**
		Hide the sprite.
	*/
    public void hide()  {	render = false;  }

	/**
		Show the sprite
	*/
    public void show()  {  	render = true;   }

	/**
		Check the visibility status of the sprite.
	*/
    public boolean isVisible() { return render; }

	/**
		Set an x & y offset to use when drawing the sprite.
		Note this does not affect its actual position, just
		moves the drawn position.
	*/
    public void setOffsets(int x, int y)
    {
    	xoff = x;
    	yoff = y;
    }
    

}
