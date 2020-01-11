package com.mysongktv.cn.tms.utils;

import com.alibaba.fastjson.TypeReference;

import com.mysongktv.cn.tms.entity.ResultData;
import com.mysongktv.cn.tms.model.P2pModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import static com.mysongktv.cn.tms.utils.Constant.*;


public class StoreUtil {

    private static Logger logger = LoggerFactory.getLogger(StoreUtil.class);

    public static final String TABLE_SOFTWARE = "software_pack";

    public static final String TABLE_CLEAR = "clear";

    private static final String[] normalDataTypes = {
            "banner",
            "module",
            "module_content",
            "rank_android",
            "buttonsettings",
            "version",
            "song_hot",
            "singer_hot",
            TABLE_SOFTWARE
    };

    private static final String[] hurryDataTypes = {
            TABLE_SOFTWARE,
            "singer",
            "song"
    };

    private static final int STATUS_CLEAR = 2;

    private static final int P2P_DONE = 1;

    private static final int P2P_PROGRESS = 100;

    private static final int P2P_FAILED = 7;

    private static final int P2P_EMPTY_TIMES = 3;// TODO

    private static int retryTime = 0;

    private static volatile long queryTime = 0;

    private static HashMap<String, String> normalOneDayMap = new HashMap<>();
    private static HashMap<String, String> normal2HourMap = new HashMap<>();
    private static HashMap<String, String> hurryMap = new HashMap<>();
    private static HashMap<String, String> editMap = new HashMap<>();





    public static boolean downloadMsFileP2p(String seedId) {
        logger.info("---------------download p2p : " + seedId);
        HashMap<String, String> params = getP2pParams(seedId);
        ResultData response =
                HttpUtil.post(HOST_P2P + URL_DOWNLOAD_P2P, params, new TypeReference<ResultData>() {
                });
        if (null == response || response.getError() != 0) {
            return false;
        }
        return true;
    }

    public static P2pModel queryP2pProgress(String seedId) {
        queryTime = System.currentTimeMillis();
        return queryP2pProgress(queryTime, seedId);
    }

    private static P2pModel queryP2pProgress(long current, String seedId) {
        logger.info("---------------query progress p2p : " + seedId);
        if (current - queryTime > P2P_TIME_OUT_MINUTES) {
            return null;
        }
        HashMap<String, String> params = getP2pParams(seedId);
        ResultData<P2pModel> response =
                HttpUtil.get(
                        HOST_P2P + URL_PROGRESS_P2P, params, new TypeReference<ResultData<P2pModel>>() {
                        });
        // failed
        if (null == response || response.getError() == P2P_FAILED || null == response.getData()) {
            return null;
        }
        P2pModel model = response.getData();
        P2pModel.InfoData infoData = model.getInfo();
        if (null == infoData) {
            return null;
        }
        if (P2P_DONE == infoData.getComplete()) {
            retryTime = 0;
            return model;
        } else if (P2P_PROGRESS >= infoData.getProgress() && retryTime < P2P_EMPTY_TIMES) {
            retryTime++;
            return loopProgress(seedId);
        } else {
            retryTime = 0;
            return null;
        }
    }

    private static P2pModel loopProgress(String seedId) {
        try {
            Thread.sleep(P2P_PROGRESS_QUERY_SEC_PERIOD);
            return queryP2pProgress(seedId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 请求返回结果  【{"data":{"path":"D:/metaDataFile/12/song_1575277960.txt"},"message":"sucess","status":0}】
     *
     * @param url
     * @param dataType
     * @param reqTypeName
     * @param map
     */
//    private static void requestTxt(String url, String dataType, String reqTypeName, Map<String, String> map) {
//        String requestUrl = buildUrl(url, getTaskParams(dataType, reqTypeName));
//        BaseResponse<TxtModel> model =
//                HttpUtil.get(requestUrl, null, new TypeReference<BaseResponse<TxtModel>>() {
//                });
//        handleResult(model, dataType, map);
//    }

    /**
     * 处理结果
     *
     * @param model
     * @param dataType
     * @param map
     */
//    private static void handleResult(BaseResponse<TxtModel> model, String dataType, Map<String, String> map) {
//        if (null == model || null == model.getData()) {
//            return;
//        }
//        // only when data_type = normal_day and status=2 the db needs to be cleared
//        if (RequestType.NORMAL_DAY.getValue().equals(dataType)
//                && model.getStatus() == STATUS_CLEAR) {
//            map.put(dataType, TABLE_CLEAR);
//            logger.info("store util response ---------> clear db ");
//            return;
//        }
//        if (model.isSuccess()) {
//            //"D:/metaDataFile/12/song_1574940092.txt"
//            String path = model.getData().getPath();
//            if (null == path || path.isEmpty()) {
//                return;
//            }
//            String fullPath = downloadTxt(path);
//            map.put(dataType, fullPath);
//        }
//    }

    /**
     * http://113.31.89.164:8080/OnlineSongsV2/metadataDownload/downfile.do?file_name=song_1574940092.txt&file_path=D:/metaDataFile/12/&sign=dc5d6b439af0417817fabb65eaf1e95d
     * 下载文件
     *
     * @param path ["D:/metaDataFile/12/song_1574940092.txt"]
     * @return
     */
//    public static String downloadTxt(String path) {
//        String dir = "";
//        if (null == path || path.isEmpty()) {
//            return null;
//        }
//        //获取路径
//        String[] filePathAndName = FileUtil.getHttpDirAndPathFromHttp(path);
//        String requestUrl =
//                buildUrl(HOST_TXT + URL_DOWNLOAD_TXT, getDownloadParams(filePathAndName));
//
//        /***/
//        System.out.println("==================" + requestUrl);
//
//        // 到这之前是给服务器的不用管地址
//        //其中进行了Linux和Windows路径判断
//        dir = FileUtil.getTxtSaveDir(filePathAndName);
//
//
//        //http://113.31.89.164:8080/OnlineSongsV2/metadataDownload/downfile.do?file_name=song_1574932309.txt&file_path=D:/metaDataFile/12/&sign=37AE3BF604739F3DABF2210919122A29
//
//        // 从这开始是下载到自己目录下的
//        HttpUtil.downloadGet(requestUrl, null, dir, filePathAndName[1]);
//        dir = FileUtil.createWindowsDir(dir);
//        String fullPath = dir + filePathAndName[1];
//
//        //windows
//        fullPath = fullPath.replace("//", "\\\\");
//        //Linux
//
//
//        logger.info("store util downloadTxt ---------> fullPath : " + fullPath);
//        return fullPath;
//    }

//    private static void offerToQueue(Map<String, String> map, RequestType requestType) {
//        if (null == map || map.isEmpty()) {
//            return;
//        }
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            if (null == entry || null == entry.getValue() || entry.getValue().isEmpty()) {
//                continue;
//            }
//            StoreModel storeModel = new StoreModel();
//            storeModel.setPath(entry.getValue());
//            logger.info("Store UTIL ================= path " + entry.getValue());
//            storeModel.setTableName(entry.getKey());
//            storeModel.setRequestType(requestType);
//            QueueUtil.put(storeModel);
//        }
//    }

    public static String buildUrl(String url, String params) {
        return url + "?" + params + "&sign=" + getSign(params);
    }

    private static String getDurationParams() {
        return "shop_id=" + Constant.STORE_ID;
    }

    private static String getTaskParams(String dataType, String requestType) {
        return "data_type=" + dataType + "&request_type=" + requestType + "&shop_id="
                + Constant.STORE_ID;
    }

    public static String getDownloadParams(String[] params) {
        return "file_name=" + params[1] + "&file_path=" + params[0];
    }

    private static HashMap<String, String> getP2pParams(String seedId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("torrentId", seedId);
        return params;
    }

    private static String getSign(String str) {
        return Md5Util.strToMd5(str + "f5d0a4695ad2342716ca7671fe4817b6");
    }
}
