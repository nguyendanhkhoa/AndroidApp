package knguy202.calpoly.edu.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingDeque;

public class TodoList extends AppCompatActivity {

    private static final String TAG = "Todo List";
    private EditText editText;
    private LinearLayout myListLayout;
    private LinearLayout entry;
    private ListView listView;
    private MyAdapter adapter;
    public static class Entry
    {
        String things;
        boolean isChecked;
    }

    private ArrayList<Entry> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        myListLayout = (LinearLayout) findViewById(R.id.linearLayout);
        entry = (LinearLayout) findViewById(R.id.entry);
        Button button = (Button) findViewById(R.id.button);
        Button undo = (Button) findViewById(R.id.undo);

        final Stack<Entry> stack = new Stack<>();

        editText = (EditText) findViewById(R.id.edit_text);

        mList = (ArrayList<Entry>) getLastCustomNonConfigurationInstance();

        listView = (ListView) findViewById(R.id.listView);

        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);

        if(mList == null)
        {
            mList = new ArrayList<>();
        }

        adapter = new MyAdapter(mList);

        listView.setAdapter(adapter);

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (editText.getText().length() > 0
                        && event.getAction() == KeyEvent.ACTION_DOWN
                        && keyCode == KeyEvent.KEYCODE_ENTER) {
                    Entry entry = new Entry();

                    entry.things = editText.getText().toString();
                    entry.isChecked = false;

                    mList.add(entry);
                    adapter.notifyDataSetChanged();
                    return true;
                } else {
                    return false;
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if (!text.isEmpty()) {
                    Entry entry = new Entry();
                    entry.things = text;
                    entry.isChecked = false;

                    mList.add(entry);
                    adapter.notifyDataSetChanged();
                }

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Entry entry = mList.remove(position);
                stack.push(entry);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stack.size() != 0) {
                    Entry entry = stack.pop();
                    mList.add(entry);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance()
    {
        return mList;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.share:
                shareList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void shareList(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, formatString());
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "Share Intent"));
    }

    public String formatString(){
        String result = new String();
        StringBuilder builder = new StringBuilder(result);
        for(int i = 0; i< mList.size(); i++)
        {
            Entry entry = mList.get(i);
            if(entry.isChecked)
                result = result.concat(entry.things + "\t" + "1" + "\n");
            else
                result = result.concat(entry.things + "\t" + "0" + "\n");

            Log.d(TAG, result);
        }
        Log.d(TAG, result);
        return result;
    }
}
