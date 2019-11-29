package uz.mirafzal.ttpuapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.List;

import uz.mirafzal.ttpuapp.R;
import uz.mirafzal.ttpuapp.adapters.FragmentsTabAdapter;
import uz.mirafzal.ttpuapp.fragments.TimetablesFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentsTabAdapter adapter;
    private ViewPager viewPager;
    private AppCompatSpinner spinnerCourse;
    private AppCompatSpinner spinnerGroup;

    public static final String TAG = "mTag1";

    public static final String KEY_COURSE = "uz.mirafzal.ttpuapp.course";
    public static final String KEY_GROUP = "uz.mirafzal.ttpuapp.group";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        int course = getPreferences(MODE_PRIVATE).getInt(KEY_COURSE, 0);
        spinnerCourse.setSelection(course);
        spinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getPreferences(MODE_PRIVATE).edit().putInt(KEY_COURSE, i).apply();
                Log.d(TAG, i + " - course");
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

        String group = getPreferences(MODE_PRIVATE).getString(KEY_GROUP, "G1-19");

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
        spinnerGroup.setSelection(adapterGroup.getPosition(group));
        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getPreferences(MODE_PRIVATE).edit().putString(KEY_GROUP, spinnerGroup.getSelectedItem().toString()).apply();
                Log.d(TAG, spinnerGroup.getSelectedItem().toString() + " - group");
                // Reload current fragment
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                for (Fragment fragment : fragments) {
                    fragmentTransaction
                            .detach(fragment)
                            .attach(fragment);
                }
                fragmentTransaction.commit();
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
        adapter.addFragment(TimetablesFragment.newInstance("Monday"), getResources().getString(R.string.monday));
        adapter.addFragment(TimetablesFragment.newInstance("Tuesday"), getResources().getString(R.string.tuesday));
        adapter.addFragment(TimetablesFragment.newInstance("Wednesday"), getResources().getString(R.string.wednesday));
        adapter.addFragment(TimetablesFragment.newInstance("Thursday"), getResources().getString(R.string.thursday));
        adapter.addFragment(TimetablesFragment.newInstance("Friday"), getResources().getString(R.string.friday));
        adapter.addFragment(TimetablesFragment.newInstance("Saturday"), getResources().getString(R.string.saturday));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(day == 1 ? 0 : day - 2, true);
        tabLayout.setupWithViewPager(viewPager);
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
            case R.id.exams:
            case R.id.teachers:
                Toast.makeText(getApplicationContext(), "Coming soon", Toast.LENGTH_SHORT).show();
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.settings:
                Toast.makeText(getApplicationContext(), "Currently no settings", Toast.LENGTH_SHORT).show();
                drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            default:
                drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
        }
    }

}
