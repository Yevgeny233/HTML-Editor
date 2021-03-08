package com.javarush.task.task32.task3209;


import javax.swing.filechooser.FileFilter;
import java.io.File;

public class HTMLFileFilter extends FileFilter {


    @Override
    public boolean accept(File pathname) {
        boolean a = false;
        String s = pathname.getName();
       if(s.toLowerCase().endsWith(".html") || s.toLowerCase().endsWith(".htm") || pathname.isDirectory())
        a = true;
           return a;

    }

    @Override
    public String getDescription() {
        return "HTML и HTM файлы";
    }
}
