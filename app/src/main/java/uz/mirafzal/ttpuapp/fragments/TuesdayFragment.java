package uz.mirafzal.ttpuapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uz.mirafzal.ttpuapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TuesdayFragment extends Fragment {


    public TuesdayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tuesday, container, false);
    }

}