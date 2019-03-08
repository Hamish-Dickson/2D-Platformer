package game2D;

/**
	The Velocity class provides the ability to manipulate and specify a
	Velocity as a speed and direction. Using this class, you can
	create a velocity object with a particular speed and direction and
	then query the object to find our what the corresponding change in
	vertical and horizontal pixels per millisecond should be. These
	queries are achieved using the methods 'getdx' and 'getdy'.
	respectively.
	
	@author David Cairns
*/
public class Velocity {

	private	double angle;	// Angle of velocity in radians
	private double dangle;	// Angle expressed in degrees
	private double speed;	// Speed of change

	private double dx;		// Above values broken down into x and y changes
	private double dy;

	/**
		Initialise a default velocity with 0 speed and an
		angle of 0 degrees.
	*/
	public Velocity()
	{
		dx = 0.0f;
		dy = 0.0f;
		speed = 0.0f;
		angle = 0.0f;
		dangle = 0.0f;
	}

	/**
		Initialise a default velocity with a speed of 's' and an
		angle of 'a' degrees.
	*/
	public Velocity(double s, double a)
	{
		dx = 0.0f;
		dy = 0.0f;
		speed = s;
		dangle = a;
		reCalc();
	}

	/**
		Recalculates the dx and dy values for the current
		speed and angle. Automatically called whenever you
		change the speed or angle.
	*/
	private void reCalc()
	{
		// Get the value of the angle in radians
		angle = Math.toRadians(dangle);
		// Work out the change in pixels/millisecond in x direction
		dx = speed * Math.cos(angle);
		// Work out the change in pixels/millisecond in y direction
		dy = speed * Math.sin(angle);
	}

	/**
		Similar to the constructor. Set the velocity
		to a speed of 's' and an angle of 'a' degrees.
	*/
	public void setVelocity(double s, double a)
	{
		speed = s;
		dangle = a;
		angle = Math.toRadians(a);
		reCalc();
	}

	/**
		Set the current angle to 'a' degrees whilst keeping the same
		value for 'speed'.
	*/
	public void setAngle(double a)
	{
		dangle = a; // Angle in degree
		reCalc();
	}


	/**
		Set the current speed to 's' pixels/millisecond whilst keeping the same
		value for 'angle'.
	*/
	public void setSpeed(double s)
	{
		speed = s;
		reCalc();
	}

	/**
		Get the current angle in degrees.
	*/
	public double getAngle()
	{
		return dangle;
	}

	/**
		Get the current speed in pixels/millisecond.
	*/
	public double getSpeed()
	{
		return speed;
	}

	/**
		Add the velocity 'v' to this velocity to produce a new
		angle and direction.
	*/
	public void add(Velocity v)
	{
		dx += v.dx;
		dy += v.dy;

		speed = Math.sqrt((dx*dx) + (dy*dy));
		angle = Math.acos(dx/speed);
	}

	/**
		Subtract the velocity 'v' from this velocity to produce a new
		angle and direction.
	*/
	public void subtract(Velocity v)
	{
		dx -= v.dx;
		dy -= v.dy;

		speed = Math.sqrt((dx*dx) + (dy*dy));
		angle = Math.acos(dx/speed);
	}

	/**
		Get the current speed in the x direction in
		pixels/millisecond. You would normally call this
		method after changing the angle or speed to find
		out the corresponding x and y components of the
		current angle and speed.
	*/
	public double getdx() { return dx; }

	/**
		Get the current speed in the y direction in
		pixels/millisecond. You would normally call this
		method after changing the angle or speed to find
		out the corresponding x and y components of the
		current angle and speed.
	*/
	public double getdy() { return dy; }

}
