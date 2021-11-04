package com.nhr.notebook.Entity;



import java.io.Serializable;
import java.util.List;

public class Diary implements Serializable {
   private Integer id = 0;
   private String tittle= "";
   private String context = "";
   private String Date = "";
   private String author = "";
   private List<Photo> photo = null;

   public Diary() {
   }

   public Diary(Integer id, String tittle, String context, String date, String author, List<Photo> photo) {
      this.id = id;
      this.tittle = tittle;
      this.context = context;
      Date = date;
      this.author = author;
      this.photo = photo;
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getTittle() {
      return tittle;
   }

   public void setTittle(String tittle) {
      this.tittle = tittle;
   }

   public String getContext() {
      return context;
   }

   public void setContext(String context) {
      this.context = context;
   }

   public String getDate() {
      return Date;
   }

   public void setDate(String date) {
      Date = date;
   }

   public String getAuthor() {
      return author;
   }

   public void setAuthor(String author) {
      this.author = author;
   }

   public List<Photo> getPhoto() {
      return photo;
   }

   public void setPhoto(List<Photo> photo) {
      this.photo = photo;
   }

   @Override
   public String toString() {
      return "Diary{" +
              "id=" + id +
              ", tittle='" + tittle + '\'' +
              ", context='" + context + '\'' +
              ", Date='" + Date + '\'' +
              ", author='" + author + '\'' +
              ", photo=" + photo +
              '}'+"\n";
   }
}
