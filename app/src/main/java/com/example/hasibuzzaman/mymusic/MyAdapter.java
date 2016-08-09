package com.example.hasibuzzaman.mymusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hasibuzzaman on 7/12/2016.
 */
public class MyAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> songsName;


    public MyAdapter(Context context, ArrayList<String> songsName) {
        this.context = context;
        this.songsName = songsName;
    }
   class ViewHolder
   {
       TextView tv;
   }
    @Override
    public int getCount() {
        return songsName.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null)
        {   viewHolder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view= inflater.inflate(R.layout.item_list_row,null,false);
            viewHolder.tv=(TextView) view.findViewById(R.id.textView);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.tv.setText(songsName.get(i));
        return view;
    }
}
