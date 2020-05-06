package org.example.tree.avltree;

import javafx.util.Pair;
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

    private int height(Node<E> node) {
        return (node != null) ? node.getHeight() : 0;
    }

    private void fixHeight(Node<E> current) {
        int hl = height(current.getLeft());
        int hr = height(current.getRight());
        current.setHeight(Math.max(hl, hr) + 1);
    }

    private int getBalance(Node<E> current) {
        int hl = height(current.getLeft());
        int hr = height(current.getRight());
        return hr - hl;
    }

    private Node<E> rotateRight(Node<E> current) {
        Node<E> left = current.getLeft();
        current.setLeft(left.getRight());
        left.setRight(current);
        fixHeight(current);
        fixHeight(left);
        return left;
    }

    private Node<E> rotateLeft(Node<E> current) {
        Node<E> right = current.getRight();
        current.setRight(right.getLeft());
        right.setLeft(current);
        fixHeight(current);
        fixHeight(right);
        return right;
    }

    private Node<E> balance(Node<E> current) {
        fixHeight(current);
        if (getBalance(current) == 2) {
            if (getBalance(current.getRight()) < 0) {
                current.setRight(rotateRight(current.getRight()));
            }
            return rotateLeft(current);
        }
        if (getBalance(current) == -2) {
            if (getBalance(current.getLeft()) > 0) {
                current.setLeft(rotateLeft(current.getLeft()));
            }
            return rotateRight(current);
        }
        return current;
    }

    private Node<E> insert(Node<E> current, E element) throws Exception
    {
        if (current == null) return new Node<>(element);
        int compRes = element.compareTo(current.getKey());
        if (compRes == 0) throw new Exception("Элемент уже есть в дереве");
        if (compRes > 0) {
            current.setRight(insert(current.getRight(), element));
        } else {
            current.setLeft(insert(current.getLeft(), element));
        }
        return balance(current);
    }

    private Node<E> findMin(Node<E> current) {
        return (current.getLeft() != null) ? findMin(current.getLeft()) : current;
    }

    private Node<E> removeMin(Node<E> current) {
        if (current.getLeft() == null) {
            return current.getRight();
        }
        current.setLeft(removeMin(current.getLeft()));
        return balance(current);
    }

    private Node<E> remove(Node<E> current, E element) {
        if (current == null) return null;
        int compRes = element.compareTo(current.getKey());
        if (compRes > 0) {
            current.setRight(remove(current.getRight(), element));
        } else if (compRes < 0) {
            current.setLeft(remove(current.getLeft(), element));
        } else { // element == current.getKey()
            Node<E> left = current.getLeft();
            Node<E> right = current.getRight();
            current = null;
            if (right == null) return left;
            Node <E> min = findMin(right);
            min.setRight(removeMin(right));
            min.setLeft(left);
            return balance(min);
        }
        return balance(current);
    }
}
