package traer.physics;

public class Gravity extends Function<Particle,Particle> {
	public static final float DEFAULT_GRAVITY = 0f;

	public Gravity() { this(DEFAULT_GRAVITY); }
	public Gravity(float g) { this(0,g,0); }
	public Gravity(float gx, float gy, float gz) { gravity = Vector3D.of(gx,gy,gz); }
	public Gravity(Vector3D g) { gravity = g.copy(); }
	
	public Gravity setGravity(float g) { return setGravity(0,g,0); }
	public Gravity setGravity(float gx, float gy, float gz) { gravity.set(gx,gy,gz); return this; }
	public Gravity setGravity(Vector3D g) { gravity.set(g); return this; }
	
	private Vector3D gravity;
	@Override
	public Particle apply(Particle p) {
		p.force().add(gravity);
		return p;
	}
}
