package com.nhr.notebook.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.nhr.notebook.Dao.DiaryDao;
import com.nhr.notebook.Entity.Diary;
import com.nhr.notebook.R;
import com.nhr.notebook.Tools.Tool;

public class showActivity extends AppCompatActivity {
    private Diary diary;
    private void Init(){
        if(diary.getTittle().equals("") && diary.getContext().equals("")){
            finish();
        }

        TextView context =  findViewById(R.id.show_context);
        TextView date = findViewById(R.id.show_date);
        TextView author = findViewById(R.id.show_author);
        TextView tittle = findViewById(R.id.show_tittle);
        context.setMovementMethod(ScrollingMovementMethod.getInstance());

        context.setText(Tool.contextToSpSpannableString(diary.getContext()));
        date.setText(diary.getDate());
        author.setText(diary.getAuthor());
        tittle.setText(diary.getTittle());

        context.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(showActivity.this,EditActivity.class);
                intent1.putExtra("clickDiary",diary);
                startActivityForResult(intent1,1);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*状态栏颜色设置为深色*/
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.activity_show);
        Intent intent = getIntent();
        diary = (Diary) intent.getSerializableExtra("clickDiary");
        Init();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK){
            diary = (Diary) data.getSerializableExtra("diary");
            Init();
        }
    }
}