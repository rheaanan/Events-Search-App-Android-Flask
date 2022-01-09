package com.example.ticketing;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArtistInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArtistInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArtistInfo.
     */
    // TODO: Rename and change types and number of parameters



    public static ArtistInfo newInstance(String param1, String param2) {
        ArtistInfo fragment = new ArtistInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void useVolley2(View root, String firstArtistName){
        RequestQueue mQueue = Volley.newRequestQueue(getContext());
        //String url = "https://yogoslavia.wl.r.appspot.com/getResults?keyword=Justin%20Bieber&category=all&distance=10&unit=miles&location_check=here&location_text=&lat=34.0030&lng=-118.2863";
        String url = "https://yogoslavia.wl.r.appspot.com/spotify?artist="+firstArtistName;
        System.out.println("Use Volley Called for artist "+url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("volley_response in artistinfo fragment"+response);
                TableLayout infoTable = (TableLayout) root.findViewById(R.id.tl_artist_info);
                infoTable.setStretchAllColumns(true);
                infoTable.bringToFront();
                infoTable.setColumnShrinkable(1,true);
                //spotheadings = ['Name','Followers','Popularity','Check']
                List<String> spotHeadings  = Arrays.asList("Name", "City", "Followers","Check");
                //spotheadings_kv = {'Name':'Name','Followers':'Followers','Popularity':'Popularity','Check':'Check At'}
                Map<String, String> myMap = new HashMap<String, String>() {{
                    put("Name", "Name  ");
                    put("Followers", "Followers  ");
                    put("Popularity", "Popularity  ");
                    put("Check", "Check At  ");
                }};
                if (response.length()!=0) {
                    for (int i = 0; i < spotHeadings.size(); i++) {
                        if (response.has(spotHeadings.get(i))) {
                            if (spotHeadings.get(i)=="Check"){
                                TableRow tr = new TableRow(getContext());
                                TextView c1 = new TextView(getContext());
                                c1.setText(myMap.get(spotHeadings.get(i)));
                                c1.setTypeface(null, Typeface.BOLD);
                                tr.addView(c1);
                                TextView c2 = new TextView(getContext());
                                String UrlText = null;
                                try {
                                    UrlText = "<a href=\""+response.getJSONObject(spotHeadings.get(i)).getString("spotify")+"\" > Spotify </a>";
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                c2.setClickable(true);
                                c2.setMovementMethod(LinkMovementMethod.getInstance());
                                c2.setText(Html.fromHtml(UrlText));
                                tr.addView(c2);
                                infoTable.addView(tr);

                            }
                            else {
                                TableRow tr = new TableRow(getContext());
                                TextView c1 = new TextView(getContext());
                                c1.setTypeface(null, Typeface.BOLD);
                                c1.setText(myMap.get(spotHeadings.get(i)));
                                tr.addView(c1);
                                TextView c2 = new TextView(getContext());
                                try {
                                    c2.setText(response.getString(spotHeadings.get(i)));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    c2.setText("N/A");
                                }
                                tr.addView(c2);
                                infoTable.addView(tr);
                            }
                        } else {
                            System.out.print("nothing to add");
                        }
                    }
                }
                else{
                    TableRow tr = new TableRow(getContext());
                    TextView c1 = new TextView(getContext());
                    c1.setText(firstArtistName+":");
                    c1.setTypeface(null, Typeface.BOLD);
                    tr.addView(c1);

                    TextView c2 = new TextView(getContext());
                    c2.setText("No details");
                    tr.addView(c2);
                    infoTable.addView(tr);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    public void useVolley(View root, String firstArtistName, String secondArtistName){
        RequestQueue mQueue = Volley.newRequestQueue(getContext());
        //String url = "https://yogoslavia.wl.r.appspot.com/getResults?keyword=Justin%20Bieber&category=all&distance=10&unit=miles&location_check=here&location_text=&lat=34.0030&lng=-118.2863";
        String url = "https://yogoslavia.wl.r.appspot.com/spotify?artist="+firstArtistName;
        System.out.println("Use Volley Called for artist "+url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("volley_response in artistinfo fragment"+response);
                TableLayout infoTable = (TableLayout) root.findViewById(R.id.tl_artist_info);
                infoTable.setStretchAllColumns(true);
                infoTable.bringToFront();
                infoTable.setColumnShrinkable(1,true);
                //spotheadings = ['Name','Followers','Popularity','Check']
                List<String> spotHeadings  = Arrays.asList("Name", "City", "Followers","Popularity","Check");
                //spotheadings_kv = {'Name':'Name','Followers':'Followers','Popularity':'Popularity','Check':'Check At'}

                Map<String, String> myMap = new HashMap<String, String>() {{
                    put("Name", "Name  ");
                    put("Followers", "Followers  ");
                    put("Popularity","Popularity  ");
                    put("Check","Check At  ");
                }};
                if (response.length()!=0) {
                    for (int i = 0; i < spotHeadings.size(); i++) {
                        if (response.has(spotHeadings.get(i))) {
                            if (spotHeadings.get(i)=="Check"){
                                TableRow tr = new TableRow(getContext());
                                TextView c1 = new TextView(getContext());
                                c1.setText(myMap.get(spotHeadings.get(i)));
                                c1.setTypeface(null, Typeface.BOLD);
                                tr.addView(c1);
                                TextView c2 = new TextView(getContext());
                                String UrlText = null;
                                try {
                                    UrlText = "<a href=\""+response.getJSONObject(spotHeadings.get(i)).getString("spotify")+"\" > Spotify </a>";
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                c2.setClickable(true);
                                c2.setMovementMethod(LinkMovementMethod.getInstance());
                                c2.setText(Html.fromHtml(UrlText));
                                tr.addView(c2);
                                infoTable.addView(tr);

                            }
                            else {
                                TableRow tr = new TableRow(getContext());
                                TextView c1 = new TextView(getContext());
                                c1.setTypeface(null, Typeface.BOLD);
                                c1.setText(myMap.get(spotHeadings.get(i)));
                                tr.addView(c1);
                                TextView c2 = new TextView(getContext());
                                try {
                                    c2.setText(response.getString(spotHeadings.get(i)));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    c2.setText("N/A");
                                }
                                tr.addView(c2);
                                infoTable.addView(tr);
                            }
                        } else {
                            System.out.print("nothing to add");
                        }
                    }
                }
                else{
                    TableRow tr = new TableRow(getContext());
                    TextView c1 = new TextView(getContext());
                    c1.setTypeface(null, Typeface.BOLD);
                    c1.setText(firstArtistName+":");
                    tr.addView(c1);

                    TextView c2 = new TextView(getContext());
                    c2.setText("No details");
                    tr.addView(c2);
                    infoTable.addView(tr);
                }
                if (secondArtistName!="notanartist"){
                    useVolley2(root, secondArtistName);
                }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_artist_info, container, false);
        DisplayEventInfo eventActivity = (DisplayEventInfo) getActivity();
        System.out.println("getting parent activity");
        JSONObject mjson = eventActivity.getEventInfo();

        JSONArray venue_array = null;
        try {
            JSONArray artist_arr = mjson.getJSONArray("Artist");
            String first_artist, second_artist;
            first_artist = artist_arr.getString(0);
            if(artist_arr.length()==1){
                second_artist = "notanartist";
            }
            else{
                second_artist = artist_arr.getString(1);
            }
            useVolley(root, first_artist,second_artist);
        } catch (JSONException e) {
            System.out.println("no artist info");
            //TBD handle no artist info
            TextView no_results =  root.findViewById(R.id.tv_no_records);
            no_results.setVisibility(View.VISIBLE);
            no_results.setGravity(Gravity.CENTER);
            e.printStackTrace();
        }
        return root;
    }
}