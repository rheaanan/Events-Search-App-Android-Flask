package com.example.ticketing;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static EventInfo newInstance(String param1, String param2) {
        EventInfo fragment = new EventInfo();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_event_info, container, false);
        DisplayEventInfo eventActivity = (DisplayEventInfo) getActivity();
        System.out.println("getting parent activity");
        JSONObject mjson = eventActivity.getEventInfo();
        List<String> detailsHeadings = Arrays.asList("Artist", "Venue", "Date","Genres", "Price", "Ticket", "Buy", "Seatmap");
        String[] special = new String[]{"Artist","Buy","Seatmap","Venue"};
        List<String> special_list = new ArrayList<>(Arrays.asList(special));
        //detailsHeadings_kv = {'Date':'Time' ,'Artist':'Artist/Team', 'Venue':'Venue', 'Genres':'Category', 'Price':'Price Ranges','Ticket':'Ticket Status','Buy':'Buy Ticket At','Seatmap':'Seat Map'}
        JSONArray artist_array, venue_array;
        Map<String, String> myMap = new HashMap<String, String>() {{
            put("Artist", "Artist/Team  ");
            put("Venue", "Venue  ");
            put("Date","Date  ");
            put("Price", "Price Ranges  ");
            put("Ticket","Ticket Status  ");
            put("Buy", "Buy Ticket At  ");
            put("Seatmap", "Seat Map  ");
            put("Genres","Category  ");
        }};
        TableLayout infoTable = (TableLayout) root.findViewById(R.id.tl_eventinfo);
        infoTable.setStretchAllColumns(true);
        infoTable.bringToFront();
        infoTable.setColumnShrinkable(1,true);
        for (int i =0; i<detailsHeadings.size(); i++){
            if (special_list.contains(detailsHeadings.get(i))){
                if(detailsHeadings.get(i)=="Artist"){
                    try {
                        artist_array = mjson.getJSONArray("Artist");
                        TableRow tr = new TableRow(getContext());

                        TextView c1 = new TextView(getContext());
                        c1.setTypeface(null, Typeface.BOLD);
                        c1.setText(myMap.get(detailsHeadings.get(i)));
                        TextView c2 = new TextView(getContext());
                        c2.setText(artist_array.join( "|" ).replace("\"",""));
                        tr.addView(c1);
                        tr.addView(c2);
                        infoTable.addView(tr);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(detailsHeadings.get(i)=="Buy"){
                    try {
                        TableRow tr = new TableRow(getContext());
                        TextView c1 = new TextView(getContext());
                        c1.setText(myMap.get(detailsHeadings.get(i)));
                        c1.setTypeface(null, Typeface.BOLD);
                        TextView c2 = new TextView(getContext());
                        String UrlText = "<a href=\""+mjson.getString(detailsHeadings.get(i))+"\" > Ticketmaster </a>";
                        c2.setText(Html.fromHtml(UrlText));
                        c2.setClickable(true);
                        c2.setMovementMethod(LinkMovementMethod.getInstance());
                        tr.addView(c1);
                        tr.addView(c2);
                        infoTable.addView(tr);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(detailsHeadings.get(i)=="Seatmap"){
                    try {
                        TableRow tr = new TableRow(getContext());
                        TextView c1 = new TextView(getContext());
                        c1.setText(myMap.get(detailsHeadings.get(i)));
                        c1.setTypeface(null, Typeface.BOLD);
                        TextView c2 = new TextView(getContext());
                        String UrlText = "<a href=\""+mjson.getString(detailsHeadings.get(i))+"\" > View Seatmap here </a>";
                        c2.setClickable(true);
                        c2.setMovementMethod(LinkMovementMethod.getInstance());
                        c2.setText(Html.fromHtml(UrlText));
                        tr.addView(c1);
                        tr.addView(c2);
                        infoTable.addView(tr);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(detailsHeadings.get(i)=="Venue"){
                    try {
                        venue_array = mjson.getJSONArray("Venue");
                        TableRow tr = new TableRow(getContext());
                        TextView c1 = new TextView(getContext());
                        c1.setText(myMap.get(detailsHeadings.get(i)));
                        c1.setTypeface(null, Typeface.BOLD);
                        TextView c2 = new TextView(getContext());
                        c2.setText(venue_array.getString(0).toString());
                        tr.addView(c1);
                        tr.addView(c2);
                        infoTable.addView(tr);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            else{
                if (mjson.has(detailsHeadings.get(i))) {
                    TableRow tr = new TableRow(getContext());
                    TextView c1 = new TextView(getContext());
                    c1.setText(myMap.get(detailsHeadings.get(i)));
                    c1.setTypeface(null, Typeface.BOLD);
                    tr.addView(c1);
                    TextView c2 = new TextView(getContext());
                    try {
                        c2.setText(mjson.getString(detailsHeadings.get(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        c2.setText("N/A");
                    }
                    tr.addView(c2);
                    infoTable.addView(tr);
                }
                else{
                    System.out.print("nothing to add");
                }
            }
        }
//        for(int i = 0; i < myobj.length(); i++){
//            TableRow tr =  new TableRow(getContext());
//            TextView c1 = new TextView(getContext());
//             c1.setText("hello");
//            TextView c2 = new TextView(getContext());
//            TextView c3 = new TextView(getContext());
//            tr.addView(c1);
//            tr.addView(c2);
//            tr.addView(c3);
//            prices.addView(tr);
//        }
//         Inflate the layout for this fragment
        return root;
    }
}