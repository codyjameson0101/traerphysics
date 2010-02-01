/*
 * May 29, 2005
 */

package traer.physics;

/**
 * Spring represents a physical spring by extending {@link TwoBodyForce} to calculate the force 
 * with a spring constant ({@link #ks}), damping factor ({@link #d}), and an ideal length ({@link #l}).
 * <p>
 * Thus, the positions of the {@link Particle}s on either end obey the equation:
 * ddot(r) = -k/m * (r-l) - d/m * dot(r)
 * @author Jeffrey Traer Bernstein
 * @author Carl Pearson
 *
 */
public class Spring extends TwoBodyForce {

	/**
	 * Constructor.
	 * <p>
	 * For Developers: all of the setting calls are delegated with the appropriate arguments.
	 * @param oneEnd the one end {@link Particle}; cannot be null
	 * @param theOtherEnd the other end {@link Particle}; cannot be null
	 * @param ks the spring constant
	 * @param d the damping constant
	 * @param l the ideal length
	 * @throws NullPointerException if either Particle is null, via {@link TwoBodyForce#TwoBodyForce(Particle, Particle)}
	 * @throws IllegalArgumentException if l <= 0, via {@link #setRestLength(float)}.
	 * @throws IllegalArgumentException if ks <= 0, via {@link #setStrength(float)}.
	 * @throws IllegalArgumentException if d < 0, via {@link #setDamping(float)}.
	 */
	public Spring( final Particle oneEnd, final Particle theOtherEnd, final float ks, final float d, final float l )
	throws NullPointerException, IllegalArgumentException {
		super(oneEnd,theOtherEnd);
		setStrength(ks);
		setDamping(d);
		setRestLength(l);
	}
  
	// ------------------------ SPRING LENGTH, IDEAL LENGTH ------------------------------ //
	
	/**
	 * Gets the current Spring length; uses the end particle positions to do so.
	 * @return the current Spring length
	 */
	public final float currentLength() {
		return getOneEnd().position().distanceTo( getTheOtherEnd().position() );
	}

	/**
	 * The ideal spring length; always > 0.
	 */
	private float l;

	/**
	 * Gets the ideal length, {@link #l}; always > 0.
	 * @return the ideal length
	 */
	public final float restLength() { return l; }
	
	/**
	 * Sets {@link #l}.
	 * @param l the new rest length; must be > 0.
	 * @return this
	 * @throws IllegalArgumentException if <code>l<=0</code>
	 */
	public final Spring setRestLength( final float l )
	throws IllegalArgumentException {
		if (l<=0) throw new IllegalArgumentException("Argument l <= 0; spring ideal length must be positive.");
		this.l = l; return this;
	}

	// ------------------------ SPRING STRENGTH ------------------------------------- //

	/**
	 * The Spring-force constant; always > 0.
	 */
	private float ks;
	/**
	 * Get {@link #ks}, the spring constant; always > 0.
	 * @return {@link #ks}, the spring constant
	 */
	public final float strength() { return ks; }
	/**
	 * Set {@link #ks}; must be > 0
	 * @param ks the new spring constant; must be > 0
	 * @return this
	 * @throws IllegalArgumentException if <code>ks<=0</code>
	 */
	public final Spring setStrength( final float ks )
	throws IllegalArgumentException {
		if (ks<=0) throw new IllegalArgumentException("Argument ks <= 0; the spring constant must be > 0.");
		this.ks = ks; return this;
	}
	
	// ------------------------ SPRING DAMPING ------------------------------------- //
	
	/**
	 * The damping constant; always >= 0
	 */
	private float d;
	/**
	 * Get {@link #d}; always >= 0.
	 * @return {@link #d}, the damping constant
	 */
	public final float damping() { return d; }
	/**
	 * Set {@link #d}; must be >= 0
	 * @param d the new damping constant
	 * @return this
	 * @throws IllegalArgumentException if <code>d<0</code>
	 */
	public final Spring setDamping( final float d )
	throws IllegalArgumentException {
		if (d<0) throw new IllegalArgumentException("Argument d is < 0; damping constant must be >= 0.");
		this.d = d; return this;
	}
    
	// ------------------------ FORCE PAIR IMPLEMENTATION ------------------------------------- //

	/**
	 * Provides the spring forces on oneEnd and theOtherEnd
	 * @return the spring forces
	 */
	@Override public ForcePair forcePair() {	
		Vector3D springForce = Vector3D
			.subtract(getOneEnd().position(), getTheOtherEnd().position()); // first, set the spring force equal to the distance between the particles
		springForce
			.length(-(springForce.length()-l)) // set the spring force equal to the negative difference between the distance and the ideal length
			.multiplyBy(ks);				// scale that by the spring constant
		
		Vector3D dampingForce = Vector3D
			.subtract(getOneEnd().velocity(), getTheOtherEnd().velocity())	// first, set the damping force equal to the difference between the velocities
			.projectOnto(springForce)										// project that force in the direction of the spring, i.e. - the velocity along the spring
			.multiplyBy(-d);											// scale to the damping factor

		springForce.add(dampingForce);	//combine the spring and damping forces
		return equalAndOpposite(springForce); //apply the springForce to oneEnd, and -springForce to theOtherEnd
  }

}