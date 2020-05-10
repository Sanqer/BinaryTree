package org.example.tree;

import java.util.List;

public interface TreeContainer<E extends Comparable<E>> {
    boolean add(E element);
    boolean delete(E sameElement);
    E find(E sameElement);
    List<E> getAll();
    boolean isEmpty();

    default void configure() {}
}