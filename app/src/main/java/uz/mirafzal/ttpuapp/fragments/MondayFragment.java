package uz.mirafzal.ttpuapp.fragments;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uz.mirafzal.ttpuapp.R;
import uz.mirafzal.ttpuapp.adapters.WeekAdapter;
import uz.mirafzal.ttpuapp.model.Week;

/**
 * A simple {@link Fragment} subclass.
 */
public class MondayFragment extends Fragment {

    private ListView listView;
    private WeekAdapter adapter;

    public static final String TAG = "mTag";

    private String [] fromTime = {"8:30", "10:00", "11:30", "13:50", "15:20"};
    private String [] toTime = {"9:50", "11:20", "12:50", "15:10", "16:40"};

    public MondayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monday, container, false);
        listView = view.findViewById(R.id.mondaylist);

        final ArrayList<Week> weekList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Schedule/Second level IT/Monday")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Log.d(TAG, document.get("subject").toString());
                                Week week = new Week();
                                week.setSubject(Objects.requireNonNull(document.get("subject")).toString());
                                week.setTeacher(Objects.requireNonNull(document.get("teacher")).toString());
                                week.setRoom(Objects.requireNonNull(document.get("room")).toString());
                                week.setFromTime(fromTime[i]);
                                week.setToTime(toTime[i]);
                                if (week.getTeacher().trim().equals("")) {
                                    week.setColor(Color.WHITE);
                                } else {
                                    week.setColor(Color.parseColor("#64B5F6"));
                                }
                                i++;
                                weekList.add(week);
                            }
                            adapter = new WeekAdapter(getActivity(), listView, R.layout.listview_week_adapter, weekList);
                            listView.setAdapter(adapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return view;
    }


}
