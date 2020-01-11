package com.mysongktv.cn.tms.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class JsonUtil {

    public static String toJson(Object data) {
        return JSON.toJSONString(data);
    }

    public static Object fromJson(String dataJson, TypeReference typeReference) {
        return JSON.parseObject(dataJson, typeReference);
    }

}
