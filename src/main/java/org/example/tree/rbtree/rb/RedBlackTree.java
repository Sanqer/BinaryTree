package org.example.tree.rbtree.rb;

import org.example.tree.TreeContainer;
import org.example.tree.rbtree.RedNode;
import org.example.tree.sympletree.Node;

import java.util.ArrayList;
import java.util.List;

public class RedBlackTree<E extends Comparable<E>> implements TreeContainer<E>
{
    private RedNode<E> root;

    private List<E> listForPrint = new ArrayList<>();

    public RedBlackTree() {
        root = null;
    }

    @Override
    public boolean add(E element) {
        addElem(element, root);
        return true;
    }

    @Override
    public boolean delete(E sameElement) {
        return false;
    }

    @Override
    public E find(E sameElement) {
        RedNode<E> res=search(sameElement);
        if(res==null){
            return null;
        }else{ return res.value;}
    }

    @Override
    public List<E> getAll() {
        listForPrint.clear();
        printR(root);
        return listForPrint;
    }

    @Override
    public boolean isEmpty() {
        return root==null;
    }

    private void addElem(E val, RedNode<E> elem){
        if(root==null){
            root=new RedNode<>(val);
            return;
        }
        if(val.compareTo(elem.value) < 0){
            //самый левый
            if(elem.left==null){
                    //1 Вариант
                    //P — красный узел, а X — внутренний
                if(elem.value.compareTo(val)>0 && elem.value.compareTo(root.value)!=0) {
                    if (elem.value.compareTo(root.value) != 0 && elem.right == null && elem.parent.left == null) {
                        E temp = elem.value;
                        elem.value = val;
                        val = temp;
                    } else if (elem.value.compareTo(root.value) != 0 && elem.parent.right == null) {
                        E temp = elem.value;
                        elem.value = val;
                        val = temp;
                    }
                }

                    //2 Вариант
                    //P — красный узел, а X — внешний
                    if (elem.value.compareTo(root.value)!=0 && elem.right == null && elem.parent.right == null) {
                        //правый поворот
                        rightTurn(val, elem);
                    } else if (elem.value.compareTo(root.value)!=0 && elem.parent.left == null) {
                        //левый поворот
                        leftTurn(val, elem);
                    }

                    //3 Вариант
                    //P — черный узел
                    else if(!elem.color){
                        elem.left = new RedNode<>(val);
                        elem.left.parent = elem;
                        elem.left.color = true;
                    }else {
                        elem.left = new RedNode<>(val);
                        elem.left.parent = elem;
                        elem.left.color = false;
                    }
            }else{
                addElem(val,elem.left);
            }

            //Самый правый
        }else if(val.compareTo(elem.value) > 0){
            if(elem.right==null){
                    //1 Вариант
                    //P — красный узел, а X — внутренний
                if(elem.value.compareTo(val)>0 && elem.value.compareTo(root.value)!=0){
                    if (elem.left == null && elem.parent.right == null) {
                        E temp = elem.value;
                        elem.value = val;
                        val = temp;
                    } else if (elem.parent.left == null) {
                        E temp = elem.value;
                        elem.value = val;
                        val = temp;
                    }
                }

                    //2 Вариант
                    //P — красный узел, а X — внешний
                    if (elem.value.compareTo(root.value)!=0 && elem.parent.right == null) {
                        //правый поворот
                        rightTurn(val, elem);
                    } else if (elem.value.compareTo(root.value)!=0 && elem.left == null && elem.parent.left == null) {
                        //левый поворот
                        leftTurn(val, elem);
                    }

                //3 Вариант
                //P — черный узел
                else if(!elem.color){
                    elem.right= new RedNode<>(val);
                    elem.right.parent=elem;
                    elem.right.color=true;
                }else{
                    elem.right= new RedNode<>(val);
                    elem.right.parent=elem;
                    elem.right.color=false;
                }

            }else{
                addElem(val,elem.right);
            }
        }else {
            System.out.println("Элемент "+val+" уже был добавлен ранее!");
        }
    }

    private void leftTurn(E val, RedNode<E> elem){
        if(elem.parent.value.compareTo(root.value)==0){
            E temp = root.value;
            root.value=elem.value;
            elem.value=temp;
            if(elem.value.compareTo(root.value)>0){
                root.right=elem;
                root.left=new RedNode<>(val);
                root.left.parent=root;
                root.left.color=true;
            }else{
                root.left=elem;
                root.right=new RedNode<>(val);
                root.right.parent=root;
                root.right.color=true;
            }

        }else{
            elem.color=!elem.color;
            elem.parent.color=!elem.parent.color;
            RedNode<E> temp = elem.parent;
            if(elem.parent.value.compareTo(elem.parent.parent.left.value)==0){
                elem.parent.parent.left=elem;
            }else{
                elem.parent.parent.right=elem;
            }
            elem.left=elem.parent;
            elem.parent.parent=elem;
            elem.right= new RedNode<>(val);
            elem.parent=temp.parent;
            elem.right.parent=elem;
        }
    }

    private void rightTurn(E val, RedNode<E> elem){
        if(elem.parent.value.compareTo(root.value)==0){
            E temp = root.value;
            root.value=elem.value;
            elem.value=temp;
            if(elem.value.compareTo(root.value)>0){
                root.right=elem;
                root.left=new RedNode<>(val);
                root.left.parent=root;
                root.left.color=true;
            }else{
                root.left=elem;
                root.right=new RedNode<>(val);
                root.right.parent=root;
                root.right.color=true;
            }
        }else {
            elem.color = !elem.color;
            elem.parent.color = !elem.parent.color;
            RedNode<E> temp = elem.parent;
            if (elem.parent.value.compareTo(elem.parent.parent.left.value) == 0) {
                elem.parent.parent.left = elem;
            } else {
                elem.parent.parent.right = elem;
            }
            elem.right = elem.parent;
            elem.parent.parent = elem;
            elem.left = new RedNode<>(val);
            elem.parent = temp.parent;
            elem.left.parent = elem;
        }
    }

    private RedNode<E> searchR(RedNode<E> tree, E val){
        if(tree == null) return null;
        if(val.compareTo(tree.value)>0){
            return searchR(tree.right, val);
        }
        if(val.compareTo(tree.value)<0){
            return searchR(tree.left, val);
        }
        return tree;
    }
    public RedNode<E> search(E val){
        return searchR(this.root, val);
    }

    private void printR(RedNode<E> node){
        if(node == null) return;
        printR(node.left);
        listForPrint.add(node.value);
        if(node.right!=null)
            printR(node.right);
    }

}
