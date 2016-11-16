package com.example.khoanguyen1.todolistfragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.khoanguyen1.todolistfragment.dummy.DummyContent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static android.app.Activity.RESULT_OK;

/**
 * A fragment representing a single todoItem detail screen.
 * This fragment is either contained in a {@link todoItemListActivity}
 * in two-pane mode (on tablets) or a {@link todoItemDetailActivity}
 * on handsets.
 */
public class todoItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public todoItemDetailFragment() {
    }

    private ImageView iv;
    private int id;
    private EditText et;
    private Button chooseImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.todoitem_detail, container, false);

        // Show the dummy content as text in a TextView.
//        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.todoitem_detail)).setText("Khoa");//mItem.details);
//        }

//        String stringId = getArguments().getString(ARG_ITEM_ID);

        id = getArguments().getInt(ARG_ITEM_ID);


        et = (EditText) rootView.findViewById(R.id.edit_text_3);
        Button button = (Button) rootView.findViewById(R.id.button);
        Button changeCheck = (Button) rootView.findViewById(R.id.check);
        chooseImage = (Button) rootView.findViewById(R.id.choose_image);
        iv = (ImageView) rootView.findViewById(R.id.image_view);

        Button removeImage = (Button) rootView.findViewById(R.id.undo_image);

        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (et.getText().length() > 0
                        && event.getAction() == KeyEvent.ACTION_DOWN
                        && keyCode == KeyEvent.KEYCODE_ENTER) {

                    EntryList.todoList.get(id).setName(et.getText().toString());
                    todoItemListActivity.todoAdapter.notifyDataSetChanged();
                    return true;
                } else {
                    return false;
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = et.getText().toString();
                if (!text.isEmpty()) {
                    EntryList.todoList.get(id).setName(text);
                    todoItemListActivity.todoAdapter.notifyDataSetChanged();
                }

            }
        });

        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntryList.todoList.get(id).setmImagePath("");
                todoItemListActivity.todoAdapter.notifyDataSetChanged();

            }
        });

        changeCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = EntryList.todoList.get(id).getChecked();
                EntryList.todoList.get(id).setChecked(!checked);
                todoItemListActivity.todoAdapter.notifyDataSetChanged();
            }
        });

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 100) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                Uri uri = data.getData();
                // Do something with the contact here (bigger example below)
                //iv.setImageURI(uri);
                File dir = getActivity().getFilesDir();
                File writeToMe = null;

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                    writeToMe = new File(dir, ""+ System.currentTimeMillis());

                    FileOutputStream fos;
                    fos = new FileOutputStream(writeToMe);
                    // Use the compress method on the BitMap object to write image to the OutputStream
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                EntryList.todoList.get(id).setmImagePath(writeToMe.getAbsolutePath());
                EntryList.todoList.get(id).setImageId(id +"");

                todoItemListActivity.todoAdapter.notifyDataSetChanged();

                File f=new File(writeToMe.getAbsolutePath());
                Bitmap b = null;
                try {
                    b = BitmapFactory.decodeStream(new FileInputStream(f));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                iv.setImageBitmap(b);
            }
        }
    }



}
