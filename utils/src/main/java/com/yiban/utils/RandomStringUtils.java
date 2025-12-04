package com.yiban.utils;

import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomStringUtils {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 适用于一般用途，比如生成普通随机 ID。
     * @param length
     * @return
     */
    public static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    private static final SecureRandom random = new SecureRandom();

    /**
     * 用于安全场景，如生成 token、验证码等。
     * @param length
     * @return
     */
    public static String generateSecureString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static String generate(int length) {
        Random random = new Random();
        return random.ints(length, 0, CHARACTERS.length())
                .mapToObj(CHARACTERS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    public static void main(String[] args) {
        System.out.println(generateRandomString(10)); // 示例输出：aZ8kL2xPqR
        System.out.println(generateSecureString(10)); // 示例输出：aZ8kL2xPqR
        System.out.println(generate(10));
    }
}
