package com.yiban.mock;

import org.springframework.stereotype.Service;

/**
 * @author david.duan
 * @packageName com.yiban.java.junit.mock
 * @className MemberService
 * @date 2025/10/23
 * @description
 */
@Service
public class MemberService {
    public String add(int uid) {
        System.out.println("come in add user, uid is " + uid);
        if (uid == -1) {
            throw new IllegalArgumentException("parameter is negative");
        }
        return "ok";
    }

    public int del(int uid) {
        System.out.println("come in delete uid,uid is " + uid);
        return uid;
    }
}
