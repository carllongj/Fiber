package org.carl.fiber.jline;

import jline.console.ConsoleReader;
import jline.console.completer.ArgumentCompleter;
import jline.console.completer.Completer;
import jline.console.completer.FileNameCompleter;
import jline.console.completer.NullCompleter;
import jline.console.completer.StringsCompleter;
import org.carl.fiber.jline.compeleter.ClassNameCache;
import org.carl.fiber.jline.compeleter.ClassNameCompleter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
            // 添加命令列表的补全
            consoleReader.addCompleter(new StringsCompleter(commandListIndex));
        }
        // 单例
        final ClassNameCache classNameCache = new ClassNameCache();
        classNameCache.preLoad();

        // 添加ClassName相关的命令补全
        consoleReader.addCompleter(buildStopCommandCompleter(classNameCache));
        consoleReader.addCompleter(buildClassCommandCompleter(classNameCache));
        // 添加 sourcepath与 use 命令的文件路径补全
        consoleReader.addCompleter(buildSourceFileCompleter());
        return consoleReader;
    }

    /**
     * 构建使用源码路径的补全
     *
     * @return 返回补全的对象
     */
    private static Completer buildSourceFileCompleter() {
        List<Completer> completerList = new ArrayList<>();
        completerList.add(new StringsCompleter(Arrays.asList("use", "sourcepath")));
        completerList.add(new FileNameCompleter());
        completerList.add(new NullCompleter());
        return new ArgumentCompleter(completerList);
    }

    /**
     * 构建 class,methods,fields,print,dump,eval,run 等命令的参数补全
     *
     * @param classNameCache 类名称缓存
     * @return 返回结果
     */
    private static Completer buildClassCommandCompleter(ClassNameCache classNameCache) {
        List<Completer> completerList = new ArrayList<>();
        completerList.add(new StringsCompleter(Arrays.asList("class", "methods", "fields", "print", "dump", "eval", "run")));
        completerList.add(new ClassNameCompleter(classNameCache.getClassNameCache()));
        return new ArgumentCompleter(completerList);
    }

    /**
     * 添加 stop 命令的补全
     *
     * @param classNameCache 类缓存
     * @return 返回对应补全
     */
    private static Completer buildStopCommandCompleter(ClassNameCache classNameCache) {
        List<Completer> completerList = new ArrayList<>();
        completerList.add(new StringsCompleter(Collections.singletonList("stop")));
        completerList.add(new StringsCompleter(Arrays.asList("at", "in")));
        completerList.add(new ClassNameCompleter(classNameCache.getClassNameCache()));
        return new ArgumentCompleter(completerList);
    }
}
