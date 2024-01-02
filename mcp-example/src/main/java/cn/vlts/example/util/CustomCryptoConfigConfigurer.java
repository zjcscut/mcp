package cn.vlts.example.util;

import cn.vlts.mcp.crypto.CryptoConfig;
import cn.vlts.mcp.crypto.CryptoConfigConfigurer;
import cn.vlts.mcp.spi.CryptoField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author throwable
 * @version v1
 * @description
 * @since 2024/1/2 11:46
 */
@Slf4j
@Component
public class CustomCryptoConfigConfigurer implements CryptoConfigConfigurer {

    @Override
    public void apply(Field field, CryptoField cryptoField, CryptoConfig config) {
        if (Objects.nonNull(field)) {
            if (Objects.equals(field.getName(), "foo")){
                config.setKey("1111111122222222");
            }
        }
    }
}
