package traer.physics;

public class ModifiedEulerIntegrator extends Integrator {
	
	public ModifiedEulerIntegrator( ParticleSystem s ) { super(s); }
	
	public ModifiedEulerIntegrator step( float deltat )
	{
		s.clearForces();
		s.applyForces();
		
		float halftt = 0.5f*deltat*deltat;
		
		Vector3D a;
		for ( Particle p : s.particles() ) if (p.isFree()) {
				a = p.force().multiplyBy(1/p.mass());		
				p.position()
					.add( p.velocity().copy().multiplyBy(deltat) )
					.add( a.copy().multiplyBy(halftt) );
				p.velocity().add( a.multiplyBy(deltat) );
		}
		return this;
	}

}
