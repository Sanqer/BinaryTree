package org.example.tree.rbtree;

import org.example.tree.sympletree.Node;

public class RedNode<E> {
    public E value;
    public RedNode<E> left;
    public RedNode<E> right;
    public RedNode<E> parent;
    public int numLeft = 0;
    public int numRight = 0;
    public boolean color;


    public RedNode(E val) {
        this.value=val;
        right=null;
        left=null;
        parent=null;
        numLeft = 0;
        numRight = 0;
        color=false;
    }

    public E value() {
        return value;
    }

    public RedNode<E> left() {
        return left;
    }

    public RedNode<E> right() {
        return right;
    }

    public boolean color() { return color; }
}

