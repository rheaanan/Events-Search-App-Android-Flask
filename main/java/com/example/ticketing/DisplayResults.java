package com.example.ticketing;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplayResults extends AppCompatActivity {
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private TextView no_results;
    List<EventDetails> eventList = new ArrayList<EventDetails>();

    @Override
    protected void onRestart(){
        super.onRestart();
        eventList = new ArrayList<EventDetails>();
        try {
            JSONObject mJsonObject = new JSONObject(getIntent().getStringExtra("searchResults"));
            System.out.println(mJsonObject);
            recyclerView = findViewById(R.id.lv_event_list);
            recyclerView.setHasFixedSize(true);
            fillEventsList(mJsonObject);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new RecycleViewAdapter(eventList, this);
            recyclerView.setAdapter(mAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (eventList.size() == 0) {
            no_results = findViewById(R.id.tv_no_results);
            no_results.setVisibility(View.VISIBLE);
            no_results.setGravity(Gravity.CENTER);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_display_results);
        eventList = new ArrayList<EventDetails>();
        try {

            JSONObject mJsonObject = new JSONObject(getIntent().getStringExtra("searchResults"));
            System.out.println(mJsonObject);
            recyclerView = findViewById(R.id.lv_event_list);
            recyclerView.setHasFixedSize(true);
            fillEventsList(mJsonObject);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new RecycleViewAdapter(eventList, this);
            recyclerView.setAdapter(mAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (eventList.size() == 0){
            no_results =  findViewById(R.id.tv_no_results);
            no_results.setVisibility(View.VISIBLE);
            no_results.setGravity(Gravity.CENTER);
        }
    }

    private void fillEventsList(JSONObject mJsonObject) {
        try {

            JSONArray mJsonArray= mJsonObject.getJSONArray("eventsArr");
            System.out.println(mJsonObject);

            for(int i = 0;i<mJsonArray.length();i++){
                JSONObject jsonobject = mJsonArray.getJSONObject(i);

                String date = jsonobject.getString("Date");
                String event_name = jsonobject.getString("EventName");
                String event_id = jsonobject.getString("EventId");
                String category = jsonobject.getString("Category");
                String venue = jsonobject.getString("Venue");
                EventDetails item = new EventDetails(date,event_name,event_id,category,venue);
                eventList.add(item);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}