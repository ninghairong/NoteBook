package com.nhr.notebook.Tools;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.nhr.notebook.Dao.DiaryDao;
import com.nhr.notebook.Entity.Diary;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tool {
    public static ArrayList<Diary> toDiary(Cursor diary) {
        ArrayList<Diary> diaryArrayList = new ArrayList<>();
        while (diary.moveToNext()) {
            Diary diaryTemp = new Diary();
            Integer id = diary.getInt(0);
            diaryTemp.setId(id);
            diaryTemp.setTittle(diary.getString(1));
            diaryTemp.setContext(diary.getString(2));
            diaryTemp.setAuthor(diary.getString(3));
            diaryTemp.setDate(diary.getString(4));

            diaryTemp.setPhoto(diary.getString(5));
            diaryArrayList.add(diaryTemp);
        }
        return diaryArrayList;
    }

    //    private static ArrayList<Photo> findPhoto(Integer DiaryID, Context context){
//        ArrayList<Photo> photoArrayList = new ArrayList<>();
//        DiaryDao diaryDao = new DiaryDao(context);
//        Cursor photo = diaryDao.searchPhotoByDiaryID(DiaryID);
//        while(photo.moveToNext()){
//            Photo photoTemp = new Photo();
//            photoTemp.setId(photo.getInt(0));
//            photoTemp.setPath(photo.getString(1));
//            photoTemp.setDiaryId(photo.getInt(2));
//            photoArrayList.add(photoTemp);
//        }
//        return photoArrayList;
//    }
    //基姆拉尔森计算公式根据日期判断星期几
    public static String CalculateWeekDay(String date) {
        int y, m, d;
        y = Integer.parseInt(date.substring(0, 4));
        m = Integer.parseInt(date.substring(5, 7));
        d = Integer.parseInt(date.substring(8));
        System.out.println(y + " " + " " + m + " " + d);


        if (m < 1 || m > 12) {
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

    public static SpannableStringBuilder contextToSpSpannableString(String note) {
        SpannableStringBuilder result = new SpannableStringBuilder(note);
        Pattern img = Pattern.compile("<img src='(.*?)'/>");
        Matcher mImg = img.matcher(note);

        //查找图片
        while (mImg.find()) {
            int start = mImg.start();
            int end = mImg.end();
            String imgPath = mImg.group(1);
            Drawable drawable = getDrawable(imgPath);
            ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
            result.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return result;
    }



    public static String getFirstPhoto (String note) {
        Pattern img = Pattern.compile("<img src='(.*?)'/>");
        Matcher mImg = img.matcher(note);

        //查找图片
        if(mImg.find()) {
            int start = mImg.start();
            int end = mImg.end();
            String imgPath = mImg.group(1);
            return imgPath;
        }
        return null;
    }

    public static Drawable getDrawable(String s) {
        Drawable drawable = null;

        Log.e("TAG", "getDrawable: " + s);
        drawable = Drawable.createFromPath(s); //显示本地图片
        if (drawable == null) {
            return null;
        } else
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
                    .getIntrinsicHeight());
        return drawable;
    }


}
