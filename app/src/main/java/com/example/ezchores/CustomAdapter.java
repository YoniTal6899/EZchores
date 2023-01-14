package com.example.ezchores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.Inflater;

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
        if (pts != null && c!='u') {
            this.points = new String[pts.size()];
            for (int i = 0; i < pts.size(); i++) {
                this.points[i] = pts.get(i);
            }
        }
        if (bars != null && c!= 'u') {
            this.bars = new ProgressBar[bars.size()];
            for (int i = 0; i < bars.size(); i++) {
                this.bars[i] = bars.get(i);

            }
        }
        this.classificator = c;
        if (goal_prog != null && c!='u') {
            this.goal_prog = new int[goal_prog.size()];
            for (int i = 0; i < goal_prog.size(); i++) {
                this.goal_prog[i] = (int) goal_prog.get(i);

            }
        }else if (c == 'u'){
            this.goal_prog = new int[goal_prog.size()];
            this.goal_prog = goal_prog.stream().mapToInt(i -> i).toArray();
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
            case 'm':
                view = inflater.inflate(R.layout.activity_member_list_view, null);
                TextView member_name = (TextView) view.findViewById(R.id.participant_name);
                FloatingActionButton trash_Icon = (FloatingActionButton) view.findViewById(R.id.trash_icon);
                member_name.setText(this.content[pos]);
                return view;
            case 'u':
                RecyclerView.ViewHolder holder;
                view = inflater.inflate(R.layout.name_and_checkbox_listview, null);
                TextView nameTextView = (TextView) view.findViewById(R.id.name);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                checkBox.setEnabled(true);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            // the button is clicked at place position
                            goal_prog[pos] =1;
                            System.out.println("element number"+pos+" is validated");
                        }else{
                            goal_prog[pos] =0;
                        }
                    }
                });
                ImageView trash = (ImageView) view.findViewById(R.id.trash_icon);
                nameTextView.setText(this.content[pos]);
                return view;
            default:
                break;
        }
        return null;
    }

    public int[] getCheckBox(){
        return this.goal_prog;
    }
}
