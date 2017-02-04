package com.example.khoanguyen1.todolistfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by khoanguyen1 on 10/20/16.
 */
public class EditScreen extends AppCompatActivity implements todoItemDetailFragment.SayHello{

    private ImageView iv;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_screen);

        Intent intent = getIntent();

        id = intent.getIntExtra("id", 1);
        Bundle arguments = new Bundle();
        arguments.putInt(todoItemDetailFragment.ARG_ITEM_ID, id);
        todoItemDetailFragment fragment = new todoItemDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.todoitem_detail_container, fragment)
                .commit();

    }


    public void sayHello(){
        finish();
    }
}
