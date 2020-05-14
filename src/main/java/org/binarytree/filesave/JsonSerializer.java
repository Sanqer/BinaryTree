package org.binarytree.filesave;

import org.binarytree.utils.Common;
import java.lang.reflect.Type;

public class JsonSerializer implements Serializer
{
    @Override
    public <T> String serialize(T elem) {
        return Common.getPrettyGson().toJson(elem);
    }

    @Override
    public <T> T deserialize(String str, Type elementType) {
        return Common.getPrettyGson().fromJson(str, elementType);
    }
}
