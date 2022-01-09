package com.example.ticketing;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link favourites#newInstance} factory method to
 * create an instance of this fragment.
 */
public class favourites extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    View rootView;


    private RecyclerView.Adapter mAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public favourites() {
        // Required empty public constructor
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            List<EventDetails> eventList = new ArrayList<EventDetails>();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            System.out.println("i'm creating this view agaaaaaainnnn");
            sharedPreferences = getContext().getSharedPreferences("favorite", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            JSONObject jsonobject;
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            try{
                Map<String, ?> allEntries = sharedPreferences.getAll();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    jsonobject = new JSONObject(entry.getValue().toString());
                    String date = jsonobject.getString("Date");
                    String event_name = jsonobject.getString("EventName");
                    String category = jsonobject.getString("Category");
                    String venue = jsonobject.getString("Venue");
                    String event_id = jsonobject.getString("EventId");
                    EventDetails item = new EventDetails(date, event_name, event_id, category, venue);
                    eventList.add(item);
                }
                System.out.println(eventList);
                mAdapter = new RecyclerViewAdapterFaves(eventList,getContext());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
//                    Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            }catch (Exception e){
                e.printStackTrace();
            }
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.detach(YourFragment.this).attach(YourFragment.this).commit();
            System.out.println("refreshing my faves fragment");
            if (eventList.size() == 0){
                View no_results =  rootView.findViewById(R.id.tv_no_results3);
                no_results.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment favourites.
     */
    // TODO: Rename and change types and number of parameters
    public static favourites newInstance(String param1, String param2) {
        favourites fragment = new favourites();
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
        //return inflater.inflate(R.layout.fragment_favourites, container, false);
//        System.out.println("i'm creating this view agaaaaaainnnn");
       // inflaterobj = inflater;
        rootView = inflater.inflate(R.layout.fragment_favourites,container, false);

//        sharedPreferences = getContext().getSharedPreferences("favorite", Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//        JSONObject jsonobject;
        recyclerView = (RecyclerView) rootView.findViewById(R.id.lv_fav_list);
//        layoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);
//        try{
//            Map<String, ?> allEntries = sharedPreferences.getAll();
//            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
//                jsonobject = new JSONObject(entry.getValue().toString());
//                String date = jsonobject.getString("Date");
//                String event_name = jsonobject.getString("EventName");
//                String category = jsonobject.getString("Category");
//                String venue = jsonobject.getString("Venue");
//                String event_id = jsonobject.getString("EventId");
//                EventDetails item = new EventDetails(date, event_name, event_id, category, venue);
//                eventList.add(item);
//            }
//            System.out.println(eventList);
//            mAdapter = new RecyclerViewAdapterFaves(eventList,getContext());
//            recyclerView.setAdapter(mAdapter);
//            mAdapter.notifyDataSetChanged();
////                    Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
//        }catch (Exception e){
//            e.printStackTrace();
//        }


        return rootView;
    }
}