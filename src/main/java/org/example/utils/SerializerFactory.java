package org.example.utils;

import org.example.filesave.JsonSerializer;
import org.example.filesave.Serializer;

public class SerializerFactory
{
    private static Serializer serializer;

    private SerializerFactory() {}

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
}
