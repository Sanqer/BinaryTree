package org.binarytree.utils;

import org.binarytree.filesave.*;

public class FileManagerFactory
{
    private static FileManager fileManager;

    private FileManagerFactory() {}

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
