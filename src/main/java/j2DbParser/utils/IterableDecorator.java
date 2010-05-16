package j2DbParser.utils;

import java.util.Iterator;

/**
 * Decorates {@link Iterator} to use in foreach loop.
 * 
 * @param <E>
 */
public class IterableDecorator<E> implements Iterable<E> {
	private final Iterator<E> iter;

	public IterableDecorator(Iterator<E> iter) {
		super();
		this.iter = iter;
	}

	public Iterator<E> iterator() {
		return iter;
	}

	public static <E> Iterable<E> create(Iterator<E> it) {
		return new IterableDecorator<E>(it);
	}

}
