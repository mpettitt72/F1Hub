package com.example.f1hub.ui.favourites;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.f1hub.R;
import com.example.f1hub.data.repository.F1Repository;
import com.example.f1hub.data.repository.FavouritesRepository;

public class FavouritesFragment extends Fragment {

    private FavouritesRepository favRepo;
    private DriverFavAdapter driverAdapter;
    private FavouriteAdapter teamAdapter;
    private TextView tvEmpty, tabDrivers, tabTeams;
    private RecyclerView recycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        favRepo    = FavouritesRepository.getInstance(requireContext());
        tvEmpty    = view.findViewById(R.id.tv_empty);
        tabDrivers = view.findViewById(R.id.tab_drivers);
        tabTeams   = view.findViewById(R.id.tab_teams);
        recycler   = view.findViewById(R.id.recycler_favourites);

        driverAdapter = new DriverFavAdapter(entity -> favRepo.remove(entity));
        teamAdapter   = new FavouriteAdapter(entity -> favRepo.remove(entity));

        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        F1Repository.getInstance().getAllHeadshots()
                .observe(getViewLifecycleOwner(), map -> driverAdapter.setHeadshotMap(map));

        tabDrivers.setOnClickListener(v -> switchTab(true));
        tabTeams.setOnClickListener(v -> switchTab(false));

        switchTab(true);
    }

    private void switchTab(boolean drivers) {
        tabDrivers.setBackgroundResource(drivers ? R.drawable.bg_tab_selected : android.R.color.transparent);
        tabDrivers.setTextColor(drivers ? Color.WHITE : requireContext().getColor(R.color.f1_muted));
        tabTeams.setBackgroundResource(drivers ? android.R.color.transparent : R.drawable.bg_tab_selected);
        tabTeams.setTextColor(drivers ? requireContext().getColor(R.color.f1_muted) : Color.WHITE);

        if (drivers) {
            recycler.setAdapter(driverAdapter);
            favRepo.getDriverFavourites().observe(getViewLifecycleOwner(), list -> {
                driverAdapter.submitList(list);
                tvEmpty.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
                tvEmpty.setText("No favourite drivers yet");
            });
        } else {
            recycler.setAdapter(teamAdapter);
            favRepo.getConstructorFavourites().observe(getViewLifecycleOwner(), list -> {
                teamAdapter.submitList(list);
                tvEmpty.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
                tvEmpty.setText("No favourite teams yet");
            });
        }
    }
}
