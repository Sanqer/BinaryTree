package org.example.filesave;

import org.example.model.Person;
import org.example.tree.TreeContainer;
import java.lang.reflect.Type;

public interface Serializer<T extends Comparable<T>>
{
    String Serialize(TreeContainer<T> tree);
    TreeContainer<T> Deserialize(String str, Type elementType);
}
