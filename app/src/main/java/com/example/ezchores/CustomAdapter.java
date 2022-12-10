package com.example.ezchores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater inflater;
    String[] content;
    String[] points;
    ProgressBar[] bars;
    char classificator;

    public CustomAdapter(Context ctx, String[] con, String[] pts, ProgressBar[] bars, char c){
        this.content=con;
        this.ctx=ctx;
        this.inflater=LayoutInflater.from(ctx);
        this.points=pts;
        this.bars=bars;
        this.classificator=c;
    }
    @Override
    public int getCount() {
        return this.content.length;
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
    public View getView(int i, View view, ViewGroup viewGroup){return get_view(i, view, viewGroup,this.classificator);}

    public View get_view(int pos, View view, ViewGroup viewGroup, char c){
        switch (c){
            case 't':
                view = inflater.inflate(R.layout.activity_task_view, null);
                TextView task_name= (TextView) view.findViewById(R.id.task_1);
                TextView task_val= (TextView) view.findViewById(R.id.val_1);
                task_name.setText(content[pos]);
                task_val.setText(points[pos]);
                return view;
            case 'g':
                view = inflater.inflate(R.layout.activity_goal_view, null);
                TextView goal_name= (TextView) view.findViewById(R.id.goal_name1);
                ProgressBar prog= (ProgressBar) view.findViewById(R.id.prog1);
                goal_name.setText(content[pos]);
                prog.setProgress(50);
                return view;
            default:
                break;
        }
        return null;
    }
}
