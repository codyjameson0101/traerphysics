package traer.physics;

import java.util.Iterator;

public abstract class Function<From,To> {

	public abstract To apply(From arg0);
	
	public static <From,To> void functor(Iterable<From> source, Function<From,To> sideEffector) {
		for (From f : source) sideEffector.apply(f);
	}
	
	public static <To,From> Iterable<To> transform(final Iterable<From> source, final Function<From,To> transform) {
		return new Iterable<To>() {
			@Override public Iterator<To> iterator() {
				return new Iterator<To>(){
					Iterator<From> delegate = source.iterator();
 					@Override public boolean hasNext() { return delegate.hasNext(); }
					@Override public To next() { return transform.apply(delegate.next()); }
					@Override public void remove() { throw new UnsupportedOperationException("Iterator provided via Iterable interface does not support removal.");
					}};
			}
			
		};
	}
	
	public <ToNew> Function<From, ToNew> combine(final Function<To,ToNew> other) {
		final Function<From,To> local = this;
		return new Function<From, ToNew>() {
			@Override public ToNew apply(From arg0) { return other.apply(local.apply(arg0)); }
		};
	}
	
}
