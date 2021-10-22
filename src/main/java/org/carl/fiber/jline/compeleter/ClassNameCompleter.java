package org.carl.fiber.jline.compeleter;

import jline.console.completer.Completer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ClassNameCompleter implements Completer {

    /**
     * 输入时的路径分隔符
     */
    private static final String INPUT_PACKAGE_SEPARATOR = ".";

    /**
     * 缓存Map
     */
    private final Map<String, Object> classNameCache;

    public ClassNameCompleter(Map<String, Object> classNameCache) {
        this.classNameCache = classNameCache;
    }

    @Override
    public int complete(String buffer, int cursor, List<CharSequence> candidates) {
        return findCandidates(buffer, cursor, candidates);
    }

    /**
     * 进行检索缓存中的索引,进行匹配
     *
     * @param buffer     输入的
     * @param cursor     当前光标的所处位置
     * @param candidates 所有的匹配集合
     * @return 返回对应光标的位置
     */
    @SuppressWarnings("unchecked")
    private int findCandidates(String buffer, int cursor, List<CharSequence> candidates) {

        // 收集所有的candidates
        List<PackagePresent> candidatesCollection = new ArrayList<>();

        if (Objects.isNull(buffer)) {
            findMatchCandidates("", classNameCache, candidatesCollection);
            transformCandidates(candidatesCollection, candidates);
            return 0;
        }

        // 输入的buffer未包含package分隔符
        if (!buffer.contains(INPUT_PACKAGE_SEPARATOR)) {
            findMatchCandidates(buffer, classNameCache, candidatesCollection);
            transformCandidates(candidatesCollection, candidates);
            return 0;
        }

        // 输入的数据包含了对应的package,则进行切分寻找
        // 切分对应的输入包
        String[] packageLevels = buffer.split("\\.", -1);
        // 临时变量存储classNameCache
        Map<String, Object> current = classNameCache;
        // 遍历输入的每一级别的包,找到符合的包
        for (int i = 0; i < packageLevels.length; i++) {
            // 获取到当前的packageName名称
            String packageName = packageLevels[i];
            // 最后一层的输入,则进行查找,
            if (i == packageLevels.length - 1) {
                findMatchCandidates(packageName, current, candidatesCollection);
            } else {
                // 继续匹配到下一个层级
                Object value = current.get(packageName);
                if (Objects.nonNull(value)) {
                    if (value instanceof Map) {
                        current = (Map<String, Object>) value;
                        continue;
                    }
                    return -1;
                }
            }
        }

        // 转换 candidates集合
        transformCandidates(candidatesCollection, candidates);

        // 检索匹配的路径
        return buffer.length() - packageLevels[packageLevels.length - 1].length();
    }

    /**
     * 转换最后的 candidates 数据
     *
     * @param candidatesCollection packagePresent结果集合
     * @param candidates           结果集合
     */
    private void transformCandidates(List<PackagePresent> candidatesCollection, List<CharSequence> candidates) {
        // 判断最终收集的candidates,若只有一个,则判断是否为package.若为package则补一个package分隔符
        if (candidatesCollection.size() == 1) {
            PackagePresent packagePresent = candidatesCollection.get(0);
            if (packagePresent.getPackagePresent()) {
                candidates.add(packagePresent.candidates + INPUT_PACKAGE_SEPARATOR);
            } else {
                // 若不为package,则直接补足该类名称
                candidates.add(packagePresent.candidates);
            }
        } else {
            // 进行字典排序
            Collections.sort(candidatesCollection);
            // 添加所有的candidates
            for (PackagePresent packagePresent : candidatesCollection) {
                candidates.add(packagePresent.getCandidates());
            }
        }
    }

    /**
     * 查找匹配的
     *
     * @param buffer     用以匹配的起始字符
     * @param cacheMap   缓存查找列表
     * @param collection 结果集
     */
    private void findMatchCandidates(String buffer, Map<String, Object> cacheMap, List<PackagePresent> collection) {
        for (Map.Entry<String, Object> entry : cacheMap.entrySet()) {
            if (entry.getKey().startsWith(buffer)) {
                // 当前是 package
                if (entry.getValue() instanceof Map) {
                    collection.add(new PackagePresent(entry.getKey(), true));
                } else {
                    collection.add(new PackagePresent(entry.getKey(), false));
                }
            }
        }
    }

    private static class PackagePresent implements Comparable<PackagePresent> {
        /**
         * 当前的 candidates
         */
        private final String candidates;

        /**
         * 当前是否为package
         */
        private final boolean packagePresent;

        public PackagePresent(String candidates, boolean packagePresent) {
            this.candidates = candidates;
            this.packagePresent = packagePresent;
        }

        public String getCandidates() {
            return candidates;
        }

        public boolean getPackagePresent() {
            return packagePresent;
        }

        @Override
        public int compareTo(PackagePresent o) {
            if (o == null) {
                return 1;
            }
            if (this.candidates == null) {
                return -1;
            }

            if (o.getCandidates() == null) {
                return 1;
            }

            return this.candidates.compareTo(o.getCandidates());
        }
    }
}
