package com.example.ticketing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DisplayEventInfo extends AppCompatActivity {

    private SectionsPagerAdapter1 mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public String getDate() {
        return Date;
    }

    public String getArtist() {
        return Artist;
    }

    public String getVenue() {
        return Venue;
    }

    public String getGenres() {
        return Genres;
    }

    public String getPrice() {
        return Price;
    }

    public String getTicket() {
        return Ticket;
    }

    public String getBuy() {
        return Buy;
    }

    private String Date;
    private String Artist;
    private String Venue;
    private String Genres;
    private String Price;
    private String Ticket;
    private String Buy;
    JSONObject mJson;

    TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.info_outline,
            R.drawable.artist,
            R.drawable.venue
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_event_info);
        String eventName = getIntent().getStringExtra("eventName");
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle(eventName);
        actionBar.setTitle((Html.fromHtml("<font color=\"#000000\">" + eventName + "</font>")));

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);


        mSectionsPagerAdapter = new SectionsPagerAdapter1(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_pager1);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs1);
        tabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();
        try {
            JSONObject mJsonObject = new JSONObject(getIntent().getStringExtra("searchResults"));
            mJson = mJsonObject;
            System.out.println(mJsonObject);
            //setEventInfo(mJsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

//    private void setEventInfo(JSONObject mJsonObject) {
//        try {
//            {
//            Date = jsonobject.getString("Date");
//            EventName = getIntent().getStringExtra("eventName");
//            EventId = getIntent().getStringExtra("eventId");
//            Artist = mjson.getString("Artist");
//            Venue = mjson.getString("Venue");
//            Genres = jsonobject.getString("Genres");
//            Price = jsonobject.getString("Price");
//            Ticket = jsonobject.getString("Ticket");
//            Buy = jsonobject.getString("Buy");
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    public JSONObject getEventInfo(){
        return mJson;
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

    }


    public class SectionsPagerAdapter1 extends FragmentPagerAdapter {

        public SectionsPagerAdapter1(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new EventInfo();
                case 1:
                    return new ArtistInfo();
                case 2:
                    return new VenueInfo();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "EVENTS";
                case 1:
                    return "ARTIST(S)";
                case 2:
                    return "VENUE";
            }
            return null;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game, menu);
        SharedPreferences sharedPreferences = this.getSharedPreferences("favorite", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String sharedPreferencesName = getIntent().getStringExtra("eventId");
        try{
            if (sharedPreferences.contains(sharedPreferencesName)) {
                menu.findItem(R.id.fav_button).setIcon(R.drawable.heart_fill_white);

            } else {
                menu.findItem(R.id.fav_button).setIcon(R.drawable.heart_outline_white);

            }}catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.twit_button:
                call_twitter();
                return true;
            case R.id.fav_button:
                call_setFav(item);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void call_setFav(MenuItem menu) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("favorite", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String sharedPreferencesName = getIntent().getStringExtra("eventId");

        try{

            if (sharedPreferences.contains(sharedPreferencesName)) {
                menu.setIcon(R.drawable.heart_outline_white);
                editor.remove(sharedPreferencesName);
                editor.commit();

            } else {
                menu.setIcon(R.drawable.heart_fill_white);
                String event_obj = getIntent().getStringExtra("eventObj");
                editor.putString(sharedPreferencesName, event_obj);
                editor.apply();

            }}catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String getvenuename(){
        JSONArray venue_array = null;
        try {
            venue_array = mJson.getJSONArray("Venue");
            String venue = venue_array.getString(0);
            return venue;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";

    }

    private void call_twitter() {
        try {

            String eventName = getIntent().getStringExtra("eventName");
            JSONArray venue_array = mJson.getJSONArray("Venue");
            String venue = venue_array.getString(0);
            String twitterURL = "https://twitter.com/intent/tweet?text=Checkout "
                    + eventName
                    + " at " + venue
                    + " %23CSCI571EventSearch";

            System.out.println(twitterURL);
            Uri uriUrl = Uri.parse(twitterURL);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        }catch (JSONException e){
            e.printStackTrace();

        }
    }

}