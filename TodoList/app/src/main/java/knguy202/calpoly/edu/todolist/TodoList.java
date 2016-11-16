package knguy202.calpoly.edu.todolist;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class TodoList extends AppCompatActivity {

    private static final String TAG = "Todo List";
    private EditText editText;
    private SQLiteDatabase db;
    private Gson packer = new Gson();


    public static TodoListAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        Button button = (Button) findViewById(R.id.button);
        Button undo = (Button) findViewById(R.id.undo);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        editText = (EditText) findViewById(R.id.edit_text);

        assert rv != null;

        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        final Stack<TodoListUtils.Entry> stack = new Stack<>();


        if (!readFromFile() && EntryList.todoList == null) {
            EntryList.todoList = new ArrayList<>();
        }

        System.out.println("Size of array is: " + EntryList.todoList.size());

        todoAdapter = new TodoListAdapter(EntryList.todoList);
        rv.setAdapter(todoAdapter);

        todoAdapter.notifyDataSetChanged();

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                TodoListViewHolder tvh = (TodoListViewHolder) viewHolder;
                if (direction == ItemTouchHelper.RIGHT) {
                    int index = viewHolder.getAdapterPosition();

                    stack.push(EntryList.todoList.remove(index));
                    todoAdapter.notifyItemRemoved(index);

                } else if (direction == ItemTouchHelper.LEFT) {
                    TodoListUtils.Entry a = tvh.entry;
                    a.setChecked(!a.getChecked());
                    todoAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            }
        };

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (editText.getText().length() > 0
                        && event.getAction() == KeyEvent.ACTION_DOWN
                        && keyCode == KeyEvent.KEYCODE_ENTER) {
                    TodoListUtils.Entry entry = new TodoListUtils.Entry();

                    entry.setName(editText.getText().toString());
                    entry.setChecked(false);

                    EntryList.todoList.add(entry);
                    todoAdapter.notifyDataSetChanged();
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
                    TodoListUtils.Entry entry = new TodoListUtils.Entry();
                    entry.setName(text);
                    entry.setChecked(false);
                    entry.setmImagePath("");
                    EntryList.todoList.add(entry);
                    todoAdapter.notifyDataSetChanged();
                }

            }
        });



        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stack.size() != 0) {
                    TodoListUtils.Entry entry = stack.pop();
                    EntryList.todoList.add(entry);
                    todoAdapter.notifyDataSetChanged();
                }
            }
        });

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rv);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("ANIMALS", EntryList.todoList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop(){
        writeToFile();
        super.onStop();
    }

    @Override
    public void onDestroy() {
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

    public void shareList() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, formatString());
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "Share Intent"));
    }

    public String formatString() {
        String result = "";
        for (int i = 0; i < EntryList.todoList.size(); i++) {
            TodoListUtils.Entry entry = EntryList.todoList.get(i);
            if (entry.getChecked())
                result = result.concat(entry.getName() + "\t" + "1" + "\n");
            else
                result = result.concat(entry.getName() + "\t" + "0" + "\n");

            Log.d(TAG, result);
        }
        Log.d(TAG, result);
        return result;
    }

    public class TodoListViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIv;
        private TextView mTv;
        public TodoListUtils.Entry entry;

        public TodoListViewHolder(View itemView) {
            super(itemView);
            mIv = (ImageView) itemView.findViewById(R.id.iv);
            mTv = (TextView) itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), EditScreen.class);

                    intent.putExtra("id", TodoListViewHolder.this.getAdapterPosition());
                    Log.d(TAG, "Sent");
                    v.getContext().startActivity(intent);
                }
            });

        }

        public void bind(final TodoListUtils.Entry entry) {
            this.entry = entry;
            mTv.setText(entry.getName());

            try {
                File f = new File(entry.getImagePath());
                Bitmap b = null;
                try {
                    b = BitmapFactory.decodeStream(new FileInputStream(f));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                mIv.setImageBitmap(b);
            }
            catch (NullPointerException e){
                //e.printStackTrace();
            }
            CheckBox cb = (CheckBox) itemView.findViewById(R.id.checkBox);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    entry.setChecked(isChecked);
                }
            });
            cb.setChecked(entry.getChecked());

        }
    }

    public class TodoListAdapter extends RecyclerView.Adapter<TodoListViewHolder> {

        private ArrayList<TodoListUtils.Entry> mTodoList;

        public TodoListAdapter(ArrayList<TodoListUtils.Entry> todoList) {
            this.mTodoList = todoList;
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.list_entry;
        }

        @Override
        public TodoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TodoListViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
        }

        @Override
        public void onBindViewHolder(TodoListViewHolder holder, int position) {
            holder.bind(mTodoList.get(position));
        }

        @Override
        public int getItemCount() {
            return mTodoList.size();
        }
    }

    public void editMethod(View v){

    }


    public void writeToFile(){
        String data = packer.toJson(EntryList.todoList, ArrayList.class);
        File file = new File(this.getFilesDir(), "Json");
        FileWriter writer;
        try {
            writer = new FileWriter(file);
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean readFromFile(){
        String data = "";
        File file = new File(this.getFilesDir(), "Json");
        try {
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()) {
                data = data + scan.nextLine();

            }
            Type listType = new TypeToken<ArrayList<TodoListUtils.Entry>>(){}.getType();
            EntryList.todoList = packer.fromJson(data,listType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
