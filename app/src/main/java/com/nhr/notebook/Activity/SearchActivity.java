package com.nhr.notebook.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nhr.notebook.Adapter.ItemAdapter;
import com.nhr.notebook.Dao.DiaryDao;
import com.nhr.notebook.Entity.Diary;
import com.nhr.notebook.R;
import com.nhr.notebook.Tools.Tool;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {


    private ItemAdapter itemAdapter;
    private DiaryDao diaryDao;
    ImageView searchButton;
    EditText editSearch;
    ListView searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*状态栏颜色设置为深色*/
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_search);


        searchButton = findViewById(R.id.search_button);
        searchResult = findViewById(R.id.search_result);
        editSearch = findViewById(R.id.edit_search);

        diaryDao = new DiaryDao(this);
        editSearch.setImeOptions(EditorInfo.IME_ACTION_SEND);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Diary> diaryList = Tool.toDiary(diaryDao.searchByWord("%" + editSearch.getText().toString() + "%"));
                itemAdapter = new ItemAdapter(SearchActivity.this, diaryList);
                searchResult.setAdapter(itemAdapter);
            }
        });

        searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Diary clickDiary = itemAdapter.getItem(i);
                Intent intent = new Intent(SearchActivity.this, showActivity.class);
                intent.putExtra("clickDiary", clickDiary);
                startActivity(intent);
            }
        });
        //https://blog.csdn.net/Iceshow0428/article/details/24428417
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    ArrayList<Diary> diaryList = Tool.toDiary(diaryDao.searchByWord("%" + editSearch.getText().toString() + "%"));
                    itemAdapter = new ItemAdapter(SearchActivity.this, diaryList);
                    searchResult.setAdapter(itemAdapter);
                }
                return true;

            }
        });

    }

}