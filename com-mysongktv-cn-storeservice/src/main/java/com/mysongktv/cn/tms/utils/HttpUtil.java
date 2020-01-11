package com.mysongktv.cn.tms.utils;

import com.alibaba.fastjson.TypeReference;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static final int TIME_OUT = 60 * 60 * 1000;
    public static final int MAX_RETRY = 0;

    private static OkHttpClient client = new OkHttpClient.Builder()
            //        .retryOnConnectionFailure(true)
            .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            //        .addInterceptor(new RetryInterceptor(MAX_RETRY))
            .build();

    private HttpUtil() {
    }

    public static <T> T get(String url, Map<String, String> params, TypeReference typeReference) {
        HttpUrl.Builder builder = buildGetParams(url, params);
        Request request = new Request.Builder()
                .url(builder.build())
                .build();
        return doRequest(request, typeReference);
    }

    public static <T> String get(String url, Map<String,String> params){
        HttpUrl.Builder builder = buildGetParams(url,params);
        Request request = new Request.Builder()
                .url(builder.build())
                .build();

        return doRequest(request);
    }

    public static <T> T post(String url, Map<String, String> params, TypeReference typeReference) {
        RequestBody requestBody = buildPostParams(params);
        Request request = new Request.Builder().url(url).post(requestBody).build();

        return doRequest(request, typeReference);
    }

    private static RequestBody buildPostParams(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (String key : params.keySet()) {
                if (null == key) {
                    continue;
                }
                String value = params.get(key);
                if (null == value) {
                    continue;
                }
                builder.add(key, value);
            }
        }
        return builder.build();
    }

    public static boolean downloadGet(String url, Map<String, String> params, String fileDir, String fileName) {
        logger.info(
                "Http Util -------------- download get ----> dir : " + fileDir + " name : " + fileName);
        HttpUrl.Builder builder = buildGetParams(url, params);
        Request request = new Request.Builder()
                .url(builder.build())
                .build();
        return doRequestAndSaveFile(fileDir, fileName, request);
    }

    private static <T> T doRequest(Request request, TypeReference typeReference) {
        logger.info("request : -----------------" + request);
        ResponseBody body = null;
        Response response = null;
        try {
            response = client.newCall(request).execute();
            body = response.body();
            String result = body.string();
            logger.info("response--------------->" + result);
            if (response.isSuccessful()) {
                return (T) JsonUtil.fromJson(result, typeReference);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("response:exception------------->" + e.getMessage());
        } finally {
            if (null != body) {
                body.close();
            }
            if (null != response) {
                response.close();
            }
        }
        return null;
    }

    private static String doRequest(Request request) {
        logger.info("request : -----------------" + request);
        ResponseBody body = null;
        Response response = null;
        try {
            response = client.newCall(request).execute();
            body = response.body();
            System.out.println(body);

            String result = body.string();

            System.out.println(result);
            logger.info("response--------------->" + result);
            if (response.isSuccessful()) {
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("response:exception------------->" + e.getMessage());
        } finally {
            if (null != body) {
                body.close();
            }
            if (null != response) {
                response.close();
            }
        }
        return null;
    }


    private static boolean doRequestAndSaveFile(String fileDir, String fileName, Request request) {
        logger.info("request and save file ---------------> request : " + request);
        logger.info(
                "request and save file ---------------> file : " + fileDir + " fileName : " + fileName);
        try {
            Response response = client.newCall(request).execute();
            if (null == response || null == response.body() || !response.isSuccessful()) {
                return false;
            }
            FileUtil.saveFile(response.body().byteStream(), fileDir, fileName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param url
     * @param params
     * @return
     */
    private static HttpUrl.Builder buildGetParams(String url, Map<String, String> params) {
        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            for (String key : params.keySet()) {
                builder.addQueryParameter(key, params.get(key));
            }
        }
        return builder;
    }


}
