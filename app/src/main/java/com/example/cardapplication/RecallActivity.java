package com.example.cardapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecallActivity extends Activity {

    private RecyclerView rcvOriginal;
    private RecallCardAdapter origAdapter;
    private Context mContext;
    private RecyclerView rcvRecall;
    private RecallCardAdapter recAdapter;

    private final String TAG = "RecallActivity";

    public static final int DOWNID = 0x001;
//    public static final int UPID = 0x002;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case DOWNID:
//                    recAdapter.addCard((CardEntity) msg.obj);
                    updateCards();
                    break;
//                case UPID:
//                    origAdapter.addCard((CardEntity) msg.obj);
//                    break;
            }
            super.handleMessage(msg);
        }
    };
    private String mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recall);
        mContext = RecallActivity.this;
        mEndTime = getIntent().getStringExtra("endTime");

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        rcvOriginal = findViewById(R.id.recycler_view_original);
        rcvOriginal.setLayoutManager(layoutManager);
//        List<CardEntity> origCards = CardUtil.getCards(mContext);
//        Collections.sort(origCards, new Comparator<CardEntity>() {
//            @Override
//            public int compare(CardEntity cardEntity, CardEntity t1) {
//                return cardEntity.getIndex() > t1.getIndex() ? 1 : -1;
//            }
//        });
        List<CardEntity> origCards = CardUtil.getOragCards();
        origAdapter = new RecallCardAdapter(mContext, origCards, rcvOriginal);
        origAdapter.setHasStableIds(true);
        rcvOriginal.setAdapter(origAdapter);

        LinearLayoutManager llmRec = new LinearLayoutManager(mContext);
        llmRec.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvRecall = findViewById(R.id.recycler_view_recall);
        rcvRecall.setLayoutManager(llmRec);

        recAdapter = new RecallCardAdapter(mContext, CardUtil.getSourceList(), rcvRecall);
        recAdapter.setHasStableIds(true);
        rcvRecall.setAdapter(recAdapter);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d(TAG, "dispatchKeyEvent event.getAction() = " + event.getAction() + " event.getKeyCode() = " + event.getKeyCode());
        if (event.getAction() == KeyEvent.ACTION_DOWN &&
                (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            updateCards();
//            Message msg = mHandler.obtainMessage();
//            msg.what = DOWNID;
//            mHandler.sendMessage(msg);
//            origAdapter.notifyDataSetChanged();
//            recAdapter.notifyDataSetChanged();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void updateCards() {
        if (origAdapter.getCurCardEntity() != null) {
            if (recAdapter.getLastFocusPosition() > -1) {
                recAdapter.insertCard(origAdapter.removeCard());
            } else {
                recAdapter.addCard(origAdapter.removeCard());
            }
//            rcvOriginal.getChildAt(origAdapter.getCurPosition()).requestFocus();
        }else if(recAdapter.getCurCardEntity() != null) {
            origAdapter.addCard(recAdapter.removeCard());
        }
    }

    public void onOK(View view) {
        int num = recAdapter.getCorrectNum();
        Toast.makeText(mContext, "你正确回忆" + num + "张牌，用时" + mEndTime + "秒", Toast.LENGTH_LONG).show();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.d(TAG, "onKeyDown event.getAction() = " + event.getAction() + "event.getKeyCode() = " + event.getKeyCode());
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
////            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
////                Log.d(TAG, "KEYCODE_DPAD_DOWN");
////                recAdapter.addCard(origAdapter.removeCard());
////
//////               origAdapter.removeCard(new CardUtil.ICopyListener() {
//////                   @Override
//////                   public void onSuccess(CardEntity cardCopy) {
////////                       recAdapter.addCard(cardCopy);
//////                       Message msg = mHandler.obtainMessage();
//////                       msg.what = DOWNID;
//////                       msg.obj = cardCopy;
//////                       mHandler.sendMessage(msg);
//////                   }
//////               });
////                return true;
////            } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
////                Log.d(TAG, "KEYCODE_DPAD_UP");
////                origAdapter.addCard(recAdapter.removeCard());
////
//////                recAdapter.removeCard(new CardUtil.ICopyListener() {
//////                    @Override
//////                    public void onSuccess(CardEntity cardCopy) {
////////                        origAdapter.addCard(cardCopy);
//////                        Message msg = mHandler.obtainMessage();
//////                        msg.what = UPID;
//////                        msg.obj = cardCopy;
//////                        mHandler.sendMessage(msg);
//////                    }
//////                });
////                return true;
////            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
