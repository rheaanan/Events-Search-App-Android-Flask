package com.example.ticketing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class search extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button toastBtn;


    public search() {
        System.out.println("hello search constructor called");

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment search.
     */
    // TODO: Rename and change types and number of parameters
    public static search newInstance(String param1, String param2) {
        search fragment = new search();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("hello oncreate called");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }
//    public void checkButton(View v){
//        radioGroup = v.findViewById(R.id.radioGroup);
//        textView = v.findViewById(R.id.radio_value);
//        System.out.println("hello Rhea 2");
//        int radioId = radioGroup.getCheckedRadioButtonId();
//        radioButton = v.findViewById(radioId);
//        System.out.println("Getting Button Value"+ radioButton);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        toastBtn = view.findViewById(R.id.search);
        System.out.println("hello on create view called");
        toastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
                System.out.println("button works");
            }
        });


        return view;



    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("hello starting fragment now");
    }
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//
//        Button buttonApply = getView().findViewById(R.id.search);
//        System.out.println("Reaching here");
//        buttonApply.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                checkButton(v);
//
//            }
//        });
//    }

}