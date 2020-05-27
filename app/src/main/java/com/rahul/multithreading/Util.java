package com.rahul.multithreading;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;

public class Util {

    public static final String IMAGE_1 = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQztXT4xoS2eb1dvkKxUrEkBykLeaSn-J_C3VlB905pnFi3ebh7&usqp=CAU";
    public static final String IMAGE_2 = "https://images.edexlive.com/uploads/user/imagelibrary/2020/4/16/original/download_2.jpg";
    public static final String LOG_TAG = "SepBackgroundThread";
    public static final int ID = 1;
    public static final String DATA = "DATA";

    //method for passing bitmap to handlers
    public static Message createMessage(int id, Bitmap data) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Util.DATA,data);
        Message message = new Message();
        message.what = id;
        message.setData(bundle);
        return message;
    }
}
