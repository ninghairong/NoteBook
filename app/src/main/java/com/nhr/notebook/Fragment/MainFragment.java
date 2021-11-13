package com.nhr.notebook.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhr.notebook.Activity.EditActivity;
import com.nhr.notebook.Activity.SearchActivity;
import com.nhr.notebook.Activity.showActivity;
import com.nhr.notebook.Adapter.ItemAdapter;
import com.nhr.notebook.Dao.DiaryDao;
import com.nhr.notebook.Entity.Diary;
import com.nhr.notebook.R;
import com.nhr.notebook.Tools.Tool;

import java.util.ArrayList;
import java.util.Calendar;


public class MainFragment extends Fragment {
    ListView diaryListView;
    TextView month;
    TextView day;
    ImageView newDiary;
    ImageView search;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void Init() {
        DiaryDao diaryDao = new DiaryDao(getContext());
        Cursor cursor = diaryDao.searchAllDiary();
        ArrayList<Diary> list = Tool.toDiary(cursor);
        Log.e("TAG", "onCreateView: " + list.size());
        Calendar calendar = Calendar.getInstance();

        day.setText(calendar.get(Calendar.DAY_OF_MONTH) + "");
        month.setText(calendar.get(Calendar.MONTH) + 1 + "月");

        ItemAdapter itemAdapter = new ItemAdapter(getContext(), list);
        diaryListView.setAdapter(itemAdapter);
        diaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Diary clickDiary = itemAdapter.getItem(i);
                Intent intent = new Intent(getActivity(), showActivity.class);
                intent.putExtra("clickDiary", clickDiary);
                startActivity(intent);
            }
        });

        diaryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog dialog = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("删除确认");
                builder.setMessage("您确定要删除这条日记吗？");
                SpannableString sure = new SpannableString("确认");
                ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
                sure.setSpan(span,0,2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setPositiveButton(sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Diary diary = list.get(position);
                        diaryDao.delete(diary.getId());
                        Init();
                        Toast.makeText(getContext(),"已成功删除！",Toast.LENGTH_SHORT).show();
                    }
                }).create();
                SpannableString cancel = new SpannableString("取消");
                ForegroundColorSpan span1 = new ForegroundColorSpan(Color.BLACK);
                cancel.setSpan(span1,0,2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
                return true;
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        day = view.findViewById(R.id.day);

        month = view.findViewById(R.id.month);

        diaryListView = view.findViewById(R.id.diary_list);

        newDiary = view.findViewById(R.id.new_diary);

        search = view.findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        newDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),EditActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Init();
    }
}