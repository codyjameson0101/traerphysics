package traer.physics;

import java.util.*;

public class SettlingRungeKuttaIntegrator extends RungeKuttaIntegrator {
		
	public static final int DEFAULT_SETTLING_AGE = 50;
	private final int settlingAge;
	private final float epsilon = 0.0001f;

	public SettlingRungeKuttaIntegrator( ParticleSystem s, int settlingAge ) { super(s); this.settlingAge = settlingAge; }
	public SettlingRungeKuttaIntegrator( ParticleSystem s ) { this(s, DEFAULT_SETTLING_AGE); }
	
	@Override protected Function<Particle,?>
		updater(final Map<Particle,Vector3D> k1Forces,
				final Map<Particle,Vector3D> k1Velocities,
				final Map<Particle,Vector3D> k2Forces,
				final Map<Particle,Vector3D> k2Velocities,
				final Map<Particle,Vector3D> k3Forces,
				final Map<Particle,Vector3D> k3Velocities,
				final Map<Particle,Vector3D> k4Forces,
				final Map<Particle,Vector3D> k4Velocities,
				final Map<Particle,Vector3D> originalPositions,
				final Map<Particle,Vector3D> originalVelocities,
				final float deltaT) {
		final Function<Particle,?> superUpdater = super.updater(k1Forces,
				k1Velocities,
				k2Forces,
				k2Velocities,
				k3Forces,
				k3Velocities,
				k4Forces,
				k4Velocities,
				originalPositions,
				originalVelocities,
				deltaT);

		return new Function<Particle,Object>() {
			

			@Override public Object apply(Particle from) {
				superUpdater.apply(from);

				if (from.velocity().length()<epsilon) {
					from.age+=1;
				} else from.age=0;
				
				if (from.age > settlingAge) {
					from.makeFixed();
				}
				
				return null;
			}
			
		};
	}
		
}
