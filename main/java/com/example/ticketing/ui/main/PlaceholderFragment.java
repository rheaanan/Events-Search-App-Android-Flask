package com.example.ticketing.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ticketing.DisplayResults;
import com.example.ticketing.EventDetails;
import com.example.ticketing.R;
import com.example.ticketing.databinding.FragmentMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private Button search;
    private Button clear;
    private PageViewModel pageViewModel;
    private FragmentMainBinding binding;
    private Spinner category;
    private EditText location;
    private EditText keyword;
    private RadioButton other;
    private RadioButton current;
    private EditText distance;
    private Spinner unit;
    FusedLocationProviderClient fusedLocationProviderClient;
    private RequestQueue mQueue;
    private JSONObject volley_response;
    private double lat;
    private double lng;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<EventDetails> eventList = new ArrayList<EventDetails>();
    private RecyclerView.Adapter mAdapter;





    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }
// String keyword, category, distance, unit, lat, lng
    public void useVolley(String keyword_val, String category_val, String distance_val, String unit_val){
        mQueue = Volley.newRequestQueue(getContext());
        //String url = "https://yogoslavia.wl.r.appspot.com/getResults?keyword=Justin%20Bieber&category=all&distance=10&unit=miles&location_check=here&location_text=&lat=34.0030&lng=-118.2863";
        String url = "https://yogoslavia.wl.r.appspot.com/getResults?keyword="+keyword_val+"&category="+category_val+"&distance="+distance_val+"&unit="+unit_val+"&lat="+lat+"&lng="+lng;
        System.out.println("Use Volley Called"+url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("volley_response"+response);
                Intent intent = new Intent(getActivity(), DisplayResults.class);
                intent.putExtra("searchResults",  response.toString());
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    public void useVolley_with_location(String keyword,String category, String distance, String unit, String location){
        mQueue = Volley.newRequestQueue(getContext());
        String url = "https://yogoslavia.wl.r.appspot.com/getLocation?location="+location;
        System.out.println("Use Volley Called");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("volley_location_resposnce"+response);
                try {
                    lat = Double.parseDouble(response.getString("latitude"));
                    lng = Double.parseDouble(response.getString("longitude"));
                    useVolley(keyword, category, distance, unit);// String keyword, category, distance, unit, lat, lng

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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode ==100 && (grantResults.length>0)&&(grantResults[0]+grantResults[1] ==PackageManager.PERMISSION_GRANTED)){
            getLocation();
        }else {
            System.out.println("location validation"+requestCode);//+ requestCode+grantResults.length+grantResults[0]+grantResults[1]+PackageManager.PERMISSION_GRANTED);
        }
    }
    public void getLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onComplete(@NonNull @NotNull Task<Location> task) {

                    Location location = task.getResult();
                    if (location != null) {
                        System.out.println(location.getLatitude());
                        lat = location.getLatitude();
                        System.out.println(location.getLongitude());
                        lng = location.getLongitude();

                    } else {
                        LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(1000)
                                .setFastestInterval(1000).setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {

                                Location location2 = locationResult.getLastLocation();
                                System.out.println(location2.getLatitude());
                                lat = location.getLatitude();
                                System.out.println(location2.getLongitude());
                                lng = location.getLongitude();

                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        }else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (getArguments().getInt(ARG_SECTION_NUMBER)==1){
            getLocationPermission();
            View rootView = inflater.inflate(R.layout.fragment_search,container, false);
            search = rootView.findViewById(R.id.search);
            keyword = rootView.findViewById(R.id.keyword);
            location = rootView.findViewById(R.id.location);
            other = rootView.findViewById(R.id.other);
            current = rootView.findViewById(R.id.current);
            category = rootView.findViewById(R.id.category);
            distance =  rootView.findViewById(R.id.distance);
            unit = rootView.findViewById(R.id.unit);
            System.out.println("hello on create view called");
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                    System.out.println("button works");
                    boolean valid = true;
                    String location_val = location.getText().toString().trim();
                    String keyword_val = keyword.getText().toString().trim();
                    String category_val = category.getSelectedItem().toString();
                    int spinner_pos = category.getSelectedItemPosition();
                    String[] SegmentIDs = getResources().getStringArray(R.array.categoryValues);
                    String category_val_asli = String.valueOf(SegmentIDs[spinner_pos]);
                    System.out.println(category);
                    System.out.println("category val "+category_val);
                    String distance_val = distance.getText().toString().trim();
                    System.out.println("distance val"+distance_val);
                    String unit_val = unit.getSelectedItem().toString();
                    spinner_pos = unit.getSelectedItemPosition();
                    SegmentIDs = getResources().getStringArray(R.array.unitsValues);
                    String unit_val_asli = String.valueOf(SegmentIDs[spinner_pos]);
                    System.out.println("unit val"+unit_val);
                    if(keyword_val.isEmpty()){
                        valid = false;
                        keyword.setError("Please enter mandatory field");
                        if(other.isChecked() && location_val.isEmpty()){
                            location.setError("Please enter mandatory field");
                        }
                        else {
                            location.setError(null);
                        }
                    }
                    else{
                        keyword.setError(null);
                    }

                    if(valid) {
                        if (other.isChecked()){
                            System.out.println("checking for user specified location");
                            useVolley_with_location(keyword_val, category_val_asli,distance_val,unit_val_asli,location_val);
                        }
                        else {
                            useVolley(keyword_val, category_val_asli, distance_val, unit_val_asli);// String keyword, category, distance, unit, lat, lng
                        }
                    }
                }
            });
            clear = rootView.findViewById(R.id.clear);
            System.out.println("hello on create view called");
            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    keyword.setError(null);
                    location.setError(null);
                    keyword.setText("");
                    location.setText("");
                    distance.setText("10");
                    category.setSelection(0);
                    unit.setSelection(0);
                    current.setChecked(true);
                    location.setText("");
                    location.setEnabled(false);

                }
            });
            other.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    location.setEnabled(true);
                }
            });
            current.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    location.setEnabled(false);
                }
            });
            category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    Object item = parent.getItemAtPosition(pos);
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            return rootView;
        }
//        else if (getArguments().getInt(ARG_SECTION_NUMBER)==2){
//            View rootView = inflater.inflate(R.layout.fragment_favourites,container, false);
//            sharedPreferences = getContext().getSharedPreferences("favorite", Context.MODE_PRIVATE);editor = sharedPreferences.edit();
//            JSONObject jsonobject;
//            recyclerView = (RecyclerView) rootView.findViewById(R.id.lv_fav_list);
//            layoutManager = new LinearLayoutManager(getContext());
//            recyclerView.setLayoutManager(layoutManager);
//            try{
//                Map<String, ?> allEntries = sharedPreferences.getAll();
//                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
//                    jsonobject = new JSONObject(entry.getValue().toString());
//                    String date = jsonobject.getString("Date");
//                    String event_name = jsonobject.getString("EventName");
//                    String category = jsonobject.getString("Category");
//                    String venue = jsonobject.getString("Venue");
//                    String event_id = jsonobject.getString("EventId");
//                    EventDetails item = new EventDetails(date, event_name, event_id, category, venue);
//                    eventList.add(item);
//                }
//                System.out.println(eventList);
//                mAdapter = new RecyclerViewAdapterFaves(eventList,getContext());
//                recyclerView.setAdapter(mAdapter);
//                mAdapter.notifyDataSetChanged();
////                    Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//
//            return rootView;
//        }

        final TextView textView = binding.sectionLabel;
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}