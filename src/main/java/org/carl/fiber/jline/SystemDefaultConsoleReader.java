package org.carl.fiber.jline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * JDB默认的读取逻辑
 */
public class SystemDefaultConsoleReader implements JDBConsoleReader {

    private static final BufferedReader READER =
        new BufferedReader(new InputStreamReader(System.in));

    @Override
    public String readLine() throws IOException {
        return READER.readLine();
    }

}
