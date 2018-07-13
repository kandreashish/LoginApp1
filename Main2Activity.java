package com.example.ashishthelegend.loginapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "Main2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Splash");
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);


        Full full=new Full();
        full.start();

    }
    private class Full extends Thread
    {
        public void run() {
            try
            {
                sleep(3000);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            Main2Activity.this.finish();

        }

    }

}
