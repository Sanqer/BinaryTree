package org.example.tree.sympletree;

public class Node<E>{
    public E key;
    public transient Node<E> parent;
    public Node<E> left;
    public Node<E> right;

    public Node(E val) {
        this.key =val;
    }

    public Node() {}

    public E val() {
            return key;
    }

    public Node<E> left() {
            return left;
    }

    public Node<E> right() {
            return right;
    }

    public Node<E> parent() {
            return parent;
    }
    @Override
    public String toString() {
            return "BinaryTree{" +
                    "val=" + key +
                    '}';
        }
}
