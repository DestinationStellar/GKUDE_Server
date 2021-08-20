package com.tsingle.gkude_server.utils;

import lombok.NonNull;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class MD5Util {
    private static final String salt = "#tsingle%";

    public static String getMD5(@NonNull String string) {
        return DigestUtils.md5DigestAsHex((string + salt).getBytes(StandardCharsets.UTF_8));
    }
}
