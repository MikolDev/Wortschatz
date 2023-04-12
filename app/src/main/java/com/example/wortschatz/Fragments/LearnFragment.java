package com.example.wortschatz.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wortschatz.MainActivity;
import com.example.wortschatz.R;

public class LearnFragment extends Fragment {
    MainActivity mainActivity;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_learn, container, false);
        mainActivity = (MainActivity) getActivity();
        return view;
    }
}
