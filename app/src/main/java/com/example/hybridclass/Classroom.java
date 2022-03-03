package com.example.hybridclass;

import java.util.HashMap;
import java.util.List;

public class Classroom {
    private String className;
    private String classCode;
    private String creatorName;
    private int studentCount;
    private HashMap<String,String> studentList;

    public HashMap<String, String> getStudentList() {
        return studentList;
    }

    public void setStudentList(HashMap<String, String> studentList) {
        this.studentList = studentList;
    }

    public Classroom(String className, String classCode, HashMap<String, String> getStudentList, String classDescription, String creatorEmail) {
        this.className = className;
        this.classCode = classCode;
        this.studentList = studentList;
        this.classDescription = classDescription;
        this.creatorEmail = creatorEmail;
    }

    public Classroom(String className, String classCode, int studentCount, HashMap<String, String> getStudentList, String classDescription, String creatorEmail) {
        this.className = className;
        this.classCode = classCode;
        this.studentCount = studentCount;
        this.studentList = studentList;
        this.classDescription = classDescription;
        this.creatorEmail = creatorEmail;
    }



    public Classroom(String className, String classCode, int studentCount, String classDescription, String creatorEmail) {
        this.className = className;
        this.classCode = classCode;
        this.studentCount = studentCount;
        this.classDescription = classDescription;
        this.creatorEmail = creatorEmail;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

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
