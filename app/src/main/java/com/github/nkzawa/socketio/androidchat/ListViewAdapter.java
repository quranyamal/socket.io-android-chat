package com.github.nkzawa.socketio.androidchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cmrudi on 18/04/18.
 */

public class ListViewAdapter extends ArrayAdapter<String> {

    int groupid;
    String[] item_list;
    ArrayList<String> desc;
    Context context;

    public ListViewAdapter(@NonNull Context context, int resource, @NonNull String[] item_list) {
        super(context, resource, item_list);
        this.context = context;
        groupid = resource;
        this.item_list = item_list;
    }

    // Hold views of the ListView to improve its scrolling performance
    static class ViewHolder {
        public Button button;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        // Inflate the list_item.xml file if convertView is null
        if(rowView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(groupid, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.button= (Button) rowView.findViewById(R.id.bt);
            rowView.setTag(viewHolder);

        }
        // Set text to each TextView of ListView item
        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.button.setText(item_list[position]);
        return rowView;
    }

}
