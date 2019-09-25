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
import static uz.mirafzal.ttpuapp.activities.MainActivity.KEY_COURSE;
import static uz.mirafzal.ttpuapp.activities.MainActivity.KEY_GROUP;

public class FirestoreHelper {

    private static final String TAG = "mTag";


    public static void setAdapterGG(final Activity activity, final String day) {

        final String[] fromTime = activity.getResources().getStringArray(R.array.fromtime);
        final String[] toTime = activity.getResources().getStringArray(R.array.totime);

        final String[] fromTimePrep = activity.getResources().getStringArray(R.array.fromtimeprep);
        final String[] toTimePrep = activity.getResources().getStringArray(R.array.totimeprep);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final String group = requireNonNull(activity).getPreferences(Context.MODE_PRIVATE).getString(KEY_GROUP, "G1-19");

        CollectionReference collectionReference = db.collection("TimeTable").document(requireNonNull(group)).collection(day);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                requireNonNull(queryDocumentSnapshots).getDocumentChanges();
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

    public static void setAdapter(final Activity activity, final String day) {

        final String[] fromTime = activity.getResources().getStringArray(R.array.fromtime);
        final String[] toTime = activity.getResources().getStringArray(R.array.totime);

        final String[] fromTimePrep = activity.getResources().getStringArray(R.array.fromtimeprep);
        final String[] toTimePrep = activity.getResources().getStringArray(R.array.totimeprep);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final String group = requireNonNull(activity).getPreferences(Context.MODE_PRIVATE).getString(KEY_GROUP, "G1-19");
//        final String group = "ME1";

        final int course = requireNonNull(activity).getPreferences(Context.MODE_PRIVATE).getInt(KEY_COURSE, 0);
//        final int course = 2;

        CollectionReference collectionReference = db.collection(String.valueOf(course));

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                requireNonNull(queryDocumentSnapshots).getDocumentChanges();
            }
        });

        collectionReference
                .whereEqualTo("Days", day)
//                .whereEqualTo("Lessons", "1")
                .get(Source.CACHE)
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
                            Log.d(TAG, group);
                            for (QueryDocumentSnapshot document : requireNonNull(task.getResult())) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                int lesson = Integer.parseInt(requireNonNull(document.get("Lessons")).toString()) - 1;
                                if (document.get(group) != null) {
                                    String text = document.get(group).toString().trim();
                                    if (text.equals("Tutor Hours(Conference Hall)")) {
                                        Week week = new Week();
                                        week.setSubject("Tutors Hours");
                                        week.setTeacher("Сlassroom teacher");
                                        week.setRoom("Conference Hall");
                                        if (group.contains("G")) {
                                            week.setFromTime(fromTimePrep[lesson]);
                                            week.setToTime(toTimePrep[lesson]);
                                        } else {
                                            week.setFromTime(fromTime[lesson]);
                                            week.setToTime(toTime[lesson]);
                                        }

                                        if (week.getTeacher().trim().equals("")) {
                                            week.setColor(Color.WHITE);
                                        } else {
                                            week.setColor(Color.parseColor("#64B5F6"));
                                        }
                                        weekList.add(week);
                                    } else if (text.equals("Fund. of Thermodynamics and heat transfer / Lecture / Green hall/ Prof. P. Leone")) {
                                        Week week = new Week();
                                        week.setSubject("Fund. of Thermodynamics and heat transfer");
                                        week.setTeacher("Prof. P. Leone");
                                        week.setRoom("Green hall");
                                        if (group.contains("G")) {
                                            week.setFromTime(fromTimePrep[lesson]);
                                            week.setToTime(toTimePrep[lesson]);
                                        } else {
                                            week.setFromTime(fromTime[lesson]);
                                            week.setToTime(toTime[lesson]);
                                        }

                                        if (week.getTeacher().trim().equals("")) {
                                            week.setColor(Color.WHITE);
                                        } else {
                                            week.setColor(Color.parseColor("#64B5F6"));
                                        }
                                        weekList.add(week);
                                    } else if (text.equals("Fund. of Thermodynamics and heat transfer / Lecture / Blue hall/ Prof. P. Leone")) {
                                        Week week = new Week();
                                        week.setSubject("Fund. of Thermodynamics and heat transfer");
                                        week.setTeacher("Prof. P. Leone");
                                        week.setRoom("Blue hall");
                                        if (group.contains("G")) {
                                            week.setFromTime(fromTimePrep[lesson]);
                                            week.setToTime(toTimePrep[lesson]);
                                        } else {
                                            week.setFromTime(fromTime[lesson]);
                                            week.setToTime(toTime[lesson]);
                                        }

                                        if (week.getTeacher().trim().equals("")) {
                                            week.setColor(Color.WHITE);
                                        } else {
                                            week.setColor(Color.parseColor("#64B5F6"));
                                        }
                                        weekList.add(week);
                                    } else {
                                        int slashIndex = text.indexOf("/");
                                        String subject = text.substring(0, slashIndex).trim();
                                        Log.d(TAG, subject);
                                        String secondPart = text.substring(slashIndex + 1).trim();
                                        int secondSlashIndex = secondPart.indexOf("/");
                                        String room;
                                        if (secondSlashIndex != -1) {
                                            room = secondPart.substring(0, secondSlashIndex).trim();
                                        } else {
                                            room = secondPart.substring(0,secondPart.indexOf("."));
                                        }
                                        Log.d(TAG, room);
                                        String teacher = secondPart.substring(secondSlashIndex + 1).trim();
                                        Log.d(TAG, teacher);

                                        Week week = new Week();
                                        week.setSubject(subject);
                                        week.setTeacher(teacher);
                                        week.setRoom(room);
                                        if (group.contains("G")) {
                                            week.setFromTime(fromTimePrep[lesson]);
                                            week.setToTime(toTimePrep[lesson]);
                                        } else {
                                            week.setFromTime(fromTime[lesson]);
                                            week.setToTime(toTime[lesson]);
                                        }

                                        if (week.getTeacher().trim().equals("")) {
                                            week.setColor(Color.WHITE);
                                        } else {
                                            week.setColor(Color.parseColor("#64B5F6"));
                                        }
                                        weekList.add(week);
                                    }
                                } else {
                                    Week week = new Week();
                                    week.setSubject("");
                                    week.setTeacher("");
                                    week.setRoom("");
                                    if (group.contains("G")) {
                                        week.setFromTime(fromTimePrep[lesson]);
                                        week.setToTime(toTimePrep[lesson]);
                                    } else {
                                        week.setFromTime(fromTime[lesson]);
                                        week.setToTime(toTime[lesson]);
                                    }

                                    if (week.getTeacher().trim().equals("")) {
                                        week.setColor(Color.WHITE);
                                    } else {
                                        week.setColor(Color.parseColor("#64B5F6"));
                                    }
                                    weekList.add(week);
                                }
                                WeekAdapter adapter = new WeekAdapter(activity, listView, R.layout.listview_week_adapter, weekList);
                                requireNonNull(listView).setAdapter(adapter);
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
