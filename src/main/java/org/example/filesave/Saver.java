package org.example.filesave;

import java.util.function.Supplier;

public interface Saver {
    boolean save(Supplier<String> lazy);
}
