package org.example.utils;

import com.google.gson.reflect.TypeToken;
import org.example.filesave.*;
import org.example.model.Person;
import org.example.tree.DataAdapter;
import org.example.tree.RedBlackTree;
import org.example.tree.TreeAdapter;
import org.example.tree.TreeContainer;
import org.example.tree.avltree.AvlTree;

public class Factory {
    //private static TreeContainer<Person> tree;
    private static Serializer serializer;
    private static FileManager fileManager;

    private Factory() {

    }

    public static DataAdapter getDataAdapter() {
        return TreeAdapter.GetInstance();
    }

    public static Serializer getSerializer() {
        if (serializer == null) {
            String formatProp = PropertyManager.getPropertyAsString("format", "json");
            if (formatProp.equalsIgnoreCase("xml")) {
                serializer = null;
                //add xml serializer
            } else {
                serializer = new JsonSerializer();
            }
        }
        return serializer;
    }

    public static FileManager getFileManager() {
        if (fileManager == null) {
            Loader loader = new EmptyLoader();
            Saver saver = new EmptySaver();
            String loaderProp = PropertyManager.getPropertyAsString("db.loader", "");
            String saverProp = PropertyManager.getPropertyAsString("db.saver", "");

            if (loaderProp.equalsIgnoreCase("file")) {
                loader = new FileLoader();
            }
            if (saverProp.equalsIgnoreCase("file")) {
                saver = new FileSaver();
            }

            fileManager = new FileManager(loader, saver);
        }
        return fileManager;
    }
}
