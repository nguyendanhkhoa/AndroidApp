package com.example.khoanguyen1.todolistfragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
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

/**
 * An activity representing a list of todoItems. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link todoItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class todoItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private EditText editText;
    private Gson packer = new Gson();

    public static SimpleItemRecyclerViewAdapter todoAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todoitem_list);

        Button button = (Button) findViewById(R.id.button);
        Button undo = (Button) findViewById(R.id.undo);
        editText = (EditText) findViewById(R.id.edit_text);

        final Stack<TodoListUtils.Entry> stack = new Stack<>();


        View recyclerView = findViewById(R.id.todoitem_list);
        assert recyclerView != null;

        //recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        if (!readFromFile() && EntryList.todoList == null) {
            EntryList.todoList = new ArrayList<>();
        }
        setupRecyclerView((RecyclerView) recyclerView);

        todoAdapter.notifyDataSetChanged();

        if (findViewById(R.id.todoitem_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                ViewHolder tvh = (ViewHolder) viewHolder;
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

        new ItemTouchHelper(simpleCallback).attachToRecyclerView((RecyclerView) recyclerView);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStop() {
        writeToFile();
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        todoAdapter = new SimpleItemRecyclerViewAdapter(EntryList.todoList);
        recyclerView.setAdapter(todoAdapter);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("todoItemList Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<ViewHolder> {

        private ArrayList<TodoListUtils.Entry> mTodoList;

        public SimpleItemRecyclerViewAdapter(ArrayList<TodoListUtils.Entry> todoList) {
            this.mTodoList = todoList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_entry, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
//            holder.mItem = mValues.get(position);
//            holder.mIdView.setText(mValues.get(position).id);
//            holder.mContentView.setText(mValues.get(position).content);

            holder.bind(mTodoList.get(position));
            holder.mTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("FLAG", "Goes in here!");
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(todoItemDetailFragment.ARG_ITEM_ID, position);
                        todoItemDetailFragment fragment = new todoItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.todoitem_detail_container, fragment)
                                .commit();
                    } else {
//                        Context context = v.getContext();
//                        Intent intent = new Intent(context, todoItemDetailActivity.class);
//                        intent.putExtra(todoItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
//
//                        context.startActivity(intent);
                        Intent intent = new Intent(v.getContext(), EditScreen.class);

                        intent.putExtra("id", position);
                        v.getContext().startActivity(intent);
                    }
                }
            });

        }


        @Override
        public int getItemCount() {
            return mTodoList.size();
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIv;
        private TextView mTv;
        public TodoListUtils.Entry entry;

        public ViewHolder(View itemView) {
            super(itemView);
            mIv = (ImageView) itemView.findViewById(R.id.iv);
            mTv = (TextView) itemView.findViewById(R.id.textView);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mTwoPane) {
//                        Bundle arguments = new Bundle();
//                        //arguments.putString(todoItemDetailFragment.ARG_ITEM_ID, ViewHolder.this.getAdapterPosition() + "");//holder.mItem.id);
//                        todoItemDetailFragment fragment = new todoItemDetailFragment();
//                        Log.d("Todo", "Goes in here");
//                        fragment.setArguments(arguments);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.todoitem_detail_container, fragment)
//                                .commit();
//                    } else {
//                        Intent intent = new Intent(v.getContext(), EditScreen.class);
//
//                        intent.putExtra("id", ViewHolder.this.getAdapterPosition());
//                        v.getContext().startActivity(intent);
//                    }
//                }
//            });

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
            } catch (NullPointerException e) {
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

    public void writeToFile() {
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

    public boolean readFromFile() {
        String data = "";
        File file = new File(this.getFilesDir(), "Json");
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                data = data + scan.nextLine();

            }
            Type listType = new TypeToken<ArrayList<TodoListUtils.Entry>>() {
            }.getType();
            EntryList.todoList = packer.fromJson(data, listType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
