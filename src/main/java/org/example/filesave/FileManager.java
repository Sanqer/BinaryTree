package org.example.filesave;

import com.google.gson.reflect.TypeToken;
import org.example.model.Person;
import org.example.tree.RedBlackTree;
import org.example.tree.TreeContainer;
import org.example.tree.avltree.AvlTree;
import org.example.utils.Factory;
import org.example.utils.PropertyManager;

public class FileManager
{
    private Loader loader;
    private Saver saver;

    public FileManager(Loader loader, Saver saver) {
        this.loader = loader;
        this.saver = saver;
    }

    public boolean save(TreeContainer<Person> tree) {
        return saver.save(Factory.getSerializer().Serialize(tree));
    }

    public TreeContainer<Person> load() {
        TreeContainer<Person> tree;
        String treeProp = PropertyManager.getPropertyAsString("tree.type", "avl");
        if (treeProp.equalsIgnoreCase("avl")) {
            tree = Factory.getSerializer().Deserialize(loader.load(), new TypeToken<AvlTree<Person>>() {}.getType());
            if (tree == null)
                tree = new AvlTree<>();
        } else {
            tree = Factory.getSerializer().Deserialize(loader.load(), new TypeToken<RedBlackTree<Person>>() {}.getType());
            if (tree == null)
                tree = new RedBlackTree<>();
        }
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
