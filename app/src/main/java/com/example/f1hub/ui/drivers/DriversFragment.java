package com.example.f1hub.ui.drivers;

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
import com.example.f1hub.data.repository.FavouritesRepository;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DriversFragment extends Fragment {

    private DriversViewModel viewModel;
    private DriverAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drivers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recycler = view.findViewById(R.id.recycler_drivers);
        ProgressBar progress  = view.findViewById(R.id.progress);
        TextView tvError      = view.findViewById(R.id.tv_error);

        viewModel = new ViewModelProvider(this).get(DriversViewModel.class);

        adapter = new DriverAdapter(driver -> {
            viewModel.selectDriver(driver);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.drivers_container, new DriverDetailFragment())
                    .addToBackStack(null)
                    .commit();
        });

        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setAdapter(adapter);

        progress.setVisibility(View.VISIBLE);
        viewModel.getDrivers().observe(getViewLifecycleOwner(), drivers -> {
            progress.setVisibility(View.GONE);
            if (drivers != null && !drivers.isEmpty()) {
                adapter.submitList(drivers);
            } else {
                tvError.setText("Could not load drivers");
                tvError.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getHeadshotMap().observe(getViewLifecycleOwner(),
                map -> adapter.setHeadshotMap(map));

        FavouritesRepository.getInstance(requireContext())
                .getDriverFavourites()
                .observe(getViewLifecycleOwner(), favs -> {
                    Set<String> ids = favs != null
                            ? favs.stream().map(e -> e.id).collect(Collectors.toCollection(HashSet::new))
                            : new HashSet<>();
                    adapter.setFavouriteIds(ids);
                });
    }
}
