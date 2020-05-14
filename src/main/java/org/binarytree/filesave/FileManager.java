package org.binarytree.filesave;

import com.google.gson.reflect.TypeToken;
import org.binarytree.model.Person;
import org.binarytree.tree.rbtree.RedBlackTree;
import org.binarytree.tree.TreeContainer;
import org.binarytree.tree.avltree.AvlTree;
import org.binarytree.tree.sympletree.SimpleTree;
import org.binarytree.utils.Common;
import org.binarytree.utils.SerializerFactory;

public class FileManager
{
    private Loader loader;
    private Saver saver;

    public FileManager(Loader loader, Saver saver) {
        this.loader = loader;
        this.saver = saver;
    }

    public <T extends Comparable<T>> boolean save(TreeContainer<T> tree) {
        return saver.save(() -> SerializerFactory.getSerializer().Serialize(tree));
    }

    public TreeContainer<Person> load() {
        TreeContainer<Person> tree;
        String treeProp = Common.getTreeType();
        if (treeProp.equalsIgnoreCase("avl")) {
            tree = SerializerFactory.getSerializer().Deserialize(loader.load(), new TypeToken<AvlTree<Person>>() {}.getType());
            if (tree == null)
                tree = new AvlTree<>();
        } else if (treeProp.equalsIgnoreCase("rbtree")){
            tree = SerializerFactory.getSerializer().Deserialize(loader.load(), new TypeToken<RedBlackTree<Person>>() {}.getType());
            if (tree == null)
                tree = new RedBlackTree<>();
        } else {
            tree = SerializerFactory.getSerializer().Deserialize(loader.load(), new TypeToken<SimpleTree<Person>>() {}.getType());
            if (tree == null)
                tree = new SimpleTree<>();
        }
        tree.configure();
        return tree;
    }

    public Loader getLoader() {
        return loader;
    }

    public void setLoader(Loader loader) {
        this.loader = loader;
    }

    public Saver getSaver() {
        return saver;
    }

    public void setSaver(Saver saver) {
        this.saver = saver;
    }
}
