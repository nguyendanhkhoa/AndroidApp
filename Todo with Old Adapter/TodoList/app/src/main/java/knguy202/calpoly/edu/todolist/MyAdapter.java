package knguy202.calpoly.edu.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by khoanguyen1 on 10/12/16.
 */
public class MyAdapter extends BaseAdapter{

    ArrayList<TodoList.Entry> mEntries = new ArrayList<>();
    public MyAdapter(ArrayList entries){
        mEntries = entries;
    }


    @Override
    public int getCount() {
        return mEntries.size();
    }

    @Override
    public Object getItem(int position) {
        return mEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inf = LayoutInflater.from(parent.getContext());
            convertView = inf.inflate(R.layout.list_entry, parent, false);
        }
        final TodoList.Entry e = (TodoList.Entry) getItem(position);
        TextView tv = (TextView) convertView.findViewById(R.id.textView);
        tv.setText(e.things);

        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                e.isChecked = isChecked;
            }
        });

        cb.setChecked(e.isChecked);
        return convertView;
    }
}
