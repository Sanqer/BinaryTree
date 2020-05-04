package org.example.filesave;

import org.example.tree.TreeContainer;
import org.example.utils.Common;
import java.lang.reflect.Type;

public class JsonSerializer<T extends Comparable<T>> implements Serializer<T>
{
    @Override
    public String Serialize(TreeContainer<T> tree) {
        return Common.getPrettyGson().toJson(tree);
    }

    @Override
    public TreeContainer<T> Deserialize(String str, Type elementType) {
        return Common.getPrettyGson().fromJson(str, elementType);
    }
}
