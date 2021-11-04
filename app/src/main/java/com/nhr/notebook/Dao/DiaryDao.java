package com.nhr.notebook.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nhr.notebook.DataBase.DBContants;
import com.nhr.notebook.DataBase.DBHelper;
import com.nhr.notebook.Entity.Diary;
import com.nhr.notebook.Entity.Photo;

import java.util.ArrayList;
import java.util.List;

public class DiaryDao {
    DBHelper helper;

    public DiaryDao(Context context) {
        helper = new DBHelper(context);
    }

    public long insert(Diary diary) {
        SQLiteDatabase db = helper.getWritableDatabase();

        /*
         *插入日记
         * */
        ContentValues cv = new ContentValues();
        cv.put(DBContants.TABLE_FIELD_TITLE, diary.getTittle());
        cv.put(DBContants.TABLE_FIELD_AUTHOR, diary.getAuthor());
        cv.put(DBContants.TABLE_FIELD_CONTEXT, diary.getContext());
        cv.put(DBContants.TABLE_FIELD_DATE, diary.getDate());
        long result1 = db.insert(DBContants.TABLE_DIARY_NAME, null, cv);
        long result2 = 1;


        /*
         *插入照片
         * */

        List<Photo> photoList = diary.getPhoto();
        if(photoList == null||photoList.isEmpty() ){
            result2 = 1;
        }else
        for (int i = 0; i < photoList.size(); i++) {
            ContentValues cvPhoto = new ContentValues();
            cvPhoto.put(DBContants.TABLE_FIELD_PHOTO_ID, photoList.get(i).getId());
            cvPhoto.put(DBContants.TABLE_FIELD_PHOTO_PATH, photoList.get(i).getPath());
            cvPhoto.put(DBContants.TABLE_FIELD_PHOTO2DIARY_ID, photoList.get(i).getDiaryId());
            if (db.insert(DBContants.TABLE_PHOTO_NAME, null, cvPhoto) < 0) {
                result2 = -1;
            }
        }


        /*成功*/
        if (result2 == 1 && result1 >= 0) {
            return result1;
        }
        // 失败
        return -1;
    }

    public Cursor searchAllDiary() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from " + DBContants.TABLE_DIARY_NAME;
        Log.e("TAG", "searchAll: "  +sql );
        //  Cursor result = db.query(DBContants.TABLE_DIARY_NAME, null, null, null, null, null, null);
        Cursor result = db.rawQuery(sql, null);

        return result;
    }

    public Cursor searchPhotoByDiaryID(Integer DiaryID) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from " + DBContants.TABLE_PHOTO_NAME+" where "+DBContants.TABLE_FIELD_PHOTO2DIARY_ID+" = ?";
        Log.e("TAG", "searchAll: "  +sql );
        //  Cursor result = db.query(DBContants.TABLE_DIARY_NAME, null, null, null, null, null, null);
        Cursor result = db.rawQuery(sql, new String[]{DiaryID.toString()});
        return result;
    }


    public long update(Diary diary) {

        /*更新日记*/
        ContentValues cv = new ContentValues();
        cv.put(DBContants.TABLE_FIELD_TITLE, diary.getTittle());
        cv.put(DBContants.TABLE_FIELD_AUTHOR, diary.getAuthor());
        cv.put(DBContants.TABLE_FIELD_CONTEXT, diary.getContext());
        cv.put(DBContants.TABLE_FIELD_DATE, diary.getDate());
        SQLiteDatabase db = helper.getWritableDatabase();
        long result1 = db.update(DBContants.TABLE_DIARY_NAME, cv, DBContants.TABLE_FIELD_ID + "=?", new String[]{diary.getId().toString()});

       /*更新图片*/
        long result2 = 1;
        db.delete(DBContants.TABLE_PHOTO_NAME, DBContants.TABLE_FIELD_PHOTO2DIARY_ID + "=?", new String[]{diary.getId().toString()});
        List<Photo> photoList = diary.getPhoto();
        if(photoList == null||photoList.isEmpty() ){
            result2 = 1;
        }else
        for (int i = 0; i < photoList.size(); i++) {
            ContentValues cvPhoto = new ContentValues();
            cvPhoto.put(DBContants.TABLE_FIELD_PHOTO_ID, photoList.get(i).getId());
            cvPhoto.put(DBContants.TABLE_FIELD_PHOTO_PATH, photoList.get(i).getPath());
            cvPhoto.put(DBContants.TABLE_FIELD_PHOTO2DIARY_ID, diary.getId());
            if (db.insert(DBContants.TABLE_PHOTO_NAME, null, cvPhoto) < 0) {
                result2 = -1;
            }
        }


        /*成功*/
        if (result2 == 1 && result1 >= 0) {
            return result1;
        }
        // 失败
        return -1;

    }

    public long delete(Integer diaryID){
        SQLiteDatabase db = helper.getWritableDatabase();
       long result =  db.delete(DBContants.TABLE_DIARY_NAME,DBContants.TABLE_FIELD_ID+"=?",new String[]{diaryID.toString()});
        db.delete(DBContants.TABLE_PHOTO_NAME,DBContants.TABLE_FIELD_PHOTO2DIARY_ID+"=?",new String[]{diaryID.toString()});
        return result;
    }
}
