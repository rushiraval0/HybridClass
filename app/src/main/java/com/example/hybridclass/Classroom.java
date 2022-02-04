package com.example.hybridclass;

public class Classroom {
    private String className;
    private String classCode;
    private String creatorName;

    public Classroom(String className, String classCode, String creatorName) {
        this.className = className;
        this.classCode = classCode;
        this.creatorName = creatorName;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
}
