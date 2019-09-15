package uz.mirafzal.ttpuapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import uz.mirafzal.ttpuapp.R;
import uz.mirafzal.ttpuapp.adapters.FragmentsTabAdapter;
import uz.mirafzal.ttpuapp.fragments.FridayFragment;
import uz.mirafzal.ttpuapp.fragments.MondayFragment;
import uz.mirafzal.ttpuapp.fragments.SaturdayFragment;
import uz.mirafzal.ttpuapp.fragments.ThursdayFragment;
import uz.mirafzal.ttpuapp.fragments.TuesdayFragment;
import uz.mirafzal.ttpuapp.fragments.WednesdayFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentsTabAdapter adapter;
    private ViewPager viewPager;
    private AppCompatSpinner spinnerCourse;
    private AppCompatSpinner spinnerGroup;

    public static final String TAG = "mTag1";

    public static final String KEY_COURSE = "course";
    public static final String KEY_GROUP = "group";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPreferences(MODE_PRIVATE).edit()
                .putString(KEY_GROUP, "G1-19")
                .putInt(KEY_COURSE, 0)
                .apply();

        setupNavigation();
        setupFragments();
        setupSpinnerCourse();
        setupSpinnerGroup();
    }

    private void setupSpinnerCourse() {
        spinnerCourse = findViewById(R.id.spinnerCourse);

        ArrayAdapter<CharSequence> adapterCourse = ArrayAdapter.createFromResource(this,
                R.array.courses, R.layout.spinner_item);
        adapterCourse.setDropDownViewResource(R.layout.spinner_item);
        spinnerCourse.setAdapter(adapterCourse);
        spinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getPreferences(MODE_PRIVATE).edit().putInt(KEY_GROUP, i).apply();
                setupSpinnerGroup();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupSpinnerGroup() {

        spinnerGroup = findViewById(R.id.spinnerGroup);

        findViewById(R.id.drawer_layout).invalidate();

        int course = getPreferences(MODE_PRIVATE).getInt(KEY_COURSE, 0);

        int spinnerArray;

        switch (course) {
            case 0:
                spinnerArray = R.array.groupPrep;
                break;
            case 1:
                spinnerArray = R.array.groupFirst;
                break;
            case 2:
                spinnerArray = R.array.groupSecond;
                break;
            case 3:
                spinnerArray = R.array.groupThird;
                break;
            default:
                spinnerArray = R.array.groupPrep;
                break;
        }

        ArrayAdapter<CharSequence> adapterGroup = ArrayAdapter.createFromResource(this,
                spinnerArray, R.layout.spinner_item);
        adapterGroup.setDropDownViewResource(R.layout.spinner_item);
        spinnerGroup.setAdapter(adapterGroup);
        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupNavigation() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupFragments() {
        adapter = new FragmentsTabAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        adapter.addFragment(new MondayFragment(), getResources().getString(R.string.monday));
        adapter.addFragment(new TuesdayFragment(), getResources().getString(R.string.tuesday));
        adapter.addFragment(new WednesdayFragment(), getResources().getString(R.string.wednesday));
        adapter.addFragment(new ThursdayFragment(), getResources().getString(R.string.thursday));
        adapter.addFragment(new FridayFragment(), getResources().getString(R.string.friday));
        adapter.addFragment(new SaturdayFragment(), getResources().getString(R.string.saturday));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(day == 1 ? 6 : day - 2, true);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initEmptySchedule() {
        final String[] groups = {"G1-19", "G2-19", "G3-19", "G4-19", "G5-19", "G6-19", "G7-19", "G8-19", "G9-19", "G10-18",
                "G11-18", "G12-18", "G13-18", "G14-19", "G15-19", "G16-19", "G17-19", "G18-19", "G19-19", "G20-19",
                "ME1-18", "ME2-18", "ME3-18", "ME4-18", "ME5-18", "ME6-18", "CIE1-18", "CIE2-18", "IT1-18", "IT2-18", "IT3-18", "IT4-18",
                "ME1-17", "ME2-17", "ME3-17", "ME4-17", "E-17", "CIE-17", "IT-17",
                "ME1-16", "ME2-16", "ME3-16", "ME4-16", "E-16", "CIE-16", "IT-16"};

        final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = db.collection("TimeTable");

        Map<String, Object> lesson = new HashMap<>();
        lesson.put("subject", "");
        lesson.put("teacher", "");
        lesson.put("room", "");

        for (String group : groups) {
            DocumentReference documentReference = collectionReference.document(group);
            for (String day : days) {
                for (int k = 1; k <= 5; k++) {
                    documentReference.collection(day).document(k + "")
                            .set(lesson)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final NavigationView navigationView = findViewById(R.id.nav_view);
        switch (item.getItemId()) {
//            case R.id.schoolwebsitemenu:
//                String schoolWebsite = "polito.uz";//PreferenceManager.getDefaultSharedPreferences(this).getString(SettingsActivity.KEY_SCHOOL_WEBSITE_SETTING, null);
//                if (!TextUtils.isEmpty(schoolWebsite)) {
//                    //openUrlInChromeCustomTab(getApplicationContext(), schoolWebsite);
////                    Snackbar.make(navigationView, "GG", Snackbar.LENGTH_SHORT).show();
//                } else {
////                    Snackbar.make(navigationView, R.string.school_website_snackbar, Snackbar.LENGTH_SHORT).show();
//                }
//                return true;
//            case R.id.exams:
//                Intent exams = new Intent(MainActivity.this, ExamsActivity.class);
//                startActivity(exams);
//                return true;
//            case R.id.teachers:
//                Intent teacher = new Intent(MainActivity.this, TeachersActivity.class);
//                startActivity(teacher);
//                return true;
//            case R.id.homework:
//                Intent homework = new Intent(MainActivity.this, HomeworksActivity.class);
//                startActivity(homework);
//                return true;
//            case R.id.notes:
//                Intent note = new Intent(MainActivity.this, NotesActivity.class);
//                startActivity(note);
//                return true;
//            case R.id.settings:
//                Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
//                startActivity(settings);
//                return true;
            default:
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
        }
    }

}
