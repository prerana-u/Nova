package com.example.project;

import android.app.Fragment;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class level_info_fragment extends Fragment {
    ImageButton bb;
    public level_info_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_level_info_fragment, container, false);
    }

    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bb = (ImageButton) view.findViewById(R.id.crossb);

        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardView cv = getActivity().findViewById(R.id.cardv);
                cv.setVisibility(View.INVISIBLE);
            }
        });

    }
}