package org.example.utils;

import org.example.filesave.JsonSerializer;
import org.example.filesave.Serializer;
import org.example.filesave.XmlSerializer;

import javax.naming.InvalidNameException;

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
