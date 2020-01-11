package com.mysongktv.cn.tms.utils;

import java.util.Map;

public class Constant {

    public static final int TAG_ORDINARY = 1;
    public static final int TAG_EXTRA = 2;
    public static String DOWNLOAD_DIR = "/data/mysong";
    /**
     * 主服务器路径
     * 测试服务器路径
     */
    public static String HOST_TXT = "http://192.168.11.206:8080";

    public static String HOST_P2P = "http://192.168.11.206:6220";

    public static String STORE_ID = "12";

    public  static final String TASK_ALL_URL= "127.0.0.1:9999";
    public static final String P2P_TASK="/v1/tasks";
    public static final String FAST_QUEUE_NAME="_c";

    public static final String WINDOWS_TYPE = "WINDOWS";
    public static final String LINUX_TYPE = "LINUX";
    public static String SERVER_TYPE = "LINUX";


    /**
     * p2p下载路径
     * 如果是Windows服务器则正常操作
     * 如果是Linux服务器则全部变为 "/"来创建目录
     */
    public static String P2P_DOWNLOAD_DIR = "/data/mysong";

    public static String IMAGE_DOWNLOAD_DIR = "/data/mysong/image";

    public static String VIDEO_DOWNLOAD_DIR = "/data/mysong/video";


    public static String GATEWAY_HOST = null;

    public static long PERIOD_DAY = 24 * 60 * 60 * 1000;

    public static long PERIOD_HALF_HOUR = 30 * 60 * 1000;

    public static long PERIOD_ONE_HOUR = 60 * 60 * 1000;

    public static long PERIOD_TEN_MINUTES = 10 * 60 * 1000;

    public static int ONE_O_CLOCK = 1;

    public static long P2P_TIME_OUT_MINUTES = 120 * 60 * 1000;

    public static long P2P_PROGRESS_QUERY_SEC_PERIOD = 10 * 1000;

    public static int IS_ONCE_DAY = 1;

    public static void set(Map<String, String> map) {
        HOST_TXT = map.get("HOST_TXT");
        HOST_P2P = map.get("HOST_P2P");
        STORE_ID = map.get("STORE_ID");

        DOWNLOAD_DIR = map.get("DOWNLOAD_DIR");
        P2P_DOWNLOAD_DIR = map.get("P2P_DOWNLOAD_DIR");
        IMAGE_DOWNLOAD_DIR = map.get("IMAGE_DOWNLOAD_DIR");
        VIDEO_DOWNLOAD_DIR = map.get("VIDEO_DOWNLOAD_DIR");
        GATEWAY_HOST = map.get("GATEWAY_HOST");

        PERIOD_DAY = null == map.get("PERIOD_DAY") ? PERIOD_DAY :
                (long) (Double.parseDouble(map.get("PERIOD_DAY")) * PERIOD_DAY);
        PERIOD_HALF_HOUR = null == map.get("PERIOD_HALF_HOUR") ?
                PERIOD_HALF_HOUR :
                (long) (Double.parseDouble(map.get("PERIOD_HALF_HOUR")) * PERIOD_HALF_HOUR);
        PERIOD_ONE_HOUR = null == map.get("PERIOD_ONE_HOUR") ? PERIOD_ONE_HOUR :
                (long) (Double.parseDouble(map.get("PERIOD_ONE_HOUR")) * PERIOD_ONE_HOUR);
        PERIOD_TEN_MINUTES = null == map.get("PERIOD_TEN_MINUTES") ? PERIOD_TEN_MINUTES :
                (long) (Double.parseDouble(map.get("PERIOD_TEN_MINUTES")) * PERIOD_TEN_MINUTES);
        IS_ONCE_DAY =
                null == map.get("IS_ONCE_DAY") ? IS_ONCE_DAY : Integer.parseInt(map.get("IS_ONCE_DAY"));
        ONE_O_CLOCK =
                null == map.get("ONE_O_CLOCK") ? ONE_O_CLOCK : Integer.parseInt(map.get("ONE_O_CLOCK"));

        P2P_TIME_OUT_MINUTES = null == map.get("P2P_TIME_OUT_MINUTES") ?
                P2P_TIME_OUT_MINUTES :
                (long) (Double.parseDouble(map.get("P2P_TIME_OUT_MINUTES")) * 60 * 1000);
        P2P_PROGRESS_QUERY_SEC_PERIOD = null == map.get("P2P_PROGRESS_QUERY_SEC_PERIOD") ?
                P2P_PROGRESS_QUERY_SEC_PERIOD :
                (long) (Double.parseDouble(map.get("P2P_PROGRESS_QUERY_SEC_PERIOD")) * 1000);
    }


    public static String URL_DOWNLOAD_P2P = "/v1/torrent/down";

    public static String URL_PROGRESS_P2P = "/v1/torrent/status";

    private static String IP1="192.168.1.1";
    public static int SERVER_NUM=1;

    /**
     * 绑定端口
     */
    public static final Integer BIND_PORT=9817;

    /**
     * 心跳前缀
     */
    public static final String HEARTBEAT_MSG="HeartBeat";

    /**
     * Server端
     * Client端
     */
    public static final String MASTER_SERVER="server";
    public static final String MASTER_CLIENT="";

}
