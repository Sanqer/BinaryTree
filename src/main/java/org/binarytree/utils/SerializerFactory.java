package org.binarytree.utils;

import org.binarytree.filesave.JsonSerializer;
import org.binarytree.filesave.Serializer;
import org.binarytree.filesave.XmlSerializer;

public class SerializerFactory {
    private static Serializer serializer;

    private SerializerFactory() {
    }

    public static Serializer getSerializer() {
        if (serializer == null) {
            String formatProp = Common.getFormat();
            if (formatProp.equalsIgnoreCase("xml")) {
                serializer = new XmlSerializer();
            } else {
                serializer = new JsonSerializer();
            }
        }
        return serializer;
    }
}
