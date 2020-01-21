package com.example.cardapplication;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecallCardAdapter extends CardAdapter {
    private int curPosition = -1;
    private int lastFocusPosition = -1;
    private CardEntity curCardEntity = null;

    private static final String TAG = "RecallCardAdapter";

    public RecallCardAdapter(Context context, List<CardEntity> entities, RecyclerView recycleViewCard) {
        super(context, entities, recycleViewCard);
//        recycleViewCard.setItemAnimator(null);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
        if (cardWidth == 0) {
            cardHeight = mRecyclerView.getHeight();
            cardWidth = cardHeight * 2 / 3;

            hiddenCardWidth = cardWidth / 6;
            Log.d(TAG, "cardHeight = " + cardHeight + "cardWidth = " + cardWidth);
        }
        return new CardViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    @Override
//    public void setHasStableIds(boolean hasStableIds) {
//        this.setHasStableIds(true);
//    }

    @Override
    protected void setCardListener(@NonNull RecyclerView.ViewHolder holder, int position, View view) {
        RecallCardOnListener listener = new RecallCardOnListener(holder, position);
        view.setOnFocusChangeListener(listener);
        view.setOnClickListener(new DoubleClickListener(new DoubleClickListener.DoubleClickCallBack() {
            @Override
            public void oneClick(View view) {

            }

            @Override
            public void doubleClick(View view) {
                Log.d(TAG, "doubleClick");
                CardUtil.onKeyEvent(KeyEvent.KEYCODE_DPAD_CENTER);
            }
        }));
    }

    public int getCorrectNum() {
        int correctNum = 0;
        List<CardEntity> cards = CardUtil.getCards(mContext);
        for (int i = 0; i < mEntityList.size(); i++) {
            if (cards.get(i).getIndex() == mEntityList.get(i).getIndex()) {
                correctNum++;
            }
        }
        return correctNum;
    }

    class RecallCardOnListener extends CardOnListener {

        public RecallCardOnListener(RecyclerView.ViewHolder holder, int position) {
            super(holder, position);
        }

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            Log.d(TAG, "cardPosition = " + cardPosition + " hasFocus = " + hasFocus);
            ImageView imageView = cardholder.itemView.findViewById(R.id.image_card);
            if (hasFocus) {
                setFilter(imageView);
                curPosition = cardPosition;
                lastFocusPosition = curPosition;
                curCardEntity = mEntityList.get(cardPosition);
            } else {
                clearFilter(imageView);
                curPosition = -1;
                curCardEntity = null;
            }
        }
    }

    public int getCurPosition() {
        return curPosition;
    }

    public CardEntity getCurCardEntity() {
        return curCardEntity;
    }

    public CardEntity removeCard() {
        Log.d(TAG, "removeCard curPosition = " + curPosition);
        if (curCardEntity == null || curPosition == -1 || curPosition >= getItemCount()) {
            return null;
        }
//        CardEntity cardEntity = curCardEntity;
//        mEntityList.remove(cardEntity);
        CardEntity cardEntity = mEntityList.remove(curPosition);
        notifyDataSetChanged();
//        notifyItemRemoved(curPosition);
        return cardEntity;
    }

    public void addCard(CardEntity cardEntity) {
        if (cardEntity != null) {
            mEntityList.add(cardEntity);
            notifyDataSetChanged();
//            notifyItemInserted(getItemCount());
        }
    }

    public void insertCard(CardEntity cardEntity) {
        if (cardEntity != null && lastFocusPosition > -1 && lastFocusPosition < mEntityList.size() - 1) {
            mEntityList.add(lastFocusPosition, cardEntity);
            notifyDataSetChanged();
        } else {
            if (cardEntity != null) {
                mEntityList.add(cardEntity);
                notifyDataSetChanged();
            }
        }
    }

    public int getLastFocusPosition(){
        if (lastFocusPosition == getItemCount() - 1) {
            lastFocusPosition = -1;
        }
        return lastFocusPosition;
    }

//    public int removeCard() {
//        CardEntity cardEntity = mEntityList.get(curCardEntity);
//        int cardIndex = cardEntity.getIndex();
//        mEntityList.remove(cardEntity);
//        notifyDataSetChanged();
//        return cardIndex;
//    }

//    public void addCard(int cardIndex) {
//        CardEntity cardEntity = new CardEntity();
//        cardEntity.setIndex(cardIndex);
//        cardEntity.setBitmap(CardUtil.getOragCards().get(cardIndex).getBitmap());
//        mEntityList.add(cardEntity);
//        notifyDataSetChanged();
//    }

//    @Override
//    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
//        super.onViewRecycled(holder);
//        CardViewHolder viewHolder =(CardViewHolder)holder;
//        if (viewHolder.imageView != null) {
//            viewHolder.imageView.setImageBitmap(null);
//        }
//    }
}
