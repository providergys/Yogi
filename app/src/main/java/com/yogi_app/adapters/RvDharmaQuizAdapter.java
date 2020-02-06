package com.yogi_app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yogi_app.R;


public class RvDharmaQuizAdapter extends RecyclerView.Adapter< RvDharmaQuizAdapter.MyViewHolder> {

    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View view) {
            super(view);

        }
    }

    public RvDharmaQuizAdapter(Context context) {
        this.context    = context;
    }

    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.dharma_quiz_items, parent, false);

        return  new  MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
