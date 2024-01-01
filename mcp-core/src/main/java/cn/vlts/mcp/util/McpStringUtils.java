package cn.vlts.mcp.util;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author throwable
 * @version v1
 * @description 字符串工具类
 * @since 2023/12/20 9:45
 */
public enum McpStringUtils {

    /**
     * 单例
     */
    X;

    private static final String EMPTY = "";

    public int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    public boolean isBlank(final CharSequence cs) {
        final int strLen = length(cs);
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    public boolean isNotEmpty(final CharSequence cs) {
        return Objects.nonNull(cs) && cs.length() > 0;
    }

    public boolean isEmpty(final CharSequence cs) {
        return Objects.isNull(cs) || cs.length() == 0;
    }

    public static int length(final String str) {
        return Objects.nonNull(str) ? str.length() : 0;
    }

    public static String repeat(final char ch, final int repeat) {
        if (repeat <= 0) {
            return EMPTY;
        }
        final char[] buf = new char[repeat];
        Arrays.fill(buf, ch);
        return new String(buf);
    }

    public static String mustLeft(final String str, final int len) {
        return str.substring(0, len);
    }

    public static String mustRight(final String str, final int len) {
        return str.substring(str.length() - len);
    }

    public static String left(final String str, final int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(0, len);
    }

    public static String right(final String str, final int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(str.length() - len);
    }

    public static String leftPad(final String str, final int size, final char padChar) {
        if (str == null) {
            return null;
        }
        final int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        return repeat(padChar, pads).concat(str);
    }

    public static String rightPad(final String str, final int size, final char padChar) {
        if (str == null) {
            return null;
        }
        final int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        return str.concat(repeat(padChar, pads));
    }

    public static boolean startsWith(final String str, final String prefix) {
        if (str == null) {
            return false;
        }
        return str.startsWith(prefix);
    }
}
