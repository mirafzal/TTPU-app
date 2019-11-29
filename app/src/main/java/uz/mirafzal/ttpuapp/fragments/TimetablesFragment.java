package uz.mirafzal.ttpuapp.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import uz.mirafzal.ttpuapp.R;
import uz.mirafzal.ttpuapp.utils.FirestoreHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimetablesFragment extends Fragment {

    public static TimetablesFragment newInstance(String day) {
        TimetablesFragment fragment = new TimetablesFragment();
        Bundle arguments = new Bundle();
        arguments.putString("arg_day", day);
        fragment.setArguments(arguments);
        return fragment;
    }

    private String day;

    private TimetablesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        day = Objects.requireNonNull(getArguments()).getString("arg_day");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timetables, container, false);
        ListView listView = view.findViewById(R.id.listView);
        FirestoreHelper.setAdapter(requireActivity(), day, listView);

        return view;
    }

}
