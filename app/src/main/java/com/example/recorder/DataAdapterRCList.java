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

import java.util.ArrayList;
import java.util.List;

public class DataAdapterRCList extends BaseAdapter {

    private Activity activity;
    private List<String> items_title;
    private List<String> items_length;
    private List<String> items_date;
    private ProgressBar[] progressBar;
    private Integer flag = 1;

    public DataAdapterRCList(Activity activity, List<String> title, List<String> length, List<String> date) {
        this.activity = activity;
        this.items_title = title;
        this.items_length = length;
        this.items_date = date;
    }

    @Override
    public int getCount() {
        return items_title.size();
    }

    @Override
    public Object getItem(int i) {
        return items_title.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // Gọi layoutInflater ra để bắt đầu ánh xạ view và data.
        LayoutInflater inflater = activity.getLayoutInflater();

        // Dùng inflater để gắn giao diện list_rc_item vào biến view
        view = inflater.inflate(R.layout.list_rc_item, null);

        // set tên từng record
        TextView tvName = (TextView) view.findViewById(R.id.list_item_title);
        tvName.setText(items_title.get(i));

        //Set độ dài từng record
        TextView tvName1 = (TextView) view.findViewById(R.id.list_item_length);
        tvName1.setText(items_length.get(i));

        //set ngày từng record
        TextView tvName2 = (TextView) view.findViewById(R.id.list_item_date);
        tvName2.setText(items_date.get(i));

        //set ẩn cái thanh thời gian của từng record
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.list_item_progress);
        progressBar.setVisibility(View.INVISIBLE);

        //lắng nghe sự kiện click vào title của 1 record thì cho thanh thời gian hiện lên
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (progressBar.getVisibility() == View.VISIBLE){
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }

            }
        });

        //Bật tắt nút play và pasuse
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

            }
        });


        // Trả về view kết quả.
        return view;
    }
}