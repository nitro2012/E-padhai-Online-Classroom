package com.example.e_padhai;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentData {

    private String name,email,roll,key,category;
    ArrayList<String> classIdArr;


    public StudentData(){}


    public ArrayList<String> getClassIdArr() {
        return classIdArr;
    }

    public void setClassIdArr(ArrayList<String> classIdArr) {
        this.classIdArr = classIdArr;
    }

    public StudentData(String name, String email, String roll, String key,String category, ArrayList<String> classIdArr) {
        this.name = name;
        this.email = email;
        this.classIdArr=classIdArr;
        this.roll = roll;
        this.category=category;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }



    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("roll", roll);
        result.put("category",category);
        result.put("key", key);
        result.put("classIdArr", classIdArr);

        return result;
    }

}
