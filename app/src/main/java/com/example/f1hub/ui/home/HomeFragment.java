package com.example.f1hub.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.f1hub.NotificationHelper;
import com.example.f1hub.R;
import com.example.f1hub.data.model.jolpica.Race;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class HomeFragment extends Fragment {

    private HomeViewModel viewModel;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable countdownRunnable;
    private LocalDateTime raceDateTime;

    private TextView tvDays, tvHours, tvMins;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        TextView tvRaceName    = view.findViewById(R.id.tv_next_race_name);
        TextView tvCircuit     = view.findViewById(R.id.tv_next_race_circuit);
        tvDays                 = view.findViewById(R.id.tv_days);
        tvHours                = view.findViewById(R.id.tv_hours);
        tvMins                 = view.findViewById(R.id.tv_mins);
        TextView tvLeaderName  = view.findViewById(R.id.tv_leader_name);
        TextView tvLeaderTeam  = view.findViewById(R.id.tv_leader_team);
        TextView tvLeaderPts   = view.findViewById(R.id.tv_leader_points);
        TextView tvConName     = view.findViewById(R.id.tv_constructor_name);
        TextView tvConPts      = view.findViewById(R.id.tv_constructor_points);

        viewModel.getNextRace().observe(getViewLifecycleOwner(), race -> {
            if (race == null) return;
            tvRaceName.setText(race.raceName);
            String location = race.circuit != null && race.circuit.location != null
                    ? race.circuit.location.locality + " • " + race.date
                    : race.date != null ? race.date : "";
            tvCircuit.setText(location);
            startCountdown(race);
            NotificationHelper.scheduleRaceNotification(requireContext(), race);
        });

        viewModel.getChampionshipLeader().observe(getViewLifecycleOwner(), ds -> {
            if (ds == null) return;
            if (ds.driver != null)
                tvLeaderName.setText(ds.driver.givenName + " " + ds.driver.familyName);
            if (ds.constructors != null && !ds.constructors.isEmpty())
                tvLeaderTeam.setText(ds.constructors.get(0).name);
            tvLeaderPts.setText(ds.points);
        });

        viewModel.getTopConstructor().observe(getViewLifecycleOwner(), cs -> {
            if (cs == null) return;
            if (cs.constructor != null) tvConName.setText(cs.constructor.name);
            tvConPts.setText(cs.points);
        });
    }

    private void startCountdown(Race race) {
        if (race.date == null) return;
        try {
            String timeStr = race.time != null ? race.time.replace("Z", "") : "14:00:00";
            raceDateTime = LocalDateTime.parse(race.date + "T" + timeStr);
        } catch (Exception e) {
            return;
        }

        countdownRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isAdded()) return;
                LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
                long totalSeconds = ChronoUnit.SECONDS.between(now, raceDateTime);
                if (totalSeconds <= 0) {
                    tvDays.setText("0");
                    tvHours.setText("0");
                    tvMins.setText("0");
                    return;
                }
                long days  = totalSeconds / 86400;
                long hours = (totalSeconds % 86400) / 3600;
                long mins  = (totalSeconds % 3600) / 60;
                tvDays.setText(String.valueOf(days));
                tvHours.setText(String.valueOf(hours));
                tvMins.setText(String.valueOf(mins));
                handler.postDelayed(this, 60_000);
            }
        };
        handler.post(countdownRunnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (countdownRunnable != null) handler.removeCallbacks(countdownRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (countdownRunnable != null) handler.post(countdownRunnable);
    }
}
