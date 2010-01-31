package traer.physics;

public class ForwardEulerIntegrator extends Integrator
{
	
	public ForwardEulerIntegrator( ParticleSystem s ) { super(s); }
	
	public ForwardEulerIntegrator step( float deltat ) {
		// clear any residual forces, then apply all existing forces
		s.clearForces();
		s.applyForces();
		
		for ( Particle p : s.particles() ) if (p.isFree()) {							// for all free particles...
			p.position().add( Vector3D.multiplyBy(p.velocity(),deltat) );	// update position
			p.velocity().add( p.force().multiplyBy(deltat/p.mass() ) );		// update velocity
		}
		return this;
	}

}
