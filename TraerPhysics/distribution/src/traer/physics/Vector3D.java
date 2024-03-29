package traer.physics;

/**
 * The Vector3D class used for representing 3D vectors and the operations on them.  Most operations change the underlying
 * Vector3D.  So if v1 = (1,1,1) = v2, after adding (0,0,1) to v1 via {@link #add(float, float, float)}, v1 != v2.  The one exception
 * is the {@link Vector3D#cross(Vector3D)} method, which always returns a new Vector3D.  Also, none of the operations alter their parameters, except in self-referential
 * conditions (e.g., <code>Vector3D.add(v1,v2,v1)</code> modifies v1).
 * <p>
 * The methods in the class are designed to allow "chaining"; given a Vector3D v1, it could be used as:
 * <code>
 * v1.add(1,2,3);<br>
 * v1.add(3,2,1);<br>
 * v1.multiplyBy(2);<br>
 * </code>
 * However, because the class is designed to allow chaining, those operations can also be done as:<br>
 * <code>
 * v1.add(1,2,3).add(3,2,1).multiplyBy(2);<br>
 * </code>
 * <p>
 * Of course, standard mathematical order-of-operations applies, so<br>
 * <code>
 * v1.add(1,2,3).add(3,2,1).multiplyBy(2) == v1.add(3,2,1).add(1,2,3).multiplyBy(2)<br> 
 * v1.add(1,2,3).add(3,2,1).multiplyBy(2) != v1.multiplyBy(2).add(1,2,3).add(3,2,1)<br> 
 * </code>
 * <p>
 * Finally, most of the methods throw NullPointerException if provided a null argument instead of Vector3D argument.  Setting the null
 * Vector3D to (0,0,0) was considered as an alternative, but seemed likely to cause bugs in use.
 * @author Jeffrey Traer Bernstein
 * @author Carl Pearson
 *
 */
public class Vector3D {

	/**
	 * The x component
	 */
	private float x;

	/**
	 * The y component
	 */
	private float y;

	/**
	 * The z component
	 */
	private float z;

	/**
	 * Construct a Vector3D with supplied x, y, z components.
	 * @param X the x component
	 * @param Y the y component
	 * @param Z the z component
	 */
	public Vector3D( final float X, final float Y, final float Z )
		{ x = X; y=Y; z=Z; }

	/**
	 * Static constructor; convenience method for chaining calls.  Replaces<br>
	 * <code>
	 * Vector3D v2 = (new Vector3D(x,y,z)).add(v1)...<br>
	 * </code>
	 * with<br>
	 * <code>
	 * Vector3D v2 = Vector3D.of(x,y,z).add(v1)...<br>
	 * </code>
	 * @param X the x component
	 * @param Y the y component
	 * @param Z the z component
	 * @return a new Vector3D, with the appropriate components
	 */
	public static final Vector3D of( final float X, final float Y, final float Z ) { return new Vector3D(X,Y,Z); }
	/**
	 * Version of {@link #of(float, float, float)} for zero vector.
	 * @return a new Vector3D (0,0,0)
	 */
	public static final Vector3D of() { return new Vector3D(0,0,0); }
	
	/**
	 * Construct a Vector3D copied from the supplied Vector3D
	 * @param p the source Vector3D
	 * @throws NullPointerException if p==null
	 */
	public Vector3D( final Vector3D p )
		throws NullPointerException {
		if (p!=null) { set(p.x, p.y, p.z); }
		else thrower("Argument Vector3D p cannot be null in constructor Vector3D(p).");
	}
	
	/**
	 * Construct a (0,0,0) Vector3D.
	 */
	public Vector3D()
		{ this(0,0,0); }
	/**
	 * Copies the provided Vector3D
	 * @param from the source Vector3D
	 * @return a new Vector3D with identical components to from
	 * @throws NullPointerException if from==null
	 */
	public final static Vector3D
		copy(Vector3D from)
		{ return new Vector3D(from); }
	/**
	 * Provides a copy of this Vector3D.
	 * @return a new Vector3D with identical components to this one
	 */
	public final Vector3D
		copy()
		{ return copy(this); }
	
	/**
	 * Throws a NullPointerException with the provided message.
	 * @param message the message
	 * @return convenience return type for use with ? : ; operator
	 */
	public static final Vector3D
		thrower(String message)
		throws NullPointerException {
		throw new NullPointerException(message);
	}
	
	/**
	 * Subtract the arguments from this vector's components.
	 * @param x the x component
	 * @param y the y component
	 * @param z the z component
	 * @return this, modified by subtraction
	 */
	public final Vector3D
		subtract( final float x, final float y, final float z )
		{ return set(x()-x, y()-y, z()-z); }
	/**
	 * Subtract the argument from this vector
	 * @param p the vector to subtract from this one; p is unmodified by this call
	 * @return this, modified by subtraction
	 * @throws NullPointerException if p==null
	 */
	public final Vector3D
		subtract( final Vector3D p ) throws NullPointerException
		{ return p!=null ? subtract(this, p, this) : thrower("Argument Vector3D p cannot be null in subtract(p)."); }
	/**
	 * Returns a new Vector3D, v1 - v2
	 * @param v1 the first vector; not modified by this operation
	 * @param v2 the second vector; not modified by this operation
	 * @return a new Vector3D, v1-v2
	 * @throws NullPointerException if either of v1 or v2 is null
	 */
	public final static Vector3D
		subtract( final Vector3D v1, final Vector3D v2) throws NullPointerException
		{ return subtract(v1,v2,null); }
	/**
	 * Returns v1-v2 in target, or a new Vector3D if target is null
	 * @param v1 the first vector; unmodified by this operation
	 * @param v2 the second vector; unmodified by this operation
	 * @param target the target vector, may be null
	 * @return target, or a new Vector3D
	 * @throws NullPointerException if either of v1 or v2 is null
	 */
	public final static Vector3D
		subtract(final Vector3D v1, final Vector3D v2, final Vector3D target) throws NullPointerException
		{ return 	v1!=null && v2!=null 	? target==null ? new Vector3D(v1.x-v2.x,v1.y-v2.y,v1.z-v2.z) : target.set(v1.x-v2.x,v1.y-v2.y,v1.z-v2.z) :
					v1==null && v2==null 	? thrower("Both Vector3D v1 and v2 are null in subtract(v1,v2,target).") :
					v1==null				? thrower("Vector3D v1 is null in subtract(v1,v2,target).") :
										 	  thrower("Vector3D v2 is null in subtract(v1,v2,target)."); }
	/**
	 * Add the arguments to this vector's components.  Relies on {@link #set(float, float, float)}.
	 * @param x the x component
	 * @param y the y component
	 * @param z the z component
	 * @return this, modified by addition
	 */
	public final Vector3D
		add( final float x, final float y, final float z )
		{ return set(x()+x, y()+y, z()+z); }
	/**
	 * Add the argument Vector3D's components to this Vector3D's components.  Uses {@link #add(Vector3D, Vector3D, Vector3D)}.
	 * @param p the Vector3D to be added to this one; unmodified by this operation
	 * @return this Vector3D, after modification
	 * @throws NullPointerException if p==null
	 */
	public final Vector3D
		add( Vector3D p )
		throws NullPointerException
		{ return p!=null ? add(this,p,this) : thrower("Argument p in add(p) is null."); }
	/**
	 * Creates a new Vector3D from v1+v2.  Uses {@link #add(Vector3D, Vector3D, Vector3D)}.
	 * @param v1 one Vector3D, unmodified by this operation
	 * @param v2 the other Vector3D, unmodified by this operation
	 * @return a new Vector3D, v1+v2
	 * @throws NullPointerException if either v1 or v2 is null
	 */
	public static Vector3D
		add(Vector3D v1, Vector3D v2)
		throws NullPointerException
		{ return add(v1,v2,null); }
	/**
	 * Returns v1+v2 in target, or a new Vector3D if target is null
	 * @param v1 the first vector; unmodified by this operation (unless also the target)
	 * @param v2 the second vector; unmodified by this operation (unless also the target)
	 * @param target the target vector, may be null
	 * @return target, or a new Vector3D
	 * @throws NullPointerException if either of v1 or v2 is null
	 */
	public static Vector3D
		add(Vector3D v1, Vector3D v2, Vector3D target)
		throws NullPointerException {
		return v1!=null && v2!=null ? target==null ? new Vector3D(v1.x+v2.x,v1.y+v2.y,v1.z+v2.z) : target.set(v1.x+v2.x,v1.y+v2.y,v1.z+v2.z) :
			   v1==null && v2==null ? thrower("Both Vector3D v1 and v2 are null in add(v1,v2,target).") :
			   v1==null				? thrower("Vector3D v1 is null in add(v1,v2,target).") :
								 	  thrower("Vector3D v2 is null in add(v1,v2,target).");
	}
		
	/**
	 * Gets the z component.
	 * @return the z component
	 */
	public final float
		z()
		{ return z; }
	/**
	 * Gets the y component.
	 * @return the y component
	 */
	public final float
		y()
		{ return y; }
	/**
	 * Gets the x component.
	 * @return the x component
	 */
	public final float
		x()
		{ return x; }
	
	/**
	 * Sets the x component and return this Vector3D after modification.
	 * @param X the new x component
	 * @return this Vector3D, after modification
	 */
	public final Vector3D
		setX( float X )
		{ x = X; return this; }
	/**
	 * Sets the y component and return this Vector3D after modification.
	 * @param Y the new y component
	 * @return this Vector3D, after modification
	 */
	public final Vector3D
		setY( float Y )
		{ y = Y; return this; }
	/**
	 * Sets the z component and return this Vector3D after modification.
	 * @param Z the new z component
	 * @return this Vector3D, after modification
	 */
	public final Vector3D
		setZ( float Z )
		{ z = Z; return this; }
	/**
	 * Sets all the components.  Relies on {@link #setX(float)}, {@link #setY(float)}, {@link #setZ(float)}.
	 * @param X the desired x component
	 * @param Y the desired y component
	 * @param Z the desired z component
	 * @return this Vector3D, after modification
	 */
	public final Vector3D
		set( float X, float Y, float Z )
		{ return setX(X).setY(Y).setZ(Z); }
	/**
	 * Sets this Vector3D components to those of another Vector3D.  Relies on {@link #set(float, float, float)}.
	 * @param p the other Vector3D
	 * @return this Vector3D, modified
	 * @throws NullPointerException if p==null
	 */
	public final Vector3D
		set( Vector3D p )
		throws NullPointerException
		{ return p!=null ? set(p.x, p.y, p.z) : thrower("Argument p is null in set(p)."); }
	
	/**
	 * Multiplies each component by <code>f</code>.  Relies on {@link #set(float, float, float)}.		  
	 * @param f the scaling factor.
	 * @return this Vector3D, modified
	 */
	public final Vector3D
		multiplyBy( float f )
		{ return set(x*f, y*f, z*f); }
	/**
	 * Creates a new Vector3D from v, by copying it and multiplying each of its components by f.
	 * @param v the original Vector3D; unmodified by this operation
	 * @param f the scaling factor
	 * @return a new Vector3D, v*f
	 * @throws NullPointerException if v==null
	 */
	public final static Vector3D
		multiplyBy(Vector3D v, float f)
		throws NullPointerException
		{ return v!=null ? multiplyBy(v,f,null) : thrower("Argument v is null in multiplyBy(v) call."); }
	
	/**
	 * Returns the result of v*f in target, or creates a new Vector3D if target==null.
	 * @param v the source Vector3D; unmodified by this operation
	 * @param f the scaling factor
	 * @param target the target Vector3D; modified by this operation, may be null
	 * @return target, modified, or a new Vector3D
	 * @throws NullPointerException if v==null
	 */
	public final static Vector3D
		multiplyBy(Vector3D v, float f, Vector3D target)
		throws NullPointerException {
		if (v==null) thrower("Argument v is null in multiplyBy(v,f,target) call.");
		return target == null ? of(v.x*f,v.y*f,v.z*f) : target.set(v.x*f,v.y*f,v.z*f);
	}
	
	/**
	 * Limits the length of this Vector3D to <code>f</code>.  Calling this with f<=0 sets length to 0.
	 * @param f the desired limit
	 * @return this Vector3D, appropriately modified
	 */
	public final Vector3D
		limit( float f )
		{ return length() > f? length(f) : this; }
	
	/**
	 * The opposite of {@link #limit(float)}: puts a lower bound on the length of <code>f</code>.  Calling this with f<=0 is a no-op.
	 * @param f the desired minimum length
	 * @return this Vector3D, appropriately modified
	 */
	public final Vector3D
		floor( float f )
		{ return f>0 && length() < f ? length(f) : this; }
	
	/**
	 * Sets this Vector3Ds length to one by appropriately scaling x, y, and z.
	 * @return this Vector3D, modified
	 * @throws ArithmeticException if this is a zero vector
	 */
	public final Vector3D
		normalize()
		throws ArithmeticException
		{ return multiplyBy(1/length()); }
	
	/**
	 * Calculates the distance between the tip of this Vector3D and that of p.
	 * Relies on {@link #subtract(Vector3D, Vector3D)} and {@link #length()}.
	 * @param v the other Vector3D; unmodified by this operation
	 * @return the distance between this and p
	 * @throws NullPointerException if p==null
	 */
	public final float
		distanceTo( Vector3D v )
		throws NullPointerException {
		thrower(v,"Argument v is null in distanceTo(p) call.");
		return Vector3D.subtract(this, v).length();
	}
	
	/**
	 * Like {@link #distanceTo(Vector3D)}, only squared
	 * @param v the other Vector3D; unmodified by this operation
	 * @return the distance between this and p, squared
	 * @throws NullPointerException if p==null
	 */
	public final float
		distanceSquaredTo( Vector3D v )
		throws NullPointerException {
		thrower(v,"Argument v is null in distanceSquaredTo(p) call."); 
		return Vector3D.subtract(this, v).lengthSquared();
	}
	
	public final float
		distanceTo( float x, float y, float z ) {
		float dx = this.x - x;
		float dy = this.y - y;
		float dz = this.z - z;
		return (float)Math.sqrt( dx*dx + dy*dy + dz*dz );
	}
	
	/**
	 * Projects this Vector3D onto another Vector3D.
	 * @param p the other Vector3D
	 * @return this Vector3D, after projection onto p
	 */
	public final Vector3D
		projectOnto( Vector3D p)
		{ return set(p.copy().length(p.dot(this)/p.length())); }
		
	/**
	 * Calculates the dot product between this Vector3D and another - this.x*p.x + this.y*p.y + this.y*p.y
	 * @param p the other Vector3D
	 * @return the dot product; always >=0
	 * @throws NullPointerException if p==null
	 */
	public final float
		dot( Vector3D p )
		throws NullPointerException {
		thrower(p,"Argument p is null in dot(p) call.");
		return x*p.x + y*p.y + z*p.z;
	}
	
	private static boolean thrower(Vector3D p, String message) {
		if (p==null) thrower(message);
		return true;
	}

	public final float length()                 			{ return (float)Math.sqrt( lengthSquared() ); }
	public final Vector3D length(float f)					{ return multiplyBy(f/length()); }
	public final float lengthSquared()						{ return dot(this); }
	  
	public final void clear()                   				{ x = 0; y = 0; z = 0; }

	public final String toString()              				{ return new String( "(" + x + ", " + y + ", " + z + ")" ); }

	/**
	 * Creates a new Vector3D from this x p
	 * @param p the other Vector3D in the cross-products
	 * @return a new Vector3D, this x p
	 * @throws NullPointerException if p==null
	 */
	public final Vector3D
		cross( Vector3D p )
		throws NullPointerException {
		return p==null ?thrower("Argument p is null in cross(Vector3D)."):
						new Vector3D( 	this.y*p.z - this.z*p.y, 
										this.x*p.z - this.z*p.x,
										this.x*p.y - this.y*p.x );
	}
	
	/**
	 * Indicates whether or not this Vector3D is the zero vector
	 * @return true if all components == 0
	 */
	public boolean isZero() { return x == 0 && y == 0 && z == 0; }

	@Override public boolean equals(Object other) { return (other instanceof Vector3D) ? equals((Vector3D)other) : false; }
	
	/**
	 * The equals method given another Vector3D; called from {@link #equals(Object)}
	 * @param other the other Vector3D
	 * @return true if all of the components are equal
	 */
	protected boolean equals(Vector3D other) { return this==other ? true : x == other.x && y == other.y && z == other.z; }
	
}
