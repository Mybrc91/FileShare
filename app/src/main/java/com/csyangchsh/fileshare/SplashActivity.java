package com.csyangchsh.fileshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.Toast;


/**
 * @author csyangchsh
 * 2014-08-28
 */
public class SplashActivity extends Activity {
    protected volatile boolean mActive = true;
    protected int mSplashTime = 2500;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (mActive && (waited < mSplashTime)) {
                        sleep(100);
                        if (mActive) {
                            waited += 100;
                        }
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    finish();
                    startActivity(new Intent(SplashActivity.this, FileActivity.class));
                }
            }
        };
        splashTread.start();
        //Check wifi status
        if(!FSApplication.isWifi()) {
            Toast.makeText(getApplicationContext(), R.string.wifi_toast, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mActive = false;
        }
        return true;
    }
}