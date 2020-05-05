package org.example.filesave;

import org.apache.commons.io.IOUtils;
import org.example.utils.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;

public class FileLoader implements Loader
{
    private static Logger log = LoggerFactory.getLogger(FileLoader.class.getSimpleName());

    @Override
    public String load() {
        String path = Common.getDbPath();
        try (FileInputStream stream = new FileInputStream(path)) {
            return IOUtils.toString(stream, "UTF-8");
        } catch (IOException ex) {
            log.error("Error loading file: " + ex.getMessage());
            return null;
        }
    }
}
