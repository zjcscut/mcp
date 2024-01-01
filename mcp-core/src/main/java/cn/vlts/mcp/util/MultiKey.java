package cn.vlts.mcp.util;

import java.util.Arrays;

/**
 * 多个KEY
 *
 * @author throwable
 * @version v1
 * @description 多个KEY
 * @since 2023/12/26 11:52
 */
public final class MultiKey {

    private final String[] keys;

    public MultiKey(String[] keys) {
        this.keys = keys;
    }

    public static MultiKey newInstance(String... keys) {
        return new MultiKey(keys);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MultiKey multiKey = (MultiKey) o;
        return Arrays.equals(keys, multiKey.keys);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(keys);
    }
}
