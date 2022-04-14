package com.loong.common.download;

public class DownInfo {


    /*下载url*/
    private String url;
    /*存储位置*/
    private String savePath;

    private IDownApi service;

    /*文件总长度*/
    private long countLength;
    /*下载长度*/
    private long readLength;

    /*state状态数据库保存*/
    private int stateInte;

    /*回调监听*/
    private DownloadOnNextListener listener;

    public DownInfo(String url){
        setUrl(url);
    }
    public DownInfo(String url,DownloadOnNextListener listener){
        setUrl(url);
    }


    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public IDownApi getService() {
        return service;
    }
    public void setService(IDownApi service) {
        this.service = service;
    }

    public DownloadOnNextListener getListener() {
        return listener;
    }
    public void setListener(DownloadOnNextListener listener) {
        this.listener = listener;
    }

    public String getSavePath() {
        return savePath;
    }
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public long getCountLength() {
        return countLength;
    }
    public void setCountLength(long countLength) {
        this.countLength = countLength;
    }

    public long getReadLength() {
        return readLength;
    }
    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }


    public int getStateInte() {
        return stateInte;
    }

    public void setStateInte(int stateInte) {
        this.stateInte = stateInte;
    }

    public DownState getState() {
        switch (getStateInte()){
            case 0:
                return DownState.START;
            case 1:
                return DownState.DOWN;
            case 2:
                return DownState.PAUSE;
            case 3:
                return DownState.STOP;
            case 4:
                return DownState.ERROR;
            case 5:
            default:
                return DownState.FINISH;
        }
    }

    public void setState(DownState state) {
        setStateInte(state.getState());
    }


}
