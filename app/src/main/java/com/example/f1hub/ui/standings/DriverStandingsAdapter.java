package com.example.f1hub.ui.standings;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.f1hub.R;
import com.example.f1hub.data.model.jolpica.DriverStanding;

public class DriverStandingsAdapter extends ListAdapter<DriverStanding, DriverStandingsAdapter.ViewHolder> {

    public DriverStandingsAdapter() {
        super(DIFF);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_driver_standing, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        DriverStanding item = getItem(position);
        h.position.setText(item.position);

        int pos = 0;
        try { pos = Integer.parseInt(item.position); } catch (NumberFormatException ignored) {}
        h.position.setBackgroundResource(pos <= 3 ? R.drawable.bg_round_badge : 0);
        h.position.setTextColor(pos <= 3
                ? Color.WHITE
                : h.position.getContext().getColor(R.color.f1_muted));

        if (item.driver != null) {
            h.name.setText(item.driver.givenName + " " + item.driver.familyName);
        }
        if (item.constructors != null && !item.constructors.isEmpty()) {
            h.team.setText(item.constructors.get(0).name);
        }
        h.points.setText(item.points);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView position, name, team, points;

        ViewHolder(View v) {
            super(v);
            position = v.findViewById(R.id.tv_position);
            name     = v.findViewById(R.id.tv_driver_name);
            team     = v.findViewById(R.id.tv_team);
            points   = v.findViewById(R.id.tv_points);
        }
    }

    private static final DiffUtil.ItemCallback<DriverStanding> DIFF = new DiffUtil.ItemCallback<DriverStanding>() {
        @Override
        public boolean areItemsTheSame(@NonNull DriverStanding a, @NonNull DriverStanding b) {
            return a.driver != null && a.driver.driverId.equals(b.driver.driverId);
        }
        @Override
        public boolean areContentsTheSame(@NonNull DriverStanding a, @NonNull DriverStanding b) {
            return a.points != null && a.points.equals(b.points);
        }
    };
}
