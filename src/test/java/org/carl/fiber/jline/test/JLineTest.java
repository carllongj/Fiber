package org.carl.fiber.jline.test;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class JLineTest {

    public static void main(String[] args) throws IOException {
        Properties properties = System.getProperties();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
