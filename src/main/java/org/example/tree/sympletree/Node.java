package org.example.tree.sympletree;

public class Node<E>{
        public E val;
        public int height;
        public Node<E> parent;
        public Node<E> left;
        public Node<E> right;

    public Node(E val) {
        this.val=val;
    }

    public E val() {
            return val;
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
                    "val=" + val +
                    '}';
        }
}
