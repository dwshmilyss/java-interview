package com.yiban.danmu.entities;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author david.duan
 * @packageName com.yiban.danmu.entities
 * @className Comment
 * @date 2025/12/4
 * @description 弹幕内容对象
 */
@Data
public class Content implements Serializable {
    @Serial
    private static final long serialVersionUID = -1099387549150481924L;
    private long id;//每条弹幕的id
    private int userId;//发弹幕的用户ID
    private String content;//弹幕内容
}
