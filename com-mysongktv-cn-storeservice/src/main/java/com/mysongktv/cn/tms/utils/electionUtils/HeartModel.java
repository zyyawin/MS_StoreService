package com.mysongktv.cn.tms.utils.electionUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author ZhangYang
 * @date 2020/1/6 18:13
 */
public class HeartModel {
    private List<LinkedBlockingQueue<ConcurrentHashMap<String, String>>> queueList;

    public List<LinkedBlockingQueue<ConcurrentHashMap<String, String>>> getQueueList() {
        return queueList;
    }

    public void setQueueList(List<LinkedBlockingQueue<ConcurrentHashMap<String, String>>> queueList) {
        this.queueList = queueList;
    }

    public HeartModel(List<LinkedBlockingQueue<ConcurrentHashMap<String, String>>> queueList) {
        this.queueList = queueList;
    }

    public HeartModel() {
    }
}
