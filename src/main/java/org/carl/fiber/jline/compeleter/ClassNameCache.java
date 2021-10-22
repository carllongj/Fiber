package org.carl.fiber.jline.compeleter;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassNameCache {
    /**
     * 通过属性配置
     */
    private static final String CLASS_PATH_PROPERTY_KEY = "java.class.path";

    /**
     * 文件协议前缀
     */
    private static final String FILE_PREFIX = "file";

    /**
     * jar包后缀
     */
    private static final String JAR_SUFFIX = "jar";

    /**
     * class文件后缀
     */
    private static final String CLASS_SUFFIX = ".class";

    /**
     * 判断当前是否为结束标记
     */
    private static final Object END_FLAG = new Object();

    /**
     * JAR包中的路径分隔符
     */
    private static final String PATH_SPLIT = "/";

    /**
     * 内部类标识
     */
    private static final char INNER_CLASS_FLAG = '$';


    /**
     * 类名称缓存
     */
    private Map<String, Object> classNameCache = new HashMap<>();

    /**
     * 是否忽略掉匿名内部类
     */
    private boolean ignoreAnonymousInnerClassName = true;

    /**
     * 是否忽略掉内部类,该选项用以将内部类进行转换,添加一层类结构
     */
    private boolean ignoreInnerClassName = false;

    /**
     * 是否忽略匿名内部类名称,该选项为false,则内部类忽略也必须为false.
     *
     * @param ignoreAnonymousInnerClassName 忽略匿名内部类名称
     */
    public void setIgnoreAnonymousInnerClassName(boolean ignoreAnonymousInnerClassName) {
        this.ignoreAnonymousInnerClassName = ignoreAnonymousInnerClassName;
    }

    /**
     * 是否忽略内部类名称,若该选项为true,则 {@link ClassNameCache#ignoreAnonymousInnerClassName} 也必须为true
     * 也就表示忽略整个的内部类
     *
     * @param ignoreInnerClassName 是否忽略内部类名称
     */
    public void setIgnoreInnerClassName(boolean ignoreInnerClassName) {
        this.ignoreInnerClassName = ignoreInnerClassName;
    }

    /**
     * 返回不可变的Map
     *
     * @return 返回不可变的类索引缓存
     */
    public Map<String, Object> getClassNameCache() {
        return Collections.unmodifiableMap(classNameCache);
    }

    /**
     * note
     *
     * <code>
     * <p>
     * // 此种此方式只能加载 AppContextLoader 所加载的库,java的库无法加载
     * ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
     * List<URL> urlList = new ArrayList<>();
     * if (classLoader instanceof URLClassLoader) {
     *    URL[] classpathURLList = ((URLClassLoader) classLoader).getURLs();
     *      if (Objects.nonNull(classpathURLList) && classpathURLList.length != 0) {
     *          urlList.addAll(Arrays.asList(classpathURLList));
     *      }
     * }
     *
     *
     * </code>
     * 加载整个类路径中的所有文件,建立对应的文件列表索引
     */
    public void preLoad() {

        // 检查内部的字段状态
        checkInnerClassLoaded();

        List<URL> urlList = new ArrayList<>();

        // 通过属性配置获取
        String classpathValue = System.getProperty(CLASS_PATH_PROPERTY_KEY);
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

        if (urlList.isEmpty()) {
            return;
        }

        // 遍历所有的 classpath
        for (URL url : urlList) {
            try {
                if (Objects.equals(url.toURI().getScheme(), FILE_PREFIX)) {
                    File file = new File(url.toURI());
                    if (!file.exists()) {
                        continue;
                    }

                    // classpath 只能为目录或者jar包文件,其它情况则忽略
                    // 为目录
                    if (file.isDirectory()) {
                        // 递归扫描该目录下的所有目录与文件
                        scanDiskFiles(file);
                    } else if (file.getName().endsWith(JAR_SUFFIX)) {
                        JarFile jarFile = new JarFile(file);
                        Enumeration<JarEntry> entries = jarFile.entries();
                        while (entries.hasMoreElements()) {
                            // 获取Jar包中的文件,一个文件或者目录都是一个Entry
                            JarEntry jarEntry = entries.nextElement();
                            // 当前为文件且为 .class 文件,则舍弃其后缀
                            if (!jarEntry.isDirectory() && jarEntry.getName().endsWith(CLASS_SUFFIX)) {
                                // 获取目录的名称
                                String entryName = jarEntry.getName();
                                indexPath(entryName.substring(0, entryName.length() - CLASS_SUFFIX.length()));
                            } else {
                                // 普通文件和文件夹则按照正常索引
                                indexPath(jarEntry.getName());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // ignore
            }
        }
    }

    private void checkInnerClassLoaded() {
        if (ignoreInnerClassName && !ignoreAnonymousInnerClassName) {
            throw new IllegalStateException("ignoreInnerClassName is true but ignoreAnonymousInnerClassName is false");
        }
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
            indexPath(formatFilePath(rootPath, file));
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
     * @return 返回格式化完成的路径, 若返回null, 则将忽略该路径
     */
    private String formatFilePath(String rootPath, File filePath) {
        // 获取到相对路径地址
        String filePathName = filePath.getPath();
        // 截取到相对路径地址
        String relativePath = filePathName.substring(rootPath.length());
        // 格式化改路径
        String normalizeFile = reNormalizeFileIfNeed(relativePath);
        // 由于此处已经为相对路径,需要截取掉绝对路径开头的斜杠
        if (normalizeFile.startsWith(PATH_SPLIT)) {
            normalizeFile = normalizeFile.substring(1);
        }
        // 文件若以 .class 结尾,则忽略掉其后缀
        if (normalizeFile.endsWith(CLASS_SUFFIX)) {
            // 定义父级路径
            String parent = "";
            // 是否包含多级路径
            if (normalizeFile.contains(PATH_SPLIT)) {
                // 获取父级别路径
                parent = normalizeFile.substring(0, normalizeFile.lastIndexOf('/') + 1);
            }
            // 文件名
            String fileName = filePath.getName().substring(0, normalizeFile.length() - 6);
            // 该类为内部类
            if (fileName.indexOf(INNER_CLASS_FLAG) != -1) {
                // 需要建立索引的情况
                if (needIndexFile(fileName)) {
                    return parent + fileName.replace(INNER_CLASS_FLAG, '/');
                }
                // 其他情况则均不建立索引
                return null;
            }

            return normalizeFile;
        }
        //非class文件则直接返回,不去掉后缀
        return normalizeFile;
    }

    /**
     * 两种情况下建立索引
     * 1. 都不进行忽略,则所有的内部类建立索引
     * 2. 忽略匿名内部类,但是不忽略非匿名内部类
     *
     * @param fileName 当前的文件名称,不包含后缀
     * @return 返回是否建立索引
     */
    private boolean needIndexFile(String fileName) {
        return (!ignoreInnerClassName && !ignoreAnonymousInnerClassName)
            || (!ignoreInnerClassName && ignoreAnonymousInnerClassName &&
            !isAnonymousInnerClass(fileName) && !isNestedAnonymousInnerClass(fileName));
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
     * 根据文件名判断是否为匿名内部类
     * 匿名内部类的格式为 <code>OuterClass$数字</code>
     *
     * @param fileName 指定的文件名
     * @return 返回是否为匿名内部类
     */
    private boolean isAnonymousInnerClass(String fileName) {
        try {
            Integer.parseInt(fileName.substring(fileName.lastIndexOf(INNER_CLASS_FLAG) + 1));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断是否为 匿名内部类中嵌套的非匿名内部类
     *
     * @param fileName 指定的文件名称
     * @return 返回是否为匿名内部类中嵌套的非匿名内部类
     */
    private boolean isNestedAnonymousInnerClass(String fileName) {
        String[] classNames = fileName.split("\\$");
        for (String name : classNames) {
            try {
                Integer.parseInt(name);
                return true;
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        return false;
    }

    /**
     * 建立当前路径的索引
     *
     * @param path 指定的路径,若为null,则忽略
     */
    @SuppressWarnings("unchecked")
    private void indexPath(String path) {

        // 为 null 则忽略该路径
        if (Objects.isNull(path)) {
            return;
        }

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
