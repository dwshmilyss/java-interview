package com.yiban.java.GOF.behavioral_patterns;

/**
 * @author david.duan
 * @packageName com.yiban.java.GOF.behavioral_patterns
 * @className StrategyPatternDemo
 * @date 2025/12/9
 * @description
 */
public class StrategyPatternDemo {
    public static void main(String[] args) {
        Strategy bluePen = new BluePen();
        Strategy greenPen = new GreenPen();
        Strategy redPen = new RedPen();
        Context context = new Context(bluePen);
        context.executeDraw();
    }
}

class Context {
    Strategy strategy;

    public Context(Strategy strategy) {
        this.strategy = strategy;
    }

    public void executeDraw() {
        strategy.draw();
    }
}

interface Strategy {
    void draw();
}

class RedPen implements Strategy {

    @Override
    public void draw() {
        System.out.println("红笔画图");
    }
}

class GreenPen implements Strategy {

    @Override
    public void draw() {
        System.out.println("绿笔画图");
    }
}

class BluePen implements Strategy {

    @Override
    public void draw() {
        System.out.println("蓝笔画图");
    }
}
