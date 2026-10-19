package com.example.f1hub.ui.races;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.f1hub.R;
import com.example.f1hub.data.model.jolpica.Race;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class RaceAdapter extends ListAdapter<Race, RaceAdapter.ViewHolder> {

    private static final DateTimeFormatter IN  = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter OUT = DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH);

    interface OnClickListener { void onClick(Race race); }
    private final OnClickListener listener;

    public RaceAdapter(OnClickListener listener) {
        super(DIFF);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_race, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Race race = getItem(position);
        h.round.setText(race.round);
        h.name.setText(race.raceName);

        if (race.circuit != null && race.circuit.location != null) {
            h.location.setText(race.circuit.location.country);
        }

        if (race.date != null) {
            try {
                LocalDate date = LocalDate.parse(race.date, IN);
                h.date.setText(date.format(OUT));
                LocalDate today = LocalDate.now();
                if (date.isBefore(today)) {
                    h.status.setText("Completed");
                } else if (date.isEqual(today)) {
                    h.status.setText("Today");
                } else {
                    h.status.setText("Upcoming");
                }
            } catch (Exception e) {
                h.date.setText(race.date);
                h.status.setText("");
            }
        }
        h.itemView.setOnClickListener(v -> listener.onClick(race));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView round, name, location, date, status;

        ViewHolder(View v) {
            super(v);
            round    = v.findViewById(R.id.tv_round);
            name     = v.findViewById(R.id.tv_race_name);
            location = v.findViewById(R.id.tv_location);
            date     = v.findViewById(R.id.tv_date);
            status   = v.findViewById(R.id.tv_status);
        }
    }

    private static final DiffUtil.ItemCallback<Race> DIFF = new DiffUtil.ItemCallback<Race>() {
        @Override
        public boolean areItemsTheSame(@NonNull Race a, @NonNull Race b) {
            return a.round != null && a.round.equals(b.round);
        }
        @Override
        public boolean areContentsTheSame(@NonNull Race a, @NonNull Race b) {
            return a.round != null && a.round.equals(b.round)
                    && a.raceName != null && a.raceName.equals(b.raceName);
        }
    };
}
