package com.nhr.notebook.Tools;

import android.content.Context;
import android.database.Cursor;

import com.nhr.notebook.Dao.DiaryDao;
import com.nhr.notebook.Entity.Diary;
import com.nhr.notebook.Entity.Photo;

import java.util.ArrayList;

public class Tool {
    public static ArrayList<Diary> toDiary(Context context,Cursor diary) {
        ArrayList<Diary> diaryArrayList = new ArrayList<>();
        ArrayList<Photo> photoArrayList = null;



        while (diary.moveToNext()) {
            Diary diaryTemp = new Diary();
            Integer id = diary.getInt(0);
            diaryTemp.setId(id);
            diaryTemp.setTittle(diary.getString(1));
            diaryTemp.setContext(diary.getString(2));
            diaryTemp.setAuthor(diary.getString(3));
            diaryTemp.setDate(diary.getString(4));
           photoArrayList =  findPhoto(id,context);
            diaryTemp.setPhoto(photoArrayList);
            diaryArrayList.add(diaryTemp);
        }
        return diaryArrayList;
    }

    private static ArrayList<Photo> findPhoto(Integer DiaryID, Context context){
        ArrayList<Photo> photoArrayList = new ArrayList<>();
        DiaryDao diaryDao = new DiaryDao(context);
        Cursor photo = diaryDao.searchPhotoByDiaryID(DiaryID);
        while(photo.moveToNext()){
            Photo photoTemp = new Photo();
            photoTemp.setId(photo.getInt(0));
            photoTemp.setPath(photo.getString(1));
            photoTemp.setDiaryId(photo.getInt(2));
            photoArrayList.add(photoTemp);
        }
        return photoArrayList;
    }
    //基姆拉尔森计算公式根据日期判断星期几
    public static String CalculateWeekDay(String date) {
        int y,m,d;
        y = Integer.parseInt(date.substring(0,4));
        m = Integer.parseInt(date.substring(5,7));
        d = Integer.parseInt(date.substring(8));
        System.out.println(y+" "+" "+m+" "+d);



        if(m < 1 || m >12){
            System.out.println("你输入的月份不再范围内，请重新输入！");
        }
        if (m == 1 || m == 2) {
            m += 12;
            y--;
        }
        int iWeek = (d + 2 * m + 3 * (m + 1) / 5 + y + y / 4 - y / 100 + y / 400) % 7;
        switch (iWeek) {
            case 0:
                return "周一";

            case 1:
                return "周二";

            case 2:
                return "周三";

            case 3:
                return "周四";

            case 4:
                return "周五";

            case 5:
                return "周六";

            case 6:
                return "周日";

        }
        return "未知";
    }


}
