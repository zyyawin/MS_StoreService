package com.mysongktv.cn.tms;

import com.mysongktv.cn.tms.utils.Constant;
import com.mysongktv.cn.tms.utils.FileUtil;
import com.mysongktv.cn.tms.utils.electionUtils.HeartBeatClient;
import com.mysongktv.cn.tms.utils.electionUtils.HeartBeatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ZhangYang
 * @date 2020/1/6 11:23
 */
public class RunApp {
    private static Logger logger = LoggerFactory.getLogger(RunApp.class);
    private static final String SERVER = "SERVER";
    private static final String CLIENT = "CLIENT";
    private final Lock lock = new ReentrantLock();//锁对象

    public static void main(String[] args) throws NoSuchFieldException, ClassNotFoundException {
        initTask();
    }

    private static void initTask() {
        if (Constant.MASTER_SERVER.toUpperCase().equals(SERVER)) {
            HeartBeatServer.start();
        }

        if (Constant.MASTER_CLIENT.toUpperCase().equals(CLIENT)) {
            try {
                HeartBeatClient.start();
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
    }


    public static void startDownloadTimer(){
        Timer timer  = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                StartTask.downFileWithQueue();
            }
        };
        timer.schedule(task,10000,10000);
    }

}
