//package com.nhr.notebook.Entity;
//
//import com.nhr.notebook.DataBase.DBContants;
//
//import java.io.Serializable;
//
//public class Photo implements Serializable {
//    private Integer id = 0;
//    private String path = "";
//    private Integer diaryId = 0;
//
//    public Photo() {
//    }
//
//    public Photo(int id, String path, int diaryId) {
//        this.id = id;
//        this.path = path;
//        this.diaryId = diaryId;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getPath() {
//        return path;
//    }
//
//    public void setPath(String path) {
//        this.path = path;
//    }
//
//    public int getDiaryId() {
//        return diaryId;
//    }
//
//    public void setDiaryId(int diaryId) {
//        this.diaryId = diaryId;
//    }
//
//    @Override
//    public String toString() {
//        return "Photo{" +
//                "id=" + id +
//                ", path='" + path + '\'' +
//                ", diaryId=" + diaryId +
//                '}';
//    }
//}
