package org.example.tree.avltree;

import org.example.tree.TreeContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AvlTree<E extends Comparable<E>> implements TreeContainer<E>
{
    private static Logger log = LoggerFactory.getLogger(AvlTree.class.getSimpleName());
    private Node<E> root;
    private boolean isDeleted;

    public AvlTree() {
        root = null;
    }

    @Override
    public boolean add(E element) {
        try {
            root = insert(root, element);
            return true;
        } catch (AlreadyExistsException ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(E sameElement) {
        isDeleted = false;
        root = remove(root, sameElement);
        return isDeleted;
    }

    @Override
    public E find(E sameElement) {
        if (isEmpty()) return null;
        Node<E> next = root;
        while (next != null) {
            int compRes = sameElement.compareTo(next.getKey());
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
        List<E> list = new ArrayList<>();
        dfs(root, list);
        return list;
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

    private int getBalFactor(Node<E> current) {
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
        if (getBalFactor(current) == 2) {
            if (getBalFactor(current.getRight()) < 0) {
                current.setRight(rotateRight(current.getRight()));
            }
            return rotateLeft(current);
        }
        if (getBalFactor(current) == -2) {
            if (getBalFactor(current.getLeft()) > 0) {
                current.setLeft(rotateLeft(current.getLeft()));
            }
            return rotateRight(current);
        }
        return current;
    }

    private Node<E> insert(Node<E> current, E element) throws AlreadyExistsException
    {
        if (current == null) return new Node<>(element);
        int compRes = element.compareTo(current.getKey());
        if (compRes == 0) throw new AlreadyExistsException("Элемент уже есть в дереве");
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
            isDeleted = true;
            if (right == null) return left;
            Node <E> min = findMin(right);
            min.setRight(removeMin(right));
            min.setLeft(left);
            return balance(min);
        }
        return balance(current);
    }

    private void dfs(Node<E> current, List<E> list) {
        if (current == null) return;
        dfs(current.getLeft(), list);
        list.add(current.getKey());
        dfs(current.getRight(), list);
    }
}
