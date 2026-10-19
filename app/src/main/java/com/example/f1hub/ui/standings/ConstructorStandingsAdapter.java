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
import com.example.f1hub.data.model.jolpica.ConstructorStanding;

public class ConstructorStandingsAdapter extends ListAdapter<ConstructorStanding, ConstructorStandingsAdapter.ViewHolder> {

    interface OnClickListener {
        void onClick(ConstructorStanding item);
    }

    private final OnClickListener listener;

    public ConstructorStandingsAdapter(OnClickListener listener) {
        super(DIFF);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_constructor_standing, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        ConstructorStanding item = getItem(position);
        h.position.setText(item.position);

        int pos = 0;
        try { pos = Integer.parseInt(item.position); } catch (NumberFormatException ignored) {}
        h.position.setBackgroundResource(pos <= 3 ? R.drawable.bg_round_badge : 0);
        h.position.setTextColor(pos <= 3
                ? Color.WHITE
                : h.position.getContext().getColor(R.color.f1_muted));

        if (item.constructor != null) {
            h.name.setText(item.constructor.name);
            h.nationality.setText(item.constructor.nationality);
        }
        h.points.setText(item.points);
        h.itemView.setOnClickListener(v -> listener.onClick(item));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView position, name, nationality, points;

        ViewHolder(View v) {
            super(v);
            position    = v.findViewById(R.id.tv_position);
            name        = v.findViewById(R.id.tv_constructor_name);
            nationality = v.findViewById(R.id.tv_nationality);
            points      = v.findViewById(R.id.tv_points);
        }
    }

    private static final DiffUtil.ItemCallback<ConstructorStanding> DIFF = new DiffUtil.ItemCallback<ConstructorStanding>() {
        @Override
        public boolean areItemsTheSame(@NonNull ConstructorStanding a, @NonNull ConstructorStanding b) {
            return a.constructor != null && a.constructor.constructorId.equals(b.constructor.constructorId);
        }
        @Override
        public boolean areContentsTheSame(@NonNull ConstructorStanding a, @NonNull ConstructorStanding b) {
            return a.points != null && a.points.equals(b.points);
        }
    };
}
