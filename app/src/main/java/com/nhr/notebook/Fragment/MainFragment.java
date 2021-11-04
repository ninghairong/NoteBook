package com.nhr.notebook.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nhr.notebook.Activity.EditActivity;
import com.nhr.notebook.Adapter.ItemAdapter;
import com.nhr.notebook.Dao.DiaryDao;
import com.nhr.notebook.Entity.Diary;
import com.nhr.notebook.R;
import com.nhr.notebook.Tools.Tool;

import java.util.ArrayList;
import java.util.Calendar;




public class MainFragment extends Fragment {


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        DiaryDao diaryDao = new DiaryDao(getContext());
        Cursor cursor =  diaryDao.searchAllDiary();
        ArrayList<Diary> list = Tool.toDiary(getContext(), cursor);
        Log.e("TAG", "onCreateView: "+list.size());
        Calendar calendar = Calendar.getInstance();
        TextView day = view.findViewById(R.id.day);
        TextView month = view.findViewById(R.id.month);
        day.setText(calendar.get(Calendar.DAY_OF_MONTH)+"");
        month.setText(calendar.get(Calendar.MONTH)+1+"月");
        ListView diaryListView = view.findViewById(R.id.diary_list);
        ItemAdapter itemAdapter = new ItemAdapter(getContext(),list);
        diaryListView.setAdapter(itemAdapter);
        diaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 Diary clickDiary = itemAdapter.getItem(i);
                Intent intent = new Intent(getActivity(), EditActivity.class);
                intent.putExtra("clickDiary",clickDiary);

                startActivity(intent);
            }
        });
        return view;
    }
}