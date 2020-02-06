package com.yogi_app.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yogi_app.R;
import com.yogi_app.activity.DocsURL;

import java.util.List;



public class RvDramaBooksAdapter extends RecyclerView.Adapter< RvDramaBooksAdapter.MyViewHolder> {

    Context context;
    Typeface mFont;
    List<FaqResponse> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView doc_type;
        ImageView imgDoc;
        public MyViewHolder(View view) {
            super(view);
            doc_type=(TextView) view.findViewById(R.id.doc_type);
            imgDoc=(ImageView) view.findViewById(R.id.imgDoc);
        }
    }

    public RvDramaBooksAdapter(Context context, List<FaqResponse> list) {
        this.context    = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.dharna_books_items, parent, false);
        mFont = Typeface.createFromAsset(context.getAssets(), "robotlight.ttf");
        return  new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final FaqResponse item = list.get(position);
        holder.doc_type.setTypeface(mFont);
        holder.doc_type.setText(item.getTitle());
        holder.imgDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DocsURL.class);
                intent.putExtra("DOCURL",""+item.getLink());
                Log.d("URL",item.getLink());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
