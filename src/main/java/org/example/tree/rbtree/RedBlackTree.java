package org.example.tree.rbtree;

import org.example.tree.TreeContainer;

import java.util.List;

public class RedBlackTree<E extends Comparable<E>> implements TreeContainer<E>
{
    @Override
    public boolean add(E element) {
        return false;
    }

    @Override
    public boolean delete(E sameElement) {
        return false;
    }

    @Override
    public E find(E sameElement) {
        return null;
    }

    @Override
    public List<E> getAll() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
