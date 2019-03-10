package krd180000.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class PropertyReader {
    public static Properties read(){
        Properties prop = new Properties();
        Scanner in = new Scanner(System.in);
        InputStream is = null;
        String fileName = in.nextLine();
        try {
            is = new FileInputStream(fileName);
            prop.load(is);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return prop;
    }
}
