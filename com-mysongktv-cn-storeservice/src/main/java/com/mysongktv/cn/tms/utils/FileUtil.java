package com.mysongktv.cn.tms.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysongktv.cn.tms.model.P2pModel;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static com.mysongktv.cn.tms.utils.Constant.SERVER_TYPE;
import static com.mysongktv.cn.tms.utils.Constant.WINDOWS_TYPE;

public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private static int READ_LINE_OFFSET = 1000;

    private static HashMap<String, Integer> lineNumberMap = new HashMap<>();



    public static void startDownFile(String storeId) throws InterruptedException {

        logger.info("==========>处理请求信息，并添加到处理队列");
        String res = null;
        LinkedBlockingQueue<ConcurrentHashMap<String, String>> normalP2pQueue = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<ConcurrentHashMap<String, String>> fastP2pQueue = new LinkedBlockingQueue<>();

        List<LinkedBlockingQueue> queueList =null;
        HashMap<String, String> paramMap = new HashMap<>();
        ConcurrentHashMap<String, String> queueMap = new ConcurrentHashMap<>();

        String url = Constant.TASK_ALL_URL + Constant.P2P_TASK;
        paramMap.put("storeId", storeId);
        res = HttpUtil.get(url, paramMap);

        //转换为Json
        JSONObject resJsonObj = JSONObject.parseObject(res);

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
                logger.info("FileUtil 添加队列失败=====>" + e.getMessage());
            }

        }


        //优先下载队列
        while (!fastP2pQueue.isEmpty()) {
            FileUtil.downloadFile(fastP2pQueue.poll());
        }

        while (fastP2pQueue.isEmpty()) {
            FileUtil.downloadFile(Objects.requireNonNull(normalP2pQueue.poll()));

            if (normalP2pQueue.isEmpty()) {
                Thread.sleep(10000);
                //跳转心跳检测
                logger.info("==========>任务执行完成，跳转心跳检测");
            }
        }

    }
    /**
     * 下载文件
     * @param
     * @return
     */
    public static void downloadFile(ConcurrentHashMap<String, String> p2pMap) {
        String seedId = p2pMap.get("seedId");

        if (seedId == null && seedId.equals("")) {
            return;
        }

        String downpath = null;
        String toPath = null;
        P2pModel p2pModel;

        if (StoreUtil.downloadMsFileP2p(seedId) && null != (p2pModel = StoreUtil.queryP2pProgress(seedId))) {
            List<P2pModel.FileData> files = p2pModel.getFiles();
            if (null == files || files.isEmpty()) {
                return;
            }
            for (P2pModel.FileData data : files) {
                if (null == data) {
                    continue;
                }
                String fileName = data.getFileName();
                if (null == fileName || fileName.isEmpty()) {
                    continue;
                }

                //如果是Windows服务器
                if (WINDOWS_TYPE.equals(SERVER_TYPE)) {
                    downpath = Constant.P2P_DOWNLOAD_DIR + "\\\\" + seedId + "\\\\" + fileName;
                    //转小写
                    toPath = p2pMap.get(SERVER_TYPE.toLowerCase());
                } else if (Constant.LINUX_TYPE.equals(SERVER_TYPE)) {
                    downpath = Constant.P2P_DOWNLOAD_DIR + "/" + seedId + "/" + fileName;
                    toPath = p2pMap.get(SERVER_TYPE.toLowerCase());
                } else {
                    logger.info("服务器类型传入错误");
                }


                operationWhenDownloadSuccess(downpath, toPath);

            }

        }


    }

    private static void operationWhenDownloadSuccess(String fromPath, String toPath) {
        logger.info("download success -------- fromPath : " + fromPath);


        if (fromPath == null || fromPath.equals("")) {
            return;
        }
        if (null == toPath || toPath.isEmpty()) {
            return;
        }

        logger.info("download success -------- toPath : " + toPath);
        moveFile(fromPath, toPath);

    }

    public static boolean moveFile(String fromPath, String toPath) {

        if (null == fromPath || fromPath.isEmpty() || null == toPath || toPath.isEmpty()) {
            return false;
        }

        if (fromPath.equals(toPath)) {
            return true;
        }
        logger.info("moveFile to ==========" + toPath + "当前时间:" + new Date());
        //new File
        File fromFile = new File(fromPath);
        try {

            FileUtils.moveFile(fromFile, new File(toPath));
            //boolean sss = fromFile.exists();

            if (!fromFile.exists()) {
                fromFile.getParentFile().delete();
            }
            logger.info("the file move success");
            return true;
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        logger.info("文件移动失败");
        return false;
    }


    public static String readFirstLine(String filePath) {
        logger.info("read FIRST line -------------------- " + lineNumberMap.get(filePath));
        List<String> lines = readLines(filePath, 0, 1);
        return (null == lines || lines.isEmpty()) ? null : lines.get(0);
    }

    public static List<String> readContentLines(String filePath, boolean firstLoad) {
        logger.info(
                "read file LINES ---------------- " + lineNumberMap.get(filePath) + " first load : "
                        + firstLoad);
        if (firstLoad) {
            lineNumberMap.put(filePath, 1);
        }
        return readLines(filePath, lineNumberMap.get(filePath), READ_LINE_OFFSET);
    }

    private static List<String> readLines(String filePath, int offset, int limit) {
        logger.info(
                "read lines filePath : " + filePath + " offset : " + offset + " limit : " + limit);
        if (limit <= 0) {
            return null;
        }
        File file = new File(filePath);
        List<String> lines = new ArrayList<>();
        WeakReference<List<String>> weakReference = new WeakReference<>(lines);
        if (file.exists()) {
            InputStreamReader reader = null;
            LineNumberReader br = null;
            try {
                reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
                br = new LineNumberReader(reader);

                int start;
                int target;
                start = offset;
                target = offset + limit;

                logger.info(
                        "read lines line number --------- : " + " start : " + start + " end : "
                                + target);
                String lineContent;
                int lineNumber = -1;
                while ((lineContent = br.readLine()) != null) {
                    lineNumber = br.getLineNumber();
                    // only end of the '|' means a line
                    while (lineContent.charAt(lineContent.length() - 1) != '|') {
                        lineContent = lineContent + br.readLine();
                        // limit the target
                        target = br.getLineNumber() - lineNumber + target;
                    }
                    if (lineNumber > start) {
                        lines.add(lineContent);
                    }
                    if (lineNumber >= target) {
                        break;
                    }
                }
                logger.info("file read line --------- line number : " + lineNumber);
                lineNumberMap.put(filePath, lineNumber);
                br.close();
                reader.close();
                return weakReference.get();
            } catch (FileNotFoundException e) {
                System.out.println("no this file");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("io exception");
                e.printStackTrace();
            } finally {
                if (null != br) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (null != reader) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * indexOf:
     * 0:path
     * 1:name
     * full path http
     * --> R://download//xxx.txt
     * --> D:/metadatafile/xxx.txt
     *
     * @param fullHttpPath [D:/metaDataFile/12/song_1574940092.txt]
     * @return [/ms/]
     */
    public static String[] getHttpDirAndPathFromHttp(String fullHttpPath) {
        int index;
        String file_name = "";
        String file_path = "";


        //从写此方法
        if ((index = fullHttpPath.lastIndexOf("//")) != -1) {
            file_name = getFileName(fullHttpPath, index, 2);
            file_path = windowsToHttpPath(fullHttpPath.replace(file_name, ""));
        } else if ((index = fullHttpPath.lastIndexOf("/")) != -1) {
            file_name = getFileName(fullHttpPath, index, 1);
            if (fullHttpPath.startsWith("/")) {
                file_path = fullHttpPath.replace(file_name, "");
            } else {
                file_path = unixToHttpPath(fullHttpPath.replace(file_name, ""));
            }

        }


        logger.info(
                "fileUtil - http ----------------- dir : " + file_path + " name:" + file_name);
        return new String[]{file_path, file_name};
    }

    public static String[] getHttpDirAndPathFromTxt(String fullPath) {
        if (fullPath.contains(".")) {
            int index;
            String file_name = "";
            if ((index = fullPath.lastIndexOf("\\\\")) != -1) {
                file_name = getFileName(fullPath, index, 2);
            } else if ((index = fullPath.lastIndexOf("\\")) != -1) {
                file_name = getFileName(fullPath, index, 1);
            }
            String file_path = windowsToHttpPath(fullPath.replace(file_name, ""));
            return new String[]{file_path, file_name};
        } else {
            return new String[]{fullPath, ""};
        }
    }

    /**
     * 获取文件名称，
     *
     * @param fullHttpPath
     * @param index
     * @param offset
     * @return
     */
    public static String getFileName(String fullHttpPath, int index, int offset) {
        String file_name = "";
        if (index != -1) {
            file_name = fullHttpPath.substring(index + offset);
        }
        return file_name;
    }

    /**
     * 获取目录原系统未改变
     *
     * @param
     * @return
     */
//    public static String getTxtSaveDir(String[] filePathAndName) {
//
//        String filePath = filePathAndName[0];
//        String dir = null;
//        logger.info(" txt origin path : " + filePath);
//        if (Constant.WINDOWS_TYPE.equals(Constant.SERVER_TYPE)) {
//
//            //  int index = filePath.indexOf("//");
//            //  dir = filePath.replace(filePath.substring(0, index + 2), Constant.DOWNLOAD_DIR);
//            //需要把前缀去掉
//            dir = filePath.replace(Constant.UNIX_PATH_PREFIX, Constant.DOWNLOAD_DIR);
//            dir = httpToWindowsPath(dir);
//
//
//        }
//        if (Constant.LINUX_TYPE.equals(Constant.SERVER_TYPE)) {
//            dir = filePath.replaceAll("//", "/");
//        }
//
//        logger.info(" txt save dir : " + dir);
//        return dir;
//    }
    public static boolean deleteFile(String path) {
        if (null == path || path.isEmpty()) {
            return false;
        }
        // absolute path
        if (path.contains(":")) {
            File file = new File(path);
            if (file.isFile() && file.exists()) {
                file.delete();
                return true;
            }
            return false;
        }
        // relative path
        File[] roots = getFileRoots();
        if (null == roots || roots.length <= 0) {
            return false;
        }
        for (File file : roots) {
            File f = new File(file, path);
            if (f.isFile() && f.exists()) {
                f.delete();
                return true;
            }
        }
        return false;
    }

    public static boolean moveFile(String fromPath, String toDir, String toPath) {
        if (null == fromPath || fromPath.isEmpty() || null == toPath || toPath.isEmpty()) {
            return false;
        }
        if (fromPath.equals(toDir + toPath)) {
            return true;
        }
        File file = new File(toDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (!file.isFile() || !file.exists()) {
            return false;
        }
        logger.info("move file -------- start ");
        File from = new File(fromPath);
        return from.renameTo(new File(toDir, toPath));
    }


    public static void saveFile(InputStream inputStream, String fileDir, String fileName) {
        logger.info("FileUtil--------------save file dir : " + fileDir + " name : " + fileName);
        if (null == inputStream) {
            return;
        }
        byte[] buffer = new byte[1024];
        FileOutputStream fileOutputStream = null;
        fileDir = createWindowsDir(fileDir);
        File file = new File(fileDir, fileName);
        int length;
        try {
            fileOutputStream = new FileOutputStream(file);
            while ((length = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, length);
            }
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static File[] getFileRoots() {
        return File.listRoots();
    }

    public static String windowsToHttpPath(String replace) {
        if (null == replace || replace.isEmpty()) {
            return replace;
        }
        String path = toWindowsPath(replace);
        path = path.replace("\\\\", "//");
        return path;
    }

    public static String createWindowsDir(String dir) {
        if (null == dir || dir.isEmpty()) {
            return dir;
        }
        dir = toWindowsPath(dir);
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    private static String httpToWindowsPath(String path) {
        if (null == path || path.isEmpty()) {
            return path;
        }
        if (path.contains("//")) {
            path = path.replace("//", "\\\\");
        }
        if (path.contains("/")) {
            path = path.replace("/", "\\\\");
        }
        return path;
    }

    public static String toWindowsPath(String path) {
        if (null == path || path.isEmpty()) {
            return path;
        }
        if (path.contains("/")) {
            return httpToWindowsPath(path);
        }
        if (path.contains("\\\\")) {
            return path;
        }
        if (path.indexOf("\\") == 0) {
            path = path.substring(1);
        }
        if (path.contains("\\")) {
            path = path.replace("\\", "\\\\");
        }
        return path;
    }

    public static String toLinuxPath(String path) {
        if (null == path || path.isEmpty()) {
            return path;
        }
        return null;
    }

    public static String unixToHttpPath(String path) {
        if (null == path || path.isEmpty()) {
            return path;
        }
        if (path.contains("/")) {
            path = path.replace("/", "//");
        }
        return path;
    }
}
