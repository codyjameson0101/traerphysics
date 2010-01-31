package traer.physics;

/**
 * A Class for representing a Particle; contains the Particle's position, velocity, and the force on the Particle as Vector3Ds.
 * The Particle also has a mass, and a fixed vs free state.
 * @author Jeffrey Traer Bernstein
 * @author Carl Pearson
 */
public class Particle {

	/**
	 * The default mass for the no-argument constructor.
	 */
	public static final float DEFAULT_MASS = 1.0f;
	
	/**
	 * Create a Particle with mass m.
	 * <p>
	 * For developers: delegates to {@link #setMass(float)}. 
	 * @param m the desired mass; may not be <= 0
	 * @throws IllegalArgumentException via {@link #setMass(float)} if m<=0
	 */
	public Particle( float m )
	throws IllegalArgumentException { 
		setMass(m);
	}
	
	/**
	 * Constructor for a Particle with {@link #DEFAULT_MASS}.
	 * <p>
	 * For Developers: delegates to {@link #Particle(float)} with argument {@link #DEFAULT_MASS}.
	 */
	public Particle() { this(DEFAULT_MASS); }

	/**
	 * Calculates the distance between this Particle and another.
	 * @param p the other Particle
	 * @return the distance between the two Particles
	 * @throws NullPointerException if p == null
	 */
	public final
	float distanceTo( Particle p )
		throws NullPointerException {
		if (p==null) throw new NullPointerException("The Particle p is null.");
		return this.position().distanceTo( p.position() );
	}

	/**
	 * Whether or not this Particle is fixed; initially set to false.
	 */
	protected boolean fixed = false;
	/**
	 * Fixes the Particle.  Delegates to {@link #setFixed(boolean)} with a true argument.
	 * @return this Particle, modified
	 */
	public final
	Particle makeFixed() { return setFixed(true); }
	/**
	 * Frees the Particle.  Delegates to {@link #setFixed(boolean)} with a false argument.
	 * @return this Particle, modified
	 */
	public final
	Particle makeFree() { return setFixed(false); }
	/**
	 * Sets the Particles fixity state to fixed.  Has the side-effect of clearing the velocity if fixed==true.
	 * @param fixed the new fixity state
	 * @return this Particle modified
	 */
	public final
	Particle setFixed(boolean fixed) {
		this.fixed = fixed;
		if (fixed) velocity.clear();
		return this;
	}
	/**
	 * Get the fixed state - returns true if the Particle is fixed, false if not
	 * @return the fixed state
	 */
	public final
	boolean isFixed() { return fixed; }
	/**
	 * Get the free state - returns false if the Particle is fixed, true if not.  Essentially, !{@link #isFixed()}.
	 * @return the fixed state
	 */	
	public final
	boolean isFree() { return !isFixed(); }

	// ---------------------------------- VELOCITY and POSITION ---------------------------------- //
	//TODO: provide the Vector3D mutators directly ala addForce()?
	
	/**
	 * The Vector3D position of the Particle; automatically allocated to 0,0,0 on creation
	 */
	protected Vector3D position = Vector3D.of();
	public final Vector3D position() { return position; }

	/**
	 * The Vector3D velocity of the Particle; automatically allocated to 0,0,0 on creation
	 */
	protected Vector3D velocity = Vector3D.of();
	public final Vector3D velocity() { return velocity; }

	// ------------------------------------------- MASS ------------------------------------------ //
	
	/**
	 * The Particle mass.
	 */
	private float mass;
	/**
	 * Get the Particle mass
	 * @return the mass; always > 0
	 */
	public final float mass() { return mass; }
	/**
	 * Sets the Particle mass to m and return the Particle; m must be > 0
	 * @param m the new mass; must be > 0.
	 * @return this Particle, mass updated
	 * @throws IllegalArgumentException if m<=0
	 */
	public final Particle setMass( float m ) {
		if (m<=0) throw new IllegalArgumentException("Particle mass must be >0.  Supplied m: "+m);
		mass = m;
		return this;
	}

	// ------------------------------------------- FORCE ------------------------------------------ //
	
	/**
	 * The Vector3D force on the Particle; automatically allocated on creation
	 */
	Vector3D force = Vector3D.of();
	/**
	 * Gets the Vector3D force on the Particle.  Using this to clear the force is deprecated; use {@link #clearForce()} instead.
	 * Using this to add force is deprecated; use {@link #addForce(Vector3D)} instead.
	 * @return the force on the Particle
	 */
	public final Vector3D force() { return force; }
	/**
	 * Updates the force on the Particle by adding the extra force.
	 * @param added the force to add to the current Particle force
	 * @return this Particle, force updated
	 * @throws NullPointerException via {@link Vector3D#add(Vector3D)} if added==null
	 */
	public final Particle addForce(Vector3D added)
		{ force.add(added); return this; }
	/**
	 * Clears the force on the Particle using {@link Vector3D#clear()}, then returns the Particle.
	 * @return this Particle, updated.
	 */
	public final Particle clearForce() { force.clear(); return this; }
	
	/**
	 * The age of the Particle.
	 */
	protected float age = 0;
	/**
	 * Gets the age of the Particle
	 * @return the current Particle age
	 */
	public final float age() { return age; }
  
	protected boolean dead = false;

	/**
	 * Sets age=0, dead=false, clears the position, velocity, force Vector3Ds, and mass = DEFAULT_MASS.
	 * @return the reset Particle
	 */
	public Particle reset() {
		age = 0;
		dead = false;
		position.clear();
		velocity.clear();
		force.clear();
		mass = DEFAULT_MASS;
		return this;
	}
	
}