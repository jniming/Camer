package com.example.ipcam.camer.adpter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.ipcam.camer.R;
import com.example.ipcam.camer.entity.AlarmMsg;
import com.example.ipcam.camer.util.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;


public class AlarmMsgAdpter extends BaseAdapter {
    private Context context;
    private List<AlarmMsg> list;
    private boolean isvisb;   //是否显示

    public boolean isvisb() {
        return isvisb;
    }

    public void setIsvisb(boolean isvisb) {
        this.isvisb = isvisb;
    }

    public AlarmMsgAdpter(List<AlarmMsg> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Viewholder viewholder;
        if (convertView == null) {
            viewholder = new Viewholder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.alarm_item, null);
            viewholder.time = (TextView) convertView
                    .findViewById(R.id.alarm_time);
            viewholder.dname = (TextView) convertView
                    .findViewById(R.id.alarm_name);
            viewholder.box = (CheckBox) convertView
                    .findViewById(R.id.msg_item_check);
            viewholder.msg = (TextView) convertView.findViewById(R.id.alarmmsg);
            viewholder.file = (TextView) convertView
                    .findViewById(R.id.alarmfile);

            convertView.setTag(viewholder);
        } else
            viewholder = (Viewholder) convertView.getTag();

        final AlarmMsg manager = list.get(position);
        final String path = manager.getFilepath();
        viewholder.box.setVisibility(isvisb ? View.VISIBLE : View.GONE);
        viewholder.box.setChecked(manager.ischeck());

        if (manager.getMsg().equals(Config.CUP_IMGTYPE) || manager.getMsg().contains("警报")) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(path));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (bitmap != null) {
                Drawable drawable = new BitmapDrawable(bitmap);
                viewholder.file.setBackground(drawable);
            }
        } else if (manager.getMsg().equals(Config.VIDE_TYPE)) {
            viewholder.file.setBackgroundResource(R.drawable.index_default);
        }

        viewholder.dname.setText(manager.getName());
        viewholder.time.setText(manager.getTime());
        viewholder.msg.setText(manager.getMsg());

        viewholder.box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                manager.setIscheck(isChecked);
                list.set(position, manager);
            }
        });

        convertView.setOnClickListener(new OnClickListener() {

                                           @Override
                                           public void onClick(View v) {
                                               if (manager.getMsg().equals(Config.VIDE_TYPE)) {
                                                   openFile(path, 2);
                                               } else {
                                                   openFile(path, 1);
                                               }
                                           }
                                       }

        );

        return convertView;

    }

    /**
     * 调用系统管理器查看文件
     *
     * @param path
     * @param ntype 1为查看图片,2为查看录像
     */
    public void openFile(String path, int ntype) {

        File file = new File(path);
        Intent it = new Intent(Intent.ACTION_VIEW);
        Uri mUri = Uri.parse("file://" + file.getPath());
        if (ntype == 1) {
            it.setDataAndType(mUri, "image/*");
        } else if (ntype == 2) {
            it.setDataAndType(mUri, "video/mp4");
        }
        context.startActivity(it);
    }

    class Viewholder {
        TextView dname, time, msg, file;
        CheckBox box;
    }
}
