package com.example.hybridclass;

public class Classroom {
    private String className;
    private String classCode;
    private String creatorName;

    private String classDescription;

    public Classroom(){

    }

    public Classroom(String className, String classCode, String creatorName, String classDescription) {
        this.className = className;
        this.classCode = classCode;
        this.creatorName = creatorName;
        this.classDescription = classDescription;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public void setClassDescription(String classDescription) {
        this.classDescription = classDescription;
    }
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
