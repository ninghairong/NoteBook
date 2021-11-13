package com.nhr.notebook.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhr.notebook.Adapter.FragmentAdapter;
import com.nhr.notebook.Dao.DiaryDao;
import com.nhr.notebook.Entity.Diary;

import com.nhr.notebook.Fragment.MainFragment;
import com.nhr.notebook.Fragment.MyFragment;
import com.nhr.notebook.R;
import com.nhr.notebook.Tools.Tool;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Fragment> fragmentList;
    private List<ImageView> ImageViewList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*状态栏颜色设置为深色*/
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main);


        ViewPager fgContainer = findViewById(R.id.replaced_page);
        ImageView main = findViewById(R.id.main);
        ImageView  mine = findViewById(R.id.mine);



       
        
        DiaryDao diaryDao = new DiaryDao(this);
//        ArrayList<Photo> photoArrayList = new ArrayList<>();


//        diaryDao.insert(new Diary(1,"测试","第一个日记","2019-01-01","宁海荣",photoArrayList));
//        diaryDao.insert(new Diary(1,"测试","第一个日记","2019-01-01","宁海荣",photoArrayList));
//        diaryDao.insert(new Diary(1,"测试","第一个日记","2019-01-01","宁海荣",photoArrayList));
//        diaryDao.insert(new Diary(1,"今天的第一条日记","第一个日记","2019-01-11","宁海荣",null));
//        diaryDao.insert(new Diary(1,"今天的第2条日记","第一个日记","2019-01-11","宁海荣",null));
//        diaryDao.insert(new Diary(1,"今天的第3条日记","第一个日记","2019-01-11","宁海荣",null));
//        diaryDao.insert(new Diary(1,"今天的第4条日记","第一个日记","2019-01-11","宁海荣","i AM A PHOTO"));

//        diaryDao.insert(new Diary(1,"图片测试", "下面是图片了 " +"<img src='http://www.qqpk.cn/Article/UploadFiles/201411/20141116135722282.jpg'/>" +
//                "这也是图片" +"<img src='/storage/emulated/0/DCIM/Camera/IMG_20211101_222139.jpg'/>" +
//                "还有一张"+  "<img src='http://img.61gequ.com/allimg/2011-4/201142614314278502.jpg' />","2019-01-12","宁海荣",null));
//        diaryDao.insert(new Diary(1,"测试","第一个日记","2019-01-01","宁海荣",photoArrayList));

        Cursor cursor =  diaryDao.searchAllDiary();
        ArrayList<Diary> list = Tool.toDiary(cursor);
        System.out.println(list);
        System.out.println();
        System.out.println(cursor);

        fragmentList=new ArrayList<>();
        fragmentList.add(new MainFragment());
        fragmentList.add(new MyFragment());

        ImageViewList = new ArrayList<>();
        ImageViewList.add(main);
        ImageViewList.add(mine);
        ImageViewList.get(0).setColorFilter(Color.rgb(18, 150, 219));
        ImageViewList.get(1).setColorFilter(Color.GRAY);
        fgContainer.setAdapter(new FragmentAdapter(getSupportFragmentManager(),fragmentList));
        //fgContainer.seton(new );
        /*设置颜色动态变化*/
       fgContainer.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
           }
           @Override
           public void onPageSelected(int position) {
               for (ImageView temp:ImageViewList
                    ) {
                   temp.setColorFilter(Color.GRAY);
               }
               ImageViewList.get(position).setColorFilter(Color.rgb(18, 150, 219));
           }

           @Override
           public void onPageScrollStateChanged(int state) {

           }
       });
       main.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               fgContainer.setCurrentItem(0);
           }
       });
       mine.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               fgContainer.setCurrentItem(1);
           }
       });




        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }



    }
}