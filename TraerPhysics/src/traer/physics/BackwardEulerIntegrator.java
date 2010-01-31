package traer.physics;

public class BackwardEulerIntegrator extends Integrator
{
	
	public BackwardEulerIntegrator( ParticleSystem s ) { super(s); }
	
	public BackwardEulerIntegrator step( float deltat ) {
		// clear any residual forces, then apply all existing forces
		s.clearForces();
		s.applyForces();
		
		for ( Particle p : s.particles() ) if (p.isFree()) {							// for all free particles...
			p.velocity().add( p.force().multiplyBy(deltat/p.mass() ) );		// update velocity first
			p.position().add( Vector3D.multiplyBy(p.velocity(),deltat) );	// position based on new velocity
		}
		return this;
	}

}
