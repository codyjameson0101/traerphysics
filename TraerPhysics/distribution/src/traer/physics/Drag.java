package traer.physics;

public class Drag extends Function<Particle,Particle> {
	public static final float DEFAULT_DRAG = 0.01f;

	public Drag() { this(DEFAULT_DRAG); }
	public Drag(float drag) { this.drag = drag; }
	
	public Drag setDrag(float drag) { this.drag = drag; return this; }
	
	private float drag;
	@Override
	public Particle apply(Particle p) {
		p.force().add(p.velocity().copy().multiplyBy(-drag));
		return p;
	}
}
