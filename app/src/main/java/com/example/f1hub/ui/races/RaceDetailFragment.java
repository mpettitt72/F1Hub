package com.example.f1hub.ui.races;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.f1hub.R;
import com.example.f1hub.data.model.jolpica.Race;
import com.example.f1hub.data.model.jolpica.RaceResult;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class RaceDetailFragment extends Fragment {

    private static final DateTimeFormatter IN  = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter OUT = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_race_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RacesViewModel viewModel = new ViewModelProvider(requireParentFragment())
                .get(RacesViewModel.class);

        TextView tvRound         = view.findViewById(R.id.tv_round);
        TextView tvName          = view.findViewById(R.id.tv_race_name);
        TextView tvCircuit       = view.findViewById(R.id.tv_circuit);
        TextView tvDate          = view.findViewById(R.id.tv_date);
        TextView tvLocation      = view.findViewById(R.id.tv_location);
        TextView tvResultsHeader = view.findViewById(R.id.tv_results_header);
        ProgressBar progressRes  = view.findViewById(R.id.progress_results);
        LinearLayout resultsContainer = view.findViewById(R.id.results_container);

        view.findViewById(R.id.btn_back).setOnClickListener(v ->
                requireParentFragment().getChildFragmentManager().popBackStack());

        viewModel.getSelectedRace().observe(getViewLifecycleOwner(), race -> {
            if (race == null) return;

            tvRound.setText(race.round);
            tvName.setText(race.raceName);

            if (race.circuit != null) {
                tvCircuit.setText(race.circuit.circuitName);
                if (race.circuit.location != null) {
                    tvLocation.setText(race.circuit.location.country);
                }
            }

            if (race.date != null) {
                try {
                    tvDate.setText(LocalDate.parse(race.date, IN).format(OUT));
                } catch (Exception e) {
                    tvDate.setText(race.date);
                }

                try {
                    if (LocalDate.parse(race.date, IN).isBefore(LocalDate.now())) {
                        tvResultsHeader.setVisibility(View.VISIBLE);
                        progressRes.setVisibility(View.VISIBLE);
                        loadResults(viewModel, resultsContainer, progressRes);
                    }
                } catch (Exception ignored) { }
            }
        });
    }

    private void loadResults(RacesViewModel viewModel, LinearLayout container, ProgressBar progress) {
        if (viewModel.getRaceResults() == null) {
            progress.setVisibility(View.GONE);
            return;
        }
        viewModel.getRaceResults().observe(getViewLifecycleOwner(), races -> {
            progress.setVisibility(View.GONE);
            if (races == null || races.isEmpty()) return;
            Race race = races.get(0);
            if (race.results == null || race.results.isEmpty()) return;

            container.setVisibility(View.VISIBLE);
            container.removeAllViews();

            List<RaceResult> results = race.results;
            int limit = Math.min(results.size(), 10);
            for (int i = 0; i < limit; i++) {
                RaceResult r = results.get(i);
                View row = LayoutInflater.from(requireContext())
                        .inflate(R.layout.item_race_result, container, false);

                TextView tvPos    = row.findViewById(R.id.tv_position);
                TextView tvDriver = row.findViewById(R.id.tv_driver);
                TextView tvTeam   = row.findViewById(R.id.tv_team);
                TextView tvTime   = row.findViewById(R.id.tv_time);

                tvPos.setText(r.position);
                int pos = 0;
                try { pos = Integer.parseInt(r.position); } catch (NumberFormatException ignored) {}
                tvPos.setBackgroundResource(pos == 1 ? R.drawable.bg_round_badge : 0);
                tvPos.setTextColor(pos == 1
                        ? requireContext().getColor(android.R.color.white)
                        : requireContext().getColor(R.color.f1_muted));

                if (r.driver != null)
                    tvDriver.setText(r.driver.givenName + " " + r.driver.familyName);
                if (r.constructor != null)
                    tvTeam.setText(r.constructor.name);

                if (r.time != null && r.time.time != null) {
                    tvTime.setText(r.time.time);
                } else if (r.status != null) {
                    tvTime.setText(r.status);
                }

                container.addView(row);
            }
        });
    }
}
