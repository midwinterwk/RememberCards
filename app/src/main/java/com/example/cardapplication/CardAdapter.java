package com.example.cardapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter {
    protected Context mContext;
    protected List<CardEntity> mEntityList;
    protected RecyclerView mRecyclerView;
    protected int hiddenCardWidth = 0;
    protected int cardWidth = 0;
    protected int cardHeight = 0;
    private int focusType = 0;

    private String TAG = "";

    public CardAdapter(Context context, List<CardEntity> entities, RecyclerView recycleViewCard) {
        mContext = context;
        mEntityList = entities;
        mRecyclerView = recycleViewCard;
        TAG = CardAdapter.this.getClass().getSimpleName();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
        if (cardWidth == 0) {
            cardHeight = mRecyclerView.getHeight();
            cardWidth = cardHeight * 2 / 3;
            hiddenCardWidth = cardWidth / 12;
            Log.d(TAG, "cardHeight = " + cardHeight + "cardWidth = " + cardWidth);
        }
//        view.getLayoutParams().width = hiddenCardWidth;
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder position = " + position);
        CardEntity entity = mEntityList.get(position);
        ((CardViewHolder) holder).getImageView().setImageBitmap(entity.getBitmap());
        ((CardViewHolder) holder).getImageView().getLayoutParams().width = cardWidth;
        View view = holder.itemView;
        if (position == getItemCount() - 1) {
            Log.d(TAG, "position = " + position + "getItemCount() = " + getItemCount());
            view.getLayoutParams().width = cardWidth;
        } else {
            view.getLayoutParams().width = hiddenCardWidth;
        }

        setCardListener(holder, position, view);
    }

    protected void setCardListener(@NonNull RecyclerView.ViewHolder holder, int position, View view) {
        CardOnListener listener = new CardOnListener(holder, position);
        view.setOnFocusChangeListener(listener);
//        view.setOnKeyListener(listener);
    }

    @Override
    public int getItemCount() {
        return mEntityList.size();
    }

    public void Shuffle() {
        Collections.shuffle(mEntityList);
        notifyDataSetChanged();
    }

//    protected class CardViewHolder extends RecyclerView.ViewHolder {
//
//        private ImageView imageView;
//
//        public CardViewHolder(View itemView) {
//            super(itemView);
//            imageView = itemView.findViewById(R.id.image_card);
//        }
//    }

    protected class CardOnListener implements View.OnFocusChangeListener, View.OnKeyListener {
        protected final RecyclerView.ViewHolder cardholder;
        protected int cardPosition;

        public CardOnListener(RecyclerView.ViewHolder holder, int position) {
            cardholder = holder;
            cardPosition = position;
        }

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            Log.d(TAG, "cardPosition = " + cardPosition + " hasFocus = " + hasFocus);
            ImageView imageView = cardholder.itemView.findViewById(R.id.image_card);
            if (hasFocus) {
                setFilter(imageView);
            } else {
                clearFilter(imageView);
            }
            ViewGroup.LayoutParams vlp = cardholder.itemView.getLayoutParams();
            if (hasFocus) {
                if (cardPosition == getItemCount() - 1) {
                    Log.d(TAG, "cardPosition = " + cardPosition + " cardWidth = " + cardWidth);
                    vlp.width = cardWidth;
                } else {
                    vlp.width = cardWidth / 6;
                }
                vlp.height = cardHeight;
//                vlp.height = (int) (cardHeight * 1.1);
//                scrollToView(view);
//                mRecyclerView.scrollToPosition(cardPosition);
                ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                        .scrollToPositionWithOffset(cardPosition, mRecyclerView.getWidth() / 2);
            } else {
                if (cardPosition == getItemCount() - 1) {
//                    vlp.width = cardWidth;
                } else {
                    vlp.width = hiddenCardWidth;
                }
                vlp.height = cardHeight;
            }

            cardholder.itemView.setLayoutParams(vlp);
        }

        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            int action = keyEvent.getAction();
            Log.d(TAG, "onKey action = " + action + "keyCode = " + keyCode);
            if (action == KeyEvent.ACTION_DOWN) {
                if (keyCode != KeyEvent.KEYCODE_DPAD_RIGHT || keyCode != KeyEvent.KEYCODE_DPAD_LEFT) {
//                    focusType = keyCode;
                    return true;
                }
            }
            return false;
        }
    }

    private void changeLight(ImageView imageView, int brightness) {
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, // 改变亮度
                0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});
        imageView.setColorFilter(new ColorMatrixColorFilter(cMatrix));
    }

    /**
     * 设置滤镜
     */
    protected void setFilter(ImageView imageView) {
        //先获取设置的src图片
        Drawable drawable = imageView.getDrawable();
        if (drawable != null) {
            //设置滤镜
            drawable.setColorFilter(Color.YELLOW, PorterDuff.Mode.DARKEN);
        }
    }

    /**
     * 清除滤镜
     */
    protected void clearFilter(ImageView imageView) {
        //先获取设置的src图片
        Drawable drawable = imageView.getDrawable();
        if (drawable != null) {
            //清除滤镜
            drawable.clearColorFilter();
        }
    }

    private void scrollToView(View child) {
        if (child == null)
            return;
        int scrollDistance = getScrollDistance(child);
        Log.d("scrollToView", "scrollDistance =" + scrollDistance);
        if (scrollDistance != -1)
            mRecyclerView.smoothScrollBy(scrollDistance, 0);
    }

    private int getScrollDistance(View child) {
        int x = (int) child.getX();
        int scrollX = child.getScrollX();
        Log.d("getScrollDistance", "x =" + x);
        Log.d("getScrollDistance", "scrollX =" + scrollX);

        int recyclerViewWidth = mRecyclerView.getWidth();
        Log.d("getScrollDistance", "recyclerViewWidth =" + recyclerViewWidth);
        if (focusType == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (x > recyclerViewWidth - cardWidth) {
                return recyclerViewWidth - cardWidth;
            }
        } else if (focusType == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (x < cardWidth) {
                return 0;
            }
        }
        return -1;
    }
}
