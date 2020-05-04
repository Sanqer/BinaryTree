package org.example.utils;

import org.example.filesave.JsonSerializer;
import org.example.filesave.Loader;
import org.example.filesave.Serializer;
import org.example.model.Person;
import org.example.tree.DataAdapter;
import org.example.tree.RedBlackTree;
import org.example.tree.TreeAdapter;
import org.example.tree.TreeContainer;

public class Factory {
    private static TreeContainer<Person> tree;
    private static Serializer serializer;
    private static Loader loader;

    private Factory() {

    }

    public static DataAdapter getDataAdapter() {
        return TreeAdapter.GetInstance();
    }

    public static Serializer getSerializer() {
        if (serializer == null) {
            //from property
            //serializer = new JsonSerializer<>();
        }
        return serializer;
    }

    public static Loader getLoader() {
        if (loader == null) {
            //
        }
        return loader;
    }

    public static TreeContainer<Person> getTree() {
        if (tree == null) {
            tree = new RedBlackTree<>();
            tree = getSerializer().Deserialize(loader.load(), RedBlackTree.class);
            //get property and create RIGHT tree from file, if tree == null, with right Serializer;
        }
        return tree;
    }
}
