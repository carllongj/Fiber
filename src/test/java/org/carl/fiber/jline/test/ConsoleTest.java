package org.carl.fiber.jline.test;

import org.testng.annotations.Test;

import java.io.Console;
import java.util.Objects;

public class ConsoleTest {

    @Test(enabled = false)
    public void testReadPassword() {
        // 单元测试该对象为null
        Console console = System.console();
        if (Objects.nonNull(console)) {
            char[] password = console.readPassword();
            System.out.println(new String(password));
        }
    }
}
