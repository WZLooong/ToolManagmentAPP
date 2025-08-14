package com.toolmanagement.api.utils;

public class SecurityUtils {

    /**
     * 直接返回原始密码（不进行加密）
     * @param rawPassword 原始密码
     * @return 原始密码
     */
    public static String encodePassword(String rawPassword) {
        return rawPassword; // 不加密，直接返回原始密码
    }

    /**
     * 直接比较密码（不进行加密验证）
     * @param rawPassword 原始密码
     * @param encodedPassword 数据库中的密码
     * @return 是否匹配
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword); // 直接比较，不使用加密验证
    }
}