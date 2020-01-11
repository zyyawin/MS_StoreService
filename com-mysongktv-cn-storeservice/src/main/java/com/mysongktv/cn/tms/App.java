package com.mysongktv.cn.tms;

import com.mysongktv.cn.tms.utils.Constant;
import com.mysongktv.cn.tms.utils.electionUtils.HeartBeatClient;
import com.mysongktv.cn.tms.utils.electionUtils.HeartBeatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class App {
    private static Logger logger = LoggerFactory.getLogger(App.class);
    private static final String SERVER = "SERVER";
    private static final String CLIENT = "CLIENT";

    public static void main(String[] args)  {
        SpringApplication.run(App.class, args);
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

}
