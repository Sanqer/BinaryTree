package org.example.tree.rbtree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.example.tree.TreeContainer;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RedBlackTree <E extends Comparable<E>> implements TreeContainer<E>
{
    private transient List<E> listForPrint = new ArrayList<>();
    //private transient RedNode<E> nil() = new RedNode<>();
    @JacksonXmlProperty
    private RedNode<E> root;

    private RedNode<E> nil() { return new RedNode<>(); }

    public RedBlackTree() {
        root = nil();
        root.left = nil();
        root.right = nil();
        root.parent = nil();
    }

    @Override
    public boolean add(E element) {
        return insert(new RedNode<E>(element));
    }

    @Override
    public boolean delete(E sameElement){
        RedNode<E> z = search(sameElement);

        RedNode<E> x = nil();
        RedNode<E> y = nil();
        if(z==null){
            return false;
        }
        // если один из дочерних элементов z равен nil(), то мы должны удалить z
        if (isNil(z.left) || isNil(z.right)){
            y = z;
        }
        // иначе мы должны удалить преемника z
        else{y = treeSuccessor(z);}

        // Пусть x будет левым или правым потомком y (у может быть только один потомок)
        if (!isNil(y.left)){
            x = y.left;
        }
        else{
            x = y.right;
        }

        // связать родителя x с родителем y
        if(x!=null){
            x.parent = y.parent;
        }

        // Если родитель у ноль, то х является корнем
        if (isNil(y.parent)){
            root = x;
        }else if (!isNil(y.parent.left) && y.parent.left.value.compareTo(y.value)==0){
            y.parent.left = x;
        }else if (!isNil(y.parent.right) && y.parent.right.value.compareTo(y.value)==0){
            y.parent.right = x;
        }

        // если y! = z, перенести значение y в z.
        if (y.value.compareTo(z.value)!=0){
            z.value = y.value;
        }

        // Обновите номера numLeft и numRight, которые могут понадобиться
        // обновление из-за удаления z.key.
        fixNodeData(x,y);

        // Если y - черный цвет, это нарушение
        // поэтому вызываем removeFixup ()
        if (!y.color){
            removeFixup(x);
        }
        return true;
    }

    @Override
    public E find(E sameElement) {
        RedNode<E> current = search(sameElement);
        if(current == null){
            return null;
        }
        return current.value;
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
        return isNil(root);
    }

    @Override
    public void configure() {
        restoreParents(root, nil());
    }

    private void restoreParents(RedNode<E> current, RedNode<E> prev) {
        if (isNil(current)) return;
        current.parent = prev;
        restoreParents(current.left, current);
        restoreParents(current.right, current);
    }

    // Вставляет z в соответствующую позицию в RedBlackTree
    private boolean insert(RedNode<E> z) {

        RedNode<E> y=nil();
        RedNode<E> x = root;

        if(search(z.value)!=null){
            return false;
        }
        // Пока мы не достигли конца дерева
        // пытаемся выяснить, куда присоеденить z
        while (!isNil(x)) {
            y = x;
            if (z.value.compareTo(x.value) < 0) {
                //идем влево
                x.numLeft++;
                x = x.left;
            }
            else {
                //идем вправо
                x.numRight++;
                x = x.right;
            }
        }
        z.parent = y;

        //в зависимости от значения у крепим я влево или вправо
        if (isNil(y)){
            root = z;
        }else if (z.value.compareTo(y.value) < 0){
            y.left = z;
        }else{
            y.right = z;
        }

        z.left = null;
        z.right = null;
        z.color = true;

        insertFixup(z);
        return true;
    }

    // @param: z, узел, который был вставлен и, возможно, вызвал нарушение
    // свойств RedBlackTree
    // Исправляет нарушение свойств RedBlackTree, которые могут иметь
    // был вызван во время вставки (z)
    private void insertFixup(RedNode<E> z){

        RedNode<E> y = nil();
        // Пока нарушаются свойства дерева
        while (z.parent.color){
            if(z.parent.parent==null){
                break;
            }
            if (z.parent.parent.left==null) {
                z.parent.parent.left = nil();
            }
            if (z.parent.parent.right == null) {
                z.parent.parent.right = nil();
            }
            if (z.parent == z.parent.parent.left){
                y = z.parent.parent.right;
                // если у - красный , меняем цвет
                if (y.color){
                    z.parent.color = false;
                    y.color = false;
                    z.parent.parent.color = true;
                    z = z.parent.parent;
                }
                // если у - черный и у z есть элемент справа
                else if (z.parent.right!=null && z == z.parent.right){
                    // Левый поворот
                    z = z.parent;
                    leftRotate(z);
                }
                // если у - черный и у z есть элемент слева
                else{
                    z.parent.color = false;
                    z.parent.parent.color = true;
                    rightRotate(z.parent.parent);
                }
            }
            // Если родитель z является правильным потомком своего родителя.
            else{
                y = z.parent.parent.left;
                // если у - красный , меняем цвет
                if (y.color){
                    z.parent.color = false;
                    y.color = false;
                    z.parent.parent.color = true;
                    z = z.parent.parent;
                }
                // если у - черный и у z есть элемент слева
                else if (z.parent.left!=null && z == z.parent.left){
                    z = z.parent;
                    rightRotate(z);
                }
                // если у - черный и у z есть элемент справа
                else{
                    z.parent.color = false;
                    z.parent.parent.color = true;
                    leftRotate(z.parent.parent);
                }
            }
        }
        // Корень дерева всегда - черный
        root.color = false;
    }


    // y, RedBlackNode, который фактически был удален из дерева
    // значение, которое было в y
    private void fixNodeData(RedNode<E> x, RedNode<E> y){
        RedNode<E> current = nil();
        RedNode<E> track = nil();

        if (isNil(x)){
            current = y.parent;
            track = y;
        }else{
            current = x.parent;
            track = x;
        }

        // пока мы не дошли до корня
        while (!isNil(current)){
            // if the node we deleted has a different key than
            // the current node
            if (y.value.compareTo(current.value)!=0) {
                if (y.value.compareTo(current.value) > 0){
                    current.numRight--;
                }
                if (y.value.compareTo(current.value) < 0){
                    current.numLeft--;
                }
            }else{
                if (isNil(current.left)){
                    current.numLeft--;
                }else if (isNil(current.right)){
                    current.numRight--;
                } else if (track.value.compareTo(current.right.value)==0){
                    current.numRight--;
                } else if (track.value.compareTo(current.left.value)==0){
                    current.numLeft--;
                }
            }

            // обновить track and current
            track = current;
            current = current.parent;

        }

    }


    // x, дочерний элемент удаленного узла от remove (RedNode v)
    // Восстанавливает свойства Red Black, которые могли быть нарушены во время удаления узла
    private void removeFixup(RedNode<E> x){

        RedNode<E> w;

        // Пока мы не исправили дерево полностью ...
        while (x!=null && x != root && (!x.color)){
            if (x.parent.left != null && x.parent.right!=null && x == x.parent.left){
                // установить родного брата
                w = x.parent.right;

                if (w.color){
                    w.color = false;
                    x.parent.color = true;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }

                if(w==null){
                    break;
                }

                if (w.left!=null && w.right!=null && (!w.left.color) && (!w.right.color)){
                    w.color = true;
                    x = x.parent;
                }

                else if(w.right!=null && w.left!=null){
                    if (!w.right.color){
                        w.left.color = false;
                        w.color = true;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = false;
                    w.right.color = false;
                    leftRotate(x.parent);
                    x = root;
                }else{
                    break;
                }
            } else{
                if(x.parent.left==null){
                    break;
                }
                w = x.parent.left;

                if (w.color){
                    w.color = false;
                    x.parent.color = true;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if(w==null){
                    break;
                }
                if (w.right!=null && w.left!=null && (!w.right.color) && (!w.left.color)){
                    w.color = true;
                    x = x.parent;
                }
                else if(w.left!=null && w.right!=null){
                    if (!w.left.color){
                        w.right.color = false;
                        w.color = true;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = false;
                    w.left.color = false;
                    rightRotate(x.parent);
                    x = root;
                }else{
                    break;
                }
            }
        }

        //устанавливаем x в черный цвет, чтобы гарантировать отсутствие нарушения
        if(x!=null){
            x.color = false;
        }
    }

    public RedNode<E> search(E key){
        RedNode<E> current = root;
        // Пока мы не достигли конца дерева
        while (!isNil(current)){
            if (current.value.compareTo(key)==0){
                return current;
            } else if (current.value.compareTo(key) < 0){
                current = current.right;
            }else{
                current = current.left;
            }
        }
        return null;
    }

    private void printR(RedNode<E> node){
        if(node==null || node.value== null) return;
        printR(node.left);
        listForPrint.add(node.value);
        if(node.right!=null)
            printR(node.right);
    }


    // @param: x, узел, на котором должен выполняться lefRotate.
    // Выполняет leftRotate вокруг x.
    private void leftRotate(RedNode<E> x){
        // для значений numRight и numLeft - leftRotateFixup()
        leftRotateFixup(x);
        RedNode<E> y;
        y = x.right;
        x.right = y.left;

        // Проверка существования y.left и изменение его родителя
        if (!isNil(y.left)){
            y.left.parent = x;
        }
        y.parent = x.parent;

        if (isNil(x.parent)){
            root = y;
        }
        // x левый потомок своего родителя
        else if (x.parent.left!=null && x.parent.left == x){
            x.parent.left = y;
        }
        // x является правым потомком своего родителя.
        else{
            x.parent.right = y;
        }

        y.left = x;
        x.parent = y;
    }


    // @param: x, узел, на котором нужно выполнить leftRotate.
    // Обновляет значения numLeft & numRight, на которые воздействует leftRotate.
    private void leftRotateFixup(RedNode<E> x){

        // 1 условие x.right и x.right.right всегда не равны нулю.
        if (isNil(x.left) && isNil(x.right.left)){
            x.numLeft = 0;
            x.numRight = 0;
            x.right.numLeft = 1;
        }

        // x.right.left также существует вместе с первым условием
        else if (isNil(x.left) && !isNil(x.right.left)){
            x.numLeft = 0;
            x.numRight = 1 + x.right.left.numLeft +
                    x.right.left.numRight;
            x.right.numLeft = 2 + x.right.left.numLeft +
                    x.right.left.numRight;
        }

        // x.left также существует вместе с первым условием
        else if (!isNil(x.left) && isNil(x.right.left)){
            x.numRight = 0;
            x.right.numLeft = 2 + x.left.numLeft + x.left.numRight;
        }

        // x.left и x.right.left существуют вместе с первым условием
        else{
            x.numRight = 1 + x.right.left.numLeft +
                    x.right.left.numRight;
            x.right.numLeft = 3 + x.left.numLeft + x.left.numRight +
                    x.right.left.numLeft + x.right.left.numRight;
        }

    }

    // @param: x, узел, на котором нужно выполнить rightRotate.
    // Обновляет значения numLeft и numRight, на которые влияет Rotate.
    private void rightRotate(RedNode<E> y){

        // rightRotateFixup для настройки значений numRight и numLeft
        rightRotateFixup(y);

        RedNode<E> x = y.left;
        y.left = x.right;

        // наличие x.right
        if (!isNil(x.right)){
            x.right.parent = y;
        }
        x.parent = y.parent;

        if (isNil(y.parent)){
            root = x;
        }
        // у - правый потомок своего родителя.
        else if (y.parent.right!=null && y.parent.right == y){
            y.parent.right = x;
        }
        // у - левый потомок своего родителя.
        else{
            y.parent.left = x;
        }
        x.right = y;
        y.parent = x;

    }

    // @param: y, узел, вокруг которого должно выполняться rightRotate.
    // Обновляет значения numLeft и numRight, на которые влияет поворот
    private void rightRotateFixup(RedNode<E> y){

        // Существует только y.left и y.left.left.
        if (isNil(y.right) && isNil(y.left.right)){
            y.numRight = 0;
            y.numLeft = 0;
            y.left.numRight = 1;
        }

        // y.left.right также существует вместе с условием 1
        else if (isNil(y.right) && !isNil(y.left.right)){
            y.numRight = 0;
            y.numLeft = 1 + y.left.right.numRight +
                    y.left.right.numLeft;
            y.left.numRight = 2 + y.left.right.numRight +
                    y.left.right.numLeft;
        }

        // y.right также существует вместе с условием 1
        else if (!isNil(y.right) && isNil(y.left.right)){
            y.numLeft = 0;
            y.left.numRight = 2 + y.right.numRight +y.right.numLeft;
        }

        // Case 4: y.right & y.left.right существуют вместе с условием 1
        else{
            y.numLeft = 1 + y.left.right.numRight +
                    y.left.right.numLeft;
            y.left.numRight = 3 + y.right.numRight +
                    y.right.numLeft +
                    y.left.right.numRight + y.left.right.numLeft;
        }

    }

    // @param: узел, RedBlackNode
    // узел с наименьшим значением
    public RedNode<E> treeMinimum(RedNode<E> node){
        // пока есть меньший ключ, продолжаем идти налево
        while (!isNil(node.left))
            node = node.left;
        return node;
    }

    // return преемника которого мы должны найти со следующим по величине ключом
    // из x.key
    public RedNode<E> treeSuccessor(RedNode<E> x){
        if (!isNil(x.left) ){
            return treeMinimum(x.right);
        }

        RedNode<E> y = x.parent;

        // пока х родитель правого
        while (!isNil(y) && x.value.compareTo(y.right.value)==0){
            // Двигаемся вверх по дереву
            x = y;
            y = y.parent;
        }
        return y;
    }

    private boolean isNil(RedNode<E> node){
        //return (node.value.compareTo(nil().value)==0);
        if(node==null) return true;
        return node.value==null;
    }

    public int size(){
        return root.numLeft + root.numRight + 1;
    }
}
