
package traer.physics;

/**
 * This interface specifies the basic mechanics of a Force.
 * <p>
 * There are two basic skeletal implementations: a {@link TargettedForce} with specified targets, where the {@link #apply()} method is used;
 * and a {@link UniversalForce} that is applied to a {@link Particle}, where the {@link #apply(Particle)} method is used.
 * <p>
 * The {@link Spring} force between two Particles is an example of the first form,
 * while the universal free-fall {@link Gravity} is an example of the second form.
 * <p>
 * An intermediate skeletal implementation of this interface is provided in the {@link AbstractForce} class.  Unless substantial extra behavior
 * associated with turning a Force on and off needs to be specified, in general users should extend the AbstractForce class instead of
 * implementing this interface.
 * @author Jeffrey Traer Bernstein
 * @author Carl Pearson
 * @since January 2008
 */
public interface Force {
	
	/**
	 * Turns the Force off; after calling this method, calls to {@link #isOff()} will return <code>true</code>.  Implementations of the
	 * Force interface should delegate this call to the {@link #turnOn(boolean)} method, with a <code>false</code> argument.
	 * @return this Force, de-activated
	 */
	public Force turnOff();

	/**
	 * Turns the Force on; after calling this method, calls to {@link #isOn()} will return <code>true</code>.  Implementations of the
	 * Force interface should delegate this call to the {@link #turnOn(boolean)} method, with a <code>true</code> argument.
	 * @return this Force, activated
	 */
	public Force turnOn();

	/**
	 * Sets the Force to the argument <code>on</code>.  Subsequent calls to {@link #isOn()} will return the value of <code>on</code>.
	 * Implementations of the Force interface should delegate {@link #turnOff()} and {@link #turnOn()} to this method.
	 * @param on the state to set the Force in.
	 * @return this Force, with appropriate activation condition
	 */
	public Force turnOn(boolean on);
		
	/**
	 * Returns <code>true</code> if this force is on.  Will also consistently return !{@link #isOff()}.  Implementations of the Force
	 * interface should delegate the behavior of one of these methods to the other, to ensure consistency.
	 * @return <code>true</code> if this force is on
	 */
	public boolean isOn();

	/**
	 * Returns <code>true</code> if this force is off.  Will also consistently return !{@link #isOn()}.  Implementations of the Force
	 * interface should delegate the behavior of one of these methods to the other, to ensure consistency.
	 * @return <code>true</code> if this force is off
	 */
	public boolean isOff();
	
	/**
	 * Applies this Force.
	 * @return this Force
	 * @throws UnsupportedOperationException if the implementing class uses {@link #apply(Particle)} instead.
	 * @throws IllegalStateException optionally, if the Force is currently off
	 */
	public abstract Force apply() throws UnsupportedOperationException, IllegalStateException;
	
	/**
	 * Applies this Force to a Particle p.  Must be specified by implementing classes.
	 * @param p the Particle to apply the Force to; may not be null
	 * @return the Particle p, after the Force is applied.
	 * @throws UnsupportedOperationException if the implementing class uses {@link #apply()} instead.
	 * @throws IllegalStateException optionally, if the Force is currently off, or if the Particle is fixed
	 * @throws NullPointerException if <code>p == null</code>
	 */
	public abstract Particle apply(Particle p) throws UnsupportedOperationException, IllegalStateException, NullPointerException;
}
