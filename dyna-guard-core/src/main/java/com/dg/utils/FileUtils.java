package com.dg.utils;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author VD
 * @date 2025/8/7 21:01
 */
public class FileUtils {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 绝对路径判断正则 Windows 绝对路径：以盘符开头（如 C:\ 或 C:/），或 UNC 路径（\\server\share） Linux/macOS 绝对路径：以 / 开头
     */
    private static final Pattern PATTERN_PATH_ABSOLUTE = Pattern.compile("^[a-zA-Z]:([/\\\\].*)?");

    /**
     * 文件扩展名分隔符
     */
    private static final String FILE_EXTENSION_SEPARATOR = ".";

    /**
     * 判断路径是否为绝对路径
     *
     * @param path 路径
     * @return 是否为绝对路径
     */
    public static Boolean isAbsolutePath(String path) {
        if (StringUtils.isNotBlank(path)) {
            return '/' == path.charAt(0) || PATTERN_PATH_ABSOLUTE.matcher(path).matches();
        }
        return false;
    }

    /**
     * 获取路径的文件匹配规则
     *
     * @param path 路径
     * @return 文件匹配规则
     */
    public static String getWildcardMatch(String path) {
        if (StringUtils.isNotBlank(path)) {
            if (!path.contains(FILE_EXTENSION_SEPARATOR)) {
                return null;
            }
            // 找到最后一个路径分隔符的位置（处理带路径的文件名）
            int lastSeparatorIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
            return path.substring(lastSeparatorIndex + 1);
        }
        return null;
    }

    /**
     * 检查单个文件是否匹配用户输入的通配符模式
     *
     * @param fileName        文件名称
     * @param wildcardPattern 通配符模式
     * @return true：匹配；false：不匹配
     */
    public static boolean matches(String fileName, String wildcardPattern) {
        if (StringUtils.isBlank(wildcardPattern)) {
            return true;
        }
        fileName = fileName.toLowerCase();
        wildcardPattern = wildcardPattern.toLowerCase();
        return FilenameUtils.wildcardMatchOnSystem(fileName, wildcardPattern);
    }

    /**
     * 获取文件绝对路径
     *
     * @param path          路径
     * @param wildcardMatch 文件匹配规则
     * @return 文件绝对路径
     */
    public static List<String> getFileAbsolutePath(String path, String wildcardMatch) {
        List<String> result = Lists.newArrayList();
        if (StringUtils.isBlank(path)) {
            return result;
        }
        // 判断是否包含通配符如果包含通配符，进行解析
        if (path.contains("*")) {
            String dirPath = getDirPath(path);
            traverseFolder(dirPath, wildcardMatch, result);
        } else {
            // 如果不包含通配符，判断是否为文件，如果为文件则添加到结果列表中
            if (new File(path).isFile()) {
                result.add(path);
            } else {
                // 如果不是文件，判断是否为目录，如果为目录则进行遍历
                traverseFolder(path, wildcardMatch, result);
            }
        }
        return result;
    }

    /**
     * 获取文件列表
     *
     * @param path 路径
     * @return 文件列表
     */
    public static String getDirPath(String path) {
        String[] dirList = path.split("/");
        StringBuilder dirPath = new StringBuilder();
        for (String dir : dirList) {
            if (!dir.contains("*")) {
                dirPath.append(dir).append(File.separator);
            } else {
                dirPath.deleteCharAt(dirPath.length() - 1);
                break;
            }
        }
        return dirPath.toString();
    }

    /**
     * 递归遍历文件夹，收集所有文件
     *
     * @param path          当前文件夹
     * @param wildcardMatch 匹配模式
     * @param pathList      存储文件的列表
     */
    public static void traverseFolder(String path, String wildcardMatch, List<String> pathList) {
        File[] files = new File(path).listFiles();
        if (files != null) {
            for (File file : files) {
                String absolutePath = file.getAbsolutePath();
                if (file.isDirectory()) {
                    // 递归处理子文件夹
                    traverseFolder(absolutePath, wildcardMatch, pathList);
                } else {
                    if (matches(file.getName(), wildcardMatch)) {
                        // 收集文件绝对路径
                        pathList.add(absolutePath);
                    }
                }
            }
        }
    }

    /**
     * 获取文件内容
     *
     * @param path 文件路径
     * @return 文件内容
     */
    public static String getFileInfo(String path) {
        try {
            return Files.asCharSource(new File(path), StandardCharsets.UTF_8).read();
        } catch (Exception e) {
            log.warn("file to content fail , path : [{}]", path, e);
        }
        return null;
    }

    /**
     * 获取文件内容
     *
     * @param file 文件
     * @return 文件内容
     */
    public static String getFileInfo(File file) {
        try {
            return Files.asCharSource(file, StandardCharsets.UTF_8).read();
        } catch (Exception e) {
            log.warn("file to content fail ", e);
        }
        return null;
    }
}
