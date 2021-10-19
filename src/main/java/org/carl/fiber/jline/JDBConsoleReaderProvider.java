package org.carl.fiber.jline;

import org.carl.fiber.asm.ClassUtils;

public class JDBConsoleReaderProvider {

    /**
     * 打印提示语
     */
    private static final String PROMPT = ">";

    /**
     * 获取控制台读取器
     *
     * @param commandList 命令列表
     * @return 返回控制台读取
     * @throws Exception 抛出异常
     */
    public static JDBConsoleReader provide(String[][] commandList) throws Exception {
        /* --- 添加额外的控制台输入控制 ---- */
        if (ClassUtils.isPresent(ConsoleReaderInitializer.CONSOLE_READER_CLASS)) {
            return new JLineConsoleReader(ConsoleReaderInitializer.initConsoleReader(commandList), PROMPT);
        }
        return new SystemDefaultConsoleReader();
    }
}
