package com.yogi_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yogi_app.BuildConfig;
import com.yogi_app.R;
import com.yogi_app.activity.Calender;
import com.yogi_app.activity.MailingList;
import com.yogi_app.model.CalendarRequest;
import com.yogi_app.model.CalendarResponse;
import com.yogi_app.model.FaqResponse;
import com.yogi_app.retroutility.MainApplication;
import com.yogi_app.utility.TinyDB;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder> {

    private Context context;
    private List<CalendarResponse> list;
    private TinyDB tinyDB;
    private SparseBooleanArray sSelectedItems;
    private CalendarResponse item;
    ArrayList<String> event_ids=new ArrayList<String>();
    String eventID;
    public CalendarAdapter(Context context, List<CalendarResponse> list) {
        this.list = list;
        this.context = context;
        sSelectedItems = new SparseBooleanArray();
        event_ids.clear();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView eventTime, eventTitle;
        private ImageView imgHeart;

        public MyViewHolder(View view) {
            super(view);
            eventTime = (TextView) view.findViewById(R.id.tvTime);
            eventTitle = (TextView) view.findViewById(R.id.tvEventTitle);
            imgHeart = (ImageView) view.findViewById(R.id.imgHeart);
            imgHeart.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (sSelectedItems.get(getAdapterPosition(), false)) {
                imgHeart.setImageResource(R.drawable.border_heart);
                sSelectedItems.delete(getAdapterPosition());
                imgHeart.setSelected(false);
              //  eventID = item.getEvent_id();
                eventID = event_ids.get(getAdapterPosition());
                apiAddFavouriteEvent("0", eventID);


            } else {
                sSelectedItems.put(getAdapterPosition(), true);
                imgHeart.setSelected(true);
                imgHeart.setImageResource(R.drawable.filled_heart);
              //  eventID = item.getEvent_id();
                eventID = event_ids.get(getAdapterPosition());
                System.out.println(event_ids+"kdbnhnhhhhhlgfs"+getAdapterPosition());
                apiAddFavouriteEvent("1", eventID);

            }
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.card_calendar, parent, false);
        tinyDB = new TinyDB(context);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        item = list.get(position);

        event_ids.add(String.valueOf(item.getEventId()));

        System.out.println("jksdffffffds"+list.get(position));
        System.out.println("jksdffffffds"+String.valueOf(item.getEventId()));
        holder.eventTime.setText(item.getEventTime());
        holder.eventTitle.setText(item.getEventTitle());

        if (item.getFavouriteOrNot().equals("0")) {
            viewHolder.imgHeart.setImageResource(R.drawable.border_heart);
            sSelectedItems.delete(position);
            viewHolder.imgHeart.setSelected(false);
        } else {
            sSelectedItems.put(position, true);
            viewHolder.imgHeart.setSelected(true);
            Log.d("seelctyEven",""+item.getEventTitle());
            viewHolder.imgHeart.setImageResource(R.drawable.filled_heart);
        }
    }

    private void apiAddFavouriteEvent(String value, String eventID) {

        CalendarRequest request = new CalendarRequest();
        request.setFavouriteOrNot(value);
        Log.e("VAsdakjdfhj",""+value);
        request.setEvent_id(eventID);
        request.setUser_id(tinyDB.getInt("user_id"));
        MainApplication.getApiService().addFavouriteEvent("text/plain", request).enqueue(new Callback<CalendarResponse>() {
            @Override
            public void onResponse(Call<CalendarResponse> call, Response<CalendarResponse> response) {

                CalendarResponse calendarResponse = response.body();
                if (response.body().getRespCode().matches("7015")) {
                    Toast.makeText(context, "" + calendarResponse.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "" + calendarResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CalendarResponse> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

//    String event_title,event_date,event_desc,event_time,event_loc;
//
//    Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
//        intent.setType("vnd.android.cursor.item/event");
//                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event_time);
//                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,"endTime");
//                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
//                intent.putExtra(CalendarContract.Events.TITLE, event_title);
//                intent.putExtra(CalendarContract.Events.DESCRIPTION, event_desc);
//                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, event_loc);
//                intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");
//                startActivity(intent);
