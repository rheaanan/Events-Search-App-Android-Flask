package com.example.ticketing;

import android.graphics.Typeface;
import android.os.Bundle;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VenueInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VenueInfo extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VenueInfo() {
        // Required empty public constructor
    }
    GoogleMap map;
    MapView mapView;
    double lat, lng;


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    public void useVolley(View root, String venueId){
        RequestQueue mQueue = Volley.newRequestQueue(getContext());
        //String url = "https://yogoslavia.wl.r.appspot.com/getResults?keyword=Justin%20Bieber&category=all&distance=10&unit=miles&location_check=here&location_text=&lat=34.0030&lng=-118.2863";
        String url = "https://yogoslavia.wl.r.appspot.com/getVenueDetails?id="+venueId;
        System.out.println("Use Volley Called"+url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("volley_response in venueinfo fragment"+response);
                TableLayout infoTable = (TableLayout) root.findViewById(R.id.tl_venueinfo);
                infoTable.setStretchAllColumns(true);
                infoTable.bringToFront();
                infoTable.setColumnShrinkable(1,true);
                List<String> venueHeadings = Arrays.asList("Name","Address", "City", "Phone","Genres", "Open", "General", "Child");
                //detailsHeadings_kv = {'Date':'Time' ,'Artist':'Artist/Team', 'Venue':'Venue', 'Genres':'Category', 'Price':'Price Ranges','Ticket':'Ticket Status','Buy':'Buy Ticket At','Seatmap':'Seat Map'}
                //venueHeadings_kv = {'Address':'Address' ,'City': 'City','':'Phone Number' ,'Open':'Open Hourse' , 'General': 'General Rule', 'Child': 'Child Rule' }
                Map<String, String> myMap = new HashMap<String, String>() {{
                    put("Address", "Address  ");
                    put("City", "City  ");
                    put("Phone","Phone Number  ");
                    put("Open","Open Hours  ");
                    put("General", "General Rule  ");
                    put("Child","Child Rule  ");
                    put("Name","Name ");
                }};
                TableRow tr = new TableRow(getContext());
                TextView c1 = new TextView(getContext());
                c1.setTypeface(null, Typeface.BOLD);
                c1.setText("Name  ");
                tr.addView(c1);
                TextView c2 = new TextView(getContext());
                DisplayEventInfo activity = (DisplayEventInfo) getActivity();


                try {
                    c2.setText(activity.getvenuename());
                    tr.addView(c2);
                    infoTable.addView(tr);

                } catch (Exception e) {
                    e.printStackTrace();
                    c2.setText("N/A");
                }


                for (int i =0; i<venueHeadings.size(); i++) {
                    if (response.has(venueHeadings.get(i))) {
                        tr = new TableRow(getContext());
                        c1 = new TextView(getContext());
                        c1.setTypeface(null, Typeface.BOLD);
                        c1.setText(myMap.get(venueHeadings.get(i)));
                        tr.addView(c1);
                        c2 = new TextView(getContext());
                        try {
                            c2.setText(response.getString(venueHeadings.get(i)));
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
                try {
                    lat = Double.parseDouble(response.getString("Lat"));
                    lng = Double.parseDouble(response.getString("Lng"));
                    mapView = root.findViewById(R.id.map);
                    if(mapView != null){
                        mapView.onCreate(null);
                        mapView.onResume();
                        mapView.getMapAsync(VenueInfo.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VenueInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static VenueInfo newInstance(String param1, String param2) {
        VenueInfo fragment = new VenueInfo();
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

        View root = inflater.inflate(R.layout.fragment_venue_info, container, false);
        DisplayEventInfo eventActivity = (DisplayEventInfo) getActivity();
        System.out.println("getting parent activity");
        JSONObject mjson = eventActivity.getEventInfo();

        JSONArray venue_array = null;
        try {
            venue_array = mjson.getJSONArray("Venue");
            useVolley(root, venue_array.getString(1).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return root;

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        map = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
        CameraPosition position = CameraPosition.builder().target(new LatLng(lat, lng)).zoom(14).bearing(0).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }
}