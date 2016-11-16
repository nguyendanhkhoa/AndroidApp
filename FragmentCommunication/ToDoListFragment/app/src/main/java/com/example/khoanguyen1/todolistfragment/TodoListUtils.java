package com.example.khoanguyen1.todolistfragment;

import android.os.Parcel;
import android.os.Parcelable;

public class TodoListUtils {

    public static class Entry implements Parcelable {
        private String mName;
        private String dir;
        private boolean isChecked;
        private String imageId;
        private int entryID;

        public void setEntryID(int entryId){
            this.entryID = entryId;
        }

        public int getEntryId(){
            return entryID;
        }

        public void setImageId(String imageId){
            this.imageId = imageId;
        }

        public String getImageId(){
            return imageId;
        }
        public String getName() {
            return mName;
        }

        public void setmImagePath(String dir) {
            this.dir = dir;
        }


        public void setName(String name) {
            this.mName = name;
        }

        public String getImagePath() {
            return dir;
        }

        public void setChecked(boolean checked) {
            this.isChecked = checked;
        }

        public boolean getChecked() {
            return isChecked;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(mName);
            //out.write(mImageUrl);
            boolean[] arr = new boolean[1];
            arr[0] = isChecked;
            out.writeBooleanArray(arr);
        }

        public static final Parcelable.Creator<Entry> CREATOR
                = new Parcelable.Creator<Entry>() {
            public Entry createFromParcel(Parcel in) {
                Entry a = new Entry();
                a.setName(in.readString());
                //a.setmImageUrl(in.readString());
                boolean[] arr = new boolean[1];
                in.readBooleanArray(arr);
                a.setChecked(arr[0]);
                return a;
            }

            public Entry[] newArray(int size) {
                return new Entry[size];
            }
        };
    }

}