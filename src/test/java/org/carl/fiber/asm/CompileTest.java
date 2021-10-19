package org.carl.fiber.asm;

import jline.console.ConsoleReader;

import java.util.Objects;

public class CompileTest {

    public static void main(String[] args) throws Exception {
        ConsoleReader consoleReader = new ConsoleReader();
        while (true) {
            String line = consoleReader.readLine("> ");
            if (Objects.equals(line, "quit")) {
                break;
            }
            System.out.println(line);
        }
    }
}
