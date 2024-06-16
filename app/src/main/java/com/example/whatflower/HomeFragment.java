package com.example.whatflower;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import  org.tensorflow.lite.DataType;
import java.io.*;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    Button uploadBtn, captureBtn;
    ImageView imageView;
    TextView predictionTextView;
    public HomeFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the  layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
