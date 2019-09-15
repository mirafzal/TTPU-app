package uz.mirafzal.ttpuapp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;

import javax.annotation.Nullable;

import uz.mirafzal.ttpuapp.R;
import uz.mirafzal.ttpuapp.adapters.WeekAdapter;
import uz.mirafzal.ttpuapp.model.Week;

import static java.util.Objects.requireNonNull;
import static uz.mirafzal.ttpuapp.activities.MainActivity.KEY_GROUP;

public class FirestoreHelper {

    private static final String TAG = "mTag";


    public static void setAdapter(final Activity activity, final String day){

        final String [] fromTime = activity.getResources().getStringArray(R.array.fromtime);
        final String [] toTime = activity.getResources().getStringArray(R.array.totime);

        final String [] fromTimePrep = activity.getResources().getStringArray(R.array.fromtimeprep);
        final String [] toTimePrep = activity.getResources().getStringArray(R.array.totimeprep);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final String group = requireNonNull(activity).getPreferences(Context.MODE_PRIVATE).getString(KEY_GROUP, "G1-19");

        CollectionReference collectionReference = db.collection("TimeTable").document(requireNonNull(group)).collection(day);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                queryDocumentSnapshots.getDocumentChanges();
            }
        });

        collectionReference.get(Source.CACHE)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Week> weekList = new ArrayList<>();
                        ListView listView;
                        switch (day) {
                            case "Monday":
                                listView = activity.findViewById(R.id.mondayList);
                                break;
                            case "Tuesday":
                                listView = activity.findViewById(R.id.tuesdayList);
                                break;
                            case "Wednesday":
                                listView = activity.findViewById(R.id.wednesdayList);
                                break;
                            case "Thursday":
                                listView = activity.findViewById(R.id.thursdayList);
                                break;
                            case "Friday":
                                listView = activity.findViewById(R.id.fridayList);
                                break;
                            case "Saturday":
                                listView = activity.findViewById(R.id.saturdayList);
                                break;
                            default:
                                listView = null;
                        }
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : requireNonNull(task.getResult())) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Week week = new Week();
                                week.setSubject(requireNonNull(document.get("subject")).toString());
                                week.setTeacher(requireNonNull(document.get("teacher")).toString());
                                week.setRoom(requireNonNull(document.get("room")).toString());
                                if (group.contains("G")) {
                                    week.setFromTime(fromTimePrep[i]);
                                    week.setToTime(toTimePrep[i]);
                                } else {
                                    week.setFromTime(fromTime[i]);
                                    week.setToTime(toTime[i]);
                                }

                                if (week.getTeacher().trim().equals("")) {
                                    week.setColor(Color.WHITE);
                                } else {
                                    week.setColor(Color.parseColor("#64B5F6"));
                                }
                                i++;
                                weekList.add(week);
                            }
                            WeekAdapter adapter = new WeekAdapter(activity, listView, R.layout.listview_week_adapter, weekList);
                            requireNonNull(listView).setAdapter(adapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
