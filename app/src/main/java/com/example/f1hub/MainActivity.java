package com.example.f1hub;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.f1hub.ui.drivers.DriversFragment;
import com.example.f1hub.ui.favourites.FavouritesFragment;
import com.example.f1hub.ui.home.HomeFragment;
import com.example.f1hub.ui.races.RacesFragment;
import com.example.f1hub.ui.settings.SettingsFragment;
import com.example.f1hub.ui.standings.StandingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationHelper.createChannel(this);
        requestNotificationPermission();

        bottomNav = findViewById(R.id.bottom_nav);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home)       return loadFragment(new HomeFragment());
            if (id == R.id.nav_races)      return loadFragment(new RacesFragment());
            if (id == R.id.nav_standings)  return loadFragment(new StandingsFragment());
            if (id == R.id.nav_drivers)    return loadFragment(new DriversFragment());
            if (id == R.id.nav_favourites) return loadFragment(new FavouritesFragment());
            return false;
        });

        findViewById(R.id.btn_settings).setOnClickListener(v -> openSettings());
    }

    private boolean loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        return true;
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    public void openSettings() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SettingsFragment())
                .addToBackStack(null)
                .commit();
    }
}
