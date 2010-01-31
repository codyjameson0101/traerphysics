package traer.physics;

/**
 * A {@link Force} that has it's subject {@link Particle}s already specified, and hence does not
 * support the {@link Force#apply(Particle)} method.
 * <p>
 * User's wishing to create custom Forces of this kind should extend this class, implementing the {@link Force#apply()} method only.
 * <p>
 * Examples of this kind of Force are the {@link TwoBodyForce}s {@link Attraction} and {@link Spring} forces 
 * @author Carl Pearson
 */
public abstract class TargetedForce extends AbstractForce {

	/**
	 * This type of {@link Force} does not support application to any Particle p;
	 * an exception will be thrown by this method.
	 * @param p irrelevant, this method will always throw an exception
	 * @return irrelevant, this method will always throw an exception
	 * @throws UnsupportedOperationException this Force is guaranteed to throw this exception
	 */
	@Override public Particle apply(Particle p) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This Force applies to specific Particles, not any Particle.  The apply(p) is not supported by this Force.");
	}

}
