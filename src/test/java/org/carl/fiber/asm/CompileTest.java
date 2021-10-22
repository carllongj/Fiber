package org.carl.fiber.asm;

import jline.console.ConsoleReader;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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

    @Test
    public void loadClass() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<URL> urlList = new ArrayList<>();
        // 获取到所有的依赖以及路径
        if (classLoader instanceof URLClassLoader) {
            URL[] classpathURLList = ((URLClassLoader) classLoader).getURLs();
            // 添加所有的jar包路径
            if (Objects.nonNull(classpathURLList) && classpathURLList.length != 0) {
                urlList.addAll(Arrays.asList(classpathURLList));
            }
        }

        // 若该集合为空,则尝试通过属性来获取
        if (urlList.isEmpty()) {
            // 通过属性配置获取
            String classpathValue = System.getProperty("java.class.path");
            if (Objects.nonNull(classpathValue) && classpathValue.trim().length() > 0) {
                String[] pathList = classpathValue.split(File.pathSeparator, -1);
                if (pathList.length > 0) {
                    for (String path : pathList) {
                        try {
                            urlList.add(new File(path).toURI().toURL());
                        } catch (Exception e) {
                            // ignore
                        }
                    }
                }
            }
        }

        if (urlList.isEmpty()) {
            return;
        }

        // 遍历所有的 classpath
        for (URL url : urlList) {
            try {
                if (Objects.equals(url.toURI().getScheme(), "file")) {
                    File file = new File(url.toURI());
                    if (!file.exists()) {
                        continue;
                    }

                    // 为目录
                    if (file.isDirectory()) {
                        // 递归扫描该目录下的所有目录与文件

                    } else {
                        if (file.getName().endsWith("jar")) {
                            JarFile jarFile = new JarFile(file);
                            Enumeration<JarEntry> entries = jarFile.entries();
                            while (entries.hasMoreElements()) {
                                JarEntry jarEntry = entries.nextElement();
                                if (jarEntry.isDirectory()) {
                                    indexPath(jarEntry.getName());
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // ignore
            }
        }
        System.out.println(classNameCache);
    }

    private static final Object END_FLAG = new Object();

    /**
     * 类缓存
     */
    private Map<String, Object> classNameCache = new HashMap<>();

    /**
     * JAR包中的路径分隔符
     */
    private static final String PATH_SPLIT = "/";


    @Test
    public void test1() {
        File file = new File("target/classes");
        scanDiskFiles(file);
        System.out.println(classNameCache);
    }

    /**
     * 扫描指定目录下的磁盘文件,该文件的路径必须该file的相对路径索引
     * e.g,<code>/opt/dev/project/classes/</code> 为classpath时,其下存在 <code>org/carl/jdb/JDBLoader.class</code>
     * 其索引路径则为 <code>org/carl/jdb/JDBLoader</code>,非<code>/opt/dev/project/classes/org/carl/jdb/JDBLoader</code>
     *
     * @param rootFile classpath设定的路径
     */
    private void scanDiskFiles(final File rootFile) {
        // 执行扫描磁盘文件路径
        doScanDiskFiles(rootFile.getPath(), rootFile);
    }

    /**
     * 执行扫描对应的文件逻辑
     *
     * @param rootPath 根路径
     * @param file     指定的文件
     */
    private void doScanDiskFiles(String rootPath, File file) {
        if (file.isFile()) {
            indexPath(formatFilePath(rootPath, file.getPath()));
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (Objects.nonNull(files) && files.length > 0) {
                for (File childFile : files) {
                    doScanDiskFiles(rootPath, childFile);
                }
            }
        }
    }

    /**
     * 格式化 文件路径
     *
     * @param rootPath classpath的根路径(可能为相对路径或者绝对路径)
     * @param filePath 当前的文件路径
     */
    private String formatFilePath(String rootPath, String filePath) {
        // 截取到相对路径地址
        String relativePath = filePath.substring(rootPath.length());
        // 格式化改路径
        String normalizeFile = reNormalizeFileIfNeed(relativePath);
        // 由于此处已经为相对路径,需要截取掉绝对路径开头的斜杠
        if (normalizeFile.startsWith(PATH_SPLIT)) {
            normalizeFile = normalizeFile.substring(1);
        }
        return normalizeFile;
    }

    /**
     * 格式化 win 下的路径分隔符,方便建立文件路径索引
     *
     * @param path 文件路径地址
     * @return 返回替换后的文件路径 <code>/</code> 分隔
     */
    private String reNormalizeFileIfNeed(String path) {
        return path.replace('\\', '/');
    }


    /**
     * 建立当前路径的索引
     *
     * @param path 指定的路径
     */
    private void indexPath(String path) {
        Objects.requireNonNull(path);
        String[] split = path.split(PATH_SPLIT);
        if (split.length == 0) {
            return;
        }
        Map<String, Object> current = classNameCache;
        for (int i = 0; i < split.length; i++) {
            String name = split[i];
            // 该路径的最后一层,若为文件,则记录最后标识位
            if (i == split.length - 1) {
                // 写入当前的层级信息
                current.putIfAbsent(name, END_FLAG);
                break;
            }
            Object value = current.get(name);

            // 之前未添加过该路径,或者为最后一层级路径
            if (Objects.isNull(value) || value == END_FLAG) {
                // 存入一个信息路径信息,由于存在下一层路径
                Map<String, Object> newMap = new HashMap<>();
                current.put(name, newMap);
                current = newMap;
                continue;
            }
            //强制转换
            current = (Map<String, Object>) value;
        }
    }
}
