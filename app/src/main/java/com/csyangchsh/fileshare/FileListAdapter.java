package com.csyangchsh.fileshare;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * @author csyangchsh
 * 2014-08-28
 *
 */
public class FileListAdapter extends ArrayAdapter<File> {

    public FileListAdapter(Activity activity, List<File> objects, ListView listView) {
        super(activity, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = (Activity) getContext();
        ViewHolder holder;
        if (convertView==null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.file_row, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView)convertView.findViewById(R.id.file_image);
            holder.fileName = (TextView)convertView.findViewById(R.id.file_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        File item = getItem(position);
        holder.fileName.setText(item.getName());
        if(item.isFile()) {
            holder.image.setImageResource(R.drawable.file);
        } else {
            holder.image.setImageResource(R.drawable.folder);
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView image;
        TextView fileName;
    }
}
