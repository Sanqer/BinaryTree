package org.binarytree.tree.rbtree;

public class RedNode<E> {
    public E value;
    public RedNode<E> left;
    public RedNode<E> right;
    public transient RedNode<E> parent;
    public int numLeft = 0;
    public int numRight = 0;
    public boolean color;


    public RedNode() {
        right=null;
        left=null;
        parent=null;
        numLeft = 0;
        numRight = 0;
        color=false;
    }

    RedNode(E value){
        this();
        this.value = value;
    }

    @Override
    public String toString() {
        return "RedBlackTree{" +
                "val=" + value +
                '}';
    }

    public RedNode<E> left() {
        return left;
    }

    public RedNode<E> right() {
        return right;
    }

    public boolean color() { return color; }

}

