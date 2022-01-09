package com.example.ticketing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
    List<EventDetails> eventDetailsList;
    String category_list;
    Context context;
    private RequestQueue mQueue;

    public RecycleViewAdapter(List<EventDetails> eventList, Context context) {
        this.eventDetailsList = eventList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_event,parent,false);
        MyViewHolder holder = new  MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.tv_event_name.setText(eventDetailsList.get(position).getEventName());
        holder.tv_venue.setText(eventDetailsList.get(position).getVenue());
        holder.tv_date.setText(eventDetailsList.get(position).getDate());
        category_list = eventDetailsList.get(position).getCategory();
        System.out.println("categories list"+category_list.toString());
        if (category_list.contains("Arts & Theatre")){
            Glide.with(this.context).load("").error(R.drawable.art_icon).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_cat_image);
        }
        else if (category_list.contains("Sports")){
            Glide.with(this.context).load("").error(R.drawable.ic_sport_icon).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_cat_image);
        }
        else if (category_list.contains("Film")){
            Glide.with(this.context).load("").error(R.drawable.film_icon).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_cat_image);
        }
        else if (category_list.contains("Music")){
            Glide.with(this.context).load("").error(R.drawable.music_icon).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_cat_image);
        }
        else if (category_list.contains("Miscellaneous")){
            Glide.with(this.context).load("").error(R.drawable.miscellaneous_icon).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_cat_image);
        }
        else{
            Glide.with(this.context).load("").error(R.drawable.miscellaneous_icon).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_cat_image);
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("favorite", Context.MODE_PRIVATE);
        String sharedPreferencesName = eventDetailsList.get(position).getEventId();
        //Glide.with(this.context).load("").error(R.drawable.heart_outline_black).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_fav_image);

        if (sharedPreferences.contains(sharedPreferencesName)) {
            Glide.with(this.context).load("").error(R.drawable.heart_fill_red).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_fav_image);
        } else {
            Glide.with(this.context).load("").error(R.drawable.heart_outline_black).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_fav_image);
        }




        holder.iv_fav_image.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       SharedPreferences sharedPreferences = context.getSharedPreferences("favorite", Context.MODE_PRIVATE);
                                                       SharedPreferences.Editor editor = sharedPreferences.edit();
                                                       String sharedPreferencesName = eventDetailsList.get(position).getEventId();

                                                       if (sharedPreferences.contains(sharedPreferencesName)) {
                                                           holder.iv_fav_image.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_outline_black));
                                                           editor.remove(sharedPreferencesName);
                                                           editor.commit();
                                                       } else {
                                                           holder.iv_fav_image.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_fill_red));
                                                           editor.putString(sharedPreferencesName, eventDetailsList.get(position).toString());
                                                           editor.apply();
                                                       }
                                                   }
                                               });


        holder.tv_event_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              getVenueDetails(eventlist.get(position).getVenueId());
//              getEventDetails(eventlist.get(position).getEventId());
//              System.out.println(res_list);
//              System.out.println("hello");
//              System.out.println(event);
                getEventDetails_volley(eventDetailsList.get(position).getEventId(),eventDetailsList.get(position).getEventName(),position);

            }
        });
    }

    private void getEventDetails_volley(String event_id, String event_name, int position) {
        mQueue = Volley.newRequestQueue(context);
        //String url = "https://yogoslavia.wl.r.appspot.com/getResults?keyword=Justin%20Bieber&category=all&distance=10&unit=miles&location_check=here&location_text=&lat=34.0030&lng=-118.2863";
        String url = "https://yogoslavia.wl.r.appspot.com/getEventDetails?id="+event_id;
        System.out.println("Use Volley for event details  Called"+url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("volley_response"+response);
                Intent intent = new Intent(context, DisplayEventInfo.class);
                intent.putExtra("searchResults" ,response.toString());
                intent.putExtra("eventName",event_name);
                intent.putExtra("eventId",event_id);
                intent.putExtra("eventObj",eventDetailsList.get(position).toString());

                context.startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    @Override
    public int getItemCount() {
        return eventDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_cat_image;
        ImageView iv_fav_image;
        TextView tv_event_name;
        TextView tv_venue;
        TextView tv_date;


        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            iv_fav_image = itemView.findViewById(R.id.iv_fav_image);
            iv_cat_image = itemView.findViewById(R.id.iv_cat_image);
            tv_event_name = itemView.findViewById(R.id.tv_event_name);
            tv_venue = itemView.findViewById(R.id.tv_venue);
            tv_date = itemView.findViewById(R.id.tv_date);
        }
    }
}
