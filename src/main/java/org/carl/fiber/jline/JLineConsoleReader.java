package org.carl.fiber.jline;

import jline.console.ConsoleReader;

import java.io.IOException;

/**
 * jline 控制台读取数据
 */
public class JLineConsoleReader implements JDBConsoleReader {

    /**
     * jline 读取
     */
    private final ConsoleReader consoleReader;

    /**
     * 提示语
     */
    private final String prompt;

    public JLineConsoleReader(ConsoleReader consoleReader, String prompt) {
        this.consoleReader = consoleReader;
        this.prompt = prompt;
    }

    @Override
    public String readLine() throws IOException {
        return consoleReader.readLine(prompt);
    }
}
