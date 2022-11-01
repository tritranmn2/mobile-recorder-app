package com.example.recorder;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DataAdapterRCList extends BaseAdapter {

    private Activity activity;
    private String[] items_title;
    private String[] items_length;
    private String[] items_date;
    private ProgressBar[] progressBar;
    private Integer flag = 1;

    public DataAdapterRCList(Activity activity, String[] items, String[] items1, String[] items2) {
        this.activity = activity;
        this.items_title = items;
        this.items_length = items1;
        this.items_date = items2;
    }

    @Override
    public int getCount() {
        return items_title.length;
    }

    @Override
    public Object getItem(int i) {
        return items_title[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // Gọi layoutInflater ra để bắt đầu ánh xạ view và data.
        LayoutInflater inflater = activity.getLayoutInflater();

        // Đổ dữ liệu vào biến View, view này chính là những gì nằm trong item_name.xml
        view = inflater.inflate(R.layout.list_rc_item, null);

        // Đặt chữ cho từng view trong danh sách.
        TextView tvName = (TextView) view.findViewById(R.id.list_item_title);
        tvName.setText(items_title[i]);

        TextView tvName1 = (TextView) view.findViewById(R.id.list_item_length);
        tvName1.setText(items_length[i]);

        TextView tvName2 = (TextView) view.findViewById(R.id.list_item_date);
        tvName2.setText(items_date[i]);

        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.list_item_progress);
        progressBar.setVisibility(View.GONE);
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (progressBar.getVisibility() == View.VISIBLE){
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }

            }
        });

        ImageView img = (ImageView) view.findViewById(R.id.list_item_img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 1) {
                    img.setImageResource(R.drawable.pause);
                    flag = 0;
                } else {
                    img.setImageResource(R.drawable.playbutton);
                    flag = 1;
                }
//                if (progressBar.getVisibility() == View.VISIBLE){
//                    progressBar.setVisibility(View.GONE);
//                } else {
//                    progressBar.setVisibility(View.VISIBLE);
//                }
            }
        });


        // Trả về view kết quả.
        return view;
    }
}