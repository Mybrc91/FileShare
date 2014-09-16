package com.csyangchsh.fileshare.util;

import java.io.File;
import java.io.FileFilter;

/**
 * @author csyangchsh
 * 2014-08-28
 */
public class DefaultFileFilter implements FileFilter{
    @Override
    public boolean accept(File pathname) {
        if (pathname.isHidden()) {
            return false;
        } else {
            return true;
        }
    }
}
