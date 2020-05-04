package org.example.tree;

import java.util.List;

public class RedBlackTree<E extends Comparable<E>> implements TreeContainer<E>
{
    @Override
    public boolean add(E person) {
        return false;
    }

    @Override
    public boolean delete(E samePerson) {
        return false;
    }

    @Override
    public E find(E samePerson) {
        return null;
    }

    @Override
    public List<E> getAll() {
        return null;
    }
}
