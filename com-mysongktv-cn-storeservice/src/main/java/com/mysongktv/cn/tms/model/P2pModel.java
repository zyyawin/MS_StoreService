package com.mysongktv.cn.tms.model;

import java.io.Serializable;
import java.util.List;

public class P2pModel implements Serializable {

    private InfoData info;
    private List<FileData> files;

    private int status;

    public InfoData getInfo() {
        return info;
    }

    public void setInfo(InfoData info) {
        this.info = info;
    }

    public List<FileData> getFiles() {
        return files;
    }

    public void setFiles(List<FileData> files) {
        this.files = files;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class InfoData {
        private String id;
        private String path;
        private int progress;
        private int complete;
        private int report;
        private String createAt;
        private String updateAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }

        public int getComplete() {
            return complete;
        }

        public void setComplete(int complete) {
            this.complete = complete;
        }

        public int getReport() {
            return report;
        }

        public void setReport(int report) {
            this.report = report;
        }

        public String getCreateAt() {
            return createAt;
        }

        public void setCreateAt(String createAt) {
            this.createAt = createAt;
        }

        public String getUpdateAt() {
            return updateAt;
        }

        public void setUpdateAt(String updateAt) {
            this.updateAt = updateAt;
        }
    }


    public class FileData {
        private String fileName;
        private String fileMd5;
        private String fileLength;
        private String absFilePath;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileMd5() {
            return fileMd5;
        }

        public void setFileMd5(String fileMd5) {
            this.fileMd5 = fileMd5;
        }

        public String getFileLength() {
            return fileLength;
        }

        public void setFileLength(String fileLength) {
            this.fileLength = fileLength;
        }

        public String getAbsFilePath() {
            return absFilePath;
        }

        public void setAbsFilePath(String absFilePath) {
            this.absFilePath = absFilePath;
        }
    }

    /**
     * {
     *
     *     "info":{
     *
     *         "id":""， 种子id
     *
     *         "path":"种子路径"
     *
     *        "progress":100， 当前进度0-100
     *
     *       "complete:1, 是否已经完成， 1完成，2未完成
     *
     *       ”report“: 1， tracker是否已经接收到该种子
     *
     *       createAt, 创建时间
     *
     *       updateAt, 更新时间
     *
     *     },
     *
     *     "files":[
     *
     *         {
     *
     *             "fileName": 文件名字
     *
     *            "fileMd5":   文件md5
     *
     *            "fileLength": 文件长度，
     *
     *            "absFilePath": 文件在本地磁盘的绝对位置，如果为空，表示还在p2p Node程序指定的目录中。
     *
     *        }
     *
     *     ],
     *
     *     "status":0停止下载，1开始下载，-1，已经下载完
     *
     * }
     */

}
