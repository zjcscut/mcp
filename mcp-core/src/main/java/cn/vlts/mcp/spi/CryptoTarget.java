/*
 * MIT License
 *
 * Copyright (c) 2024 vlts.cn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cn.vlts.mcp.spi;

import java.lang.reflect.Field;

/**
 * 加解密目标
 *
 * @author throwable
 * @version v1
 * @description 加解密目标
 * @since 2024/1/19 15:40
 */
@FunctionalInterface
public interface CryptoTarget {

    /**
     * 返回唯一KEY
     */
    String key();

    default boolean isJavaField() {
        return false;
    }

    default String fieldName() {
        return null;
    }

    default Class<?> fieldType() {
        return null;
    }

    default Class<?> clazz() {
        return null;
    }

    default CryptoField cryptoField() {
        return null;
    }

    default boolean isMsId() {
        return false;
    }

    default boolean isRsmId() {
        return false;
    }

    /**
     * 基于Field构建目标
     *
     * @param field field
     * @return target
     */
    static CryptoTarget fromJavaField(Field field) {
        return JavaFieldTarget.newInstance(field);
    }

    /**
     * 基于MyBatis MsId构建目标
     *
     * @param msId     msId
     * @param property property
     * @return target
     */
    static CryptoTarget fromMsId(String msId, String property) {
        return MsIdTarget.newInstance(msId, property);
    }

    /**
     * 基于MyBatis RsmId构建目标
     *
     * @param rsmId    rsmId
     * @param property property
     * @return target
     */
    static CryptoTarget fromRsmId(String rsmId, String property) {
        return RsmIdTarget.newInstance(rsmId, property);
    }

    class JavaFieldTarget implements CryptoTarget {

        private final Field field;

        private JavaFieldTarget(Field field) {
            this.field = field;
        }

        public static JavaFieldTarget newInstance(Field field) {
            return new JavaFieldTarget(field);
        }

        @Override
        public String key() {
            return field.getDeclaringClass().getName() + "#" + field.getName();
        }

        @Override
        public String fieldName() {
            return field.getName();
        }

        @Override
        public Class<?> fieldType() {
            return field.getType();
        }

        @Override
        public Class<?> clazz() {
            return field.getDeclaringClass();
        }

        @Override
        public boolean isJavaField() {
            return true;
        }

        @Override
        public CryptoField cryptoField() {
            return field.getAnnotation(CryptoField.class);
        }
    }

    class MsIdTarget implements CryptoTarget {

        private final String msId;

        private final String property;

        private MsIdTarget(String msId, String property) {
            this.msId = msId;
            this.property = property;
        }

        public static MsIdTarget newInstance(String msId, String property) {
            return new MsIdTarget(msId, property);
        }

        @Override
        public String key() {
            return msId + "#" + property;
        }

        @Override
        public boolean isMsId() {
            return true;
        }

        @Override
        public String fieldName() {
            return property;
        }
    }

    class RsmIdTarget implements CryptoTarget {

        private final String rsmId;

        private final String property;

        private RsmIdTarget(String rsmId, String property) {
            this.rsmId = rsmId;
            this.property = property;
        }

        public static RsmIdTarget newInstance(String rsmId, String property) {
            return new RsmIdTarget(rsmId, property);
        }

        @Override
        public String key() {
            return rsmId + "#" + property;
        }

        @Override
        public String fieldName() {
            return property;
        }

        @Override
        public boolean isRsmId() {
            return true;
        }
    }
}
