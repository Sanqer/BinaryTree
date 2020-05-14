package org.binarytree.filesave;

import java.util.function.Supplier;

public class EmptySaver implements Saver
{
    @Override
    public boolean save(Supplier<String> lazy) {
        return true;
    }
}
