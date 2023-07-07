package com.example.e_padhai;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeacherData {
    private String name,email,post,image,key;
    ArrayList<String> classIdArr;

   /* public TeacherData(String tname, String temail, String tpost, String tdownloadUrl, String uid, ArrayList<String> classIdArr) {
    }*/
   public TeacherData(){}


    public ArrayList<String> getClassIdArr() {
        return classIdArr;
    }

    public void setClassIdArr(ArrayList<String> classIdArr) {
        this.classIdArr = classIdArr;
    }
public TeacherData(String name, String email, String post, String image, String key, ArrayList<String> classIdArr) {
        this.name = name;
        this.email = email;
        this.classIdArr=classIdArr;
        this.post = post;
        this.image = image;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getImage() {

        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
        result.put("post", post);
        result.put("image", image);
        result.put("key", key);
        result.put("classIdArr", classIdArr);

        return result;
    }
}
