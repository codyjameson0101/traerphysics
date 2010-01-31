package traer.physics;

/**
 * A skeletal implementation of {@link Force} covering the methods concerned with the on/off state.
 * <p>
 * For the typical user wishing to make a custom Force, it is best to extend this class (or one of the
 * other skeletal implementations that meet the custom needs more specifically: {@link TargettedForce},
 * {@link UniversalForce}, or {@link TwoBodyForce}) and deal
 * only with defining the {@link Force#apply()} and {@link Force#apply(Particle)} methods. 
 * @author Carl Pearson
 * @author Jeffrey Traer Bernstein
 * @since 4.0
 */
public abstract class AbstractForce implements Force {
	
	/**
	 * Default constructor providing an <code>on</code> Force.
	 * <p>
	 * For developers: this constructor delegates to {@link #AbstractForce(boolean)} with a <code>true</code> argument.
	 */
	protected AbstractForce() { this(true); }
	
	/**
	 * Constructor; the on/off state is set to <code>on</code>
	 * @param on the initial state
	 */
	protected AbstractForce(boolean on) { this.on = on; }
	
	/**
	 * For developers: The internal state field; calls to {@link #isOn()}<code>==on</code>.  For small modifications to the on/off behavior in extending classes,
	 * the sub-classes should use the methods in this class (e.g., {@link #turnOn()}), hence this field is private and cannot be accessed directly.
	 */
	private boolean on;

	@Override public AbstractForce turnOff() { return turnOn(false); }

	@Override public AbstractForce turnOn() { return turnOn(true); }

	@Override public AbstractForce turnOn(boolean on) { this.on = on; return this; }
		
	@Override public boolean isOn() { return on; }

	@Override public boolean isOff() { return !isOn(); }
	
}
