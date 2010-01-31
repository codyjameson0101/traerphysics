/*
 * May 29, 2005
 */

package traer.physics;

/**
 * This class implements TwoBodyForce to represent a Spring with a spring constant, damping factor, and ideal length.
 * @author Jeffrey Traer Bernstein
 * @author Carl Pearson
 *
 */
public class Spring extends TwoBodyForce {

	/**
	 * Construct a Spring
	 * @param oneEnd the Particle on one end
	 * @param theOtherEnd the Particle on the other end
	 * @param ks the spring constant
	 * @param d the damping constant
	 * @param r the ideal length
	 * @throws TwoBodyForceNullPointerException if either Particle is null
	 */
	public Spring( Particle oneEnd, Particle theOtherEnd, float ks, float d, float r )
		throws NullPointerException {
		super(oneEnd,theOtherEnd);
		setStrength(ks);
		setDamping(d);
		setRestLength(r);
	}
  
	/**
	 * Gets the current Spring length; uses the end particle positions to do so.
	 * @return the current Spring length
	 */
	public final float
  		currentLength()
  		{ return getOneEnd().position().distanceTo( getTheOtherEnd().position() ); }


	private float restLength;

	/**
	 * Gets the Spring rest length
	 * @return the Spring rest length
	 */
	public final float
		restLength()
  		{ return restLength; }
	
	/**
	 * Sets the Spring rest length, returning the modified Spring
	 * @param l the new rest length
	 * @return this Spring, modified
	 */
	public final Spring
		setRestLength( float l )
		{ restLength = l; return this; }

	private float springConstant;
	/**
	 * Gets this Spring's spring constant
	 * @return the spring constant
	 */
	public final float
		strength()
		{ return springConstant; }
	/**
	 * Sets this Spring's spring constant, returns the modified Spring
	 * @param ks the new spring constant
	 * @return this Spring, modified
	 */
	public final Spring
		setStrength( float ks )
		{ springConstant = ks; return this; }

	private float damping;
	/**
	 * Get this Spring's damping constant.
	 * @return the damping constant
	 */
	public final float
		damping()
		{ return damping; }
	/**
	 * Set this Spring's damping constant, and return the modified Spring
	 * @param d the new damping constant
	 * @return this Spring, modified
	 */
	public final Spring 
		setDamping( float d )
		{ damping = d; return this; }
    
	/**
	 * Provides the spring forces on oneEnd and theOtherEnd
	 * @return the spring forces
	 */
	@Override public ForcePair forcePair() {	
		Vector3D springForce = Vector3D
			.subtract(getOneEnd().position(), getTheOtherEnd().position()); // first, set the spring force equal to the distance between the particles
		springForce
			.length(-(springForce.length()-restLength)) // set the spring force equal to the negative difference between the distance and the ideal length
			.multiplyBy(springConstant);				// scale that by the spring constant
		
		Vector3D dampingForce = Vector3D
			.subtract(getOneEnd().velocity(), getTheOtherEnd().velocity())	// first, set the damping force equal to the difference between the velocities
			.projectOnto(springForce)										// project that force in the direction of the spring, i.e. - the velocity along the spring
			.multiplyBy(-damping);											// scale to the damping factor

		springForce.add(dampingForce);	//combine the spring and damping forces
		return equalAndOpposite(springForce); //apply the springForce to oneEnd, and -springForce to theOtherEnd
  }

}