package traer.physics;

public abstract class Integrator  {
	protected ParticleSystem s;
	public Integrator(ParticleSystem s) { this.s= s; }
	public abstract Integrator step( float t );
	public enum METHOD { 
		RUNGEKUTTA {
			@Override public Integrator factory(ParticleSystem physics) { return new RungeKuttaIntegrator(physics); }
		},
		EULER {
			@Override public Integrator factory(ParticleSystem physics) { return new BackwardEulerIntegrator(physics); }
		},
		MODEULER {
			@Override public Integrator factory(ParticleSystem physics) { return new ModifiedEulerIntegrator(physics); }
		},
		SRUNGEKUTTA {
			@Override public Integrator factory(ParticleSystem physics) { return new SettlingRungeKuttaIntegrator(physics); }
		}; 
		public abstract Integrator factory(ParticleSystem physics);
	};
}
