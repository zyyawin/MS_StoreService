package com.mysongktv.cn.tms;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysongktv.cn.tms.model.P2pModel;
import com.mysongktv.cn.tms.utils.Constant;
import com.mysongktv.cn.tms.utils.FileUtil;
import com.mysongktv.cn.tms.utils.HttpUtil;
import com.mysongktv.cn.tms.utils.StoreUtil;
import com.mysongktv.cn.tms.utils.electionUtils.HeartBeatServer;
import com.mysongktv.cn.tms.utils.electionUtils.HeartModel;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.plugin.ClassLoaderInfo;

import javax.rmi.CORBA.StubDelegate;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.mysongktv.cn.tms.utils.Constant.SERVER_TYPE;
import static com.mysongktv.cn.tms.utils.Constant.WINDOWS_TYPE;

/**
 * @author ZhangYang
 * @date 2020/1/4 15:11
 */
@Component
public class StartTask {
    private static Logger logger = LoggerFactory.getLogger(StartTask.class);

    // public static final String rrr = "{\"error\":0,\"msg\":\"OK\",\"data\":{\"totalCount\":2,\"page\":1,\"count\":2,\"dataList\":[{\"id\":400,\"storeId\":0,\"originId\":101,\"queueName\":\"a\",\"type\":11, \"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"seedId\\\":\\\"BC5546B2E84B423AA67C20151991C647\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:29.0\",\"updateAt\":\"2019-12-30 13:12:29.0\"},{\"id\":399,\"storeId\":0,\"originId\":101,\"queueName\":_c,\"type\":11,\"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289511-2.mpg\\\",\\\"seedId\\\":\\\"1BF3492830D94CC1B9B7162AA07004B9\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:28.0\",\"updateAt\":\"2019-12-30 13:12:28.0\"}]}}";
    public static final String rrr = "{\"error\":0,\"msg\":\"OK\",\"data\":{\"totalCount\":2,\"page\":1,\"count\":2,\"dataList\":[{\"id\":400,\"storeId\":0,\"originId\":101,\"type\":11,\"queueName\":\"a\",\"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"seedId\\\":\\\"BC5546B2E84B423AA67C20151991C647\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:29.0\",\"updateAt\":\"2019-12-30 13:12:29.0\"},{\"id\":399,\"storeId\":0,\"originId\":101,\"type\":11,\"queueName\":\"_c\",\"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289511-2.mpg\\\",\\\"seedId\\\":\\\"1BF3492830D94CC1B9B7162AA07004B9\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:28.0\",\"updateAt\":\"2019-12-30 13:12:28.0\"},{\"id\":399,\"storeId\":0,\"originId\":101,\"type\":11,\"queueName\":\"_c\",\"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289511-2.mpg\\\",\\\"seedId\\\":\\\"1BF3492830D94CC1B9B7162AA07004B9\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:28.0\",\"updateAt\":\"2019-12-30 13:12:28.0\"},{\"id\":399,\"storeId\":0,\"originId\":101,\"type\":11,\"queueName\":\"_c\",\"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289511-2.mpg\\\",\\\"seedId\\\":\\\"1BF3492830D94CC1B9B7162AA07004B9\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:28.0\",\"updateAt\":\"2019-12-30 13:12:28.0\"},{\"id\":399,\"storeId\":0,\"originId\":101,\"type\":11,\"queueName\":\"_c\",\"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289511-2.mpg\\\",\\\"seedId\\\":\\\"1BF3492830D94CC1B9B7162AA07004B9\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:28.0\",\"updateAt\":\"2019-12-30 13:12:28.0\"},{\"id\":399,\"storeId\":0,\"originId\":101,\"type\":11,\"queueName\":\"_c\",\"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289511-2.mpg\\\",\\\"seedId\\\":\\\"1BF3492830D94CC1B9B7162AA07004B9\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:28.0\",\"updateAt\":\"2019-12-30 13:12:28.0\"},{\"id\":399,\"storeId\":0,\"originId\":101,\"type\":11,\"queueName\":\"_c\",\"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289511-2.mpg\\\",\\\"seedId\\\":\\\"1BF3492830D94CC1B9B7162AA07004B9\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:28.0\",\"updateAt\":\"2019-12-30 13:12:28.0\"},{\"id\":399,\"storeId\":0,\"originId\":101,\"type\":11,\"queueName\":\"_c\",\"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289511-2.mpg\\\",\\\"seedId\\\":\\\"1BF3492830D94CC1B9B7162AA07004B9\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:28.0\",\"updateAt\":\"2019-12-30 13:12:28.0\"},{\"id\":399,\"storeId\":0,\"originId\":101,\"type\":11,\"queueName\":\"_c\",\"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289511-2.mpg\\\",\\\"seedId\\\":\\\"1BF3492830D94CC1B9B7162AA07004B9\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:28.0\",\"updateAt\":\"2019-12-30 13:12:28.0\"},{\"id\":399,\"storeId\":0,\"originId\":101,\"type\":11,\"queueName\":\"_c\",\"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289511-2.mpg\\\",\\\"seedId\\\":\\\"1BF3492830D94CC1B9B7162AA07004B9\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:28.0\",\"updateAt\":\"2019-12-30 13:12:28.0\"},{\"id\":399,\"storeId\":0,\"originId\":101,\"type\":11,\"queueName\":\"_c\",\"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289511-2.mpg\\\",\\\"seedId\\\":\\\"1BF3492830D94CC1B9B7162AA07004B9\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:28.0\",\"updateAt\":\"2019-12-30 13:12:28.0\"},{\"id\":399,\"storeId\":0,\"originId\":101,\"type\":11,\"queueName\":\"_c\",\"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289511-2.mpg\\\",\\\"seedId\\\":\\\"1BF3492830D94CC1B9B7162AA07004B9\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:28.0\",\"updateAt\":\"2019-12-30 13:12:28.0\"},{\"id\":399,\"storeId\":0,\"originId\":101,\"type\":11,\"queueName\":\"_c\",\"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289511-2.mpg\\\",\\\"seedId\\\":\\\"1BF3492830D94CC1B9B7162AA07004B9\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:28.0\",\"updateAt\":\"2019-12-30 13:12:28.0\"},{\"id\":399,\"storeId\":0,\"originId\":101,\"type\":11,\"queueName\":\"_c\",\"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289511-2.mpg\\\",\\\"seedId\\\":\\\"1BF3492830D94CC1B9B7162AA07004B9\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:28.0\",\"updateAt\":\"2019-12-30 13:12:28.0\"},{\"id\":399,\"storeId\":0,\"originId\":101,\"type\":11,\"queueName\":\"_c\",\"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289511-2.mpg\\\",\\\"seedId\\\":\\\"1BF3492830D94CC1B9B7162AA07004B9\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:28.0\",\"updateAt\":\"2019-12-30 13:12:28.0\"},{\"id\":399,\"storeId\":0,\"originId\":101,\"type\":11,\"queueName\":\"_c\",\"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289511-2.mpg\\\",\\\"seedId\\\":\\\"1BF3492830D94CC1B9B7162AA07004B9\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:28.0\",\"updateAt\":\"2019-12-30 13:12:28.0\"},{\"id\":399,\"storeId\":0,\"originId\":101,\"type\":11,\"queueName\":\"_c\",\"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289511-2.mpg\\\",\\\"seedId\\\":\\\"1BF3492830D94CC1B9B7162AA07004B9\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:28.0\",\"updateAt\":\"2019-12-30 13:12:28.0\"},{\"id\":399,\"storeId\":0,\"originId\":101,\"type\":11,\"queueName\":\"_c\",\"content\":\"{\\\"windows\\\":\\\"D:/home/ftp/song/wzf_song/1289503-2.mpg\\\",\\\"linux\\\":\\\"/home/ftp/song/wzf_song/1289511-2.mpg\\\",\\\"seedId\\\":\\\"1BF3492830D94CC1B9B7162AA07004B9\\\"}\",\"status\":1,\"createAt\":\"2019-12-30 13:12:28.0\",\"updateAt\":\"2019-12-30 13:12:28.0\"}]}}";
    public static HeartModel heartModel = new HeartModel();



    public static List<LinkedBlockingQueue<ConcurrentHashMap<String, String>>> startDownFile(String storeId) throws InterruptedException {

        logger.info("==========>处理请求信息，并添加到处理队列");
        String res = null;
        List<LinkedBlockingQueue<ConcurrentHashMap<String, String>>> queueList = new ArrayList<>();
        LinkedBlockingQueue<ConcurrentHashMap<String, String>> normalP2pQueue = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<ConcurrentHashMap<String, String>> fastP2pQueue = new LinkedBlockingQueue<>();

        HashMap<String, String> paramMap = new HashMap<>();

        ConcurrentHashMap<String, String> queueMap = new ConcurrentHashMap<>();

        String url = Constant.TASK_ALL_URL + Constant.P2P_TASK;
        paramMap.put("storeId", storeId);
        // res = HttpUtil.get(url, paramMap);

        //转换为Json
        JSONObject resJsonObj = JSONObject.parseObject(rrr);

        JSONObject dataJsonObj = resJsonObj.getJSONObject("data");

        JSONArray dataListJsonArray = dataJsonObj.getJSONArray("dataList");


        for (int i = 0; i < dataListJsonArray.size(); i++) {

            //获取队列名称
            String queueName = dataListJsonArray.getJSONObject(i).getString("queueName");

            JSONObject contentJsonObj = dataListJsonArray.getJSONObject(i).getJSONObject("content");

            //windows路径
            String winPath = contentJsonObj.getString("windows");
            //Linux路径
            String linuxPath = contentJsonObj.getString("linux");
            //seedId
            String seedId = contentJsonObj.getString("seedId");

            queueMap.put("queueName", queueName);
            queueMap.put("seedId", seedId);
            queueMap.put("windows", winPath);
            queueMap.put("linux", linuxPath);

            try {
                if (queueName.contains(Constant.FAST_QUEUE_NAME)) {
                    fastP2pQueue.put(queueMap);
                } else {
                    normalP2pQueue.put(queueMap);
                }
            } catch (Exception e) {
                logger.info("RunStart 添加队列失败=====>" + e.getMessage());
            }


        }
        queueList.add(fastP2pQueue);

        queueList.add(normalP2pQueue);

        System.out.println(fastP2pQueue.size()+"==============");
        System.out.println(normalP2pQueue.size());
        heartModel.setQueueList(queueList);
        return queueList;


    }


    @Scheduled(cron = "0/20 * * * * ?")
    public static void downFileWithQueue()  {
        List<LinkedBlockingQueue<ConcurrentHashMap<String, String>>> queueList = heartModel.getQueueList();
        if (queueList.isEmpty() || queueList == null) {
                logger.info("=========>队列集合为空");
        }
        LinkedBlockingQueue<ConcurrentHashMap<String, String>> fastP2pQueue = queueList.get(0);
        LinkedBlockingQueue<ConcurrentHashMap<String, String>> normalP2pQueue = queueList.get(1);


        try{
            //优先下载队列
            while (!fastP2pQueue.isEmpty()) {
                FileUtil.downloadFile(fastP2pQueue.poll());
            }
            while (fastP2pQueue.isEmpty()) {
                if (!normalP2pQueue.isEmpty()){
                    FileUtil.downloadFile(normalP2pQueue.poll());
                }

                if (normalP2pQueue.isEmpty() && fastP2pQueue.isEmpty()) {
                    logger.info("=========>任务下载完成");
                    HeartBeatServer.start();
                }
            }

        }catch (Exception e){
            logger.warn(e.getMessage());
        }


    }


}






