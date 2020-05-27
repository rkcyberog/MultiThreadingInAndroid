package com.rahul.multithreading;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity{
    private ImageView imageView1, imageView2;
    ServiceWorker serviceWorker1,serviceWorker2;
    OkHttpClient okHttpClient;
    Task task1, task2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        okHttpClient = new OkHttpClient();

        //serviceWorker1 for first task
        serviceWorker1 = new ServiceWorker("service_worker_1");
        //Callback for Task
         task1 = new Task<Bitmap>() {
            @Override
            public Bitmap onExecuteTask() {
                Log.i("thread_Task1 ", String.valueOf(Thread.currentThread().getId()));
                Request request = new Request.Builder().url(Util.IMAGE_1).build();
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    Log.i("response", String.valueOf(response));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                return bitmap;
            }
            @Override
            public void onTaskComplete(Bitmap result) {
                Log.i("thread_onCompleteTask1 ", String.valueOf(Thread.currentThread().getId()));
                imageView1.setImageBitmap(result);
            }
        };
        serviceWorker1.setTaskCallback(task1);
        serviceWorker1.start();

        //serviceWorker2 for second task
        serviceWorker2 = new ServiceWorker("service_worker_2");
        Task task2 = new Task<Bitmap>() {
            @Override
            public Bitmap onExecuteTask() {
                Log.i("thread_Task2 ", String.valueOf(Thread.currentThread().getId()));
                Request request = new Request.Builder().url(Util.IMAGE_2).build();
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    Log.i("response", String.valueOf(response));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                return bitmap;
            }
            @Override
            public void onTaskComplete(Bitmap result) {
                Log.i("thread_onCompleteTask2 ", String.valueOf(Thread.currentThread().getId()));
                imageView2.setImageBitmap(result);
            }
        };
        serviceWorker2.setTaskCallback(task2);
        serviceWorker2.start();

        Log.i("thread_main ", String.valueOf(Thread.currentThread().getId()));

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("button", "clicked1");
                fetchImage1AndSet();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("button ","clicked2");
                fetchImage2AndSet();
            }
        });
    }


    private void fetchImage1AndSet() {
      serviceWorker1.addTask(task1);
    }

    private void fetchImage2AndSet() {
       serviceWorker2.addTask(task2);
    }

}

