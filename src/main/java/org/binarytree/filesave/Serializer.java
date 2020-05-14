package org.binarytree.filesave;

import java.lang.reflect.Type;

public interface Serializer
{
    <T>String Serialize(T elem);
    <T> T Deserialize(String str, Type elementType);
}
