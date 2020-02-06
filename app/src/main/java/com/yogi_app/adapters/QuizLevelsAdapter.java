package com.yogi_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yogi_app.R;
import com.yogi_app.activity.QuizLevelQuestions;
import com.yogi_app.model.QuizModel;

import java.util.List;

/**
 * Created by AndroidDev on 30-Oct-18.
 */

public class QuizLevelsAdapter extends RecyclerView.Adapter<QuizLevelsAdapter.MyViewHolder> {
   private List<QuizModel> list;
   private Context context;

    public QuizLevelsAdapter(List<QuizModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View categoryListView = LayoutInflater.from(context).inflate(R.layout.item_quiz_levels, parent, false);
        return new MyViewHolder(categoryListView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final QuizModel item = list.get(position);
        Log.d("dfkjhgsu"," "+item.getName());
        System.out.println("jhfgsssssdfgjhdgfhjdgf"+item.getTerm_id());
        holder.tvLevel.setText(item.getName());
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, QuizLevelQuestions.class);
                int level_id = item.getTerm_id();
                intent.putExtra("LevelId",""+level_id);
                context.startActivity(intent);
                System.out.println("jhfgsssssdfgjhdgfhjdgf"+item.getTerm_id());

            }
        } );
//        Log.d("lisytqdyqdf",""+item.getLevelListCategories().get(position).getName());

    }


    @Override
    public int getItemCount() {
        return this.list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLevel;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvLevel = (TextView) itemView.findViewById(R.id.tvLevel);

        }
    }



}
