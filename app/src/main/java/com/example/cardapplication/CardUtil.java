package com.example.cardapplication;

import android.app.Instrumentation;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Collections;
import java.util.List;

public class CardUtil {

    private static LinkedList<CardEntity> mOrgiEntityList;
    private static LinkedList<CardEntity> mEntityList;
    private static LinkedList<CardEntity> mSourceList ;

    public static List<CardEntity> getCards(Context context) {
        if (mEntityList != null && mEntityList.size() > 0) {
            return mEntityList;
        }
        mOrgiEntityList = new LinkedList<>();
        mEntityList = new LinkedList<>();
        int index = 0;
        try {
            AssetManager assets = context.getAssets();
            //获取/assests/目录下的所有的文件
            String path = "poker";
            String[] images = assets.list(path);
            for (String image : images) {
                InputStream assetFile = null;
                assetFile = assets.open(path + "/" + image);
                CardEntity entity = new CardEntity();
                entity.setIndex(index++);
                entity.setBitmap(BitmapFactory.decodeStream(assetFile));
                mOrgiEntityList.add(entity);
                mEntityList.add(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.shuffle(mEntityList);
        return mEntityList;
    }

    public static List<CardEntity> getOragCards() {
        if (mOrgiEntityList != null && mOrgiEntityList.size() > 0) {
            return mOrgiEntityList;
        }
        return null;
    }

    public static List<CardEntity> getSourceList() {
        if (mSourceList != null) {
            return mSourceList;
        } else {
            return new LinkedList<CardEntity>();
        }
    }

    /**
     * 模拟系统按键。
     *
     * @param keyCode
     */
    public static void onKeyEvent(final int keyCode) {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(keyCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void copyCard(final CardEntity cardEntity, final ICopyListener listener) {
        new Thread(){
            @Override
            public void run() {
                Gson gson = new Gson();
                CardEntity cardCopy = gson.fromJson(gson.toJson(cardEntity),CardEntity.class);
                listener.onSuccess(cardCopy);
            }
        }.start();
    }

    public interface ICopyListener {
        void onSuccess(CardEntity cardCopy);
    }
}
