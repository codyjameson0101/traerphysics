package traer.physics;

/**
 * A {@link Force} that is applied to {@link Particle}s at the user's discretion, and hence does not
 * support the {@link Force#apply()} method.
 * <p>
 * User's wishing to create custom Forces of this kind should extend this class, implementing the {@link Force#apply(Particle)} method only.
 * <p>
 * Examples of this kind of Force are the {@link Gravity} and {@link Drag} classes.
 * @author Carl Pearson
 */
public abstract class UniversalForce extends AbstractForce {

	/**
	 * This type of {@link Force} must have a target {@link Particle};
	 * an exception will be thrown by this method.
	 * @return irrelevant, this method will always throw an exception
	 * @throws UnsupportedOperationException this Force is guaranteed to throw this exception
	 */
	@Override public AbstractForce apply() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This Force must be applied to a Particle.  The apply() is not supported by this Force.");
	}

}
