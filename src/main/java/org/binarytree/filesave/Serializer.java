package org.binarytree.filesave;

import java.lang.reflect.Type;

public interface Serializer
{
    <T>String serialize(T elem);
    <T> T deserialize(String str, Type elementType);
}
