package com.example.ezchores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context ctx;
    int[] goal_prog;
    LayoutInflater inflater;
    String[] content;
    String[] points;
    ProgressBar[] bars;
    char classificator;

    public CustomAdapter(Context ctx, ArrayList<String> con, ArrayList<String> pts, ArrayList<ProgressBar> bars, char c, ArrayList<Integer> goal_prog) {
        this.content = new String[con.size()];
        for (int i = 0; i < con.size(); i++) {
            this.content[i] = con.get(i);
        }
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
        if (pts != null) {
            this.points = new String[pts.size()];
            for (int i = 0; i < pts.size(); i++) {
                this.points[i] = pts.get(i);
            }
        }
        if (bars != null) {
            this.bars = new ProgressBar[bars.size()];
            for (int i = 0; i < bars.size(); i++) {
                this.bars[i] = bars.get(i);

            }
        }
        this.classificator = c;
        if (goal_prog != null) {
            this.goal_prog = new int[goal_prog.size()];
            for (int i = 0; i < goal_prog.size(); i++) {
                this.goal_prog[i] = (int) goal_prog.get(i);

            }
        }

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        return get_view(i, view, viewGroup, this.classificator, this.goal_prog);
    }

    public View get_view(int pos, View view, ViewGroup viewGroup, char c, int[] goal_prog) {
        switch (c) {
            case 't':
                view = inflater.inflate(R.layout.activity_task_view, null);
                TextView task_name = (TextView) view.findViewById(R.id.task_1);
                TextView task_val = (TextView) view.findViewById(R.id.val_1);
                task_name.setText(content[pos]);
                task_val.setText(points[pos]);
                return view;
            case 'g':
                view = inflater.inflate(R.layout.activity_goal_view, null);
                TextView goal_name = (TextView) view.findViewById(R.id.goal_name1);
                ProgressBar prog = (ProgressBar) view.findViewById(R.id.prog1);
                goal_name.setText(this.content[pos]);
                prog.setProgress(this.goal_prog[pos]);
                return view;
            default:
                break;
        }
        return null;
    }
}
