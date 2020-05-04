package org.example.tree;

import java.util.List;

public interface TreeContainer<E extends Comparable<E>> {
    boolean add(E person);
    boolean delete(E samePerson);
    E find(E samePerson);
    List<E> getAll();
}
