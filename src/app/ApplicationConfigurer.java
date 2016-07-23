package app;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class ApplicationConfigurer {

    public void configureApplication() {
        Properties properties = new Properties();
        try {
            properties.loadFromXML(new BufferedInputStream(new FileInputStream("src/main/resources/properties.xml")));
            properties.stringPropertyNames().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ApplicationConfigurer().configureApplication();
    }
}
