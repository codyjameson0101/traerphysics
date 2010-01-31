package traer.physics;

import java.util.*;

public class ParticleSystem {
	
	/**
	 * Public field to supply when setting integrator.
	 * @deprecated Expect to eventually use the enum values defined in the {@link Integrator} class.
	 */
	@Deprecated public static final int RUNGE_KUTTA = Integrator.METHOD.RUNGEKUTTA.ordinal();
	/**
	 * Public field to supply when setting integrator.
	 * @deprecated Expect to eventually use the enum values defined in the {@link Integrator} class.
	 */
	@Deprecated public static final int MODIFIED_EULER = Integrator.METHOD.MODEULER.ordinal();
	
	protected static final float DEFAULT_DRAG = 0.001f;	
	
	private List<Particle> 		particles		= new ArrayList<Particle>();
	private List<Spring> 		springs			= new ArrayList<Spring>();
	private List<Attraction> 	attractions		= new ArrayList<Attraction>();
	private List<AbstractForce> 		customForces	= new ArrayList<AbstractForce>();
	private Map<String,UniversalForce> uForces  = new HashMap<String,UniversalForce>();
	
	boolean hasDeadParticles = false;

	
	/**
	 * Convenience method for throwing NullPointerExceptions.
	 * @param o the object to test for null
	 * @param message the message to use, if o is null
	 * @throws NullPointerException if o is null, with message
	 */
	private static final void
		nullThrower(Object o, String message)
		throws NullPointerException
		{ if (o==null) throw new NullPointerException(message); }
	/**
	 * Convenience method for throwing IllegalArgumentExceptions.
	 * @param message the message to use
	 * @return convenience return type for use with ? : ; operator
	 * @throws IllegalArgumentException with message
	 */
	private static final ParticleSystem
		illegalArgThrower(String message)
		{ throw new IllegalArgumentException(message); }

	// -------------------------------------- TIME + INTEGRATION ----------------------------------------- //	
	
	/**
	 * The time step to use with {@link #tick()}; set to 1 by default.
	 */
	private float deltaT = 1f;
	/**
	 * Set the size of the time step used with {@link Integrator#step(float)} for this ParticleSystem.
	 * @param t the time step size
	 * @return this ParticleSystem, modified
	 * @throws IllegalArgumentException if t<=0
	 */
	public final ParticleSystem
		setDeltaT(float t)
		throws IllegalArgumentException {
		if (t<=0) throw new IllegalArgumentException("Argument t is "+t+"; t must be >=0.");
		deltaT = t; return this; }
	/**
	 * Gets the current time step size used with this ParticleSystem's Integrator.
	 * @return the time step size
	 */
	public final float
		getDeltaT()
		{ return deltaT; }
	/**
	 * Advances this ParticleSystem's Integrator by the local time step.  Uses {@link #tick(float)}.
	 * @return this ParticleSystem, post the advance.
	 */
	public final ParticleSystem tick() { return tick(deltaT); }
	/**
	 * Advances this ParticleSystem's Integrator by a user-specified time step.
	 * @param t the amount of time to advance
	 * @return this ParticleSystem, post the advance
	 * @throws IllegalArgumentException if t<=0
	 */
	public final ParticleSystem tick( float t ) {
		if (t<=0) throw new IllegalArgumentException("Argument t is "+t+"; t must be >=0.");
		integrator.step( t );
		return this;
	}
	
	private Integrator integrator;
	/**
	 * Sets the integrator for this particle system based on the specified integrator.
	 * @deprecated Eventually, this method will be eliminated in favor of {@link #setIntegrator(traer.physics.Integrator.METHOD)}.
	 * @param integrator the int value corresponding to the desired Integrator
	 * @return this ParticleSystem, using the specified Integrator
	 * @throws IllegalArgumentException if the argument does not correspond to defined Integrator
	 */
	@Deprecated public final ParticleSystem 
		setIntegrator( int integrator )
		throws IllegalArgumentException {
		try {
			return setIntegrator(Integrator.METHOD.values()[integrator]);
		} catch (ArrayIndexOutOfBoundsException e) {
			return illegalArgThrower("Argument integrator does not correspond to a valid Integrator; consult Integrator class for valid options.");
		}
	}
	/**
	 * Sets the integrator for this particle system based on the specified integrator.
	 * Delegates to {@link ParticleSystem#setIntegrator(Integrator)} using the factory methods in the Integrator class.
	 * @param integrator the enum value corresponding to the desired integrator
	 * @return this ParticleSystem, using the specified Integrator
	 */
	public final ParticleSystem
		setIntegrator( Integrator.METHOD integrator )
		{ return setIntegrator(integrator.factory(this)); }
	/**
	 * Sets the integrator for this particle system based on the specified integrator.
	 * @param integrator the desired integrator
	 * @return this ParticleSystem, using the specified Integrator
	 * @throws NullPointerException if integrator==null
	 */
	public final ParticleSystem
		setIntegrator(Integrator integrator)
		throws NullPointerException {
		nullThrower(integrator, "Argument integrator is null in setIntegrator(integrator) call.");
		this.integrator = integrator;
		return this;
	}
	
	// -------------------------------------- GRAVITY ----------------------------------------- //	
	
	/**
	 * The gravity vector for this ParticleSystem.
	 */
	private Vector3D gravity;
	/**
	 * The default magnitude for the y-component of gravity.
	 */
	protected static final float DEFAULT_GRAVITY = 0;
	/**
	 * Sets the x, y, z components of the gravity Vector3D
	 * @param x the x component
	 * @param y the y component
	 * @param z the z component
	 * @return this ParticleSystem, gravity = (x,y,z)
	 */
	public final ParticleSystem
		setGravity( float x, float y, float z )
		{ gravity.set( x, y, z ); return this; }
	/**
	 * Sets gravity with 0,g,0 components.  Delegates to {@link #setGravity(float, float, float)}.
	 * @param g the y component
	 * @return this ParticleSystem, gravity = (0,g,0)
	 */
	public final ParticleSystem
		setGravity( float g )
		{ return setGravity( 0, g, 0 ); }

	// -------------------------------------- DRAG ----------------------------------------- //
	
	/**
	 * The drag magnitude for this ParticleSystem
	 */
	private float drag;
	/**
	 * Sets the drag component.
	 * @param d the drag factor; positive corresponds to physical drag
	 * @return this ParticleSystem, drag modified
	 */
	public final
	ParticleSystem setDrag( float d ) { drag = d; return this; }
    
	// -------------------------------------- PARTICLES ----------------------------------------- //	

	/**
	 * Makes a Particle in the ParticleSystem, and returns that Particle
	 * @param mass the new Particle mass
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @return the new Particle
	 */
	public final
	Particle makeParticle( float mass, float x, float y, float z ) {
		Particle p = new Particle( mass );
		p.position().set( x, y, z );
		particles.add( p );
		return p;
	}
	
	/**
	 * Makes a Particle with {@link Particle#DEFAULT_MASS} and position=(0,0,0).  Does not delegate to
	 * {@link #makeParticle(float, float, float, float)} to avoid the extra Vector3D operations.
	 * @return the new Particle
	 */
	public final
	Particle makeParticle() { 
		Particle p = new Particle();
		particles.add(p);
		return p;
	}
  
	/**
	 * Adds a custom Particle to the ParticleSystem, and returns the ParticleSystem.
	 * @param p the custom particle
	 * @return p; there are no side-effects to p
	 * @throws NullPointerException if p==null
	 */
	public final Particle
		makeParticle(Particle p) {
		nullThrower(p, "Argument p is null in makeParticle(p) call.");
		particles.add(p);
		return p;
	}
	
	// -------------------------------------- FORCES ----------------------------------------- //	
	public final Spring
  		makeSpring( Particle a, Particle b, float ks, float d, float r )
		throws NullPointerException {
		Spring s = new Spring( a, b, ks, d, r );
		springs.add( s );
		return s;
	}
	
	public final Attraction
		makeAttraction( Particle a, Particle b, float k, float minDistance )
		throws NullPointerException {
		Attraction m = new Attraction( a, b, k, minDistance );
		attractions.add( m );
		return m;
	}
	
	public final
		List<Spring> springs()
		{ return springs; }
	public final int
		numberOfSprings() { return springs.size(); }
	public final
		Spring getSpring( int i ) { return springs.get( i ); }
	public final Spring removeSpring( int i ) 	{ return springs.remove( i ); }
	public final ParticleSystem removeSpring( Spring a ) { springs.remove( a ); return this; }
	  
	public final List<Attraction> attractions() { return attractions; }
	public final int numberOfAttractions() { return attractions.size(); }  
	public final Attraction getAttraction( int i ) { return attractions.get( i ); }
	public final Attraction removeAttraction( int i  ) { return attractions.remove( i ); }
	public final ParticleSystem removeAttraction( Attraction s ) { attractions.remove( s ); return this; }

	public final List<AbstractForce> customForces() { return customForces; }  
	public final ParticleSystem addCustomForce( AbstractForce f ) { customForces.add( f ); return this; }
	public final int numberOfCustomForces() { return customForces.size(); }
	public final AbstractForce getCustomForce( int i ) { return customForces.get( i ); }
	public final AbstractForce removeCustomForce( int i ) { return customForces.remove( i ); }
	public final ParticleSystem removeCustomForce( AbstractForce f ) { customForces.remove( f ); return this; }

  
	public final void
		clear() {
		particles.clear();
		springs.clear();
		attractions.clear();
		customForces.clear();
	}
  
  public ParticleSystem( float g, float somedrag ) { this(0,g,0,somedrag); }
  
  public ParticleSystem( float gx, float gy, float gz, float somedrag ) {
	  setIntegrator(Integrator.METHOD.RUNGEKUTTA);
	  gravity = new Vector3D( gx, gy, gz );
	  setDrag(somedrag);
  }
  
  public ParticleSystem() {
	  this(ParticleSystem.DEFAULT_GRAVITY,ParticleSystem.DEFAULT_DRAG);
  }
  
  protected final void applyForces() {
	  if ( !gravity.isZero() ) {
		  for ( final Particle p : particles() ) p.addForce( gravity ).addForce( Vector3D.multiplyBy(p.velocity(), -drag) );
	  } else for ( final Particle p : particles() ) p.addForce( Vector3D.multiplyBy(p.velocity(), -drag) );
	  
	  for ( final Spring f : springs() ) f.apply();
	  for ( final Attraction f : attractions() ) f.apply();
	  for ( final AbstractForce f : customForces() ) f.apply();
  }
  
  protected final void clearForces() { for ( Particle p : particles ) p.clearForce(); }
    
  public final List<Particle> particles() { return particles; }
  public final int numberOfParticles() { return particles.size(); }
  public final Particle getParticle( int i ) { return particles.get( i ); }
  public final ParticleSystem removeParticle( Particle p ) { particles.remove( p ); return this; }
  
  
}