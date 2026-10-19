package com.example.f1hub.ui.standings;

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
import com.example.f1hub.R;
import com.example.f1hub.data.db.FavouriteEntity;
import com.example.f1hub.data.model.jolpica.ConstructorStanding;
import com.example.f1hub.data.repository.FavouritesRepository;

public class ConstructorDetailFragment extends Fragment {

    private FavouritesRepository favRepo;
    private ConstructorStanding current;
    private boolean isFav = false;
    private ImageView btnBookmark;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_constructor_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        StandingsViewModel viewModel = new ViewModelProvider(requireParentFragment())
                .get(StandingsViewModel.class);

        favRepo     = FavouritesRepository.getInstance(requireContext());
        btnBookmark = view.findViewById(R.id.btn_bookmark);

        TextView tvName  = view.findViewById(R.id.tv_name);
        TextView tvPos   = view.findViewById(R.id.tv_position);
        TextView tvNat   = view.findViewById(R.id.tv_nationality);
        TextView tvWins  = view.findViewById(R.id.tv_wins);
        TextView tvPts   = view.findViewById(R.id.tv_points);

        view.findViewById(R.id.btn_back).setOnClickListener(v ->
                requireParentFragment().getChildFragmentManager().popBackStack());

        btnBookmark.setOnClickListener(v -> toggleFavourite());

        viewModel.getSelectedConstructor().observe(getViewLifecycleOwner(), cs -> {
            if (cs == null || cs.constructor == null) return;
            current = cs;
            tvName.setText(cs.constructor.name);
            tvPos.setText(cs.position);
            tvNat.setText(cs.constructor.nationality != null ? cs.constructor.nationality : "—");
            tvWins.setText(cs.wins != null ? cs.wins : "0");
            tvPts.setText(cs.points);

            favRepo.isFavourite(cs.constructor.constructorId, fav -> {
                isFav = fav;
                updateBookmarkTint();
            });
        });
    }

    private void toggleFavourite() {
        if (current == null || current.constructor == null) return;
        if (isFav) {
            FavouriteEntity e = new FavouriteEntity();
            e.id = current.constructor.constructorId;
            favRepo.remove(e);
            isFav = false;
        } else {
            FavouriteEntity e = new FavouriteEntity();
            e.id       = current.constructor.constructorId;
            e.type     = "CONSTRUCTOR";
            e.name     = current.constructor.name;
            e.subtitle = current.constructor.nationality;
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
