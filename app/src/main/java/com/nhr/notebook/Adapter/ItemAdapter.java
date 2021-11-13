package com.nhr.notebook.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.nhr.notebook.Entity.Diary;
import com.nhr.notebook.R;
import com.nhr.notebook.Tools.Tool;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends BaseAdapter {
    private Context mcontext;
    private final List<Diary> all_List;//所有元素


    public ItemAdapter(Context mcontext, List<Diary> all_List) {
        this.mcontext = mcontext;
        this.all_List = all_List;

    }


    @Override
    public int getCount() {
        return all_List.size();
    }

    @Override
    public Diary getItem(int i) {
        return all_List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    public static class ViewHolder {
        TextView item_day;
        TextView item_week;
        TextView item_tittle;
        TextView item_date;
        ImageView item_image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {

            convertView = View.inflate(mcontext, R.layout.diary_item, null);

            holder = new ViewHolder();
            holder.item_day = convertView.findViewById(R.id.item_day);

            holder.item_week = convertView.findViewById(R.id.item_week);

            holder.item_date=convertView.findViewById(R.id.item_date);
            holder.item_tittle = convertView.findViewById(R.id.item_tittle);
            holder.item_image = convertView.findViewById(R.id.item_photo);

            convertView.setTag(holder);
        } else {
            Log.e("TAG", "getView: convertView 不为空"+convertView );
            holder = (ViewHolder) convertView.getTag();
        }

        holder.item_date.setText(all_List.get(position).getDate());
        holder.item_tittle.setText(all_List.get(position).getTittle());
        /*图片和日期*/
        holder.item_day.setText(all_List.get(position).getDate().substring(8));
        holder.item_week.setText(Tool.CalculateWeekDay(all_List.get(position).getDate()));
        String photoPath = all_List.get(position).getPhoto();
        if (photoPath!=null&&photoPath!="")
            holder.item_image.setImageDrawable(Tool.getDrawable(photoPath));
        else
            holder.item_image.setImageResource(R.drawable.test);


        return convertView;
    }




//
//    @Override
//    public Filter getFilter() {
//        if (mFilter == null) {
//            mFilter = new ArrayFilter();
//        }
//        return mFilter;
//    }
//
//    private class ArrayFilter extends Filter {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {//constraint是用户的输入
//            //进行过滤的操作
//            FilterResults results = new FilterResults();
//
//            if (constraint == null || constraint.length() == 0) {
//                ArrayList<Word> list = new ArrayList<>(all_List);
//                results.values = list;
//                results.count = list.size();
//            } else {
//                String prefixString = constraint.toString().toLowerCase();//大写换成小写
//                ArrayList<Word> values = new ArrayList<>(all_List);//未过滤前的List
//                int count = values.size();
//                ArrayList<Word> newValues = new ArrayList<>();//被过滤后的匹配的List
//                /*以下为过滤的条件,可按照个人的需求修改*/
//                for (int i = 0; i < count; i++) {
//                    Word value = values.get(i);
//                    String valueText = value.getWord()+value.getMeaning();//ValueText是每一项筛选的依据
//
//                    if (valueText != null && valueText.startsWith(prefixString)) {
//                        newValues.add(value);
//                    } else {
//                        if (valueText.contains(prefixString)) {
//                            newValues.add(value);
//                        }
//                    }
//                }
//                results.values = newValues;
//                results.count = newValues.size();
//            }
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            //发表过滤的结果
//            mFilteredList = (List<Word>) results.values;
//            //mFilteredList = all_List;
//            if (results.count > 0) {
//                notifyDataSetChanged();
//            } else {
//                notifyDataSetInvalidated();
//            }
//        }
//    }
}
