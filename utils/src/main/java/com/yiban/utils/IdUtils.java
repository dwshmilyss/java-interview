package com.yiban.utils;

/**
 * @author david.duan
 * @packageName com.yiban.java.utils
 * @className IdUtils
 * @date 2025/12/4
 * @description
 */
public class IdUtils {
    // ============================== Fields ==============================
    /** 开始时间戳（可自定义），这里使用2020-01-01 */
    private final static long START_TIMESTAMP = 1577836800000L;

    /** 每部分占用位数 */
    private final static long SEQUENCE_BITS = 12;    // 序列号
    private final static long MACHINE_BITS = 5;      // 机器号
    private final static long DATACENTER_BITS = 5;   // 数据中心

    /** 每部分最大值 */
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BITS);
    private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BITS);

    /** 位移 */
    private final static long MACHINE_LEFT = SEQUENCE_BITS;
    private final static long DATACENTER_LEFT = SEQUENCE_BITS + MACHINE_BITS;
    private final static long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BITS;

    private long datacenterId;  // 数据中心
    private long machineId;     // 机器标识
    private long sequence = 0L; // 序列号
    private long lastTimestamp = -1L; // 上一次时间戳

    // ============================== Constructor ==============================
    public IdUtils(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId out of range");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId out of range");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    // ============================== Methods ==============================
    /** 生成 Snowflake ID */
    public synchronized long nextId() {
        long currentTimestamp = getNewTimestamp();

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id.");
        }

        if (currentTimestamp == lastTimestamp) {
            // 同一毫秒内序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                // 当前毫秒的序列号耗尽，等待下一毫秒
                currentTimestamp = getNextMill();
            }
        } else {
            // 不同毫秒，序列号置0
            sequence = 0;
        }

        lastTimestamp = currentTimestamp;

        return (currentTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT
                | datacenterId << DATACENTER_LEFT
                | machineId << MACHINE_LEFT
                | sequence;
    }

    private long getNextMill() {
        long mill = getNewTimestamp();
        while (mill <= lastTimestamp) {
            mill = getNewTimestamp();
        }
        return mill;
    }

    private long getNewTimestamp() {
        return System.currentTimeMillis();
    }

    // ============================== Static Helper ==============================
    /** 单例使用：IdUtils.createSnowflake().nextId() */
    private static final IdUtils DEFAULT = new IdUtils(1, 1);

    public static IdUtils createSnowflake() {
        return DEFAULT;
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            Thread t = new Thread(() -> {
                try {
                    for (int j = 0; j < 10; j++) {
                        System.out.println(Thread.currentThread().getName() + "\t" + createSnowflake().nextId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, String.format("Thread:%s",i));
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        
    }
}
