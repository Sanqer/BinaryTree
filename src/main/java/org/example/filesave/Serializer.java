package org.example.filesave;

import org.example.model.Person;
import org.example.tree.TreeContainer;
import java.lang.reflect.Type;

public interface Serializer
{
    <T>String Serialize(T elem);
    <T> T Deserialize(String str, Type elementType);
}
