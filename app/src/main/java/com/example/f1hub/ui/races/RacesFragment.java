package com.example.f1hub.ui.races;

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

public class RacesFragment extends Fragment {

    private RacesViewModel viewModel;
    private RaceAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_races, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recycler = view.findViewById(R.id.recycler_races);
        ProgressBar progress  = view.findViewById(R.id.progress);
        TextView tvError      = view.findViewById(R.id.tv_error);

        viewModel = new ViewModelProvider(this).get(RacesViewModel.class);

        adapter = new RaceAdapter(race -> {
            viewModel.selectRace(race);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.races_container, new RaceDetailFragment())
                    .addToBackStack(null)
                    .commit();
        });

        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setAdapter(adapter);

        progress.setVisibility(View.VISIBLE);
        viewModel.getRaces().observe(getViewLifecycleOwner(), races -> {
            progress.setVisibility(View.GONE);
            if (races != null && !races.isEmpty()) {
                adapter.submitList(races);
                tvError.setVisibility(View.GONE);
            } else {
                tvError.setText("Could not load schedule");
                tvError.setVisibility(View.VISIBLE);
            }
        });
    }
}
