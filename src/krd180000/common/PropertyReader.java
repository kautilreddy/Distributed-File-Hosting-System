package krd180000.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class PropertyReader {
    public static Properties read(String fileName){
        Properties prop = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream(fileName);
            prop.load(is);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return prop;
    }
}
