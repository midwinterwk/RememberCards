package com.example.cardapplication;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private Context mContext = null;
    private RecyclerView recycleViewCard;
    private CardAdapter cardAdapter;
    private ArrayList<CardEntity> mEntityList;

    private final String TAG = "MainActivity";
    private Timer mTimer;
    private TimerTask mTask;
    private long mStartTime;
    private Button btnEndTime;
    private String mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        recycleViewCard = findViewById(R.id.recycler_view_card);
        mContext = MainActivity.this;

        cardAdapter = new CardAdapter(mContext, CardUtil.getCards(mContext), recycleViewCard);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleViewCard.setLayoutManager(layoutManager);
        recycleViewCard.setAdapter(cardAdapter);

//        recycleViewCard.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
//                Log.d(TAG, "onKey keyEvent.getAction() = " + keyEvent.getAction() + "keyEvent.getKeyCode() = " + keyEvent.getKeyCode());
//                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
//                        keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
//                    Log.d(TAG,"stopTime");
//                    stopTime();
//                    return true;
//                }
//                return false;
//            }
//        });

        btnEndTime = findViewById(R.id.end_time);
        startTime();
    }

    private void getCards() {
        mEntityList = new ArrayList<>();
        int index = 0;
        try {
            AssetManager assets = getAssets();
            //获取/assests/目录下的所有的文件
            String[] images = assets.list("poker");
            for (String image : images) {
//                Log.d(MainActivity.this.getLocalClassName(), "image = " + image);
                InputStream assetFile = null;
                assetFile = assets.open("poker/" + image);
                CardEntity entity = new CardEntity();
                entity.setIndex(index++);
                entity.setBitmap(BitmapFactory.decodeStream(assetFile));
                mEntityList.add(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(mEntityList, new Comparator<CardEntity>() {
            @Override
            public int compare(CardEntity cardEntity, CardEntity t1) {
                double r = Math.random() - 0.5;
                return r > 0 ? 1 : -1;
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d(TAG, "dispatchKeyEvent event.getAction() = " + event.getAction() + "event.getKeyCode() = " + event.getKeyCode());
        if (event.getAction() == KeyEvent.ACTION_DOWN &&
                (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            Log.d(TAG,"stopTime");
            stopTime();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.d(TAG, "onKeyDown event.getAction() = " + event.getAction() + "event.getKeyCode() = " + event.getKeyCode());
//        if (event.getAction() == KeyEvent.ACTION_DOWN &&
//            event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
//            Log.d(TAG,"stopTime");
//            stopTime();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    public void Shuffle(View view) {
        cardAdapter.Shuffle();
    }

    public void onOK(View view) {
        CardUtil.onKeyEvent(KeyEvent.KEYCODE_DPAD_CENTER);
    }

    private void startTime() {
        mStartTime = System.currentTimeMillis();
        if (mTimer == null && mTask == null) {
            mTimer = new Timer();
            mTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recordTime();
                        }
                    });
                }
            };
            mTimer.schedule(mTask, 0, 100);
        }
    }

    private void stopTime() {
        recordTime();
        Intent intent = new Intent(mContext,RecallActivity.class);
        intent.putExtra("endTime",mEndTime);
        mContext.startActivity(intent);
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
    }

    private void recordTime() {
        if (mTimer == null && mTask == null)
            return;
        long time = System.currentTimeMillis() - mStartTime;
        long second = time / 1000;
        mEndTime = String.format("%d.%d", second , time % 1000);
        btnEndTime.setText(mEndTime);
    }

}
