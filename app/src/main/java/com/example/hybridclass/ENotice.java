package com.example.hybridclass;

public class ENotice {

    private String noticeUrl;

    public ENotice(String noticeUrl, String classCode) {
        this.noticeUrl = noticeUrl;
        this.classCode = classCode;
    }

    public ENotice() {
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    private String classCode;
}
