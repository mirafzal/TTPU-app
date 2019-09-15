package uz.mirafzal.ttpuapp.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.util.Objects;

import uz.mirafzal.ttpuapp.R;
import uz.mirafzal.ttpuapp.utils.FirestoreHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class WednesdayFragment extends Fragment {

    private static final String DAY = "Wednesday";

    public WednesdayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_wednesday, container, false);

        FirestoreHelper.setAdapter(Objects.requireNonNull(getActivity()), DAY);

        return view;
    }

}
