package traer.physics;

/**
 * TwoBodyForce is {@link Force} between two specified {@link Particle}s, which extends {@link TargetedForce} and simplifies implementation of custom Forces.
 * <p>
 * A TwoBodyForce deals with all of the mechanics of getting at the two "ends" of Force and reduces the details of what a user needs to specify down to creating
 * the force {@link Vector3D}s that act on the ends.  If the forces are of the standard equal-and-opposite variety, only the force on
 * one end needs to be provided.
 * <p>
 * Example uses of this framework can be seen in the {@link Attraction} and {@link Spring} classes,
 * which only both with the actual calculation of their respective forces.
 * @author Carl Pearson
 */
public abstract class TwoBodyForce extends TargetedForce {
	
	/**
	 * Constructor with the two specified end {@link Particle}s.  Turns on by default.  Neither {@link Particle} is
	 * mutated by this operation, though later use of the {@link #apply()} method will mutate the {@link Particle#force} vector.
	 * <p>
	 * For Developers: the {@link Particle}s are null-checked, then delegated to the {@link #directSetOneEnd(Particle)} and {@link #directSetTheOtherEnd(Particle)} methods.
	 * @param oneEnd the one end {@link Particle}, cannot be null
	 * @param theOtherEnd the other end {@link Particle}, cannot be null
	 * @throws NullPointerException if either {@link Particle}<code> ==null</code>.
	 */
	protected TwoBodyForce(final Particle oneEnd, final Particle theOtherEnd)
	throws NullPointerException {
		super();
		thrower(oneEnd,theOtherEnd);
		directSetOneEnd(oneEnd);
		directSetTheOtherEnd(theOtherEnd);
	}
	
	// --------------------------------------- ONE END PARTICLE ------------------------------ //
	
	/**
	 * One {@link Particle} in the intertwined pair; classes extending this one should use {@link #getOneEnd()}
	 * to access it.
	 */
	private Particle oneEnd;
	/**
	 * Sets {@link #oneEnd} to <code>p</code>.
	 * <p>
	 * For developers: delegates to {@link #directSetOneEnd(Particle)} after checking <code>p!=null</code>.
	 * @param p one desired end of this Force; cannot be null
	 * @return this
	 * @throws NullPointerException if <code>p==null</code>
	 */
	protected TwoBodyForce setOneEnd(final Particle p)
	throws NullPointerException {
		return (p!=null) ? 	directSetOneEnd(p) :
							thrower("Argument p is null.");
	}
	
	/**
	 * Private setter method; only to be used when <code>p!=null</code> is already known
	 * @param p the new {@link #oneEnd}
	 * @return this
	 */
	private TwoBodyForce directSetOneEnd(final Particle p) { oneEnd = p; return this; }
	
	/**
	 * Gets {@link #oneEnd}.  Mutating this {@link Particle} is permitted.
	 * @return {@link #oneEnd}
	 */
	public final Particle getOneEnd() { return oneEnd; }
	
	// --------------------------------------- OTHER END PARTICLE ------------------------------ //

	/**
	 * The Other {@link Particle} in the intertwined pair
	 */
	private Particle theOtherEnd;
	/**
	 * Sets {@link #theOtherEnd} to <code>p</code>.
	 * <p>
	 * For developers: delegates to {@link #directSetTheOtherEnd(Particle)} after checking <code>p!=null</code>.
	 * @param p the other desire end of this Force; cannot be null.
	 * @return this TwoBodyForce
	 * @throws NullPointerException if Particle p is null
	 */
	protected TwoBodyForce setTheOtherEnd( final Particle p )
	throws NullPointerException {
		return (p!=null) ?	directSetTheOtherEnd(p):
							thrower("Argument p is null.");
	}	

	/**
	 * Private setter method; only to be used when <code>p!=null</code> is already known
	 * @param p the new {@link #theOtherEnd}
	 * @return this
	 */
	private TwoBodyForce directSetTheOtherEnd(final Particle p) { theOtherEnd = p; return this; }
	/**
	 * Gets {@link #theOtherEnd}.  Mutating this {@link Particle} is permitted.
	 * @return {@link #theOtherEnd}
	 */
	public final Particle getTheOtherEnd() { return theOtherEnd; }
	
	// ------------------------------------- USING THE FORCE --------------------------------- //
	
	/**
	 * Applies this Force to {@link #oneEnd} and {@link #theOtherEnd}, which modifies their {@link Particle#force} values.
	 * <p>
	 * Users extending this class to create custom Forces need only implement
	 * {@link #forcePair()}, taking advantage of the static packaging method {@link #equalAndOpposite(Vector3D)}
	 * or {@link #specifyBoth(Vector3D, Vector3D)} to create the {@link ForcePair}.
	 * @return this
	 */
	@Override public TwoBodyForce apply() {
		if (isOn() && (oneEnd.isFree() || theOtherEnd.isFree())) {
			ForcePair fp = forcePair();
			if ( oneEnd.isFree() ) oneEnd.addForce( fp.forceOnOneEnd() );
			if ( theOtherEnd.isFree() ) theOtherEnd.addForce( fp.forceOnTheOtherEnd() );
		}
		return this;
	}
		
	/**
	 * This is the method which must be overridden implementing classes.  Static methods for
	 * constructing a {@link ForcePair} are provided within this class: {@link #equalAndOpposite(Vector3D)}
	 * or {@link #specifyBoth(Vector3D, Vector3D)}.
	 * @return a {@link ForcePair} specifying the forces to apply to {@link #oneEnd} and {@link #theOtherEnd}.
	 */
	protected abstract ForcePair forcePair();

	/**
	 * This method creates a {@link ForcePair} from two not necessarily equal and opposite forces.
	 * @param forceOnOneEnd the force to be applied to {@link #oneEnd}
	 * @param forceOnTheOtherEnd the force to be applied to {@link #theOtherEnd}
	 * @return the appropriate ForcePair
	 * @throws NullPointerException if either {@link Vector3D}<code>==null</code>
	 */
	protected static ForcePair specifyBoth(final Vector3D forceOnOneEnd, final Vector3D forceOnTheOtherEnd)
	throws NullPointerException { 
		ForcePair.thrower(forceOnOneEnd, forceOnTheOtherEnd);
		return new ForcePair(forceOnOneEnd, forceOnTheOtherEnd);
	}
	
	/**
	 * Use this method when the forces to be applied are equal and opposite.  Only the force on oneEnd needs to be specified.
	 * @param forceOnOneEnd the force on oneEnd
	 * @return the ForcePair
	 * @throws NullPointerException if forceOnOneEnd is null
	 */
	protected static ForcePair equalAndOpposite(final Vector3D forceOnOneEnd)
	throws NullPointerException {
		if (forceOnOneEnd == null) ForcePair.thrower("Argument forceOnOneEnd == null.");
		return new ForcePair(forceOnOneEnd);
	}

	/**
	 * This class wraps Vector3D forces to apply to the two ends of this TwoBodyForce.
	 * <p>
	 * For the especially memory conscious, the ForcePair fields can be manipulated directly;
	 * use the {@link #updateBoth(Vector3D, Vector3D)} or {@link #updateEqualAndOpposite(Vector3D)} or use the getters and manipulate the
	 * Vector3Ds directly.
	 * @author Carl Pearson
	 */
	protected static class ForcePair {
		/**
		 * The Vector3D representation of the force on the oneEnd Particle.
		 */
		private Vector3D forceOnOneEnd;
		/**
		 * The Vector3D representation of the force on the otherEnd Particle.  This is null in the case of ForcePair created as equal
		 * and opposite.
		 */
		private Vector3D forceOnTheOtherEnd;
		/**
		 * Whether or not a ForcePair is equal and opposite; set permanently at instantiation
		 */
		private final boolean equalAndOpposite;

		/**
		 * Constructor for the not equal-and-opposite case.
		 * @param forceOnOneEnd the force on the oneEnd particle; cannot be null
		 * @param forceOnTheOtherEnd the force on the otherEnd particle; cannot be null
		 */
		private ForcePair(final Vector3D forceOnOneEnd, final Vector3D forceOnTheOtherEnd) {
			this.forceOnOneEnd = forceOnOneEnd;
			this.forceOnTheOtherEnd = forceOnTheOtherEnd;
			equalAndOpposite = false;
		}
		
		/**
		 * Constructor for the equal-and-opposite case.
		 * @param forceOnOneEnd the force on the oneEnd particle; cannot be null
		 */
		private ForcePair(final Vector3D forceOnOneEnd) {
			this.forceOnOneEnd = forceOnOneEnd;
			equalAndOpposite = true;
		}
		
		/**
		 * Gets the force on oneEnd.
		 * @return the force on oneEnd
		 */
		protected Vector3D forceOnOneEnd() { return forceOnOneEnd; }

		/**
		 * Gets the force on theOtherEnd.  Changing this Vector3D will not mutate the forceOnOneEnd, even in the equal-and-opposite case.
		 * @return the force on theOtherEnd
		 */
		protected Vector3D forceOnTheOtherEnd() { return equalAndOpposite ? forceOnOneEnd.copy().multiplyBy(-1f): forceOnTheOtherEnd; }
		
		/**
		 * Sets the force on oneEnd according to forceOnOneEnd argument, and the force on theOtherEnd as equal and opposite.
		 * @param forceOnOneEnd the force on oneEnd
		 * @return this ForcePair, modified
		 * @throws NullPointerException if forceOnOneEnd is null
		 */
		protected ForcePair
			updateEqualAndOpposite(Vector3D forceOnOneEnd)
			throws NullPointerException, IllegalStateException {
			if (!equalAndOpposite) throw new IllegalStateException("This ForcePair was not setup as an equal and opposite force pair.");
			if (forceOnOneEnd == null) {
				return thrower("The forceOnOneEnd Vector3D is null.");
			} else {
				this.forceOnOneEnd = forceOnOneEnd;
				return this;
			}
		}
		
		/**
		 * Updates the force for both ends according to the parameters.
		 * @param forceOnOneEnd the force on oneEnd Particle
		 * @param forceOnTheOtherEnd the force on theOtherEnd Particle
		 * @return this ForcePair, modified
		 * @throws NullPointerException if either argument is null
		 */
		protected ForcePair updateBoth(Vector3D forceOnOneEnd, Vector3D forceOnTheOtherEnd)
		throws NullPointerException {
			thrower(forceOnOneEnd, forceOnTheOtherEnd);
			this.forceOnOneEnd = forceOnOneEnd;
			this.forceOnTheOtherEnd = forceOnTheOtherEnd;
			return this;
		}
		
		private static void thrower(Vector3D forceOnOneEnd, Vector3D forceOnTheOtherEnd)
		throws NullPointerException {
			if (forceOnOneEnd==null || forceOnTheOtherEnd==null) {
				if (forceOnOneEnd==null && forceOnTheOtherEnd==null) { throw new NullPointerException("Both Vector3D forces are null."); }
				else if (forceOnOneEnd==null) { throw new NullPointerException("The forceOnOneEnd Vector3D is null."); }
				else { throw new NullPointerException("The forceOnTheOtherEnd Vector3D is null."); }
			}
		}
		
		private static ForcePair thrower(String message) { throw new NullPointerException(message); }

	}
	
	// ------------------------------------------ ON/OFF OVERRIDES --------------------------------- //
	
	/**
	 * Overrides the AbstractForce methods to return a TwoBodyForce instead.  Delegates to the local {@link #turnOn(boolean)}.
	 * @return this TwoBodyForce
	 */
	@Override public TwoBodyForce turnOff() { return turnOn(false); }

	/**
	 * Overrides the AbstractForce methods to return a TwoBodyForce instead.  Delegates to the local {@link #turnOn(boolean)}.
	 * @return this TwoBodyForce
	 */
	@Override public TwoBodyForce turnOn() { return turnOn(true); }
	
	/**
	 * Overrides the AbstractForce methods to return a TwoBodyForce instead.  Delegates to {@link AbstractForce#turnOn(boolean)}.
	 * @return this TwoBodyForce
	 */
	@Override public TwoBodyForce turnOn(boolean on) { super.turnOn(on); return this; }

	// -------------------------------------------- CONVENIENCE EXCEPTION METHODS --------------------------- //
	
	/**
	 * Local convenience method for throwing NullPointerExceptions for Particles.
	 * @param oneEnd one Particle to check
	 * @param theOtherEnd the other Particle to check
	 * @return true if no exception is thrown
	 */
	private static boolean thrower(Particle oneEnd, Particle theOtherEnd) {
		if (oneEnd==null || theOtherEnd==null) {
			if (oneEnd==null && theOtherEnd==null) { throw new NullPointerException("Both end Particles are null."); }
			else if (oneEnd==null) { throw new NullPointerException("The oneEnd Particle is null."); }
			else { throw new NullPointerException("The theOtherEnd Particle is null."); }
		}
		return true;
	}
	
	/**
	 * Convenience method for throwing NullPointerExceptions.  Return type specified to allow use in ternary (?:) expressions.
	 * @param message the message in the resulting NullPointerException
	 * @return nothing; return type simply for syntax compatibility with ternary expressions
	 */
	private static TwoBodyForce thrower(String message) { throw new NullPointerException(message); }
	
}
