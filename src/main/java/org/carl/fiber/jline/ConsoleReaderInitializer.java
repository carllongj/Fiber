package org.carl.fiber.jline;

import jline.console.ConsoleReader;
import jline.console.completer.ArgumentCompleter;
import jline.console.completer.Completer;
import jline.console.completer.FileNameCompleter;
import jline.console.completer.NullCompleter;
import jline.console.completer.StringsCompleter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 控制台读取类初始化器
 */
public class ConsoleReaderInitializer {

    /**
     * ConsoleReader的主类
     */
    public static final String CONSOLE_READER_CLASS = "jline.console.ConsoleReader";

    /**
     * 传入JDB对应的命令行参数,用于作为 completer 的提示信息
     *
     * @param commandList JDB的命令参数定义,{@code commandList[i][0] 为命令}
     * @return 返回控制台读取
     */
    public static ConsoleReader initConsoleReader(String[][] commandList) throws IOException {
        ConsoleReader consoleReader = new ConsoleReader();
        List<Completer> completerList = new ArrayList<>();

        // 添加命令行参数
        if (Objects.nonNull(commandList)) {
            List<String> commandListIndex = new ArrayList<>(commandList.length);
            for (String[] strings : commandList) {
                commandListIndex.add(strings[0]);
            }
            completerList.add(new StringsCompleter(commandListIndex));
        }

        completerList.add(new FileNameCompleter());
        completerList.add(new NullCompleter());

        ArgumentCompleter completer = new ArgumentCompleter(completerList);
        consoleReader.addCompleter(completer);
        return consoleReader;
    }
}
