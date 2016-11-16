package com.example.khoanguyen1.todolistfragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by khoanguyen1 on 10/20/16.
 */
public class EditScreen extends AppCompatActivity {

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
//        if(fragment == null) {
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.todoitem_detail_container, fragment)
                    .commit();
//        }
//        final EditText et = (EditText) findViewById(R.id.edit_text_2);
//        Button button = (Button) findViewById(R.id.button);
//        Button changeCheck = (Button) findViewById(R.id.check);
//        Button chooseImage = (Button) findViewById(R.id.choose_image);
//        iv = (ImageView) findViewById(R.id.image_view);
//
//        Button removeImage = (Button) findViewById(R.id.undo_image);
//
//
//        Intent intent = getIntent();
//
//        id = intent.getIntExtra("id", 1);
//        System.out.println(id);
//
//        try {
//            File f = new File(EntryList.todoList.get(id).getImagePath());
//            Bitmap b = null;
//            try {
//                b = BitmapFactory.decodeStream(new FileInputStream(f));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            iv.setImageBitmap(b);
//        }
//        catch (NullPointerException e){
//            //e.printStackTrace();
//        }
//
//        et.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (et.getText().length() > 0
//                        && event.getAction() == KeyEvent.ACTION_DOWN
//                        && keyCode == KeyEvent.KEYCODE_ENTER) {
//
//                    EntryList.todoList.get(id).setName(et.getText().toString());
//                    todoItemListActivity.todoAdapter.notifyDataSetChanged();
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String text = et.getText().toString();
//                if (!text.isEmpty()) {
//                    EntryList.todoList.get(id).setName(text);
//                    todoItemListActivity.todoAdapter.notifyDataSetChanged();
//                }
//
//            }
//        });
//
//        removeImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    EntryList.todoList.get(id).setmImagePath("");
//                todoItemListActivity.todoAdapter.notifyDataSetChanged();
//
//            }
//        });
//
//        changeCheck.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean checked = EntryList.todoList.get(id).getChecked();
//                EntryList.todoList.get(id).setChecked(!checked);
//                todoItemListActivity.todoAdapter.notifyDataSetChanged();
//            }
//        });
//
//        chooseImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent, 100);
//            }
//        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Check which request we're responding to
//        if (requestCode == 100) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//                // The user picked a contact.
//                // The Intent's data Uri identifies which contact was selected.
//                Uri uri = data.getData();
//                // Do something with the contact here (bigger example below)
//                //iv.setImageURI(uri);
//                File dir = getFilesDir();
//                File writeToMe = null;
//
//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//
//                    writeToMe = new File(dir, ""+ System.currentTimeMillis());
//
//                    FileOutputStream fos;
//                    fos = new FileOutputStream(writeToMe);
//                    // Use the compress method on the BitMap object to write image to the OutputStream
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//                    fos.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                EntryList.todoList.get(id).setmImagePath(writeToMe.getAbsolutePath());
//                EntryList.todoList.get(id).setImageId(id +"");
//
//                todoItemListActivity.todoAdapter.notifyDataSetChanged();
//
//                File f=new File(writeToMe.getAbsolutePath());
//                Bitmap b = null;
//                try {
//                    b = BitmapFactory.decodeStream(new FileInputStream(f));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                iv.setImageBitmap(b);
//            }
//        }
//    }

}
