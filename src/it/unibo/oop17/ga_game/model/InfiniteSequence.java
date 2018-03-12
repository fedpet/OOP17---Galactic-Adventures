package it.unibo.oop17.ga_game.model;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Models an infinite sequence of elements.
 * 
 * @param <T>
 *            Element type
 */
public interface InfiniteSequence<T> extends Supplier<T> {
    /**
     * Builds a sequence going back and forth.
     * 
     * @param list
     *            The list
     * @param <T>
     *            Element type
     * @return The sequence
     */
    static <T> InfiniteSequence<T> backAndForth(final List<T> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List size must be > 0");
        }

        final List<T> myList = new LinkedList<>(list);
        return fromIterators(myList.iterator(), () -> {
            Collections.reverse(myList);
            final Iterator<T> it = myList.iterator();
            it.next(); // avoid duplicates
            return it;
        });
    }

    /**
     * It restarts from the beginning as soon as the end is reached.
     * 
     * @param iterable
     *            the @Iterable
     * @param <T>
     *            Iterable<T>
     * @return A sequence repeating the iterable.
     */
    static <T> InfiniteSequence<T> repeat(final Iterable<T> iterable) {
        return fromIterators(iterable.iterator(), () -> iterable.iterator());
    }

    /**
     * 
     * @param beginning
     *            starting iterator
     * @param <T>
     *            Iterable<T>
     * @param iteratorSupplier
     *            A supplier for next iterators
     * @return The sequence.
     */
    static <T> InfiniteSequence<T> fromIterators(final Iterator<T> beginning, final Supplier<Iterator<T>> iteratorSupplier) {
        return new InfiniteSequence<T>() {
            private Iterator<T> it = beginning;

            @Override
            public T get() {
                if (!it.hasNext()) {
                    it = iteratorSupplier.get();
                }
                return it.next();
            }
        };
    }
}
