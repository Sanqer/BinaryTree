package org.example.tree.avltree;

import org.example.tree.TreeContainer;
import java.util.List;
import java.util.Stack;

public class AvlTree<E extends Comparable<E>> implements TreeContainer<E>
{
    private Node<E> root;

    public AvlTree() {
        root = null;
    }

    @Override
    public boolean add(E element) {
        Node<E> node = new Node<>(element);
        if (root == null) {
            root = node;
            return true;
        } else {
            Node<E> prev = root;
            Node<E> next = root;
            while (next != null) {
                prev = next;
                if (node.getKey().compareTo(next.getKey()) == 0) return false;
                if (node.getKey().compareTo(next.getKey()) > 0) {
                    next = next.getRight();
                } else {
                    next = next.getLeft();
                }
            }

            if (node.getKey().compareTo(prev.getKey()) > 0) {
                prev.setRight(node);
            } else {
                prev.setLeft(node);
            }
            return true;
        }
    }

    @Override
    public boolean delete(E sameElement) {
        return false;
    }

    @Override
    public E find(E sameElement) {
        if (isEmpty()) return null;
        Node<E> next = root;
        while (next != null) {
            int compRes = next.getKey().compareTo(sameElement);
            if (compRes == 0) {
                return next.getKey();
            }
            if (compRes > 0) {
                next = next.getRight();
            } else {
                next = next.getLeft();
            }
        }
        return null;
    }

    @Override
    public List<E> getAll() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }
}
