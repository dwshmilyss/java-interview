package com.yiban.java;

import java.util.Scanner;

/**
 * @author david.duan
 * @packageName com.yiban.java
 * @className PointExchange
 * @date 2025/11/4
 * @description 兑换积分小程序代码
 */
public class PointExchange {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int points = 20; // 初始积分
        int needPoints;  // 每次兑换所需积分

        System.out.println("----------积分兑换小程序----------");
        System.out.println("1. 铅笔所需积分为: 1");
        System.out.println("2. 橡皮所需积分为: 2");
        System.out.println("3. 作业本所需积分为: 3");
        System.out.println("4. 文具盒所需积分为: 5");

        while (true) {
            System.out.print("请输入兑换的物品编号(1-4,输入0表示退出): ");
            int choice = scanner.nextInt();

            if (choice == 0) {
                System.out.println("感谢使用积分兑换小程序,再见!");
                break;
            }
            switch (choice) {
                case 1:
                    needPoints = 1;
                    break;
                case 2:
                    needPoints = 2;
                    break;
                case 3:
                    needPoints = 3;
                    break;
                case 4:
                    needPoints = 5;
                    break;
                default:
                    System.out.println("输入错误，请重新输入!");
                    continue;
            }
            if (points < needPoints) {
                System.out.println("积分不足，无法兑换!");
                continue;
            }
            // 执行兑换
            points -= needPoints;
            System.out.printf("成功兑换【物品编号 %d】，所需积分: %d，剩余积分: %d%n",
                    choice, needPoints, points);

            // 特殊逻辑：若还有积分且想继续兑换
            if (points > 0) {
                System.out.print("是否还想继续兑换？(输入物品编号继续，输入0结束): ");
                // 这里继续循环，等待下次输入
            } else {
                System.out.println("积分已用完，兑换结束。");
                break;
            }

        }
        scanner.close();
    }
}
