package uz.mirafzal.ttpuapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setupFragments();
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
        viewPager.setCurrentItem(day == 1 ? 6 : day-2, true);
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
            case R.id.schoolwebsitemenu:
                String schoolWebsite = "polito.uz";//PreferenceManager.getDefaultSharedPreferences(this).getString(SettingsActivity.KEY_SCHOOL_WEBSITE_SETTING, null);
                if(!TextUtils.isEmpty(schoolWebsite)) {
                    //openUrlInChromeCustomTab(getApplicationContext(), schoolWebsite);
                    Snackbar.make(navigationView, "GG", Snackbar.LENGTH_SHORT).show();
                } else {
//                    Snackbar.make(navigationView, R.string.school_website_snackbar, Snackbar.LENGTH_SHORT).show();
                }
                return true;
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
