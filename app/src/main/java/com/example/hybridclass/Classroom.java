package com.example.hybridclass;

public class Classroom {
    private String className;
    private String classCode;
    private String creatorEmail;

    public Classroom(String className, String classCode, String creatorEmail) {
        this.className = className;
        this.classCode = classCode;
        this.creatorEmail = creatorEmail;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorName) {
        this.creatorEmail = creatorName;
    }
}
