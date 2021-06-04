package com.aly8246.common.util;

import com.aly8246.common.core.SnowflakeIdGenerate;

import java.util.Random;

public class IDUtil {
    private static final SnowflakeIdGenerate snowflakeIdGenerate = new SnowflakeIdGenerate();

    /**
     * 获取一个本地雪花ID
     */
    public static Long nextSnowflakeId() {
        return snowflakeIdGenerate.nextId();
    }

    /**
     * 获取一个Long ID
     */
    public static long nextLongId() {
        //取当前时间的长整形值包含毫秒
        long millis = System.currentTimeMillis();
        //long millis = System.nanoTime();
        //加上两位随机数
        Random random = new Random();
        int end2 = random.nextInt(99);
        //如果不足两位前面补0
        String str = millis + String.format("%02d", end2);
        return Long.parseLong(str);
    }

    /**
     * 获取一个19位Long ID
     */
    public static Long nextLongLongId() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 19; i++) {
            stringBuilder.append(random.nextInt(9));
        }
        Long result = Long.parseLong(stringBuilder.toString());
        if (result < 1111111111111111111L) {
            result = nextLongLongId();
        }
        return result;
    }

    /**
     * 获取字符串ID
     */
    private static String nextStringId() {
        //取当前时间的长整形值包含毫秒
        long millis = System.currentTimeMillis();
        //long millis = System.nanoTime();
        //加上两位随机数
        Random random = new Random();
        int end2 = random.nextInt(999);
        //如果不足两位前面补0
        return millis + String.format("%03d", end2);
    }


    /**
     * 获取一个文件ID
     */
    public static String nextFileId() {
        //取当前时间的长整形值包含毫秒
        long millis = System.currentTimeMillis();
        //long millis = System.nanoTime();
        //加上三位随机数
        Random random = new Random();
        int end3 = random.nextInt(999);
        //如果不足三位前面补0
        return millis + String.format("%03d", end3);
    }

}
