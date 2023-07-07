package com.example.e_padhai;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class classesData {
    private String className,section,teacherName,classId;
    private ArrayList<String> studentList;
    private ArrayList <feedData> fData;
    private HashMap<String, HashMap<String, Object>> Tests;
    private Boolean VideoCall;


    public classesData(boolean VideoCall, String className, String section, String teacherName, String classId,ArrayList <feedData> fData) {
        this.className = className;
        this.section = section;
        this.teacherName = teacherName;
        this.classId = classId;
        this.studentList = studentList;
    }
    public classesData(String className, String section, String teacherName, String classId, ArrayList<String> studentList,ArrayList <feedData> fData) {
        this.className = className;
        this.section = section;
        this.teacherName = teacherName;
        this.classId = classId;
        this.fData=fData;
        this.studentList = studentList;
    }
    public classesData(Boolean videoCall,String className, String section, String teacherName, String classId, ArrayList<String> studentList,ArrayList <feedData> fData,HashMap<String,HashMap<String,Object>> Tests) {
        this.className = className;
        this.VideoCall=videoCall;
        this.section = section;
        this.teacherName = teacherName;
        this.classId = classId;
        this.Tests=Tests;
        this.fData=fData;
        this.studentList = studentList;
    }
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public ArrayList<String> getStudentList() {
        return studentList;
    }

    public void setStudentList(ArrayList<String> studentList) {
        this.studentList = studentList;
    }

    public classesData() {
    }
    public void setfData(ArrayList < feedData> fData) {
        this.fData=fData;
    }
    public ArrayList< feedData> getfData() {
        return fData;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("className", className);
        result.put("section", section);
        result.put("teacherName", teacherName);
        result.put("classId", classId);
        result.put("studentList", studentList);
        result.put("fData", fData);
        result.put("VideoCall",VideoCall);
        return result;
    }

    public void setTests(HashMap<String, HashMap<String, Object>> tests){
        this.Tests=tests;
    }
    public HashMap<String, HashMap<String, Object>> getTests() {

        return Tests;
    }

    public Boolean getVideoCall() {
        return VideoCall;
    }
}
