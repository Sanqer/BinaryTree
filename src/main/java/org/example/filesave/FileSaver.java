package org.example.filesave;

import org.example.utils.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class FileSaver implements Saver
{
    private static Logger log = LoggerFactory.getLogger(FileSaver.class.getSimpleName());

    @Override
    public boolean save(String str) {
        String path = Common.getDbPath();
        try (FileOutputStream stream = new FileOutputStream(path)) {
            stream.write(str.getBytes("UTF-8"));
            stream.flush();
            return true;
        } catch (IOException ex) {
            log.error("Error saving file: " + ex.getMessage());
            return false;
        }
    }
}
