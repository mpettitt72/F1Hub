package com.example.f1hub.ui.drivers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.f1hub.R;
import com.example.f1hub.data.db.FavouriteEntity;
import com.example.f1hub.data.model.jolpica.Driver;
import com.example.f1hub.data.repository.FavouritesRepository;

public class DriverDetailFragment extends Fragment {

    private FavouritesRepository favRepo;
    private Driver currentDriver;
    private String currentTeam;
    private boolean isFav = false;
    private ImageView btnBookmark;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        DriversViewModel viewModel = new ViewModelProvider(requireParentFragment())
                .get(DriversViewModel.class);

        favRepo    = FavouritesRepository.getInstance(requireContext());
        btnBookmark = view.findViewById(R.id.btn_bookmark);

        ImageView headshot = view.findViewById(R.id.iv_headshot);
        TextView tvName    = view.findViewById(R.id.tv_name);
        TextView tvTeam    = view.findViewById(R.id.tv_team);
        TextView tvNat     = view.findViewById(R.id.tv_nationality);
        TextView tvDob     = view.findViewById(R.id.tv_dob);
        TextView tvNumber  = view.findViewById(R.id.tv_number);

        view.findViewById(R.id.btn_back).setOnClickListener(v ->
                requireParentFragment().getChildFragmentManager().popBackStack());

        btnBookmark.setOnClickListener(v -> toggleFavourite());

        viewModel.getSelectedDriver().observe(getViewLifecycleOwner(), driver -> {
            if (driver == null) return;
            currentDriver = driver;
            tvName.setText(driver.givenName + " " + driver.familyName);
            tvNat.setText(driver.nationality != null ? driver.nationality : "—");
            tvDob.setText(driver.dateOfBirth != null ? driver.dateOfBirth : "—");
            tvNumber.setText(driver.permanentNumber != null ? driver.permanentNumber : "—");

            favRepo.isFavourite(driver.driverId, fav -> {
                isFav = fav;
                updateBookmarkTint();
            });
        });

        viewModel.getSelectedHeadshot().observe(getViewLifecycleOwner(), openF1Driver -> {
            if (openF1Driver == null) return;
            if (openF1Driver.teamName != null) {
                tvTeam.setText(openF1Driver.teamName);
                currentTeam = openF1Driver.teamName;
            }
            if (openF1Driver.headshotUrl != null) {
                Glide.with(headshot.getContext())
                        .load(openF1Driver.headshotUrl)
                        .placeholder(R.drawable.bg_headshot_placeholder)
                        .circleCrop()
                        .into(headshot);
            }
        });
    }

    private void toggleFavourite() {
        if (currentDriver == null) return;
        if (isFav) {
            FavouriteEntity e = new FavouriteEntity();
            e.id = currentDriver.driverId;
            favRepo.remove(e);
            isFav = false;
        } else {
            FavouriteEntity e = new FavouriteEntity();
            e.id              = currentDriver.driverId;
            e.type            = "DRIVER";
            e.name            = currentDriver.givenName + " " + currentDriver.familyName;
            e.subtitle        = currentTeam != null ? currentTeam : currentDriver.nationality;
            e.permanentNumber = currentDriver.permanentNumber;
            e.code            = currentDriver.code;
            favRepo.add(e);
            isFav = true;
        }
        updateBookmarkTint();
    }

    private void updateBookmarkTint() {
        if (btnBookmark == null || !isAdded()) return;
        int color = isFav
                ? ContextCompat.getColor(requireContext(), R.color.f1_red)
                : ContextCompat.getColor(requireContext(), R.color.f1_muted);
        btnBookmark.post(() -> btnBookmark.setColorFilter(color));
    }
}
