package com.example.f1hub.ui.standings;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.f1hub.R;

public class StandingsFragment extends Fragment {

    private StandingsViewModel viewModel;
    private DriverStandingsAdapter driverAdapter;
    private ConstructorStandingsAdapter constructorAdapter;
    private RecyclerView recycler;
    private ProgressBar progress;
    private TextView tvError;
    private TextView tabDrivers, tabConstructors;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_standings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recycler        = view.findViewById(R.id.recycler_standings);
        progress        = view.findViewById(R.id.progress);
        tvError         = view.findViewById(R.id.tv_error);
        tabDrivers      = view.findViewById(R.id.tab_drivers);
        tabConstructors = view.findViewById(R.id.tab_constructors);

        viewModel = new ViewModelProvider(this).get(StandingsViewModel.class);

        driverAdapter = new DriverStandingsAdapter();
        constructorAdapter = new ConstructorStandingsAdapter(cs -> {
            viewModel.selectConstructor(cs);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.standings_container, new ConstructorDetailFragment())
                    .addToBackStack(null)
                    .commit();
        });

        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        tabDrivers.setOnClickListener(v -> switchTab(true));
        tabConstructors.setOnClickListener(v -> switchTab(false));

        switchTab(true);
    }

    private void switchTab(boolean drivers) {
        tabDrivers.setBackgroundResource(drivers ? R.drawable.bg_tab_selected : android.R.color.transparent);
        tabDrivers.setTextColor(drivers ? Color.WHITE : requireContext().getColor(R.color.f1_muted));
        tabConstructors.setBackgroundResource(drivers ? android.R.color.transparent : R.drawable.bg_tab_selected);
        tabConstructors.setTextColor(drivers ? requireContext().getColor(R.color.f1_muted) : Color.WHITE);

        progress.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);

        if (drivers) {
            recycler.setAdapter(driverAdapter);
            viewModel.getDriverStandings().observe(getViewLifecycleOwner(), standings -> {
                progress.setVisibility(View.GONE);
                if (standings != null && !standings.isEmpty()) {
                    driverAdapter.submitList(standings);
                } else {
                    tvError.setText("Could not load standings");
                    tvError.setVisibility(View.VISIBLE);
                }
            });
        } else {
            recycler.setAdapter(constructorAdapter);
            viewModel.getConstructorStandings().observe(getViewLifecycleOwner(), standings -> {
                progress.setVisibility(View.GONE);
                if (standings != null && !standings.isEmpty()) {
                    constructorAdapter.submitList(standings);
                } else {
                    tvError.setText("Could not load standings");
                    tvError.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
