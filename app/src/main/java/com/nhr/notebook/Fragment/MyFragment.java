package com.nhr.notebook.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.nhr.notebook.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class MyFragment extends Fragment {
    private String name;
    private EditText editName;
    private String sign;
    private EditText editSign;
    private String birthday;
    private TextView editBirthday;
    private TimePickerView pvTime;
    private ImageView save;


    public MyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        //https://blog.csdn.net/gengkui9897/article/details/87929235
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("my_info", Context.MODE_PRIVATE);

        name = sharedPreferences.getString("username", "默认用户");
        sign = sharedPreferences.getString("sign", "默认的个性签名");
        birthday = sharedPreferences.getString("birthday", "2000-10-10");


        editBirthday = view.findViewById(R.id.edit_birthday);
        editName = view.findViewById(R.id.edit_name);
        editSign = view.findViewById(R.id.edit_sign);
        save = view.findViewById(R.id.my_save);


        editName.setText(name);
        editSign.setText(sign);
        editBirthday.setText(birthday);


        editBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTimePicker();
                pvTime.show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });


        return view;
    }

    private void save() {
        name = editName.getText().toString();
        sign = editSign.getText().toString();
        birthday = editBirthday.getText().toString();
        //https://blog.csdn.net/gengkui9897/article/details/87929235
        SharedPreferences.Editor editor = Objects.requireNonNull(getActivity()).getSharedPreferences("my_info", Context.MODE_PRIVATE).edit();
        editor.putString("username", name);
        editor.putString("sign", sign);
        editor.putString("birthday", birthday);
        editor.apply();
    }

    @Override
    public void onPause() {
        super.onPause();
        save();
    }

    //初始化时间选择器
    private void initTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 1, 1);//起始时间
        Calendar endDate = Calendar.getInstance();
        // endDate.set(2099, 12, 31);//结束时间
        pvTime = new TimePickerView.Builder(getActivity(),
                new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        //选中事件回调

                        editBirthday.setText(getTimes(date));
                    }
                })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "", "")
                .isCenterLabel(true)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(null)
                .build();
    }

    //格式化时间
    private String getTimes(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

}