package com.aly8246.common.properties;


import java.util.Random;

/**
 * 随机端口获得，每个服务只能获取同一个端口，不能每次都变化
 */
public class RandomServerPort {
    private int serverPort;

    public static final int defaultEnd = 15000;
    public static final int defaultStart = 12000;

    public int nextValue(int start) {
        return nextValue(start, defaultEnd);
    }

    public int nextValue(int start, int end) {
        if (start == 0) start = defaultStart;
        if (end == 0) end = defaultEnd;
        if (serverPort == 0) {
            synchronized (this) {
                if (serverPort == 0) {
                    serverPort = new Random().nextInt();
                }
            }
        }
        return serverPort;
    }
}
