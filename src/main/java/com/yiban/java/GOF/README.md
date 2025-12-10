## 一、创建型模式（Creational Patterns）
- 关注点：对象是如何创建的
- 目的：把对象的创建过程和使用过程解耦。

常见创建型模式（5 种）
- 单例（Singleton）  保证系统中只有一个实例
- 工厂方法（Factory Method）  把对象创建交给子类
- 抽象工厂（Abstract Factory）  创建一整族相关对象
- 建造者（Builder） 分步骤构建复杂对象
- 原型（Prototype）通过拷贝创建对象

典型例子：
```aiignore
// 单例
Runtime.getRuntime();

// 工厂
Calendar.getInstance();

// Builder
new StringBuilder().append("a").append("b");
```
Spring 中大量使用创建型模式（BeanFactory、FactoryBean）。

---
## 二、结构型模式（Structural Patterns）
- 关注点：类和对象如何组合成更大的结构
- 目的：让系统结构更清晰、更灵活。

常见结构型模式（7 种）
- 代理（Proxy） 控制对对象的访问
- 装饰器（Decorator） 动态增强对象功能
- 适配器（Adapter） 接口不兼容 → 可用
- 外观（Facade） 对外提供统一入口
- 桥接（Bridge） 抽象与实现分离
- 组合（Composite） 树形结构（整体-部分）
- 享元（Flyweight） 共享对象，节省内存

典型例子：
```aiignore
// 装饰器
BufferedInputStream in = new BufferedInputStream(new FileInputStream("a.txt"));

// 代理
@Transactional
```
AOP 本质就是代理模式 + 装饰思想。

---

## 三、行为型模式（Behavioral Patterns）
- 关注点：对象之间如何协作、如何分配职责
- 目的：解耦行为逻辑，让代码更易扩展。

当 if-else、switch 越来越多时，行为型模式很有用。

常见行为型模式（11 种）
- 策略（Strategy） 可互换的算法
- 模板方法（Template Method） 定义算法骨架
- 观察者（Observer） 事件通知机制
- 责任链（Chain of Responsibility） 请求链式处理
- 命令（Command） 请求封装成对象
- 状态（State） 状态驱动行为
- 迭代器（Iterator） 顺序访问集合
- 中介者（Mediator） 减少对象耦合
- 备忘录（Memento） 保存与恢复状态
- 访问者（Visitor） 对结构添加新操作
- 解释器（Interpreter） 语言解释执行

典型例子：
```aiignore
// 策略
Comparator comparator;

// 观察者
ApplicationListener

// 模板方法
AbstractApplicationContext.refresh()
```
Spring MVC、事件机制大量使用行为型模式。

## 一句话总结

| **类型** | **关心什么** | **一句话**          |
| -------- | ------------ | ------------------- |
| 创建型   | 对象怎么来   | **怎么 new 更优雅** |
| 结构型   | 关系怎么搭   | **怎么组装更清晰**  |
| 行为型   | 行为怎么跑   | **怎么协作更灵活**  |

- 创建型模式关注对象创建，
- 结构型模式关注类和对象的组合，
- 行为型模式关注对象之间的职责和协作。
