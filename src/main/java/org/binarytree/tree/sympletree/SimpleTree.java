package org.binarytree.tree.sympletree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.binarytree.tree.TreeContainer;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleTree<E extends Comparable<E>> implements TreeContainer<E> {
    @JacksonXmlProperty
    private Node<E> root;
    private transient List<E> listForPrint = new ArrayList<>();

    public SimpleTree() {
        root = null;
    }

    @Override
    public boolean add(E val) {
        addElem(val, root);
        return true;
    }

    @Override
    public boolean delete(E sameElement) {
        return remove(sameElement);
    }

    @Override
    public E find(E sameElement) {
        Node<E> res=search(sameElement);
        if(res==null){
        return null;
        }else{ return res.key;}
    }

    @JsonIgnore
    @Override
    public List<E> getAll() {
        listForPrint.clear();
        printR(root);
        return listForPrint;
    }

    @JsonIgnore
    @Override
    public boolean isEmpty() {
        return root==null;
    }

    @Override
    public void configure() {
        restoreParents(root, null);
    }

    private void restoreParents(Node<E> current, Node<E> prev) {
        if (current == null) return;
        current.parent = prev;
        restoreParents(current.left, current);
        restoreParents(current.right, current);
    }

    private void addElem(E val, Node<E> elem){
        if(root==null){
            root=new Node<>(val);
            return;
        }
        if(val.compareTo(elem.key) < 0){
            if(elem.left==null){
                elem.left = new Node<>(val);
                elem.left.parent=elem;
            }else{
                addElem(val,elem.left);
            }
        }else if(val.compareTo(elem.key) > 0){
            if(elem.right==null){
                elem.right = new Node<>(val);
                elem.right.parent=elem;
            }else{
                addElem(val,elem.right);
            }
        }else {
            System.out.println("Элемент "+val+" уже был добавлен ранее!");
        }
    }

    private Node<E> searchR(Node<E> tree, E val){
        if(tree == null) return null;
        if(val.compareTo(tree.key)>0){
            return searchR(tree.right, val);
        }
        if(val.compareTo(tree.key)<0){
            return searchR(tree.left, val);
        }
        return tree;
    }
    public Node<E> search(E val){
        return searchR(this.root, val);
    }

    public boolean remove(E val){
        //Проверяем, существует ли данный узел
        Node<E> tree = search(val);
        if(tree == null){
            //Если узла не существует, вернем false
            return false;
        }
        Node<E> curTree;
        //Если удаляем корень
        if(tree == this.root){
            if(tree.right!=null) {
                curTree = tree.right;
            }else if (tree.left!=null){
                curTree = tree.left;
            }else {
                root=null;
                return true;
            }
            if (tree.right != null) {
                while (curTree.left != null) {
                    curTree = curTree.left;
                }
            } else {
                while (curTree.right != null) {
                    curTree = curTree.right;
                }
            }
            E temp = curTree.key;
            boolean x=remove(temp);
            tree.key = temp;
            return x;
        }
        //Удаление листьев
        if(tree.left==null && tree.right==null && tree.parent != null){
            if(tree == tree.parent.left)
                tree.parent.left = null;
            else {
                tree.parent.right = null;
            }
            return true;
        }
        //Удаление узла, имеющего левое поддерево, но не имеющее правого поддерева
        if(tree.left != null && tree.right == null){
            //Меняем родителя
            tree.left.parent = tree.parent;
            if(tree == tree.parent.left){
                tree.parent.left = tree.left;
            }else if(tree == tree.parent.right){
                tree.parent.right = tree.left;
            }
            return true;
        }
        //Удаление узла, имеющего правое поддерево, но не имеющее левого поддерева
        if(tree.left == null && tree.right != null){
            //Меняем родителя
            tree.right.parent = tree.parent;
            if(tree == tree.parent.left){
                tree.parent.left = tree.right;
            }
            else if(tree == tree.parent.right){
                tree.parent.right = tree.right;
            }
            return true;
        }
        //Удаляем узел, имеющий поддеревья с обеих сторон
        if(tree.right!=null && tree.left!=null) {
            curTree = tree.right;
            while (curTree.left != null) {
                curTree = curTree.left;
            }
            //Если самый левый элемент является первым потомком
            if(curTree.parent == tree) {
                curTree.left = tree.left;
                tree.left.parent = curTree;
                curTree.parent = tree.parent;
                if (tree == tree.parent.left) {
                    tree.parent.left = curTree;
                } else if (tree == tree.parent.right) {
                    tree.parent.right = curTree;
                }
                return true;
            }
            //Если самый левый элемент НЕ является первым потомком
            else {
                if (curTree.right != null) {
                    curTree.right.parent = curTree.parent;
                }
                curTree.parent.left = curTree.right;
                curTree.right = tree.right;
                curTree.left = tree.left;
                tree.left.parent = curTree;
                tree.right.parent = curTree;
                curTree.parent = tree.parent;
                if (tree == tree.parent.left) {
                    tree.parent.left = curTree;
                } else if (tree == tree.parent.right) {
                    tree.parent.right = curTree;
                }
                return true;
            }
        }
        return false;
    }

    private void printR(Node<E> node){
        if(node == null) return;
        printR(node.left);
        listForPrint.add(node.key);
        if(node.right!=null)
            printR(node.right);
    }
}
