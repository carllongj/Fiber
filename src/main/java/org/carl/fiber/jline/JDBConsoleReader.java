package org.carl.fiber.jline;

import java.io.IOException;

public interface JDBConsoleReader {

    /**
     * 读取控制台输入的数据
     *
     * @throws IOException 抛出异常
     */
    String readLine() throws IOException;
}
