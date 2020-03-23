package com.krt.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 下划线 驼峰互转工具类
 *
 * @author 殷帅
 * @version 1.0
 * @date 2017年04月11日
 */
public class GenUnderline2Camel {

    private static Pattern pattern1 = Pattern.compile("([A-Za-z\\d]+)(_)?");

    private static Pattern pattern2 = Pattern.compile("[A-Z]([a-z\\d]+)?");

    /**
     * 下划线转驼峰法
     *
     * @param line       源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line, boolean smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Matcher matcher = pattern1.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0)) : Character.toUpperCase(word.charAt(0)));
            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰法转下划线
     *
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String camel2Underline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuilder sb = new StringBuilder();
        Matcher matcher = pattern2.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toLowerCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String line = "I_HAVE_AN_IPANG3_PIG";
        String camel = underline2Camel(line, true);
        System.out.println(camel);
        System.out.println(camel2Underline(camel));
    }
}