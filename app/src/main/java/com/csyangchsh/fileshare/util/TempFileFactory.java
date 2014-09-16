package com.csyangchsh.fileshare.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author csyangchsh
 * 2014-08-28
 */
public class TempFileFactory {

    private final String tmpDir;
    private final List<File> tempFiles;

    public TempFileFactory() {
        //FIXME use android temp dir?
        tmpDir = System.getProperty("java.io.tmpdir");
        tempFiles = new ArrayList<File>();
    }

    public File createTempFile() throws IOException {
        File tempFile = File.createTempFile("FileShare-", "", new File(tmpDir));
        tempFiles.add(tempFile);
        return tempFile;
    }

    public void destroy() {
        for (File file : tempFiles) {
            try {
                file.delete();
            } catch (Exception ignored) {
            }
        }
        tempFiles.clear();
    }
}
