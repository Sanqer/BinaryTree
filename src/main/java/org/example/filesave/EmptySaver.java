package org.example.filesave;

public class EmptySaver implements Saver
{
    @Override
    public boolean save(String str) {
        return true;
    }
}
