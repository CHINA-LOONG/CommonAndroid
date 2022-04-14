package com.loong.common.update;

/**
 * Created by  on 2016/12/5.
 */
public class UpdateBean {

    /**
     * appInfo : {"addTime":"2015-07-17 08:59:02","appIcon":"C1EBE466-1CDC-4BD3-AB69-77C3561B9DEE","appId":"C1EBE4661CDC4BD3AB6977C3561B9DEE","appName":"微跑腿苹果","appType":"ios","downloadUrl":null,"state":1,"uid":2,"updDesc":"sfsfdsfsdf","updateType":"ignoreUpdate","versionName":"v3.3.3","versionNo":1}
     */

    private ResultBean result;
    /**
     * result : {"appInfo":{"addTime":"2015-07-17 08:59:02","appIcon":"C1EBE466-1CDC-4BD3-AB69-77C3561B9DEE","appId":"C1EBE4661CDC4BD3AB6977C3561B9DEE","appName":"微跑腿苹果","appType":"ios","downloadUrl":null,"state":1,"uid":2,"updDesc":"sfsfdsfsdf","updateType":"ignoreUpdate","versionName":"v3.3.3","versionNo":1}}
     * success : true
     */

    private boolean success;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class ResultBean {
        /**
         * addTime : 2015-07-17 08:59:02
         * appIcon : C1EBE466-1CDC-4BD3-AB69-77C3561B9DEE
         * appId : C1EBE4661CDC4BD3AB6977C3561B9DEE
         * appName : 微跑腿苹果
         * appType : ios
         * downloadUrl : null
         * state : 1
         * uid : 2
         * updDesc : sfsfdsfsdf
         * updateType : ignoreUpdate
         * versionName : v3.3.3
         * versionNo : 1
         */

        private AppInfoBean appInfo;

        public AppInfoBean getAppInfo() {
            return appInfo;
        }

        public void setAppInfo(AppInfoBean appInfo) {
            this.appInfo = appInfo;
        }

        public static class AppInfoBean {
            private String addTime;
            private String appIcon;
            private String appId;
            private String appName;
            private String appType;
            private String downloadUrl;
            private int state;
            private int uid;
            private String updDesc;
            private String updateType;
            private String versionName;
            private int versionNo;

            public String getAddTime() {
                return addTime;
            }

            public void setAddTime(String addTime) {
                this.addTime = addTime;
            }

            public String getAppIcon() {
                return appIcon;
            }

            public void setAppIcon(String appIcon) {
                this.appIcon = appIcon;
            }

            public String getAppId() {
                return appId;
            }

            public void setAppId(String appId) {
                this.appId = appId;
            }

            public String getAppName() {
                return appName;
            }

            public void setAppName(String appName) {
                this.appName = appName;
            }

            public String getAppType() {
                return appType;
            }

            public void setAppType(String appType) {
                this.appType = appType;
            }

            public String getDownloadUrl() {
                return downloadUrl;
            }

            public void setDownloadUrl(String downloadUrl) {
                this.downloadUrl = downloadUrl;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getUpdDesc() {
                return updDesc;
            }

            public void setUpdDesc(String updDesc) {
                this.updDesc = updDesc;
            }

            public String getUpdateType() {
                return updateType;
            }

            public void setUpdateType(String updateType) {
                this.updateType = updateType;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public int getVersionNo() {
                return versionNo;
            }

            public void setVersionNo(int versionNo) {
                this.versionNo = versionNo;
            }
        }
    }
}
