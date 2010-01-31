package traer.physics;

import java.util.*;

public class RungeKuttaIntegrator extends Integrator {
	
	Map<Particle,Vector3D> originalPositions;
	Map<Particle,Vector3D> originalVelocities;
	Map<Particle,Vector3D> k1Forces;
	Map<Particle,Vector3D> k1Velocities;
	Map<Particle,Vector3D> k2Forces;
	Map<Particle,Vector3D> k2Velocities;
	Map<Particle,Vector3D> k3Forces;
	Map<Particle,Vector3D> k3Velocities;
	Map<Particle,Vector3D> k4Forces;
	Map<Particle,Vector3D> k4Velocities;
	
	public RungeKuttaIntegrator( ParticleSystem s ) { super(s); }
		
	protected static final Function<Particle,?> kFunctor(final Map<Particle, Vector3D> kForces, final Map<Particle,Vector3D> kVelocities) {
		return new Function<Particle,Object>(){
			@Override public Object apply(Particle p) {
				kForces.put(p, p.force().copy());
				kVelocities.put(p, p.velocity().copy());
				p.clearForce();
				return null;
			}
		};
	}
	
	protected static final Function<Particle,?>
		kApplier(final Map<Particle,Vector3D> kForces, final Map<Particle,Vector3D> kVelocities,
				final Map<Particle,Vector3D> originalPositions,
				final Map<Particle,Vector3D> originalVelocities,
				final float deltaT) {
		return new Function<Particle,Object>() {
			@Override public Object apply(Particle p) {
				p.position().set(kVelocities.get(p)).multiplyBy(0.5f*deltaT).add(originalPositions.get(p));				
				p.velocity().set(kForces.get(p)).multiplyBy(0.5f*deltaT/p.mass()).add(originalVelocities.get(p));
				p.clearForce();
				return null;
			}
			
		};
	}
	
	protected Function<Particle,?>
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
		return new Function<Particle,Object>() {

			@Override public Object apply(Particle from) {
				from.age += deltaT;
				Vector3D originalPosition = originalPositions.get(from);
				Vector3D k1Velocity = k1Velocities.get( from ).multiplyBy(deltaT/6.0f);
				Vector3D k2Velocity = k2Velocities.get( from ).multiplyBy(deltaT/3.0f);
				Vector3D k3Velocity = k3Velocities.get( from ).multiplyBy(deltaT/3.0f);
				Vector3D k4Velocity = k4Velocities.get( from ).multiplyBy(deltaT/6.0f);
				
				from.position().set(originalPosition).add(k1Velocity).add(k2Velocity).add(k3Velocity).add(k4Velocity);
				
				// update velocity
				
				Vector3D originalVelocity = originalVelocities.get(from);
				Vector3D k1Force = k1Forces.get(from).multiplyBy(deltaT / ( 6.0f * from.mass() ));
				Vector3D k2Force = k2Forces.get(from).multiplyBy(deltaT / ( 3.0f * from.mass() ));
				Vector3D k3Force = k3Forces.get(from).multiplyBy(deltaT / ( 3.0f * from.mass() ));
				Vector3D k4Force = k4Forces.get(from).multiplyBy(deltaT / ( 6.0f * from.mass() ));
				
				from.velocity().set(originalVelocity).add(k1Force).add(k2Force).add(k3Force).add(k4Force);
				return null;
			}
			
		};
	}
	
	/**
	 * Instantiates originalPositions and originalVelocities.  Side-effect: clears forces on particles.
	 */
	protected final void allocateParticles() {
		originalPositions = new HashMap<Particle,Vector3D>();
		originalVelocities = new HashMap<Particle,Vector3D>();
		for (Particle p : s.particles() ) if (p.isFree()) {
			originalPositions.put(p, p.position().copy());
			originalVelocities.put(p, p.velocity().copy());
			p.clearForce();
		}
	}
	
	public RungeKuttaIntegrator step( float deltaT ) {	
		allocateParticles();
		
		/////////////////////////////////		
		// make k1
		s.applyForces(); // apply forces
		
		// instantiate maps
		k1Forces = new HashMap<Particle,Vector3D>();
		k1Velocities = new HashMap<Particle,Vector3D>();
		
		// side effect fill the builders, and clear forces
		Function.functor(originalPositions.keySet(), kFunctor(k1Forces,k1Velocities));
		
		////////////////		
		// make k2
		
		// side effect particle positions/velocities from k1
		Function.functor(originalPositions.keySet(), kApplier(k1Forces,k1Velocities,originalPositions,originalVelocities,deltaT));
		
		//apply forces
		s.applyForces();

		//reset builders
		k2Forces = new HashMap<Particle,Vector3D>();
		k2Velocities = new HashMap<Particle,Vector3D>();		
		
		//side effect fill builders and clear forces
		Function.functor(originalPositions.keySet(), kFunctor(k2Forces,k2Velocities));
				
		/////////////////////////////////////////////////////
		// get k3 values
		
		// side effect particle positions/velocities from k2
		Function.functor(originalPositions.keySet(), kApplier(k2Forces,k2Velocities,originalPositions,originalVelocities,deltaT));
		
		//apply forces
		s.applyForces();

		//reset builders
		k3Forces = new HashMap<Particle,Vector3D>();
		k3Velocities = new HashMap<Particle,Vector3D>();		
		
		//side effect fill builders and clear forces
		Function.functor(originalPositions.keySet(), kFunctor(k3Forces,k3Velocities));
		
		
		//////////////////////////////////////////////////
		// get k4 values
		
		// side effect particle positions/velocities from k2
		Function.functor(originalPositions.keySet(), kApplier(k3Forces,k3Velocities,originalPositions,originalVelocities,deltaT*2));
		
		//apply forces
		s.applyForces();

		//reset builders
		k4Forces = new HashMap<Particle,Vector3D>();
		k4Velocities = new HashMap<Particle,Vector3D>();		
		
		//side effect fill builders and clear forces
		Function.functor(originalPositions.keySet(), kFunctor(k4Forces,k4Velocities));
		
		
		/////////////////////////////////////////////////////////////
		// put them all together and what do you get?
		
		Function.functor(originalPositions.keySet(),
				updater(k1Forces, k1Velocities,
						k2Forces, k2Velocities,
						k3Forces, k3Velocities,
						k4Forces, k4Velocities,
						originalPositions, originalVelocities, deltaT));
		
		return this;
	}
	
}
